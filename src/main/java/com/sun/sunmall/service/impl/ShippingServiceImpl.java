package com.sun.sunmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.ShippingMapper;
import com.sun.sunmall.pojo.Shipping;
import com.sun.sunmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by sun on 2017/5/23.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse addShipping(Integer userId,Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.insert(shipping);//insert中用useGenerateKey配置自动生成主键
        if (resultCount > 0) {
            Map resultMap = Maps.newHashMap();
            resultMap.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("增添地址成功",resultMap);
        }
        return ServerResponse.createByErrorMessage("增添地址失败");

    }

    @Override
    public ServerResponse<String> deleteShipping(Integer userId, Integer shippingId) {
        //int resultCount = shippingMapper.deleteByPrimaryKey(shippingId);//会发生横向越权的安全问题
        int resultCount = shippingMapper.deleteByShippingId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> updateShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShippingIdUserId(shipping);//一样需要注意横向越权
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");

    }

    @Override
    public ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId, userId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("查找地址失败");
        }
        return ServerResponse.createBySuccess("查找成功", shipping);
    }

    @Override
    public ServerResponse<PageInfo> listShippings(Integer pageNum,Integer pageSize,Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectAllShipping(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
