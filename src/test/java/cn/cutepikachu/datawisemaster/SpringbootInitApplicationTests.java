package cn.cutepikachu.datawisemaster;

import cn.cutepikachu.datawisemaster.manager.AiManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringbootInitApplicationTests {

    @Resource
    private AiManager aiManager;

    @Test
    void contextLoads() {
        String response = aiManager.doChat("分析需求: \n" +
                "用户增长趋势图\n" +
                "原始数据: \n" +
                "日期,人数\n" +
                "5月1日,12\n" +
                "5月2日,80\n" +
                "5月3日,11\n" +
                "5月4日,23\n" +
                "5月5日,70\n" +
                "5月6日,111\n" +
                "5月7日,25\n" +
                "5月8日,150\n" +
                "5月9日,11");
        System.out.println(response);
    }

}
