package cn.cutepikachu.datawisemaster.mapper;

import cn.cutepikachu.datawisemaster.util.DataConvertUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@SpringBootTest
class ChartMapperTest {

    @Resource
    ChartMapper chartMapper;

    @Test
    void createTable() {
        Set<String> fieldSet = new HashSet<>();
        fieldSet.add("日期");
        fieldSet.add("人数");
        chartMapper.createChartDataTable(1788382522973061122L, fieldSet);
    }

    @Test
    void insertData() {
        String data = "日期,人数\n" +
                "5月1日,12\n" +
                "5月2日,80\n" +
                "5月3日,11\n" +
                "5月4日,23\n" +
                "5月5日,70\n" +
                "5月6日,111\n" +
                "5月7日,25\n" +
                "5月8日,150\n" +
                "5月9日,11";
        List<Map<String, String>> dataList = DataConvertUtil.convert(data);
        boolean success = chartMapper.insertChartData(1788382522973061122L, dataList);
    }

    @Test
    void selectData() {
        List<Map<String, String>> dataList = chartMapper.selectChartData(1788382522973061122L);
        for (Map<String, String> row : dataList) {
            System.out.println(row);
        }
    }

}
