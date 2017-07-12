package com.ratsea.envelope.configure;

import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@Configuration
public class VertxConfigure {

    private Vertx vertx;

    @Bean
    public Vertx getVertxInstance() {
        if (this.vertx==null) {
            this.vertx = Vertx.vertx();
        }
        return this.vertx;
    }
}
