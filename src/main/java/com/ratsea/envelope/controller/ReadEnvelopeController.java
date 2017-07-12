package com.ratsea.envelope.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratsea.envelope.configure.VertxYml;
import com.ratsea.envelope.domain.ResultDto;
import com.ratsea.envelope.service.ReadEnvelopeService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@Component
public class ReadEnvelopeController {
    @Autowired
    private Vertx vertx;
    @Resource
    private ReadEnvelopeService readEnvelopeService;

    @Resource
    private VertxYml vertxYml;

    @PostConstruct
    public void start() throws Exception{
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
            router.post("/v1/envelope")
                    .produces("application/json")
                    .blockingHandler(this::pushEnvelope);

            router.post("/v1/envelope/:id")
                    .produces("application/json")
                    .blockingHandler(this::getReadEnvelope);

        vertx.createHttpServer().requestHandler(router::accept).listen(vertxYml.getPort());
    }
    /**
     * 发送红包
     * @param rc
     * @return
     */
    private void pushEnvelope(RoutingContext rc){
        Long userId = Long.parseLong(rc.request().getParam("userId"));
        String nickName=rc.request().getParam("nickName");
        BigDecimal money=BigDecimal.valueOf(Double.parseDouble(rc.request().getParam("money")));
        Integer num=Integer.parseInt(rc.request().getParam("num"));
        ResultDto result= readEnvelopeService.pushEnvelope(userId,nickName,money,num);
        ObjectMapper objectMapper=new ObjectMapper();
        String str=null;
        try{
            str=objectMapper.writeValueAsString(result);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200).setStatusMessage("OK").end(str);
    }


    /**
     * 抢红包
     * @param rc
     */
    private void getReadEnvelope(RoutingContext rc){
        String id=rc.request().getParam("id");
        Long userId = Long.parseLong(rc.request().getParam("userId"));
        String nickName=rc.request().getParam("nickName");
        ResultDto result= readEnvelopeService.getReadEnvelope(id,userId,nickName);
        ObjectMapper objectMapper=new ObjectMapper();
        String str=null;
        try{
            str=objectMapper.writeValueAsString(result);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        rc.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200).setStatusMessage("OK").end(str);

    }

}
