package com.sky.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
