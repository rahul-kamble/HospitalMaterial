package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.R;

import java.util.ArrayList;

public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button findHospital;
    private ArrayAdapter<String> adapterForState;
    private AutoCompleteTextView edtCity, edtState;
    private ArrayAdapter<String> adapterForCity;
    private Button findBloodBank;
    String city, state;
    HospitalDataBase hospitalDataBase;
    HospitalDataBase dbHelper;
    ArrayList<String> stateList = new ArrayList<String>();

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setVisible(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = new HospitalDataBase(getActivity());
        stateList.addAll(dbHelper.stateFromDB());
        Log.e("arraylist", "" + stateList.size());
        adapterForState = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, stateList);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        edtCity = (AutoCompleteTextView) rootView.findViewById(R.id.city);
        edtState = (AutoCompleteTextView) rootView.findViewById(R.id.state);
        edtState.setThreshold(0);//will start working from first character
        edtState.setAdapter(adapterForState);//setting the adapterForState data into the AutoCompleteTextView
        edtState.setTextColor(Color.BLACK);
        Log.e("pause", "view");

        edtState.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> cityList = new ArrayList<String>();
                cityList.clear();
                String state = edtState.getText().toString();
                HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());
                cityList = hospitalDataBase.cityFromDb(state);

                adapterForCity = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, cityList);
                edtCity.setThreshold(1);
                edtCity.setAdapter(adapterForCity);
                edtCity.setTextColor(Color.BLACK);
            }
        });

        edtCity.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
            }
        });

        findBloodBank = (Button) rootView.findViewById(R.id.bloodBank);
        findBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                city = edtCity.getText().toString();
                state = edtState.getText().toString();
                GotoUserHospDetails gotoUserHospDetails = new GotoUserHospDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", city);
                bundle1.putString("state", state);
                gotoUserHospDetails.setArguments(bundle1);
                SearchHospitalList searchHospitalList = new SearchHospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("state", state);
                searchHospitalList.setArguments(bundle);
                HospitalDataBase.bbOrhosp = true;
                hospitalDataBase = new HospitalDataBase(getActivity());
                if (hospitalDataBase.stateWiseHospitalForBloodBank(state, city).size() > 0) {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.view_main, searchHospitalList).addToBackStack(null).commit();
                }
                validationForBloodBank(edtState, edtCity);
            }
        });


        findHospital = (Button) rootView.findViewById(R.id.findHospital);
        findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                city = edtCity.getText().toString();
                state = edtState.getText().toString();

                GotoUserHospDetails gotoUserHospDetails = new GotoUserHospDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", city);
                bundle1.putString("state", state);
                gotoUserHospDetails.setArguments(bundle1);
                SearchHospitalList searchHospitalList = new SearchHospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("state", state);
                HospitalDataBase.bbOrhosp = false;
                searchHospitalList.setArguments(bundle);
                hospitalDataBase = new HospitalDataBase(getActivity());

                if (hospitalDataBase.stateWiseHospitalForHospital(state, city).size() > 0) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.view_main, searchHospitalList).addToBackStack(null).commit();
                }
                validationForHospital(edtState, edtCity);
            }
        });
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();
        if (edtState.getText().toString().length() == 0)
            edtState.setError(null);
        if (edtCity.getText().toString().length() == 0)
            edtCity.setError(null);
    }
    private void validationForHospital(AutoCompleteTextView edtState, AutoCompleteTextView edtCity) {
        if (edtState.getText().toString().length() == 0 || edtCity.getText().toString().length() == 0) {
            if (edtState.getText().toString().length() == 0)
                edtState.setError("State is required!");
            if (edtCity.getText().toString().length() == 0)
                edtCity.setError("City is required!");
        } else if (hospitalDataBase.stateWiseHospitalForHospital(state, city).size() == 0) {

            Toast.makeText(getActivity(), "Sorry No Hospital Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void validationForBloodBank(AutoCompleteTextView edtState, AutoCompleteTextView edtCity) {
        if (edtState.getText().toString().length() == 0 || edtCity.getText().toString().length() == 0) {
            if (edtState.getText().toString().length() == 0)
                edtState.setError("State is required!");
            if (edtCity.getText().toString().length() == 0)
                edtCity.setError("City is required!");
        } else if (hospitalDataBase.stateWiseHospitalForBloodBank(state, city).size() == 0) {

            Toast.makeText(getActivity(), "Sorry No Hospital Found", Toast.LENGTH_SHORT).show();
        }
    }


    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void updateStateList() {

        dbHelper = new HospitalDataBase(getActivity());
        stateList.clear();
        stateList.addAll(dbHelper.stateFromDB());
        adapterForState.notifyDataSetChanged();

    }

}
