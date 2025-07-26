package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.WorkspaceMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceMapper workspaceMapper;


    @Override
    public BusinessDataVO getBusinessData() {

        BusinessDataVO businessDataVO = new BusinessDataVO();
        LocalDate date = LocalDate.now();
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atStartOfDay().plusDays(1);
        // 查询今天新增的用户数量
        businessDataVO.setNewUsers(workspaceMapper.getNewUserCount(startTime, endTime));
        // 查询本日订单完成率
        Integer finishOrderCount =
                workspaceMapper.getOrderCountByCondition(
                        startTime, endTime, Orders.COMPLETED);
        finishOrderCount = finishOrderCount == null ? 0 : finishOrderCount;
        Integer totalOrderCount = workspaceMapper.getOrderCountByCondition(startTime, endTime, null);
        totalOrderCount = totalOrderCount == null ? 0 : totalOrderCount;
        businessDataVO.setOrderCompletionRate(totalOrderCount == 0 ? 0 : 1.0 * finishOrderCount / totalOrderCount);
        // 查询今日有效订单数、营业额、平均客单价
        Map<String, Object> map = workspaceMapper.getTurnover(startTime, endTime, Orders.COMPLETED);
        int count = map.get("count") == null ? 0 : ((Long) map.get("count")).intValue();
        businessDataVO.setValidOrderCount(count);
        businessDataVO.setTurnover(
                map.get("turnover") == null ? 0.0 : ((BigDecimal) map.get("turnover")).doubleValue());
        if(businessDataVO.getValidOrderCount() > 0)
            businessDataVO.setUnitPrice(businessDataVO.getTurnover() / businessDataVO.getValidOrderCount());
        else
            businessDataVO.setUnitPrice(0.0);
        return businessDataVO;
    }

    @Override
    public DishOverViewVO overviewDishes() {
        DishOverViewVO vo = new DishOverViewVO();
        List<Map<String,  Object>> list = workspaceMapper.overviewDishes();
        vo.setDiscontinued(0);
        vo.setSold(0);
        for (Map<String, Object> map : list) {
            Long count = (Long) map.get("count");
            if(map.get("status").equals(0)){
                vo.setDiscontinued(count.intValue());
            } else {
                vo.setSold(count.intValue());
            }
        }
        return vo;
    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {
        SetmealOverViewVO vo = new SetmealOverViewVO();
        List<Map<String,  Object>> list = workspaceMapper.overviewSetmeals();
        vo.setDiscontinued(0);
        vo.setSold(0);
        for (Map<String, Object> map : list) {
            Long count = (Long) map.get("count");
            if(map.get("status").equals(0)){
                vo.setDiscontinued(count.intValue());
            } else {
                vo.setSold(count.intValue());
            }
        }
        return vo;
    }

    @Override
    public OrderOverViewVO overviewOrders() {
        OrderOverViewVO vo = new OrderOverViewVO();
        vo.setDeliveredOrders(0);
        vo.setWaitingOrders(0);
        vo.setCancelledOrders(0);
        vo.setCompletedOrders(0);
        List<Map<String,  Object>> list = workspaceMapper.overviewOrders();
        int sum = 0;
        for (Map<String, Object> map : list) {
            int count = ((Long) map.get("count")).intValue();
            sum += count;
            switch (map.get("status").toString()) {
                case "2":
                    vo.setWaitingOrders(count);
                case "3":
                    vo.setDeliveredOrders(count);
                    break;
                case "5":
                    vo.setCompletedOrders(count);
                    break;
                case "6":
                    vo.setCancelledOrders(count);
                    break;
                default:
                    break;
            }
        }
        vo.setAllOrders(sum);
        return vo;
    }
}
