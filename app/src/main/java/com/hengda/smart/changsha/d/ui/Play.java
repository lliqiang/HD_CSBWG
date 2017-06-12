package com.hengda.smart.changsha.d.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.http.HttpResponse;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.rfid.BatteryReceiver;
import com.hengda.smart.changsha.d.widget.dialog.ExistDialog;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class Play extends BaseActivity implements View.OnClickListener,BatteryReceiver.MyListener {

    @Bind(R.id.web_play)
    WebView webPlay;
    @Bind(R.id.img_play)
    ImageView imgPlay;
    @Bind(R.id.back_play)
    ImageView backPlay;
    @Bind(R.id.play)
    ImageView play;
    @Bind(R.id.startime)
    TextView startime;
    @Bind(R.id.seekbar)
    SeekBar seekbar;
    @Bind(R.id.totaltime)
    TextView totaltime;
    @Bind(R.id.big_txt)
    TextView bigTxt;
    @Bind(R.id.small_txt)
    TextView smallTxt;
    @Bind(R.id.activity_play)
    LinearLayout activityPlay;
    private Exhibition exhibition;
    private MediaPlayer mediaPlayer;
    private ExistDialog existDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int current = mediaPlayer.getCurrentPosition();
                    seekbar.setProgress(current);
                    current /= 1000;
                    int minute = current / 60;
                    int second = current % 60;
                    startime.setText(String.format("%02d:%02d", minute, second));
                    //每隔500ms通过handler回传一次数据
                    sendEmptyMessageDelayed(1, 500);
                    break;

            }
        }
    };
    private String path1;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        existDialog = new ExistDialog(this);
        smallTxt.setTextColor(getResources().getColor(R.color.tranBlackDark));
        bigTxt.setTextColor(getResources().getColor(R.color.colorAccent));

        initlistner();
        exhibition = (Exhibition) getIntent().getSerializableExtra("exhibition");
        RequestApi.getInstance().lookCount(new Subscriber<HttpResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResponse httpResponse) {

            }
        },exhibition.getExhibit_id());

        initUI();
        loadExhibit(exhibition, handler);
    }

    private void initlistner() {
        play.setOnClickListener(this);
        backPlay.setOnClickListener(this);
        bigTxt.setOnClickListener(this);
        smallTxt.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
        play.setImageResource(R.drawable.play_icon);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        play.setImageResource(R.drawable.play_icon);

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
            case R.id.play:
                if (isPause) {
                    doPlay();
                } else {
                    doPause();
                }
                isPause = !isPause;
                break;
            case R.id.back_play:
                if (!startime.getText().toString().equals(totaltime.getText().toString())) {
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
            case R.id.big_txt:
                bigTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                smallTxt.setTextColor(getResources().getColor(R.color.tranBlackDark));
                webPlay.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                break;

            case R.id.small_txt:
                bigTxt.setTextColor(getResources().getColor(R.color.tranBlackDark));
                smallTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                webPlay.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                break;
        }

    }

    //开始播并且设置暂停图片
    public void doPlay() {
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause_icon);
    }

    //暂停播放并设置播放图片
    public void doPause() {
        mediaPlayer.pause();
        play.setImageResource(R.drawable.play_icon);
    }

    public void playPrepare() {
        //设置播放时长
        int duration = mediaPlayer.getDuration();
        seekbar.setMax(duration);
        int minute = duration / 1000 / 60;
        int second = (duration / 1000) % 60;
        //将分秒格式化
        totaltime.setText(String.format("%02d:%02d", minute, second));
        startime.setText(String.format("%02d:%02d", 0, 0));

    }

    public void loadExhibit(Exhibition exhibition, Handler handler) {
        //获取当前展品的信息后，设置展品的title、图片和对应的WebView，播放视频
        String url = HdAppConfig.getFilePath(exhibition.getExhibit_id()) + exhibition.getExhibit_id() + "_small.html";

        WebSettings settings = webPlay.getSettings();
        webPlay.setBackgroundColor(0);
        webPlay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webPlay.loadUrl("file:///" + url);
        String path = HdAppConfig.getFilePath(exhibition.getExhibit_id()) + exhibition.getExhibit_id() + ".mp3";
        path1 = HdAppConfig.getImgPath(exhibition.getExhibit_id()) + "litimg.png";
        Glide.with(Play.this)
                .load(path1)
                .into(imgPlay);
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
        addSeekbarChangeListener(seekbar);
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
                    Play.this.finish();
                }
            }
        });

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
            if (!startime.getText().toString().equals(totaltime.getText().toString())) {
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
