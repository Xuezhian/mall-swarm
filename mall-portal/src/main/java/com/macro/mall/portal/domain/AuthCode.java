package com.macro.mall.portal.domain;

import lombok.Data;

@Data
public class AuthCode {
    private String openid;
    private String session_key;
    private String unionid;
    private String errcode;
    private String errmsg;
}
