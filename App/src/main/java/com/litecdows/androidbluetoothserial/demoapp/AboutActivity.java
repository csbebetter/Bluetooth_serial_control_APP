package com.litecdows.androidbluetoothserial.demoapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
//import android.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


/**
 * Created by Administrator on 2017/5/2.
 */

public class AboutActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.pic3_about)//图片
                .setDescription("洋田婴儿车控制器")//介绍
                .addItem(new Element().setTitle("Version 2.1"))
                .setDescription("本软件由华中科技大学FOCUS团队开发，源码已上传Github，遵循相关开源协议")
                .addGroup("FOCUS团队")
                .addEmail("csbebetter@outlook.com")//邮箱
                .addWebsite("http://101.35.42.214/official/")//网站
                .addPlayStore("")
                .addGitHub("csbebetter")//github
                .create();
        relativeLayout.addView(aboutPage);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        StatusBarUtil.setStatusBarLightMode(getWindow());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return true;
    }

    private void initViews(){
        relativeLayout= findViewById(R.id.about_activity_relativeLayout);
        Toolbar toolbar = findViewById(R.id.about_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}


