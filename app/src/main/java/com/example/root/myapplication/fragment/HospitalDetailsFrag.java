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

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;

import java.util.ArrayList;

public class HospitalDetailsFrag extends Fragment {
    HospitalDataBase dbHelper;
    TextView pincode, email, website, contact, hospitalName, specialization, service, timestamp, systemsOfMedicine, city;
    TextView state, category, phoneNo;
    protected LocationListener locationListener;
    Bundle bundle = new Bundle();
    int position;
    String cno;
    String address;
    Double lat,longitude;
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
        arrayList.addAll(dbHelper.stateWiseHospitalForHospital(state,city));
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
        Uri s = Uri.parse("geo:0,0?q=" + hospital.getPvt() + ", " + address);
        if (address != null) {
            showMap(s);
        }
        return super.onOptionsItemSelected(item);
//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(18.5203, 73.856, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String cityName = addresses.get(0).getAddressLine(0);
//        String stateName = addresses.get(0).getAddressLine(1);
//        String countryName = addresses.get(0).getAddressLine(2);
//        return true;
//        LocationManager locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                longitude=location.getLongitude();
//                lat=location.getLatitude();
//                Log.e("lat",""+longitude);
//                Log.e("lat",""+lat);
//            }

//        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       // if (location != null) {

//            lat = location.getLatitude();
//            longitude = location.getLongitude();
//            Log.d("old", "lat :  " + lat);
//            Log.d("old", "long :  " + longitude);

        //}
//        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//           // addresses = gcd.getFromLocation(lat, longitude, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses.size() > 0) {
//            Log.e("hi", "" + addresses.get(0).getLocality());//city
//            Log.e("hi", "" + addresses.get(0).getAddressLine(1));//local area
//            Log.e("hi", "" + addresses.get(0).getAdminArea());//state
//        }

//        return true;
    }

    private void findId(View rootView) {
        email = (TextView) rootView.findViewById(R.id.EmailName);
        website = (TextView) rootView.findViewById(R.id.websiteName);
        hospitalName = (TextView) rootView.findViewById(R.id.HospitalName);
        state = (TextView) rootView.findViewById(R.id.StateName);
        city = (TextView) rootView.findViewById(R.id.Cityname);
        specialization = (TextView) rootView.findViewById(R.id.specializationName);
        service = (TextView) rootView.findViewById(R.id.ServiceName);
        systemsOfMedicine = (TextView) rootView.findViewById(R.id.MedicineName);
        category = (TextView) rootView.findViewById(R.id.categoryName);
        pincode = (TextView) rootView.findViewById(R.id.pincodeName);
        contact = (TextView) rootView.findViewById(R.id.contactName);
        phoneNo = (TextView) rootView.findViewById(R.id.Phone_No);
        setAllText();
    }

    private void addDialer() {
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cno));
                startActivity(callIntent);
            }
        });


    }

    private void setAllText() {
        email.setText(hospital.getEmail());
        email.setTextSize(15);
        website.setText(hospital.getWebsite());
        if (!hospital.getWebsite().equals("NA")) {
            website.setTextColor(Color.BLUE);
        }
        if (!hospital.getEmail().equals("NA")) {
            email.setTextColor(Color.BLUE);
        }
        hospitalName.setText(hospital.getPvt());
//        timestamp.setText(hospital.getTimestamp());
        state.setText(hospital.getState());
        city.setText(hospital.getCity());
        specialization.setText(hospital.getSpecializations());
        systemsOfMedicine.setText(hospital.getSystemsOfMedicine());
        service.setText(hospital.getServices());
        category.setText(hospital.getCategory());
        pincode.setText(hospital.getPincode());
        contact.setText(hospital.getContact());
        String contact = hospital.getContact();
        if (contact.contains("Phone:")) {
            String[] splits = contact.split("Phone:");
            address = splits[0];
            String no1 = splits[1].trim();
            phoneNo.setText(no1);
            phoneNo.setTextColor(Color.BLUE);
        } else if (contact.contains("Phone-")) {
            String[] splits = contact.split("Phone-");
            address = splits[0];
            String no[] = splits[1].split(",");
            cno = no[0].trim();
            phoneNo.setText(cno);
            phoneNo.setTextColor(Color.BLUE);
        }
    }


}
