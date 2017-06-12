package com.hengda.smart.changsha.d.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.model.AutoNum;
import com.hengda.smart.changsha.d.rfid.RfidNoService;
import com.hengda.smart.changsha.d.ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgramText extends BaseActivity {

    @Bind(R.id.rfid_text)
    TextView rfidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_text);
        ButterKnife.bind(this);
        startService(new Intent(this, RfidNoService.class));
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {

        rfidText.setText("当前FRID为：   "+event.getAutoNum());

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);

    }
}
