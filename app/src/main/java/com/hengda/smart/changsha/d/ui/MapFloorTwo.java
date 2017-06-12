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
public class MapFloorTwo extends Fragment implements View.OnClickListener{


    @Bind(R.id.exhibition_one_F2)
    TextView exhibitionOneF2;
    @Bind(R.id.exhibition_two_F2)
    TextView exhibitionTwoF2;
    @Bind(R.id.exhibition_three_F2)
    TextView exhibitionThreeF2;
    public int unitNo;
private Intent intent;
    public MapFloorTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_floor_two, container, false);
        ButterKnife.bind(this, view);
        exhibitionThreeF2.setOnClickListener(this);
        exhibitionOneF2.setOnClickListener(this);
        exhibitionTwoF2.setOnClickListener(this);
        RxBus.getDefault().toObservable(Exhibition.class).subscribe(new Action1<Exhibition>() {

            @Override
            public void call(Exhibition exhibition) {
                intent = new Intent(getActivity(), OneMapActicity.class);
                 unitNo = exhibition.getMap_id();
                if (unitNo==4){

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
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        intent=new Intent(getActivity(),OneMapActicity.class);
        switch (view.getId()){
            case R.id.exhibition_three_F2:
                intent.putExtra("unitno",4);
                startActivity(intent);
                break;
            case R.id.exhibition_one_F2:
                Intent intent1=new Intent(getActivity(),DigitalActivity.class);
                startActivity(intent1);
                break;
            case R.id.exhibition_two_F2:
                Intent intent2=new Intent(getActivity(),DigitalActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
