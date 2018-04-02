package com.example.ktu.myapplication;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by dum on 09.06.2015.
 */
public class fragment_report_buttons extends Fragment {

    fragment_report_buttonsInterface fdi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_dolg, container, false);
        return inflater.inflate(R.layout.fragment_report_buttons, container, false);

        //return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fdi = (fragment_report_buttonsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PocketInterface");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        fdi.atAttache();
    }

    public interface fragment_report_buttonsInterface {
        public void atAttache();
    }


}
