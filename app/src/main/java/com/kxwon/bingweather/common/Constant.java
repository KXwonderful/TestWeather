package com.kxwon.bingweather.common;

/**
 * Function：常量
 * Author：kxwon on 2017/2/1 16:39
 * Email：kxwonder@163.com
 */

public class Constant {

    public class Pref{
        public static final String WEATHER = "weather";// 天气
        public static final String BRING_PIC = "bing_pic";// 必应图片
    }

    // 省市县
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    // 获取地区url
    public static final String URL_ADDRESS_BASE = "http://guolin.tech/api/china";

    // 获取必应每日一图url
    public static final String URL_BEING_PIC = "http://guolin.tech/api/bing_pic";

    // 获取天气url
    public static final String URL_WEATHER_BASE = "http://guolin.tech/api/weather?cityid=";
    public static final String URL_WEATHER_KEY = "7a19535621fe46f9ad4b6491b3970b98";

    // 天气 id
    public static final String WEATHER_ID = "weather_id";


}
