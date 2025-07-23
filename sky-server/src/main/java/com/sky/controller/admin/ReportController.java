package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/export")
    public String export() {
        log.info("导出数据");
        return "OK";
    }

    // 查询销量前10的菜品
    @GetMapping("/top10")
    public Result top10(@RequestParam LocalDate begin, @RequestParam LocalDate end) {
        log.info("查询销量前10的菜品");
        SalesTop10ReportVO vo = reportService.top10(begin, end);
        return Result.success(vo);
    }

    //按天统计用户数量
    @GetMapping("/userStatistics")
    public Result userStatistics(@RequestParam LocalDate begin, @RequestParam LocalDate end) {
        log.info("按天统计用户数量");
        UserReportVO vo = reportService.userStatistics(begin, end);
        return Result.success(vo);
    }

    //按天统计营业额度
    @GetMapping("/turnoverStatistics")
    public Result turnoverStatistics(@RequestParam LocalDate begin, @RequestParam LocalDate end) {
        log.info("按天统计营业额度");
        return Result.success(reportService.turnoverStatistics(begin, end));
    }

    //按天统计订单数量
    @GetMapping("/ordersStatistics")
    public Result orderStatistics(@RequestParam LocalDate begin, @RequestParam LocalDate end) {
        log.info("按天统计订单数量");
        return Result.success(reportService.orderStatistics(begin, end));
    }
}
