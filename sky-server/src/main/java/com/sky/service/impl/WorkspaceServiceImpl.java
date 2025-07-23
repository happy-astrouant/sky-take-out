package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.WorkspaceMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

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

    private static final List<Integer> validStatus = new ArrayList<>();
    static {
        validStatus.add(Orders.CONFIRMED);
        validStatus.add(Orders.DELIVERY_IN_PROGRESS);
        validStatus.add(Orders.COMPLETED);
    }

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
                workspaceMapper.getOrderCountByCondition(startTime, endTime, Collections.singletonList(Orders.COMPLETED));
        Integer totalOrderCount = workspaceMapper.getOrderCountByCondition(startTime, endTime, null);
        businessDataVO.setOrderCompletionRate(totalOrderCount == 0 ? 0 : 1.0 * finishOrderCount / totalOrderCount);
        // 查询今日有效订单数、营业额、平均客单价
        Map<String, Object> map = workspaceMapper.getTurnover(startTime, endTime, validStatus);
        businessDataVO.setValidOrderCount(map.get("count") == null ? 0 : (Integer) map.get("count"));
        businessDataVO.setTurnover(map.get("turnover") == null ? 0 : (Double) map.get("turnover"));
        businessDataVO.setUnitPrice(businessDataVO.getTurnover() / businessDataVO.getValidOrderCount());
        return businessDataVO;
    }
}
