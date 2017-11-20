package com.hengda.smart.changsha.d.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.http.HttpResponse;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.model.Lchinse;
import com.hengda.smart.changsha.d.model.ScanBean;
import com.hengda.smart.changsha.d.rfid.BatteryReceiver;
import com.hengda.smart.changsha.d.widget.dialog.ExistDialog;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class TemporaryPlay extends BaseActivity implements View.OnClickListener, BatteryReceiver.MyListener {


    @Bind(R.id.temp_back_play)
    ImageView tempBackPlay;
    @Bind(R.id.temp_play)
    ImageView tempPlay;
    @Bind(R.id.temp_seekbar)
    SeekBar tempSeekbar;
    @Bind(R.id.temp_startime)
    TextView tempStartime;
    @Bind(R.id.temp_totaltime)
    TextView tempTotaltime;
    private Lchinse lchinse;
    private MediaPlayer mediaPlayer;
    private ExistDialog existDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int current = mediaPlayer.getCurrentPosition();
                    tempSeekbar.setProgress(current);
                    current /= 1000;
                    int minute = current / 60;
                    int second = current % 60;
                    tempStartime.setText(String.format("%02d:%02d", minute, second));
                    //每隔500ms通过handler回传一次数据
                    sendEmptyMessageDelayed(1, 500);
                    break;

            }
        }
    };

    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_play);
        ButterKnife.bind(this);
        existDialog = new ExistDialog(this);
        initlistner();
        lchinse = (Lchinse) getIntent().getSerializableExtra("lchinese");
        scanOrPlay();
        initUI();
        loadExhibit(lchinse, handler);
    }

    private void initlistner() {
        tempPlay.setOnClickListener(this);
        tempBackPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
        tempPlay.setImageResource(R.drawable.play_icon);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tempPlay.setImageResource(R.drawable.play_icon);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mediaPlayer != null) {

            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.temp_play:
                if (isPause) {
                    doPlay();
                } else {
                    doPause();
                }
                isPause = !isPause;
                break;
            case R.id.temp_back_play:
                if (!tempStartime.getText().toString().equals(tempTotaltime.getText().toString())) {
                    if (!existDialog.isShowing()) {
                        existDialog.setTypeface(HdApplication.typeface).message(getString(R.string.is_exist)).nBtnText(getString(R.string.cancel))
                                .nBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        existDialog.dismiss();
                                    }
                                }).pBtnText(getString(R.string.sure))
                                .pBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        existDialog.dismiss();
                                        finish();
                                    }
                                }).show();
                    }
                }
                break;
        }

    }

    //开始播并且设置暂停图片
    public void doPlay() {
        mediaPlayer.start();
        tempPlay.setImageResource(R.drawable.pause_icon);
    }

    //暂停播放并设置播放图片
    public void doPause() {
        mediaPlayer.pause();
        tempPlay.setImageResource(R.drawable.play_icon);
    }

    public void playPrepare() {
        //设置播放时长
        int duration = mediaPlayer.getDuration();
        tempSeekbar.setMax(duration);
        int minute = duration / 1000 / 60;
        int second = (duration / 1000) % 60;
        //将分秒格式化
        tempTotaltime.setText(String.format("%02d:%02d", minute, second));
        tempStartime.setText(String.format("%02d:%02d", 0, 0));

    }

    public void loadExhibit(Lchinse lchinse, Handler handler) {

        String path = HdAppConfig.getTempPath(lchinse.getExhibit_id()) + lchinse.getExhibit_id() + ".mp3";

        mediaPlayer.reset();
        try {
            //设置播放资源
            mediaPlayer.setDataSource(path);
            //准备播放
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doPlay();
        handler.sendEmptyMessage(1);
    }

    private void initUI() {
//        seekbar.setProgressDrawable(getResources().getDrawable(R.drawable
//                .seek_bar_progress_drawable));
        addSeekbarChangeListener(tempSeekbar);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    playPrepare();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    TemporaryPlay.this.finish();
                }
            }
        });

    }

    //浏览、播放加一
    private void scanOrPlay() {
        RequestApi.getInstance().scanCount(new Subscriber<ScanBean>() {
            @Override
            public void onCompleted() {
                RequestApi.getInstance().scanCount(new Subscriber<ScanBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ScanBean scanBean) {

                    }
                }, lchinse.getExhibit_id(), "3", "2");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ScanBean scanBean) {

            }
        }, lchinse.getExhibit_id(), "1", "2");
    }

    //添加seekBar的监听事件
    private void addSeekbarChangeListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!tempStartime.getText().toString().equals(tempTotaltime.getText().toString())) {
                if (!existDialog.isShowing()) {
                    existDialog.setTypeface(HdApplication.typeface).message(getString(R.string.is_exist)).nBtnText(getString(R.string.cancel))
                            .nBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    existDialog.dismiss();
                                }
                            }).pBtnText(getString(R.string.sure))
                            .pBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    existDialog.dismiss();
                                    finish();
                                }
                            }).show();
                }
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onListener(String level) {

    }
}
