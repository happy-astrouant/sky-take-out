package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebsocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private WebsocketServer websocketServer;

    @Override
    @Transactional(readOnly = true)
    public OrderVO getById(Long id) {
        OrderVO orderVO = orderMapper.getById(id);
        if(orderVO == null){
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(id));
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

    private String getOrderDishes(List<OrderDetail> list){

        if(list == null || list.isEmpty()){
            return "";
        }
        StringBuilder dishBuilder = new StringBuilder();
        StringBuilder setmealBuilder = new StringBuilder();
        for(OrderDetail orderDetail: list){
            if(orderDetail.getDishId() != null){
                dishBuilder.append(orderDetail.getName());
                if(orderDetail.getDishFlavor() != null){
                    dishBuilder.append("(").append(orderDetail.getDishFlavor()).append(")");
                }
                dishBuilder.append("*").append(orderDetail.getNumber());
                dishBuilder.append(", ");
            } else {
                setmealBuilder.append(orderDetail.getName());
                setmealBuilder.append("*").append(orderDetail.getNumber());
                setmealBuilder.append(", ");
            }
        }
        if(!dishBuilder.isEmpty()){
            // 头部插入菜品：
            dishBuilder.insert(0, "菜品：");
            dishBuilder.deleteCharAt(dishBuilder.length() - 1);
            dishBuilder.append(". ");
        }
        if(!setmealBuilder.isEmpty()){
            // 头部插入套餐：
            setmealBuilder.insert(0, "套餐：");
            setmealBuilder.deleteCharAt(setmealBuilder.length() - 1);
            setmealBuilder.append(". ");
        }
        return dishBuilder.append(setmealBuilder).toString();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult conditionSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<OrderVO> list = orderMapper.conditionSearch(dto);
        list.forEach(orderVO -> {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());
            orderVO.setOrderDishes(getOrderDishes(orderDetailList));
        });
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
        orders.setNumber(String.valueOf(System.currentTimeMillis()));

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
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult userHistoryOrders(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<OrderVO> list = orderMapper.conditionSearch(dto);
        list.forEach(orderVO -> {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());
            orderVO.setOrderDetailList(orderDetailList);
        });
        PageInfo<OrderVO> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public void repetition(Long id) {
        // 先查询订单
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setCheckoutTime(null);
        orders.setRejectionReason(null);
        orders.setCancelReason(null);
        orders.setCancelTime(null);
        orders.setEstimatedDeliveryTime(null);
        orders.setDeliveryTime(null);
        orderMapper.insert(orders);
        orderDetailList.forEach(orderDetail -> orderDetail.setOrderId(orders.getId()));
        orderDetailMapper.insert(orderDetailList);
    }

    @Override
    @Transactional
    public OrderPaymentVO payment(OrdersPaymentDTO dto) {
        Orders orders = orderMapper.getByOrderNumber(dto.getOrderNumber());
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(dto.getPayMethod());
        orderMapper.updateOrder(orders);
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setNonceStr("");
        vo.setPaySign("");
        vo.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        vo.setSignType("");
        vo.setPackageStr("");

        // 支付成功后，推送订单消息
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);  // 1 来单提醒
        map.put("orderId", orders.getId());
        map.put("content", "来单提醒：" + orders.getNumber());
        // 转换为json格式
        websocketServer.sendAll(JSON.toJSONString(map));
        return vo;
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        // 支付成功后，推送订单消息
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);  // 2 催单提醒
        map.put("orderId", orders.getId());
        map.put("content", "来单提醒：" + orders.getNumber());
        // 转换为json格式
        websocketServer.sendAll(JSON.toJSONString(map));
    }
}
