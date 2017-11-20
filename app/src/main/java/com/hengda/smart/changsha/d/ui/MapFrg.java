package com.hengda.smart.changsha.d.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hengda.frame.tileview.HDTileView;
import com.hengda.frame.tileview.bean.Marker;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.admin.DRfidReceiver;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.bottomdialog.BottomDialog;
import com.hengda.smart.changsha.d.common.util.BitmapUtils;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.model.AutoNum;
import com.hengda.smart.changsha.d.model.Exhibition;
import com.hengda.smart.changsha.d.model.MapBase;
import com.hengda.smart.changsha.d.model.Mark;
import com.hengda.smart.changsha.d.rfid.NumService;
import com.hengda.smart.changsha.d.rfid.RfidNoService;
import com.hengda.smart.changsha.d.tileview.BitmapProviderFile;
import com.hengda.smart.changsha.d.widget.dialog.ListDialog;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.widgets.ZoomPanLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.functions.Action1;

public class MapFrg extends android.app.Fragment {

    HDTileView tileView;
    MapBase mapBase;
    int unitNo;
    private int n = 0;
    private Bitmap bitmap;
    private Intent intent;
    private List<String> ints = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();
    private List<Exhibition> exhibitionList;
    private InputMethodManager im;
    private int lastNum;
    private Intent serviceIntent;
    private Exhibition exhibit;
    private int foreAuto;
    private View normalView;
    private ListDialog listDialog;
    private List<Exhibition> lists;
    private String path;
    private Exhibition exhibition;
    private BottomDialog dialog;
    private List<Exhibition> allExhibiList;
    private BitmapProviderFile bitmapProviderFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceIntent = new Intent(getActivity(), RfidNoService.class);
        if (HdAppConfig.getAutoFlag() == 1) {
            if (HdAppConfig.getAutoPlay()) {
                getActivity().startService(serviceIntent);
                EventBus.getDefault().register(this);
            } else {
                getActivity().stopService(serviceIntent);
                EventBus.getDefault().unregister(this);
            }
        }


        exhibitionList = new ArrayList<>();
        lists = new ArrayList<>();
        allExhibiList = new ArrayList<>();
        bitmapProviderFile = new BitmapProviderFile();
        listDialog = new ListDialog(getActivity());
        unitNo = getArguments().getInt("UnitNo");
        getExhibitonData(unitNo);
        getAllData();

    }

    @Override
    public void onResume() {
        super.onResume();

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


    private void getExhibitonData(int unitNo) {
        exhibitionList.clear();
        Cursor cursor = HResDdUtil.getInstance().loadAllExhibit(unitNo);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Exhibition exhibition = Exhibition.CursorToModel(cursor);
                exhibitionList.add(exhibition);
            }
        }
        cursor.close();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //初始化TileView

        addDetailLevel();
        tileView.postDelayed(new Runnable() {
            @Override
            public void run() {
                addMarker(exhibitionList);
            }
        }, 500);

