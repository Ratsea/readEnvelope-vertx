package com.ratsea.envelope.service.impl;
import com.ratsea.envelope.domain.EnvelopePushDto;
import com.ratsea.envelope.domain.ResultDto;
import com.ratsea.envelope.domain.User;
import com.ratsea.envelope.service.ReadEnvelopeService;
import com.ratsea.envelope.util.DbStorage;
import com.ratsea.envelope.util.ReadEnvelopeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 类描述:
 * 作者:严祥
 * 创建时间:2017/7/11
 **/
@Service
public class ReadEnvelopeServiceImpl implements ReadEnvelopeService {

    private static final Logger log= LoggerFactory.getLogger(ReadEnvelopeServiceImpl.class);

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final static ConcurrentHashMap dbStorage= DbStorage.getStorage();

    private final static String READ_ENVELOPE_QUEUE_NAME_PREFIX="readEnvelope_";




    @Override
    public ResultDto pushEnvelope(Long userId, String nickName, BigDecimal money, int num) {

        if(ObjectUtils.isEmpty(userId)){
            return new ResultDto(103,"用户编号为空","请登录");
        }

        if(ObjectUtils.isEmpty(nickName)){
            return new ResultDto(103,"用户昵称为空","请登录");
        }

        if(ObjectUtils.isEmpty(money)||money.equals(new BigDecimal(0))){
            return new ResultDto(103,"红包为空","红包为空");
        }
        EnvelopePushDto envelopePushDto=new EnvelopePushDto();
        envelopePushDto.setId(UUID.randomUUID().toString().replace("-",""));

        CompletableFuture.runAsync(()->{
            List<BigDecimal> moneyNum= ReadEnvelopeUtil.math(money,num);
            //创建一个红包队列
            BlockingQueue<User> readEnvelopeQueue = new LinkedBlockingQueue<User>(num);

            envelopePushDto.setUseNum(0);
            List<User> user=new ArrayList<>();
            moneyNum.forEach(n->{
                    User u=new User();
                    u.setMoney(n);
                    u.setUuid(UUID.randomUUID().toString().replace("_",""));
                    user.add(u);
                readEnvelopeQueue.add(u);
                });
            envelopePushDto.setUser(user);
            envelopePushDto.setMoney(money);
            envelopePushDto.setNum(num);
            //往存储中进行存储
            dbStorage.put(envelopePushDto.getId(),envelopePushDto);
            dbStorage.put(READ_ENVELOPE_QUEUE_NAME_PREFIX+envelopePushDto.getId(),readEnvelopeQueue);
        },threadPoolTaskExecutor);
        return new ResultDto(0,"成功","成功",envelopePushDto.getId());

    }

    @Override
    public ResultDto getReadEnvelope(String id, Long userId, String nickName) {

        if(ObjectUtils.isEmpty(id)){
            return new ResultDto(102,"红包编号没有传入","请传入一个红包编号");
        }

        if(ObjectUtils.isEmpty(userId)){
            return new ResultDto(103,"用户编号为空","请登录");
        }

        if(ObjectUtils.isEmpty(nickName)){
            return new ResultDto(103,"用户昵称为空","请登录");
        }

        //System.err.println("用户编号为:"+userId+"的用户:"+nickName+"抢的红包的编号为:"+id);
        /**
         * 逻辑处理
         * 1.判断红包是否存在
         * 2.判断红包是否被抢完
         * 3.判断红包是否被用户抢过
         * 4.用户抢红包
         */

        EnvelopePushDto envelopePushDto =(EnvelopePushDto)dbStorage.get(id);

        if(ObjectUtils.isEmpty(envelopePushDto)){
            return new ResultDto(104,"红包不存在","红包不存在");
        }

        BlockingQueue<User> blockingQueue=(BlockingQueue<User>)dbStorage.get(READ_ENVELOPE_QUEUE_NAME_PREFIX+id);

        if(blockingQueue.isEmpty()||blockingQueue.size()==0){
            return new ResultDto(105,"红包已被抢完","红包已被抢完");
        }

         //判断红包是否被用户抢过
       /*  User validateUser =envelopePushDto.getUser().stream().filter(n->n.getId().equals(userId)).findFirst().get();
        if(!ObjectUtils.isEmpty(validateUser)){
            return new ResultDto(106,"红包已被用户抢过","请勿重复抢红包");
        }*/

        try{
            Long num=   blockingQueue.stream().filter(n->n.getId()==userId).count();
            if(null!=num&&num>0){
                return new ResultDto(106,"红包已被用户抢过","请勿重复抢红包");
            }
        }catch (NullPointerException e){
            return new ResultDto(107,"系统异常","红包已被抢完");
        }



        User user;
        try{
             user=blockingQueue.take();
           CompletableFuture.runAsync(()->{
                Optional<User> optional= envelopePushDto.getUser().stream().filter(n->(n.getUuid().equals(user.getUuid()))).findFirst();
                optional.get().setId(userId);
                optional.get().setNickName(nickName);
               blockingQueue.remove(user);
               dbStorage.put(id,envelopePushDto);
            });
        }catch (InterruptedException e){
            e.printStackTrace();
            return new ResultDto(107,"系统异常","系统异常");
        }


        return new ResultDto(0,"成功","成功","用户抢到:"+user.getMoney());
    }
}
