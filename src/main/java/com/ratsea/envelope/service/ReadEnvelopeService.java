package com.ratsea.envelope.service;

import com.ratsea.envelope.domain.ResultDto;

import java.math.BigDecimal;

/**
 * 接口描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
public interface ReadEnvelopeService {

    /**
     * 发送红包
     * @param userId
     * @param nickName
     * @param money
     * @param num
     * @return
     */
    ResultDto pushEnvelope(Long userId, String nickName, BigDecimal money, int num);



    /**
     * 抢红包
     * @param id:红包编号
     * @param userId:用户编号
     * @param nickName :用户昵称
     * @return
     */
    ResultDto getReadEnvelope(String id,Long userId,String nickName);

}
