package com.ratsea.envelope.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 类描述:RelateTemplate配置
 * 作者:严祥
 * 创建时间:2017/4/24
 **/
@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate createRestTemplate()
    {
        return this.createTimePlate();
    }


    public static RestTemplate createTimePlate(){
        RestTemplate restTemplate=new RestTemplate();

        FormHttpMessageConverter fc = new FormHttpMessageConverter();

        StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
        partConverters.add(s);
        partConverters.add(new ResourceHttpMessageConverter());
        fc.setPartConverters(partConverters);

        MappingJackson2HttpMessageConverter jsonMessageConverter =
                new MappingJackson2HttpMessageConverter();
        // Add supported media type
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(new MediaType("text", "plain"));
        supportedMediaTypes.add(new MediaType("charset", "utf-8"));
        supportedMediaTypes.add(new MediaType("text", "html"));
        supportedMediaTypes.add(new MediaType("application", "json"));
        jsonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        restTemplate.getMessageConverters().addAll(Arrays.asList(fc, jsonMessageConverter));
        ClientHttpRequestInterceptor interceptor=   new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
                MediaType mt=MediaType.valueOf("application/json;charset=utf-8");
                wrapper.getHeaders().setContentType(mt);
                wrapper.getHeaders().setAccept(Collections.singletonList(new MediaType("application", "json")));
                wrapper.getHeaders().setAcceptCharset(Collections.singletonList(Charset.forName("utf-8")));
                return execution.execute(wrapper, body);
            }

        };
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }

}
