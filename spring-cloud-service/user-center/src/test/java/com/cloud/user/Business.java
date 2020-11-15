package com.cloud.user;


import com.mergeplus.annonation.MergeField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    @MergeField(key = "test", sourceKey = "value", client = StaticHttpClient.class, method = "merge", cache = "user-center:test")
    private String valueName;

    private String value2;

    @MergeField(key = "test", sourceKey = "value2", client = StaticFeignClient.class, method = "merge")
    private String valueName2;


    private String value3;

    @MergeField(key = "test", sourceKey = "value3", client = StaticFeignClient.class, method = "merge", cache = "user-center:test")
    private String valueName3;

}
