package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.BaseException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ShoppingMapper shoppingMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public OrderVO getById(Long id) {
        OrderVO orderVO = orderMapper.getById(id);
        if(orderVO == null){
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        return orderVO;
    }

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersCancelDTO, order);
        order.setStatus(OrderVO.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersRejectionDTO, order);
         order.setStatus(OrderVO.CANCELLED);
         order.setCancelTime(LocalDateTime.now());
         orderMapper.updateOrder(order);
    }

    @Override
    public void complete(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(OrderVO.COMPLETED);
        orderMapper.updateOrder(order);
    }

    @Override
    public OrderStatisticsVO statistics() {
        List<Map<String, Object>> maps =  orderMapper.statistics();
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(0);
        orderStatisticsVO.setToBeConfirmed(0);
        orderStatisticsVO.setDeliveryInProgress(0);
        for(Map<String, Object>map: maps){
            switch (map.get("status").toString()){
                case "2":
                    orderStatisticsVO.setToBeConfirmed((Integer) map.getOrDefault("count", 0));
                    break;
                case "3":
                    orderStatisticsVO.setConfirmed((Integer) map.getOrDefault("count", 0));
                    break;
                case "4":
                    orderStatisticsVO.setDeliveryInProgress((Integer) map.getOrDefault("count", 0));
                    break;
            }
        }
        return orderStatisticsVO;
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersConfirmDTO, order);
        order.setStatus(OrderVO.CONFIRMED);
        orderMapper.updateOrder(order);
    }

    @Override
    public void delivery(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(OrderVO.DELIVERY_IN_PROGRESS);
        orderMapper.updateOrder(order);
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<OrderVO> list = orderMapper.conditionSearch(dto);
        PageInfo<OrderVO> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 检查地址信息
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        if(addressBookId == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        AddressBook addressBook = addressMapper.getAddressById(addressBookId);
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 检查购物车信息
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingMapper.list(cart);
        if(list == null || list.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 构造Order表的细腻
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());

        // 需要根据userId查询用户名
        User user = userMapper.getById(userId);
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        // 填充地址信息
        orders.setAddressBookId(addressBookId);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        // 插入订单数据（需要回传ID）
        orderMapper.insert(orders);

        // 清空购物车
        shoppingMapper.clean(userId);

        // 插入orderDetail数据
        List<OrderDetail> details = new ArrayList<>(list.size());
        for(ShoppingCart shoppingCart : list){
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderId(orders.getId())
                    .name(shoppingCart.getName())
                    .dishId(shoppingCart.getDishId())
                    .dishFlavor(shoppingCart.getDishFlavor())
                    .setmealId(shoppingCart.getSetmealId())
                    .number(shoppingCart.getNumber())
                    .amount(shoppingCart.getAmount())
                    .image(shoppingCart.getImage())
                    .build();
            details.add(orderDetail);
        }
        orderDetailMapper.insert(details);

    }
}
