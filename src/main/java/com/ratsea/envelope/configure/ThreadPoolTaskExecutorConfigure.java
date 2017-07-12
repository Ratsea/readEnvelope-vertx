package com.ratsea.envelope.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Ratsea on 2017/7/2.
 */

@Configuration
public class ThreadPoolTaskExecutorConfigure {

    @Resource
    private ThreadPool threadPool;


    @Bean
    public ThreadPoolTaskExecutor createThreadPool() throws Exception{
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(threadPool.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(threadPool.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(threadPool.getQueueCapacity());
        threadPoolTaskExecutor.setKeepAliveSeconds(threadPool.getKeepAliveSeconds());
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return threadPoolTaskExecutor;
    }

}
