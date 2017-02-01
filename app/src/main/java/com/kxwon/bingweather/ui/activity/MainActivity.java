package com.kxwon.bingweather.ui.activity;

import android.content.Intent;

import com.kxwon.bingweather.R;
import com.kxwon.bingweather.base.BaseActivity;
import com.kxwon.bingweather.common.Constant;
import com.kxwon.bingweather.utils.PrefUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if (PrefUtils.getString(this, Constant.Pref.WEATHER,null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initData() {

    }

}
