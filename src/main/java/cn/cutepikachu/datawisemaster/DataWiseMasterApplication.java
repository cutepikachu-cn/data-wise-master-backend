package cn.cutepikachu.datawisemaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
public class DataWiseMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataWiseMasterApplication.class, args);
    }

}
