package com.kxwon.bingweather.ui.activity;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.gson.Forecast;
import com.kxwon.bingweather.gson.Weather;
import com.kxwon.bingweather.utils.HttpUtils;
import com.kxwon.bingweather.utils.PrefUtils;
import com.kxwon.bingweather.utils.ToastUtils;
import com.kxwon.bingweather.utils.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.btn_menu)
    Button btnMenu;
    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_bing_pic)
    ImageView ivBingPic;
    @BindView(R.id.tv_title_city)
    TextView tvTitleCity;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_degree)
    TextView tvDegree;
    @BindView(R.id.tv_weather_info)
    TextView tvWeatherInfo;
    @BindView(R.id.ll_forecast_layout)
    LinearLayout llForecastLayout;
    @BindView(R.id.tv_aqi)
    TextView tvAqi;
    @BindView(R.id.tv_pm25)
    TextView tvPm25;
    @BindView(R.id.tv_comfort)
    TextView tvComfort;
    @BindView(R.id.tv_car_wash)
    TextView tvCarWash;
    @BindView(R.id.tv_sport)
    TextView tvSport;
    @BindView(R.id.sv_weather_layout)
    ScrollView svWeatherLayout;

    private String mWeatherId;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initView() {
        // 下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        // 天气信息
        String weatherString = PrefUtils.getString(this, Constant.Pref.WEATHER, null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherReponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            //String weatherId = getIntent().getStringExtra(Constant.WEATHER_ID);
            mWeatherId = getIntent().getStringExtra(Constant.WEATHER_ID);
            svWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        // 下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        // 必应图片
        String bingPic = PrefUtils.getString(this,Constant.Pref.BRING_PIC,null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(ivBingPic);
        }else {
            loadBingPic();
        }

        // 打开抽屉
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = Constant.URL_BEING_PIC;
        HttpUtils.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                PrefUtils.setString(WeatherActivity.this,Constant.Pref.BRING_PIC,bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(ivBingPic);
                    }
                });
            }
        });
    }

    /**
     * 根据天气 id 请求城市天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = Constant.URL_WEATHER_BASE + weatherId + "&key=" + Constant.URL_WEATHER_KEY;
        HttpUtils.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("获取天气信息失败");
                        swipeRefresh.setRefreshing(false);// 刷新结束，隐藏刷新进度条
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherReponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            PrefUtils.setString(WeatherActivity.this, Constant.Pref.WEATHER, responseText);
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            ToastUtils.showShort("获取天气信息失败");
                        }
                        swipeRefresh.setRefreshing(false);// 刷新结束，隐藏刷新进度条
                    }
                });
            }
        });
    }

    /**
     * 处理并展示 Weather 实体类中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        tvTitleCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(degree);
        tvWeatherInfo.setText(weatherInfo);

        llForecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, llForecastLayout, false);
            TextView tvDate = ButterKnife.findById(view, R.id.tv_date);
            TextView tvInfo = ButterKnife.findById(view, R.id.tv_info);
            TextView tvMax = ButterKnife.findById(view, R.id.tv_max);
            TextView tvMin = ButterKnife.findById(view, R.id.tv_min);
            tvDate.setText(forecast.date);
            tvInfo.setText(forecast.more.info);
            tvMax.setText(forecast.temperature.max);
            tvMin.setText(forecast.temperature.min);
            llForecastLayout.addView(view);
        }

        if (weather.aqi != null){
            tvAqi.setText(weather.aqi.city.aqi);
            tvPm25.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度" + weather.suggestion.comfort.info;
        String carWash = "洗车指数" + weather.suggestion.comfort.info;
        String sport = "运动建议" + weather.suggestion.sport.info;
        tvComfort.setText(comfort);
        tvCarWash.setText(carWash);
        tvSport.setText(sport);

        svWeatherLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {

    }
}
