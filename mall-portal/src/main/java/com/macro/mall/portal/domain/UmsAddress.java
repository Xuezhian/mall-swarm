package com.macro.mall.portal.domain;

import lombok.Data;

@Data
public class UmsAddress {
    private Long id;
    private String address;
    private Integer city_id;
    private Integer district_id;
    private Integer is_default;
    private String mobile;
    private String name;
    private Integer province_id;
}
