package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.activity.MainActivity;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;
import com.example.root.myapplication.adapter.HospNameAdap;

import java.util.ArrayList;

public class HospitalList extends Fragment {

    ArrayList<Hospital> hospNameList = new ArrayList<>();
    HospitalDataBase dbHelper;
    HospNameAdap hospNameAdap;
    ListView listView;
    View rootView;
    int position;
    long rowId;
    String city, state;
    private MainActivity mainactivity;

    public HospitalList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_hospital_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listHospName);
        mainactivity = (MainActivity) getActivity();
        addListListener();
        dbHelper = new HospitalDataBase(getActivity());
            city = getArguments().getString("city");
            state = getArguments().getString("state");
            addDataTolist();
        return rootView;
    }
    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.stateWiseHospitalForHospital(state, city));
        hospNameAdap = new HospNameAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainactivity.hospitalDetailsFrag(i, state, city);
            }
        });
    }


}
