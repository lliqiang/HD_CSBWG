package com.hengda.smart.changsha.d.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.model.Lchinse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalActivity extends BaseActivity {

    @Bind(R.id.input)
    TextView input;
    @Bind(R.id.one)
    ImageView one;
    @Bind(R.id.two)
    ImageView two;
    @Bind(R.id.three)
    ImageView three;
    @Bind(R.id.four)
    ImageView four;
    @Bind(R.id.five)
    ImageView five;
    @Bind(R.id.backspace)
    ImageView backspace;
    @Bind(R.id.six)
    ImageView six;
    @Bind(R.id.seven)
    ImageView seven;
    @Bind(R.id.eight)
    ImageView eight;
    @Bind(R.id.nine)
    ImageView nine;
    @Bind(R.id.zero)
    ImageView zero;
    @Bind(R.id.sure_yes)
    ImageView sureYes;
    @Bind(R.id.enter_digital)
    ImageView enterDigital;
    String pwd = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital);
        ButterKnife.bind(this);



    }

    private void clearInput() {
        pwd = "";
        input.setText(getString(R.string.input_num));
    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.backspace,
            R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.sure_yes, R.id.enter_digital})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                clickNum(1);
                break;
            case R.id.two:
                clickNum(2);
                break;
            case R.id.three:
                clickNum(3);
                break;
            case R.id.four:
                clickNum(4);
                break;
            case R.id.five:
                clickNum(5);
                break;
            case R.id.backspace:
                clickClear();
                break;
            case R.id.six:
                clickNum(6);
                break;
            case R.id.seven:
                clickNum(7);
                break;
            case R.id.eight:
                clickNum(8);
                break;
            case R.id.nine:
                clickNum(9);
                break;
            case R.id.zero:
                clickNum(0);
                break;
            case R.id.sure_yes:
                clickOk();
                break;
            case R.id.enter_digital:
                finish();
                break;
        }
    }

    private void clickNum(int num) {
        if (pwd.length() < 8) {
            pwd += num;
            input.setText(pwd);
        } else {
            clearInput();
        }
    }

    private void clickClear() {
        if (pwd.length() > 1) {
            pwd = pwd.substring(0, pwd.length() - 1);
            input.setText(pwd);
        } else {
            clearInput();
        }
    }

    private void clickOk() {
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, R.string.pwd_empty, Toast.LENGTH_SHORT).show();

        } else {
            // TODO: 2017/3/3 根据数据查询数据库展品

                Cursor cursor = HResDdUtil.getInstance().QueryExhibitByFileNO(pwd);
                Cursor cursor1 = HResDdUtil.getInstance().QueryTexhibitByFileNo(pwd);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        Exhibition exhibition = Exhibition.CursorToModel(cursor);
                        Intent intent = new Intent(DigitalActivity.this, Play.class);
                        intent.putExtra("exhibition", exhibition);
                        startActivity(intent);
                    }
                    cursor.close();
                }
                else if (cursor1.getCount()>0){
                    if (cursor1.moveToFirst()) {
                        Lchinse lchinse = Lchinse.CursorToModel(cursor1);
                        Intent intent = new Intent(DigitalActivity.this, TemporaryPlay.class);
                        intent.putExtra("lchinese",lchinse);
                        startActivity(intent);
                    }
                    cursor1.close();
                }else {
                    pwd = "";
                    input.setText(getString(R.string.input_num));
                    Toast.makeText(this, R.string.file_exist, Toast.LENGTH_SHORT).show();
                }



        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
