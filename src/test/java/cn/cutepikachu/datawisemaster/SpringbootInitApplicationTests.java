package cn.cutepikachu.datawisemaster;

import cn.cutepikachu.datawisemaster.model.dto.chart.ChartAddRequest;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class SpringbootInitApplicationTests {

    @Resource
    Validator validator;

    @Test
    void contextLoads() {
        ChartAddRequest chartAddRequest = new ChartAddRequest();
        chartAddRequest.setGoal("分析趋势");
        chartAddRequest.setName("趋势图");
        chartAddRequest.setChartType("line");
        Set<ConstraintViolation<ChartAddRequest>> violationSet = validator.validate(chartAddRequest);
        System.out.println(violationSet);
    }

}
