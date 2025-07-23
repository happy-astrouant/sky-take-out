package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public interface ReportService {
    SalesTop10ReportVO top10(LocalDate begin, LocalDate end);
}
