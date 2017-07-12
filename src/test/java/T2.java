import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratsea.envelope.Application;
import com.ratsea.envelope.configure.URLConfigure;
import com.ratsea.envelope.domain.ResultDto;
import com.ratsea.envelope.service.ReadEnvelopeService;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import io.netty.util.internal.shaded.org.jctools.queues.MpmcArrayQueue;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
public class T2
{


    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ReadEnvelopeService readEnvelopeService;

    @Resource
    private URLConfigure urlConfigure;


    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private int threadNum=1000;

    private CountDownLatch latch = new CountDownLatch(threadNum);

    @Test
    public void t() throws Exception{


        this.t2();
        latch.await();


    }


    private void t2() throws Exception{

        JSONObject param = new JSONObject();
        param.put("userId",1l);
        param.put("nickName","王胖子");
        param.put("money",new BigDecimal(200.00));
        param.put("num",20);

        ResultDto resultDto= this.post(param,urlConfigure.getPushRedEnvelopeUrl());
        System.err.println(param.get("nickName")+"发送的红包："+param.get("money")+"的编号为：》》》》》》》》》》"+resultDto.getObj());
        for(long i=0;i<threadNum;i++){
            Long userId=i+1;

            JSONObject param1 = new JSONObject();
            param1.put("userId",userId);
            param1.put("nickName","张"+userId);
            param1.put("id",resultDto.getObj());

            threadPoolTaskExecutor.execute(()->{
                try{
                    ResultDto result=this.post(param1,urlConfigure.getRobRedEnvelopeUrl()+resultDto.getObj().toString());
                    System.err.println("编号为:"+Thread.currentThread().getId()+"的线程"+Thread.currentThread().getName()+">>>>>>执行结果为:"+result.toString());
                }catch (IOException e){
                    e.printStackTrace();
                }
                latch.countDown();
            });


        }

    }


    /**
     * 发送POST请求
     * @param param:参数
     * @param url:地址
     * @return
     * @throws IOException
     */
    private ResultDto post(JSONObject param,String url) throws IOException{
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String urlApi =  UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(objectMapper.convertValue(param, LinkedMultiValueMap.class))
                .build()
                .toUri().toString();
        ResponseEntity<String> responseEntity=   restTemplate.exchange(urlApi,HttpMethod.POST, HttpEntity.EMPTY,String.class);
        ResultDto resultDto= objectMapper.readValue(responseEntity.getBody(),ResultDto.class);
        return resultDto;
    }
}
