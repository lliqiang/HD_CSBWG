package com.hengda.smart.changsha.d.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.zxing.WriterException;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BarCodeActivity extends BaseActivity {


    @Bind(R.id.ivPhoto)
    ImageView mIvPhoto;
    @Bind(R.id.tvDeviceNum)
    TextView mTvDeviceNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_avtivity);

        ButterKnife.bind(this);

        performBarCode();
    }

    private void performBarCode() {
        String content = HdAppConfig.getDeviceNo();
        mTvDeviceNum.setText(getString(R.string.device_no)+content);

//        CustomDialog dialog = new CustomDialog(BarCodeActivity.this, R.style.common_dialog_style);
//        View view = View.inflate(BarCodeActivity.this, R.layout.layout_img_bar_code, null);
//        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        Bitmap bitmap = null;
        try {
            bitmap = EncodingHandler.createOneDCode(content);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mIvPhoto.setImageBitmap(bitmap);
//        ivPhoto.setImageBitmap(bitmap);
//        dialog.setContentView(R.layout.layout_img_bar_code);
//        dialog.show();
    }

    @OnClick(R.id.btnPre)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
