package com.hengda.smart.changsha.d.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.model.Exhibition;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFloorOne extends Fragment implements View.OnClickListener {


    @Bind(R.id.exhibition_one_F1)
    TextView exhibitionOneF1;
    @Bind(R.id.exhibition_two_F1)
    TextView exhibitionTwoF1;
    @Bind(R.id.exhibition_three_F1)
    TextView exhibitionThreeF1;
    private Intent intent;
    private int Floor;
    private int unitNo;
    private View view;

    public MapFloorOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_floor_one, container, false);
        ButterKnife.bind(this, view);
        exhibitionOneF1.setOnClickListener(this);
        exhibitionTwoF1.setOnClickListener(this);
        exhibitionThreeF1.setOnClickListener(this);
        RxBus.getDefault().toObservable(Exhibition.class).subscribe(new Action1<Exhibition>() {
            @Override
            public void call(Exhibition exhibition) {
                intent = new Intent(getActivity(), OneMapActicity.class);
                unitNo=exhibition.getMap_id();
                if (unitNo!=4){
                    intent.putExtra("unitno", unitNo);
                    startActivity(intent);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i("throwable","throwable:------------"+throwable);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(View view) {
        intent = new Intent(getActivity(), OneMapActicity.class);
        switch (view.getId()) {
            case R.id.exhibition_one_F1:
                intent.putExtra("unitno", 1);
                startActivity(intent);
                break;
            case R.id.exhibition_two_F1:
                intent.putExtra("unitno", 2);
                startActivity(intent);
                break;
            case R.id.exhibition_three_F1:
                intent.putExtra("unitno", 3);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
