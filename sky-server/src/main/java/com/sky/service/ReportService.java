package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public interface ReportService {
    SalesTop10ReportVO top10(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);
}
