package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;

import java.util.ArrayList;

public class GotoUserHospDetails extends Fragment {
    HospitalDataBase dbHelper;
    TextView specialization, service, systemsOfMedicine;
    TextView phoneNo;
    Bundle bundle = new Bundle();
    int position;
    String cno;
    String addressForMap, userState, userCity;
    Hospital hospital;
    BloodBank bloodBank;
    View rootViewForHospital, rootViewForBloodBank;
    ArrayList<Hospital> arrayList = new ArrayList<>();
    ArrayList<BloodBank> arrayListForBloodBank = new ArrayList<>();
    TextView pincode, email, website, contact, hospitalName, district, serviceTime, city;
    TextView state, category, helpline, address, bloodComponent, bloodGroup;
    private TextView facility, txtAdress, ambulanceNo, tollfree, fax, mobileNo;
    private TextView telephone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        position = getArguments().getInt("position");
        userCity = getArguments().getString("city");
        userState = getArguments().getString("state");
        addressForMap = userCity + ", " + userCity;
        dbHelper = new HospitalDataBase(getActivity());
        HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());

        if (hospitalDataBase.stateWiseHospitalForHospital(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp == false) {
            rootViewForHospital = inflater.inflate(R.layout.fragment_hospital_details, container, false);
            arrayList.addAll(dbHelper.stateWiseHospitalForHospital(userState, userCity));
            hospital = dbHelper.getSinglerecordForHosp(arrayList.get(position).getRowId());
            findIdForHospital(rootViewForHospital);
            emailTextViewAction();
            websiteTextViewAction();
            addDialer();
            return rootViewForHospital;
        }
        if (hospitalDataBase.stateWiseHospitalForBloodBank(userState, userCity).size() > 0 && HospitalDataBase.bbOrhosp == true) {
            rootViewForBloodBank = inflater.inflate(R.layout.fragment_blood_bank_details, container, false);
            arrayListForBloodBank.addAll(dbHelper.stateWiseHospitalForBloodBank(userState, userCity));
            bloodBank = dbHelper.getSinglerecord(arrayListForBloodBank.get(position).getRowid());
            findId(rootViewForBloodBank);
            emailTextViewAction();
            websiteTextViewAction();
            addDialerForBB();
            return rootViewForBloodBank;
        }
        return rootViewForBloodBank;
    }

    private void addDialerForBB() {
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cno));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());
        if (hospitalDataBase.gps_enabled == false) {
            hospitalDataBase.locationCheck();
        }
        if (hospitalDataBase.checkConnection() == true && hospitalDataBase.gps_enabled == true) {

            Uri s = Uri.parse("geo:0,0?q=" + hospital.getHospitalName() + ", " + addressForMap);
            if (addressForMap != null) {
                showMap(s);
            }
        } else {
            Toast.makeText(getActivity(), "Check Internet connetion or GPS", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findIdForHospital(View rootView) {
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

    private void findId(View rootView) {
        email = (TextView) rootView.findViewById(R.id.EmailName);
        website = (TextView) rootView.findViewById(R.id.websiteName);
        hospitalName = (TextView) rootView.findViewById(R.id.HospitalName);
        state = (TextView) rootView.findViewById(R.id.StateName);
        city = (TextView) rootView.findViewById(R.id.Cityname);
        district = (TextView) rootView.findViewById(R.id.DistrictName);
        serviceTime = (TextView) rootView.findViewById(R.id.serviceTimeName);
        category = (TextView) rootView.findViewById(R.id.categoryName);
        pincode = (TextView) rootView.findViewById(R.id.pincodeName);
        contact = (TextView) rootView.findViewById(R.id.contactName);
        helpline = (TextView) rootView.findViewById(R.id.helpLineName);
        address = (TextView) rootView.findViewById(R.id.addressName);
        bloodComponent = (TextView) rootView.findViewById(R.id.bloodComponentName);
        bloodGroup = (TextView) rootView.findViewById(R.id.bloodGroupName);
        fax = (TextView) rootView.findViewById(R.id.faxName);
        setAllTextForBloodBank();
    }

    private void addDialer() {
        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telephone.getText().toString()));
                startActivity(callIntent);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setVisible(true);
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

    private void setAllTextForBloodBank() {
        hospitalName.setText(bloodBank.getHospitalName());
//        timestamp.setText(hospital.getTimestamp());
        contact.setText(bloodBank.getContact());
        if (!bloodBank.getContact().equals("NA")) {
            contact.setTextColor(Color.BLUE);
        }
        state.setText(bloodBank.getState());
        city.setText(bloodBank.getCity());
        email.setText(bloodBank.getEmail());
        email.setTextSize(15);
        website.setText(bloodBank.getWebsite());
        bloodComponent.setText(bloodBank.getBloodComponent());
        bloodGroup.setText(bloodBank.getBloodGroup());
        address.setText(bloodBank.getAddress());
        pincode.setText(bloodBank.getPincode());
        fax.setText(bloodBank.getFax());
        helpline.setText(bloodBank.getHelpline());
        category.setText(bloodBank.getCategory());
        district.setText(bloodBank.getDistrict());
        serviceTime.setText(bloodBank.getServiceTime());
    }
}
