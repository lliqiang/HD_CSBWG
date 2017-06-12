package com.hengda.smart.changsha.d.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hengda.smart.changsha.d.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class UnitFragment extends Fragment {


    public UnitFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unit, container, false);
    }

}
