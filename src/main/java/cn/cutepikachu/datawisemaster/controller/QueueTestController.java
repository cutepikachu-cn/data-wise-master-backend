package cn.cutepikachu.datawisemaster.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/queue")
public class QueueTestController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @PostMapping("/add")
    public void add(String name) {
        // 创建一个异步任务
        CompletableFuture.runAsync(() -> {
            log.info("任务执行中：{}, 执行人：{}", name, Thread.currentThread().getName());
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecutor);
    }

    @GetMapping("/get")
    public Map<String, Object> get() {
        Map<String, Object> threadPoolInfoMap = new HashMap<>();
        // 线程池队列长度
        int size = threadPoolExecutor.getQueue().size();
        threadPoolInfoMap.put("队列长度", size);
        // 线程池已接收的任务总数
        long taskCount = threadPoolExecutor.getTaskCount();
        threadPoolInfoMap.put("任务总数", taskCount);
        // 线程池已完成的任务数
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        threadPoolInfoMap.put("已完成任务数", completedTaskCount);
        // 线程池中正在执行任务的线程数
        int activeCount = threadPoolExecutor.getActiveCount();
        threadPoolInfoMap.put("正在工作的线程数", activeCount);

        return threadPoolInfoMap;
    }
}
