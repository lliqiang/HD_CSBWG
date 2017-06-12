package com.hengda.smart.changsha.d.ui;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.FragmentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.container_map)
    FrameLayout containerMap;
    @Bind(R.id.activity_map)
    RelativeLayout activityMap;
    @Bind(R.id.F2_map)
    ImageView F2Map;
    @Bind(R.id.F1_map)
    ImageView F1Map;
    @Bind(R.id.back_map)
    ImageView backMap;
    private android.app.FragmentTransaction ft;
    private MapFloorOne mapFloorOne;
    private MapFloorTwo mapFloorTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);
        F1Map.setOnClickListener(this);
        F2Map.setOnClickListener(this);
        backMap.setOnClickListener(this);
        F1Map.setSelected(true);
        showcF1Map();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.F1_map:
                showcF1Map();
                F1Map.setSelected(true);
                F2Map.setSelected(false);
                break;
            case R.id.F2_map:
                showcF2Map();
                F1Map.setSelected(false);
                F2Map.setSelected(true);
                break;
            case R.id.back_map:
                finish();
                break;
        }
    }

    /**
     * 显示正常地图
     */
    private void showcF1Map() {

        ft = getFragmentManager().beginTransaction();
        if (mapFloorTwo != null) {

            ft.hide(mapFloorTwo);

        }


        if (mapFloorOne == null) {
            mapFloorOne = new MapFloorOne();
            FragmentUtil.addFragment(getFragmentManager(), R.id.container_map, mapFloorOne, "mapFloorOne", true, true);
        }
        ft.show(mapFloorOne);
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示正常地图
     */
    private void showcF2Map() {

        ft = getFragmentManager().beginTransaction();
        if (mapFloorOne != null) {

            ft.hide(mapFloorOne);

        }


        if (mapFloorTwo == null) {
            mapFloorTwo = new MapFloorTwo();
            FragmentUtil.addFragment(getFragmentManager(), R.id.container_map, mapFloorTwo, "mapFloorTwo", true, false);
        }
        ft.show(mapFloorTwo);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }
}
