package com.hengda.smart.changsha.d.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListTipActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_list_tip)
    LinearLayout activityListTip;
    @Bind(R.id.tip_iamage)
    ImageView tipIamage;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tip);
        ButterKnife.bind(this);
        activityListTip.setOnClickListener(this);
        if (HdAppConfig.getLanguage().equals("CHINESE")){
            tipIamage.setImageResource(R.drawable.cn_tip);
        }else if (HdAppConfig.getLanguage().equals("ENGLISH")){
            tipIamage.setImageResource(R.drawable.tip_en);
        }else if (HdAppConfig.getLanguage().equals("JAPANESE")){
            tipIamage.setImageResource(R.drawable.japa_tip);
        }else {
            tipIamage.setImageResource(R.drawable.korea_tip);
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                openActivity(ListTipActivity.this, ListActivity.class);
                finish();
            }
        }, 5000);
    }

    @Override
    public void onClick(View view) {
        openActivity(ListTipActivity.this, ListActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
