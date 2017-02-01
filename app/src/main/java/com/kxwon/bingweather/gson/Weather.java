package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:28
 * Email：kxwonder@163.com
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
