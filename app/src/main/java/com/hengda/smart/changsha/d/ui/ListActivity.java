package com.hengda.smart.changsha.d.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.adapter.LCommonAdapter;
import com.hengda.smart.changsha.d.adapter.ViewHolder;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.model.AutoNum;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.model.MapModel;
import com.hengda.smart.changsha.d.rfid.RfidNoService;
import com.hengda.smart.changsha.d.widget.dialog.ListDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.list_list)
    ListView listList;
    @Bind(R.id.back_scroll)
    ImageView backScroll;

    private Intent intent;
    private List<Exhibition> allExhibiList;
    private int lastNum = -1;
    private ListDialog listDialog;
    private List<MapModel> modelList;
    private LCommonAdapter<MapModel> adapter;
    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_layout);
        ButterKnife.bind(this);
        serviceIntent = new Intent(this, RfidNoService.class);
        if (HdAppConfig.getAutoFlag() == 1) {

            if (HdAppConfig.getAutoPlay()) {
                startService(serviceIntent);
                EventBus.getDefault().register(this);
            } else {
                EventBus.getDefault().unregister(this);
                stopService(serviceIntent);
            }
        } else {
            stopService(serviceIntent);
        }
        backScroll.setOnClickListener(this);
        allExhibiList = new ArrayList();
        modelList = new ArrayList<>();
        listDialog = new ListDialog(ListActivity.this);
        getAllData();
        switch (HdAppConfig.getLanguage()) {
            case "CHINESE":
                QueryMapInfo(1);
                break;

            case "JAPANESE":
                QueryMapInfo(3);
                break;
            case "ENGLISH":
                QueryMapInfo(2);
                break;
            case "KOREAN":
                QueryMapInfo(4);
                break;
        }
        listList.setAdapter(adapter = new LCommonAdapter<MapModel>(this, R.layout.list_listview, modelList) {
            private TextView listName;
            private TextView unitName;
            private ImageView imglist;

            @Override
            public void convert(ViewHolder holder, MapModel mapModel) {
                listName = ((TextView) holder.getView(R.id.name_list));
                listName.setTypeface(HdApplication.typeface);
                listName.setText(mapModel.getName());
                listName.setSelected(true);
                imglist = ((ImageView) holder.getView(R.id.img_list));
                Glide.with(ListActivity.this).load(HdAppConfig.getMapFilePath(HdAppConfig.getLanguage(), Integer.parseInt(mapModel.getMap_id())) + "/" + "list.png").placeholder(R.drawable.default_img).into(imglist);
            }
        });
        listList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(ListActivity.this, ChildListActivity.class);
                intent.putExtra("unitNo", Integer.parseInt(modelList.get(i).getMap_id()));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        for (int i = 0; i < allExhibiList.size(); i++) {
            if (event.getAutoNum() == allExhibiList.get(i).getAutonum() && isReplay(event.getAutoNum())) {

                int finalI = i;

                listDialog.message(getString(R.string.auto_voice) + allExhibiList.get(i).getByname())
                        .nBtnText(getString(R.string.cancel)).nBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listDialog.dismiss();
                    }
                }).pBtnText(getString(R.string.listener))
                        .pBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listDialog.dismiss();
                                Intent intent = new Intent(ListActivity.this, Play.class);
                                intent.putExtra("exhibition", allExhibiList.get(finalI));
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }

    private void getAllData() {
        Cursor cursor = HResDdUtil.getInstance().QueryByTableExhibition(HdAppConfig.getLanguage());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.CursorToModel(cursor);
                allExhibiList.add(exhibition);

            }
            cursor.close();
        }
    }

    private boolean isReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastNum) {
            lastNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        stopService(serviceIntent);
    }

    public void QueryMapInfo(int language) {
        modelList.clear();
        Cursor cursor = HResDdUtil.getInstance().QueryMapInfo(language);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MapModel mapModel = MapModel.CursorToMap(cursor);
                modelList.add(mapModel);

            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

}
