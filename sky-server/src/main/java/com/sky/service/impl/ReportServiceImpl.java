package com.sky.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay().plusDays(1);
        List<Map<String, Object>> list = reportMapper.top10Dish(beginTime, endTime);
        StringBuilder nameList = new StringBuilder();
        StringBuilder numberList = new StringBuilder();
        for(Map<String, Object> map : list){
            nameList.append(map.get("name")).append(",");
            numberList.append(map.get("number") == null ? 0 : map.get("number")).append(",");
        }
        nameList.deleteCharAt(nameList.length() - 1);
        numberList.deleteCharAt(numberList.length() - 1);
        return new SalesTop10ReportVO(nameList.toString(), numberList.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay().plusDays(1);
        int totalNum = reportMapper.getUserNumBefore(beginTime);
        List<Map<String, Object>> list = reportMapper.userStatistics(beginTime, endTime);
        // 有可能会有缺少日期的情况，所以需要补全
        StringBuilder dateList = new StringBuilder();
        StringBuilder totalUserList = new StringBuilder();
        StringBuilder newUserList = new StringBuilder();
        // 按日期遍历
        int i=0;
        for(LocalDate date = begin; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)){
            dateList.append(date).append(",");
            int dayCount = 0;
            if(list.size() > i && date.equals(list.get(i).get("day"))){
                dayCount = ((Long) list.get(0).get("number")).intValue();
                totalNum += dayCount;
                i++;
            }
            totalUserList.append(totalNum).append(",");
            newUserList.append(dayCount).append(",");
        }
        dateList.deleteCharAt(dateList.length() - 1);
        totalUserList.deleteCharAt(totalUserList.length() - 1);
        newUserList.deleteCharAt(newUserList.length() - 1);
        return new UserReportVO(dateList.toString(), totalUserList.toString(), newUserList.toString());
    }

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay().plusDays(1);

        List<Map<String, Object>> list = reportMapper.turnoverStatistics(beginTime, endTime);

        StringBuilder dateList = new StringBuilder();
        StringBuilder turnoverList = new StringBuilder();
        int i = 0;
        for(LocalDate start = begin; start.isBefore(end) || start.isEqual(end); start = start.plusDays(1)){
            double dayAmount = 0;
            if(list.size() > i &&  start.equals(list.get(i).get("day"))){
                dayAmount = ((Long) list.get(i).get("turnover")).intValue();
            }
            turnoverList.append(dayAmount).append(",");
            dateList.append(start).append(",");
        }
        turnoverList.deleteCharAt(turnoverList.length() - 1);
        dateList.deleteCharAt(dateList.length() - 1);
        return new TurnoverReportVO(dateList.toString(), turnoverList.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay().plusDays(1);
        // 需要进行四次查询
        int orderNum = reportMapper.getTotalOrderNum(beginTime, endTime, null);
        int validOrderNum = reportMapper.getTotalOrderNum(beginTime, endTime, Orders.COMPLETED);
        List<Map<String, Object>> orderList = reportMapper.orderStatistics(beginTime, endTime, null);
        List<Map<String, Object>> paidOrderList = reportMapper.orderStatistics(beginTime, endTime, Orders.COMPLETED);
        // 先设置基本类型变量
        OrderReportVO vo = new OrderReportVO();
        vo.setValidOrderCount(validOrderNum);
        vo.setTotalOrderCount(orderNum);
        vo.setOrderCompletionRate(orderNum == 0? 0.0 :validOrderNum * 1.0 / orderNum);
        // 构造字符串类型
        int i = 0, j=0;
        StringBuilder dateList = new StringBuilder();
        StringBuilder orderCountList = new StringBuilder();
        StringBuilder validOrderCountList = new StringBuilder();
        for(LocalDate start = begin; start.isBefore(end) || start.isEqual(end); start = start.plusDays(1)){
            int dayOrderNum = 0;
            if(orderList.size() > i && start.equals(orderList.get(i).get("day"))){
                dayOrderNum = ((Long) orderList.get(i).get("order_count")).intValue();
                i++;
            }
            int dayValidOrderNum = 0;
            if(paidOrderList.size() > j && start.equals(paidOrderList.get(j).get("day"))){
                dayValidOrderNum = ((Long) paidOrderList.get(j).get("order_count")).intValue();
                j++;
            }
            dateList.append(start).append(",");
            orderCountList.append(dayOrderNum).append(",");
            validOrderCountList.append(dayValidOrderNum).append(",");
        }
        //清除末尾逗号
        dateList.deleteCharAt(dateList.length() - 1);
        orderCountList.deleteCharAt(orderCountList.length() - 1);
        validOrderCountList.deleteCharAt(validOrderCountList.length() - 1);
        // 填充vo
        vo.setDateList(dateList.toString());
        vo.setOrderCountList(orderCountList.toString());
        vo.setValidOrderCountList(validOrderCountList.toString());
        return vo;
    }
}
