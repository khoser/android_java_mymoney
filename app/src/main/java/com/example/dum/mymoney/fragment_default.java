package com.example.dum.mymoney;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;


/**
 * Created by dum on 27.05.2015.
 */
public class fragment_default extends Fragment {

    fragment_defaultInterface fdi;

//    public fragment_default() {
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        return inflater.inflate(R.layout.fragment_default, container, false);
        //setonclicklistener

       // return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fdi = (fragment_defaultInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PocketInterface");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        fdi.atAttache();
    }

    public interface fragment_defaultInterface {
        public void atAttache();
    }
}
