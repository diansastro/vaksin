package com.example.ghost.vaksin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ghost on 29/10/16.
 */

public class SimpleSlide extends Fragment {
    private static final String LAYOUT_ID = "layoutId";
    public static SimpleSlide newInstance(int layoutId){
        SimpleSlide simpleSlide = new SimpleSlide();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        simpleSlide.setArguments(args);
        return simpleSlide;
    }

    private int layoutId;
    public SimpleSlide() {}

    @Override
    public void onCreate (@Nullable Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        if (getArguments() != null && getArguments().containsKey(LAYOUT_ID))
            layoutId = getArguments().getInt(LAYOUT_ID);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(layoutId, container,false);
    }
}
