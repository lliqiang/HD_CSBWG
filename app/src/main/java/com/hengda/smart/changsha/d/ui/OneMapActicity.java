package com.hengda.smart.changsha.d.ui;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.common.util.FragmentUtil;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.model.MapModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class OneMapActicity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mapContainer)
    FrameLayout mapContainer;
    @Bind(R.id.back_onemap)
    ImageButton backOnemap;
    @Bind(R.id.map_txt)
    TextView mapTxt;
    private int unitNO;
    private FragmentTransaction ft;
    private MapFrg mapFrg;
    private Exhibition exhibitionF;
    private List<MapModel> mapModels;
    private MapModel mapModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_map_acticity);
        ButterKnife.bind(this);
        mapModels = new ArrayList<>();
        RxBus.getDefault().toObservable(Exhibition.class).subscribe(new Action1<Exhibition>() {
            @Override
            public void call(Exhibition exhibition) {

                exhibitionF = exhibition;

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i("throwable", throwable.toString());
            }
        });
        if (exhibitionF != null) {
            unitNO = exhibitionF.getMap_id();
        } else {

            unitNO = getIntent().getIntExtra("unitno", 0);
        }
        switch (HdAppConfig.getLanguage()) {
            case "CHINESE":
                mapModel = QueryByUnitNo(unitNO, 1);
                mapTxt.setText(mapModel.getName());
                break;
            case "ENGLISH":
                mapModel = QueryByUnitNo(unitNO, 2);
                mapTxt.setText(mapModel.getName().toString());
                break;
            case "JAPANESE":
                mapModel = QueryByUnitNo(unitNO, 3);
                mapTxt.setText(mapModel.getName().toString());
                break;
            case "KOREAN":
                mapModel = QueryByUnitNo(unitNO, 4);
                mapTxt.setText(mapModel.getName().toString());
                break;
        }


        showcMap();
        backOnemap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }


    /**
     * 显示地图
     */
    private void showcMap() {
        ft = getFragmentManager().beginTransaction();
        if (mapFrg == null) {
            mapFrg = MapFrg.newInstance(unitNO);
            FragmentUtil.addFragment(getFragmentManager(), R.id.mapContainer, mapFrg, "mapFrg", false, false);
        }
        ft.show(mapFrg);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ft.remove(mapFrg);
    }

    //根据map_id查询mapModel表
    public MapModel QueryByUnitNo(int unitNo, int language) {
        mapModels.clear();
        Cursor cursor = HResDdUtil.getInstance().QueryByUnitNo(unitNo, language);
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                MapModel mapModel = MapModel.CursorToMap(cursor);
                mapModels.add(mapModel);
            }
            cursor.close();
        }
        return mapModels.get(0);
    }
}
