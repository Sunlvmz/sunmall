package com.sun.sunmall.mq;



import com.sun.sunmall.annotation.RabbitHandler;
import com.sun.sunmall.annotation.RabbitListener;
import com.sun.sunmall.dao.SeckillMapper;
import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.pojo.Record;
import com.sun.sunmall.utils.ProtoStuffSerializerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener()
public class RabbitMQReceiver {

    @Autowired
    private SeckillMapper secKillMapper;

    @RabbitHandler
    public void process(byte[] message) throws Exception {
        Record record = ProtoStuffSerializerUtil.deserialize(message, Record.class);
        //插入record
        secKillMapper.insertRecord(record);
        //更改物品库存
        secKillMapper.updateByAsynPattern(record.getProduct());
    }
}
