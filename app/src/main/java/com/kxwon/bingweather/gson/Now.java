package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:19
 * Email：kxwonder@163.com
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
