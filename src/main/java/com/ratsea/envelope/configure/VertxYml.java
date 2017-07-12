package com.ratsea.envelope.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix="vertx")
public class VertxYml {


    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