//        tileView.setMarkerTapListener(new MarkerLayout.MarkerTapListener() {
//            @Override
//            public void onMarkerTap(View view, int x, int y) {
//                for (int i = 0; i < exhibitionList.size(); i++) {
//                    if (exhibitionList.get(i).getExhibit_id().equals(view.getTag())) {
//                        showBottom(exhibitionList.get(i), dialog);
//                    }
//                }
//            }
//        });

        return tileView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        Log.i("event", "flag:---map-----------------------------------" + event.getAutoNum());
        for (int i = 0; i < allExhibiList.size(); i++) {

            if (event.getAutoNum() == allExhibiList.get(i).getAutonum() && isReplay(event.getAutoNum())) {
                exhibition = getSinglebyAuto(event.getAutoNum());
                if (exhibition != null) {
                    if (exhibition.getMap_id() != unitNo) {
                        listDialog.message(getString(R.string.swich_floor))
                                .nBtnText(getString(R.string.cancel))
                                .nBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        listDialog.dismiss();
                                    }
                                }).pBtnText(getString(R.string.sure))
                                .pBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        RxBus.getDefault().post(exhibition);
                                        getActivity().finish();
                                    }
                                }).show();
                    } else {
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
                                        Intent intent = new Intent(getActivity(), Play.class);
                                        intent.putExtra("exhibition", exhibition);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                }

            }
        }
    }

    private void addMarker(final List<Exhibition> markPointList) {
        for (final Exhibition mark : markPointList) {
            ImageView markImg = new ImageView(getActivity());
            path = HdAppConfig.getImgPath(mark.getExhibit_id()) + "map_icon.png";
            Glide.with(getActivity()).load(path).override(50, 100).into(markImg);
            bitmap = BitmapUtils.loadBitmapFromFile(path);
            if (mark.getAxis_x() != 0) {
                tileView.addMarker(markImg, mark.getAxis_x(), mark.getAxis_y(), -0.5f, -1.0f);
//                tileView.placeMarkerWithScale(bitmap,mark.getAxis_x(),mark.getAxis_y(),mark.getExhibit_id());
            }
            markImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottom(mark, dialog);
                }
            });

        }
    }

    public void showBottom(final Exhibition exhibition, BottomDialog bottomDialog) {
        bottomDialog = BottomDialog.create(getFragmentManager());

        bottomDialog.setViewListener(new BottomDialog.ViewListener() {
            private TextView dialogName;
            private ImageView dialogImg;

            @Override
            public void bindView(View v) {
                dialogImg = (ImageView) v.findViewById(R.id.img_dialog);
                dialogName = (TextView) v.findViewById(R.id.name_dialog);
                dialogName.setSelected(true);
                dialogName.setText(exhibition.getByname());
                Glide.with(getActivity()).load(HdAppConfig.getImgPath(exhibition.getExhibit_id()) + "img0.png").into(dialogImg);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Play.class);
                        intent.putExtra("exhibition", exhibition);
                        startActivity(intent);
                    }
                });
            }
        }).setLayoutRes(R.layout.dialog_layout)
                .setDimAmount(0.1f)
                .setCancelOutside(true)
                .setTag("BottomDialog").show();

    }

    public static MapFrg newInstance(int unitNo) {
        MapFrg fragment = new MapFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("UnitNo", unitNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    /*
    * 隔一复收
    * */
    private boolean isReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastNum) {
            lastNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    /**
     * 添加各级瓦片+底图
     */
    private void addDetailLevel() {
        //添加各级瓦片
        tileView = new HDTileView(getActivity());
        tileView.setBitmapProvider(bitmapProviderFile);
        String baseMapPath = HdAppConfig.getMapFilePath(HdAppConfig.getLanguage(), unitNo);
        tileView.init(2, 1920, 1080, baseMapPath);
        tileView.loadMapFromDisk();
        tileView.setMinimumScaleFullScreen();
        tileView.addSample(baseMapPath + "/img.png", false);
        tileView.setBitmapProvider(bitmapProviderFile);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void placeMarker(Mark marker, double x, double y) {
        tileView.addMarker(marker.getMarkerView(), x, y, -0.0f, -1.0f);

    }

    /*
    * 根据autoNum得到展品
    *
    * */
    public Exhibition getSinglebyAuto(int autoNum) {
        lists.clear();
        Cursor cursor = HResDdUtil.getInstance().loadExhibitByAutoNo(autoNum);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.CursorToModel(cursor);
                lists.add(exhibition);

            }
            cursor.close();
        }
        return lists.get(0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        getActivity().stopService(serviceIntent);
        bitmapProviderFile = null;
        getActivity().stopService(serviceIntent);
        if (listDialog != null && listDialog.isShowing()) {
            listDialog.dismiss();
            listDialog = null;
        }
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        tileView.destroy();
        tileView = null;
        System.gc();
    }

}
