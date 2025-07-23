package com.sky.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
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
        return new SalesTop10ReportVO(nameList.toString(), numberList.toString());
    }
}
