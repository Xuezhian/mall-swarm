package com.macro.mall.portal.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.UmsAddress;
import com.macro.mall.portal.domain.WeChatModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 认证服务远程调用
 * Created by macro on 2020/7/19.
 */
public interface WeChatService {

    CommonResult login(WeChatModel weChatModel);

    CommonResult UserDetail();

    CommonResult UserSave(UmsMember umsMember);

    CommonResult regionList(Integer parentId);

    CommonResult saveAddress(UmsAddress umsAddress);

    CommonResult getAddresses();

    CommonResult addressDetail(Integer id);

    CommonResult deleteAddress(Long id);
}
