package com.macro.mall.portal.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.HomeContentResult;
import com.macro.mall.portal.domain.UmsAddress;
import com.macro.mall.portal.domain.WeChatModel;
import com.macro.mall.portal.service.WeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(tags = "WeChatController",value = "微信接口处理")
@RequestMapping("/wechat")
public class WeChatController {
    @Autowired
    private WeChatService weChatService;
    @ApiOperation("微信小程序登录接口")
    @RequestMapping(value = "/auth/loginByWeixin", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody WeChatModel weChatModel) {
        return weChatService.login(weChatModel);
    }
    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/settings/userDetail", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult UserDetail() {
        return weChatService.UserDetail();
    }

    @ApiOperation("修改用户信息")
    @RequestMapping(value = "/settings/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult UserSave(@RequestBody UmsMember umsMember) {
        return weChatService.UserSave(umsMember);
    }

    @ApiOperation("获取地址列表")
    @RequestMapping(value = "/region/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult regionList(@RequestParam("parentId")Integer parentId) {
        return weChatService.regionList(parentId);
    }
    @ApiOperation("保存地址")
    @RequestMapping(value = "/address/saveAddress", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult saveAddress(@RequestBody UmsAddress umsAddress) {
        return weChatService.saveAddress(umsAddress);
    }

    @ApiOperation("获取地址列表")
    @RequestMapping(value = "/address/getAddresses", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAddresses() {
        return weChatService.getAddresses();
    }
    @ApiOperation("地址详情")
    @RequestMapping(value = "/address/addressDetail", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult addressDetail(@RequestParam("id")Integer id) {
        return weChatService.addressDetail(id);
    }

    @ApiOperation("删除地址")
    @RequestMapping(value = "/address/deleteAddress", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteAddress(@RequestBody UmsAddress umsAddress) {
        return weChatService.deleteAddress(umsAddress.getId());
    }


}
