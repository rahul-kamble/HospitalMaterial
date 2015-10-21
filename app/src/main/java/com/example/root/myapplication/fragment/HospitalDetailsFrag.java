package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;

import java.util.ArrayList;

public class HospitalDetailsFrag extends Fragment {
    protected LocationListener locationListener;
    HospitalDataBase dbHelper;
    TextView pincode, email, website, txtAdress, hospitalName, specialization, facility, systemsOfMedicine, district;
    TextView state, category, phoneNo, mobileNo, ambulanceNo, tollfree, telephone, fax;
    Bundle bundle = new Bundle();
    int position;
    String cno;
    String address;
    Double lat, longitude;
    Hospital hospital;
    ArrayList<Hospital> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospital_details, container, false);
        position = getArguments().getInt("position");
        dbHelper = new HospitalDataBase(getActivity());
        String city = getArguments().getString("city");
        String state = getArguments().getString("state");
        address = city + ", " + state;
        arrayList.addAll(dbHelper.stateWiseHospitalForHospital(state, city));
        hospital = dbHelper.getSinglerecordForHosp(arrayList.get(position).getRowId());
        findId(rootView);
        emailTextViewAction();
        websiteTextViewAction();
        addDialer();
        return rootView;
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void websiteTextViewAction() {
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String site = website.getText().toString();
                if (site.equals("NA")) {

                } else {
                    Log.e("site", "" + site);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + site));
                    startActivity(intent);
                }
            }
        });
    }

    private void emailTextViewAction() {
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = email.getText().toString();
                if (to.equals("NA")) {

                } else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + to));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.map);
        menuItem.setVisible(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());
        if (hospitalDataBase.gps_enabled == false) {
            hospitalDataBase.locationCheck();
        }
        if (hospitalDataBase.checkConnection()==true && hospitalDataBase.gps_enabled == true) {
            Uri s = Uri.parse("geo:0,0?q=" + hospital.getHospitalName() + ", " + address);
            if (address != null) {
                showMap(s);
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Check Internet connetion or GPS", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
    }

    private void findId(View rootView) {
        email = (TextView) rootView.findViewById(R.id.EmailName);
        website = (TextView) rootView.findViewById(R.id.websiteName);
        hospitalName = (TextView) rootView.findViewById(R.id.HospitalName);
        state = (TextView) rootView.findViewById(R.id.StateName);
        district = (TextView) rootView.findViewById(R.id.districtName);
        specialization = (TextView) rootView.findViewById(R.id.specializationName);
        facility = (TextView) rootView.findViewById(R.id.facilityName);
        systemsOfMedicine = (TextView) rootView.findViewById(R.id.MedicineName);
        category = (TextView) rootView.findViewById(R.id.categoryName);
        pincode = (TextView) rootView.findViewById(R.id.pincodeName);
        txtAdress = (TextView) rootView.findViewById(R.id.addressName);
        telephone = (TextView) rootView.findViewById(R.id.telephoneNo);
        mobileNo = (TextView) rootView.findViewById(R.id.mobileNo);
        ambulanceNo = (TextView) rootView.findViewById(R.id.ambulanceNo);
        fax = (TextView) rootView.findViewById(R.id.faxName);
        tollfree = (TextView) rootView.findViewById(R.id.tollFreeNo);
        setAllTextForHospital();
    }

    private void addDialer() {
        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"  + telephone.getText().toString()));
                startActivity(callIntent);
            }
        });


    }

    private void setAllTextForHospital() {
        email.setText(hospital.getPrimaryEmail());
        email.setTextSize(15);
        telephone.setText(hospital.getTelephone());
        website.setText(hospital.getWebsite());
        mobileNo.setText(hospital.getMobileNo());
        tollfree.setText(hospital.getTollfree());
        ambulanceNo.setText(hospital.getAmbulanceNo());
        if (!hospital.getWebsite().equals("NA")) {
            website.setTextColor(Color.BLUE);
        }
        if (!hospital.getPrimaryEmail().equals("NA")) {
            email.setTextColor(Color.BLUE);
        }
        if (!hospital.getTelephone().equals("NA")) {
            telephone.setTextColor(Color.BLUE);
        }
        if (!hospital.getMobileNo().equals("NA")) {
            mobileNo.setTextColor(Color.BLUE);
        }
        if (!hospital.getAmbulanceNo().equals("NA")) {
            ambulanceNo.setTextColor(Color.BLUE);
        }
        if (!hospital.getTollfree().equals("NA")) {
            tollfree.setTextColor(Color.BLUE);
        }
        hospitalName.setText(hospital.getHospitalName());
        state.setText(hospital.getState());
        district.setText(hospital.getDistrict());
        specialization.setText(hospital.getSpecializations());
        systemsOfMedicine.setText(hospital.getSystemsOfMedicine());
        facility.setText(hospital.getFalicilty());
        category.setText(hospital.getCategory());
        pincode.setText(hospital.getPincode());
        fax.setText(hospital.getFax());
        txtAdress.setText(hospital.getAddress());

    }


}
