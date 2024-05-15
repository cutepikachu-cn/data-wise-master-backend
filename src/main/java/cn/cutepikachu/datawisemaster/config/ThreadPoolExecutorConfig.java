package cn.cutepikachu.datawisemaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Configuration
public class ThreadPoolExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count++);
                return thread;
            }
        };

        return new ThreadPoolExecutor(
                2,
                4,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(4),
                threadFactory);
    }
}
