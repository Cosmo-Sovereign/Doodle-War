package com.example.doodle_war.ui.war;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.doodle_war.BrushView;
import com.example.doodle_war.R;

public class DashboardFragment extends Fragment {
    BrushView brushView;
    public DashboardFragment(){

    }
    public static DashboardFragment getInstance(){
        DashboardFragment fragment = new DashboardFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_war, container, false);
        return root;
    }
}