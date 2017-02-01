package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:10
 * Email：kxwonder@163.com
 */

public class Basic {

    @SerializedName("city")
    public String cityName;// 城市名

    @SerializedName("id")
    public String weatherId;// 城市对应的天气id

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;// 更新时间
    }
}
