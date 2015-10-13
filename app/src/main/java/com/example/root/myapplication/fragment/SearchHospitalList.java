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
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;
import com.example.root.myapplication.adapter.BloodBankAdap;
import com.example.root.myapplication.adapter.HospNameAdap;

import java.util.ArrayList;

public class SearchHospitalList extends Fragment {

    ArrayList<Hospital> hospNameList = new ArrayList<>();
    ArrayList<BloodBank> bloodBankList = new ArrayList<>();
    HospitalDataBase dbHelper;
    HospNameAdap hospNameAdap;
    BloodBankAdap bloodBankAdap;
    ListView listView;
    View rootView;
    String userCity;
    String userState;
    int position;
    HospitalDataBase hospitalDataBase;
    long rowId;
    private MainActivity mainactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_hospital_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.searchHospital);
        mainactivity = (MainActivity) getActivity();
        Bundle bundle = this.getArguments();
        userCity = bundle.getString("city");
        userState = bundle.getString("state");

        dbHelper = new HospitalDataBase(getActivity());
        addListListener();
        hospitalDataBase = new HospitalDataBase(getActivity());
        if (hospitalDataBase.stateWiseHospitalForBloodBank(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp==true) {
            addDataTolistForBloodBank();
        }

        if (hospitalDataBase.stateWiseHospitalForHospital(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp==false) {
            addDataTolist();
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setVisible(false);
    }

    private void addDataTolistForBloodBank() {
        bloodBankList.clear();
        bloodBankList.addAll(dbHelper.stateWiseHospitalForBloodBank(userState, userCity));
        bloodBankAdap = new BloodBankAdap(getActivity(), bloodBankList);
        bloodBankAdap.notifyDataSetChanged();
        listView.setAdapter(bloodBankAdap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.stateWiseHospitalForHospital(userState, userCity));
        hospNameAdap = new HospNameAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (hospitalDataBase.stateWiseHospitalForBloodBank(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp==true) {
                    mainactivity.gotoUserBloodBankDetails(i, bloodBankList.get(i).getState(), bloodBankList.get(i).getCity());
                }
                if (hospitalDataBase.stateWiseHospitalForHospital(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp==false) {
                    mainactivity.gotoUserHospDetails(i, hospNameList.get(i).getState(), hospNameList.get(i).getCity());
                }

            }
        });
    }
}
