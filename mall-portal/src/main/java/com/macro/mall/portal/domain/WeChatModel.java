package com.macro.mall.portal.domain;

import lombok.Data;

@Data
public class WeChatModel {
    private  String code;

    private UserInfo userInfo;
    @Data
    public  class UserInfo{
        private String encryptedData;
        private String errMsg;
        private String iv;
        private String rawData;
        private String signature;
        private WeChatUser userInfo;
        @Data
        public  class WeChatUser{
            private String avatarUrl;
            private String city;
            private String country;
            private Integer  gender;
            private String language;
            private String nickName;
            private String province;
        }
    }


}

