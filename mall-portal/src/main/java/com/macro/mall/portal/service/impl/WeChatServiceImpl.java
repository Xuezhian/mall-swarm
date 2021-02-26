package com.macro.mall.portal.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.common.constant.AuthConstant;
import com.macro.mall.common.domain.UserDto;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.mapper.UmsAreaMapper;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.mapper.UmsMemberReceiveAddressMapper;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.AuthCode;
import com.macro.mall.portal.domain.UmsAddress;
import com.macro.mall.portal.domain.WeChatModel;
import com.macro.mall.portal.service.AuthService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.portal.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 首页内容管理Service实现类
 * Created by macro on 2019/1/28.
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.appid}")
    private String AppID;
    @Value("${wechat.appsecret}")
    private String AppSecret;
    @Autowired
    private UmsMemberMapper umsMemberMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private UmsAreaMapper umsAreaMapper;
    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Override
    public CommonResult login(WeChatModel weChatModel) {
        RestTemplate restTemplate = new RestTemplate();
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="+AppID+"&secret="+AppSecret+"&js_code="+weChatModel.getCode()+"&grant_type=authorization_code";
        String result=restTemplate.getForObject(url,String.class);
        Gson gson=new Gson();
        Map<String,Object> resultMap=new HashMap<>();
        AuthCode authCode=gson.fromJson(result,AuthCode.class);
        if(authCode.getErrcode()!=null){
            String msg=null;
            if(authCode.getErrcode().equals("40029")){
                msg="code 无效";
            }
            if(authCode.getErrcode().equals("45011")){
                msg="频率限制，每个用户每分钟100次";
            }
            return  CommonResult.failed(msg);
        }else {
        UmsMemberExample umsMemberExample=new UmsMemberExample();
        umsMemberExample.createCriteria().andUsernameEqualTo(authCode.getOpenid());
        List<UmsMember> umsMembers= umsMemberMapper.selectByExample(umsMemberExample);
            UmsMember umsMember = new UmsMember();
        if(umsMembers.size()<1){
            resultMap.put("is_new",1);
            umsMember.setCity(weChatModel.getUserInfo().getUserInfo().getCity());
            umsMember.setCreateTime(new Date());
            umsMember.setGender(weChatModel.getUserInfo().getUserInfo().getGender());
            umsMember.setIcon(weChatModel.getUserInfo().getUserInfo().getAvatarUrl());
            umsMember.setNickname(weChatModel.getUserInfo().getUserInfo().getNickName());
            umsMember.setUsername(authCode.getOpenid());
            umsMember.setStatus(1);
            umsMember.setPassword(BCrypt.hashpw("123456"));
            umsMemberMapper.insert(umsMember);
        }else {
            resultMap.put("is_new",0);
            umsMember = umsMembers.get(0);
        }
            Map<String,Object> map=new HashMap<>();
//            String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
//            if(StrUtil.isEmpty(userStr)){
                Map<String, String> params = new HashMap<>();
                params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
                params.put("client_secret","123456");
                params.put("grant_type","password");
                params.put("username",authCode.getOpenid());
                params.put("password","123456");
                CommonResult token=authService.getAccessToken(params);
                map= gson.fromJson(token.getData().toString(),map.getClass());
//            }

            resultMap.put("userInfo",umsMember);
            resultMap.put("token",map.get("tokenHead").toString().trim()+" "+map.get("token").toString());
            return CommonResult.success(resultMap);
        }
    }

    @Override
    public CommonResult UserDetail() {
        UmsMember umsMember=getWeChatMember();
        umsMember.setPassword(null);
        return CommonResult.success(umsMember);
    }

    @Override
    public CommonResult UserSave( UmsMember umsMember) {
        UmsMember umsMemberOld=umsMemberService.getCurrentMember();
        umsMemberOld.setPhone(umsMember.getPhone());
        umsMemberOld.setRealName(umsMember.getRealName());
        return CommonResult.success(umsMemberMapper.updateByPrimaryKeySelective(umsMemberOld));
    }

    public UmsMember getWeChatMember() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
        UmsMember member = umsMemberMapper.selectByPrimaryKey(userDto.getId());
        return member;
    }

    @Override
    public CommonResult regionList(Integer parentId) {
        List<UmsArea> umsAreas=new ArrayList<>();
        if(parentId==1){
            UmsAreaExample umsAreaExample=new UmsAreaExample();
            umsAreaExample.createCriteria().andLevelEqualTo(1);
            umsAreas=umsAreaMapper.selectByExample(umsAreaExample);
        }else {
            UmsAreaExample umsAreaExample=new UmsAreaExample();
            umsAreaExample.createCriteria().andPidEqualTo(parentId);
            umsAreas=umsAreaMapper.selectByExample(umsAreaExample);
        }
        return CommonResult.success(umsAreas);
    }

    @Override
    public CommonResult saveAddress(UmsAddress umsAddress) {
        UmsMember umsMember=getWeChatMember();
        UmsMemberReceiveAddress umsMemberReceiveAddress=new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setName(umsAddress.getName());
        umsMemberReceiveAddress.setMemberId(umsMember.getId());
        umsMemberReceiveAddress.setProvince(umsAddress.getProvince_id().toString());
        umsMemberReceiveAddress.setCity(umsAddress.getCity_id().toString());
        umsMemberReceiveAddress.setRegion(umsAddress.getDistrict_id().toString());
        umsMemberReceiveAddress.setPhoneNumber(umsAddress.getMobile());
        umsMemberReceiveAddress.setDefaultStatus(umsAddress.getIs_default());
        umsMemberReceiveAddress.setDetailAddress(umsAddress.getAddress());
        if(umsAddress.getIs_default()==1){
            umsMemberReceiveAddressMapper.updateState(umsMember.getId());
        }
        if(umsAddress.getId()==0){
            umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
        }else{
            umsMemberReceiveAddress.setId(umsAddress.getId());
            umsMemberReceiveAddressMapper.updateByPrimaryKey(umsMemberReceiveAddress);
        }
        return CommonResult.success("保存成功");
    }

    @Override
    public CommonResult getAddresses() {
        UmsMember umsMember=getWeChatMember();
        UmsMemberReceiveAddressExample example=new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(umsMember.getId());
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=umsMemberReceiveAddressMapper.selectByExample(example);
        return CommonResult.success(umsMemberReceiveAddresses);
    }
    @Override
    public CommonResult addressDetail(Integer id) {
        UmsMemberReceiveAddress umsMemberReceiveAddress=umsMemberReceiveAddressMapper.selectByPrimaryKey(id.longValue());
        Map<String,Object> map=new HashMap<>();
        UmsArea umsArea=umsAreaMapper.selectByPrimaryKey(Integer.valueOf(umsMemberReceiveAddress.getProvince()));
        String fullRegion=umsArea.getName();
        map.put("province_id",umsArea.getId());
        map.put("province_name",umsArea.getName());
        umsArea=umsAreaMapper.selectByPrimaryKey(Integer.valueOf(umsMemberReceiveAddress.getCity()));
        fullRegion+=umsArea.getName();
        map.put("city_id",umsArea.getId());
        map.put("city_name",umsArea.getName());
        umsArea=umsAreaMapper.selectByPrimaryKey(Integer.valueOf(umsMemberReceiveAddress.getRegion()));
        fullRegion+=umsArea.getName();
        map.put("district_name",umsArea.getName());
        map.put("district_id",umsArea.getId());
        map.put("address",umsMemberReceiveAddress.getDetailAddress());
        map.put("full_region",fullRegion);
        map.put("name",umsMemberReceiveAddress.getName());
        map.put("mobile",umsMemberReceiveAddress.getPhoneNumber());
        map.put("is_default",umsMemberReceiveAddress.getDefaultStatus());
        map.put("id",umsMemberReceiveAddress.getId());
        return CommonResult.success(map);
    }
    @Override
    public CommonResult deleteAddress(Long id) {
        umsMemberReceiveAddressMapper.deleteByPrimaryKey(id);
        return CommonResult.success("删除成功");
    }
}
