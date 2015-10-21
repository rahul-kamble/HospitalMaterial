package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.R;
import com.example.root.myapplication.activity.MainActivity;
import com.example.root.myapplication.adapter.BloodBankAdap;

import java.util.ArrayList;


public class BloodBankList extends Fragment {
    ArrayList<BloodBank> hospNameList = new ArrayList<>();
    HospitalDataBase dbHelper;
    BloodBankAdap hospNameAdap;
    ListView listView;
    View rootView;
    int position;
    long rowId;
    private MainActivity mainactivity;
    private String city;
    private String state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_blood_bank_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listBloodBank);
        mainactivity = (MainActivity) getActivity();
        addListListener();
        dbHelper = new HospitalDataBase(getActivity());
        city = getArguments().getString("city");
        state = getArguments().getString("state");
        addDataTolist();
        return rootView;
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
        menuItem.setVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Blood Bank");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HospitalDataBase hospitalDataBase=new HospitalDataBase(getActivity());
        if (hospitalDataBase.gps_enabled == false) {
            hospitalDataBase.locationCheck();
        }
        if (hospitalDataBase.checkConnection()==true && hospitalDataBase.gps_enabled == true) {
            MapFragment mapFragment = new MapFragment();
            HospitalDataBase.bbOrhosp = true;
            Bundle bundle = new Bundle();
            bundle.putString("city", city);
            bundle.putString("state", state);
            mapFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.view_main, mapFragment).addToBackStack(null).commit();
            getActivity().setTitle("Map");
        }
        else
        {
            Toast.makeText(getActivity(),"Check Internet connetion or GPS",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.stateWiseHospitalForBloodBank(state, city));
        hospNameAdap = new BloodBankAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainactivity.gotoBloodBankDeatails(i, state, city);
            }
        });
    }
}
