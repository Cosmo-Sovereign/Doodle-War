package com.example.doodle_war.ui.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodle_war.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView rv;
    ArrayList<DataModel> dataholder;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv=view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder=new ArrayList<>();
        DataModel ob1=new DataModel(R.drawable.parasprofile,R.drawable.d5,"Paras");
        dataholder.add(ob1);
        DataModel ob2=new DataModel(R.drawable.pradyumnprojile,R.drawable.d2,"Pradyumn");
        dataholder.add(ob2);
        DataModel ob3=new DataModel(R.drawable.parasprofile,R.drawable.d3,"Paras");
        dataholder.add(ob3);
        DataModel ob4=new DataModel(R.drawable.pradyumnprojile,R.drawable.d4,"Pradyumn");
        dataholder.add(ob4);
        DataModel ob5=new DataModel(R.drawable.parasprofile,R.drawable.d1,"Paras");
        dataholder.add(ob5);
        DataModel ob6=new DataModel(R.drawable.pradyumnprojile,R.drawable.d6,"Pradyumn");
        dataholder.add(ob6);
        //String data[]={"C++","Java","Python","C"};
        rv.setAdapter(new MyAdapter(dataholder));
        return view;
    }
}