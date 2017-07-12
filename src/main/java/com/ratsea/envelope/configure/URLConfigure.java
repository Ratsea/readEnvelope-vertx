package com.ratsea.envelope.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@Component
public class URLConfigure {


    @Resource
    private VertxYml vertxYml;


    /**
     * 获取发红包地址
     * @return
     */
    public String  getPushRedEnvelopeUrl(){
        return  "http://localhost:"+vertxYml.getPort()+"/v1/envelope";
    }

    /**
     * 获取抢红包地址
     * @return
     */
    public String  getRobRedEnvelopeUrl(){
        return  "http://localhost:"+vertxYml.getPort()+"/v1/envelope/";
    }


    public URLConfigure() {
    }


}
