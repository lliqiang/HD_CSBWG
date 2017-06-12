package com.hengda.smart.changsha.d.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.hengda.smart.changsha.d.admin.DRfidReceiver;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.model.AutoNum;
import com.hengda.smart.changsha.d.model.Exhibit;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.rfid.NumService;
import com.hengda.smart.changsha.d.rfid.RfidNoService;
import com.hengda.smart.changsha.d.widget.dialog.ListDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChildListActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.clistview)
    ListView clistview;
    @Bind(R.id.cback_list)
    ImageView cbackList;
    private int unitNo;
    private List<Exhibition> allExhibiList;
    String path;
    private List<Exhibition> exhibitionList;
    private LCommonAdapter<Exhibition> adapter;
    private ListDialog listDialog;
    private int lastNum = -1;
    private TextView nameItem;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childlist);
        ButterKnife.bind(this);

        serviceIntent = new Intent(this, RfidNoService.class);
        if (HdAppConfig.getAutoFlag() == 1) {
            if (HdAppConfig.getAutoPlay()) {
                startService(serviceIntent);
                EventBus.getDefault().register(this);
            } else {

                stopService(serviceIntent);
                EventBus.getDefault().unregister(this);
            }
        }
        unitNo = getIntent().getIntExtra("unitNo", 0);
        exhibitionList = new ArrayList<>();
        listDialog = new ListDialog(ChildListActivity.this);
        allExhibiList = new ArrayList();
        getAllData();
        if (unitNo != 0) {
            getExhibitionData(unitNo);
        }
        cbackList.setOnClickListener(this);
        adapter = new LCommonAdapter<Exhibition>(ChildListActivity.this, R.layout.item_listview, exhibitionList) {
            private ImageView ChildImg;

            @Override
            public void convert(ViewHolder holder, Exhibition exhibition) {
                nameItem = ((TextView) holder.getView(R.id.name_item));
                nameItem.setTypeface(HdApplication.typeface);
                nameItem.setSelected(true);
                nameItem.setText(exhibition.getByname());
                ChildImg = holder.getView(R.id.img_listview);
                path = HdAppConfig.getImgPath(exhibition.getExhibit_id()) + "img0.png";
                Glide.with(ChildListActivity.this).load(path).placeholder(R.drawable.default_img).into(ChildImg);
            }
        };
        clistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChildListActivity.this, Play.class);
                intent.putExtra("exhibition", exhibitionList.get(i));
                startActivity(intent);
            }
        });
    }

    private void getExhibitionData(int map_id) {
        Cursor cursor = HResDdUtil.getInstance().loadAllExhibit(map_id);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.CursorToModel(cursor);
                exhibitionList.add(exhibition);

            }
        }
        cursor.close();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        for (int i = 0; i < allExhibiList.size(); i++) {

//
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
                                Intent intent = new Intent(ChildListActivity.this, Play.class);
                                intent.putExtra("exhibition", allExhibiList.get(finalI));
                                startActivity(intent);
                            }
                        }).show();


            }
        }
    }

    @Override
    public void onClick(View view) {
        finish();
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
        stopService(serviceIntent);
    }
}
