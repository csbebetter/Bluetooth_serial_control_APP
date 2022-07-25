package com.harrysoft.androidbluetoothserial.demoapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.pic3_about)//图片
                .setDescription("洋田婴儿车控制器")//介绍
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("与我联系")
                .addEmail("csbebetter@outlook.com")//邮箱
                .addWebsite("https://www.cnblogs.com/litecdows/")//网站
                .addGitHub("csbebetter")//github
                .create();

        relativeLayout.addView(aboutPage);
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
        relativeLayout= (RelativeLayout) findViewById(R.id.about_activity_relativeLayout);
        toolbar= (Toolbar) findViewById(R.id.about_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}


