package com.hengda.smart.changsha.d.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.StatusBarCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IntroActicity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.intro_webview)
    WebView introWebview;
    @Bind(R.id.back_webintro)
    ImageView backWebintro;
    private int lan = 0;
    private String path;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_acticity);
        ButterKnife.bind(this);
        backWebintro.setOnClickListener(this);
        StatusBarCompat.translucentStatusBar(this);
        settings = introWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        introWebview.requestFocus();
        introWebview.setBackgroundColor(0);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setTextSize(WebSettings.TextSize.LARGER);
        settings.setDisplayZoomControls(false);
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);


        introWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        if (HdAppConfig.getLanguage().equals("CHINESE")) {
            lan = 1;
        } else if (HdAppConfig.getLanguage().equals("ENGLISH")) {
            lan = 2;
        } else if (HdAppConfig.getLanguage().equals("JAPANESE")) {
            lan = 3;
        } else {
            lan = 4;
        }
        path = HdAppConfig.getDefaultFileDir() + "introduction/" + "introduction" + lan + ".html";
        Log.i("path", "path: ----------------------" + path);
        introWebview.loadUrl("file:///" + path);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
