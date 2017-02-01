package com.kxwon.bingweather.gson;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:18
 * Email：kxwonder@163.com
 */

public class AQI {

    public AQICity city;

    public class AQICity{

        public String aqi;

        public String pm25;
    }
}
