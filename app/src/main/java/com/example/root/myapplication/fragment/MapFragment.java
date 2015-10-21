package com.example.root.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.ModelClass.LatLongInBackground;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<BloodBank> bloodBanks;
    ArrayList<Hospital> hospitalArrayList;
    GoogleMap googleMap;
    Geocoder gcd;
    HospitalDataBase mdb;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String city, state;
    private LatLng latLng;
    private List<Address> addresses = null;
    private com.google.android.gms.maps.MapFragment mMapFragment;
    private List<Address> addd;
    private double double1, double2;
    private MyHandler myHandler;
    private boolean status;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        mdb = new HospitalDataBase(getActivity());
        city = getArguments().getString("city");
        state = getArguments().getString("state");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* View rootView = null;
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        *//* map is already there, just return view as it is  *//*
        }*/
       /* mMapFragment = com.google.android.gms.maps.MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_main, mMapFragment);
        fragmentTransaction.commit();
        // Inflate the layout for this fragment
//        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
*/

       /* mMapFragment = com.google.android.gms.maps.MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_main, mMapFragment);
        fragmentTransaction.addToBackStack(null).commit();*/

        return super.onCreateView(inflater, container,
                savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("sad", "das");
        this.getView().setBackgroundColor(Color.WHITE);
        // Inflate the layout for this fragment
//        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
        getMapAsync(this);


    }
//
//    @Override
//    public void onDestroyView() {
//        try{
//            Map fragment = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.map));
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.remove(fragment);
//            ft.commit();
//        }catch(Exception e){
//        }
//
//        super.onDestroyView();
//    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        gcd = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addd = gcd.getFromLocationName(city + "," + state, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addd != null && addd.size() > 0) {
            Address address1 = addd.get(0);
            double1 = address1.getLatitude();
            double2 = address1.getLongitude();
        }
        //  21.0000, 78.0000

        if (double1 != 0 && double2 != 0) {
            latLng = new LatLng(double1, double2);

        } else {
            latLng = new LatLng(21.0000, 78.0000);
        }
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10) // Sets the zoom
                .build(); // Creates a CameraPosition from the builder
        mgoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap = mgoogleMap;
        myHandler = new MyHandler();
        myHandler.execute(googleMap);


        // Construct a CameraPosition focusing on United States and move the camera to that position.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menu1 = menu.getItem(0);
        menu1.setVisible(false);
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class MyHandler extends AsyncTask {
        ArrayList<BloodBank> bloodBanks;
        ArrayList<Hospital> hospitalArrayList;
        Double aDouble = null, aDouble1 = null;
        BloodBank bloodBank = null;
        Hospital hospital = null;
        LatLng pharmacyLocation = null;
        ArrayList<LatLongInBackground> latLongInBackgrounds;

        @Override
        protected Object doInBackground(Object[] params) {
            city = getArguments().getString("city");
            state = getArguments().getString("state");
            if (mdb.stateWiseHospitalForBloodBank(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == true) {
                bloodBanks = mdb.stateWiseHospitalForBloodBank(state, city);
            }
            if (mdb.stateWiseHospitalForHospital(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == false) {
                hospitalArrayList = mdb.stateWiseHospitalForHospital(state, city);
            }


            latLongInBackgrounds = new ArrayList<>();

            if (mdb.stateWiseHospitalForHospital(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == false) {
                for (int i = 0; i < this.hospitalArrayList.size(); i++) {
                    hospital = this.hospitalArrayList.get(i);
                    LatLongInBackground latLong = new LatLongInBackground();
                    String address = hospital.getAddress();
                    String name = hospital.getHospitalName();
                    Double lat = null, log = null;
                    if (!hospital.getLat().equals("NA") && !hospital.getLog().equals("NA")) {
                        lat = Double.valueOf(hospital.getLat());
                        log = Double.valueOf(hospital.getLog());
                        Log.e("In DB::", "Mi aalo");
                    }


                    if (lat == null || log == null) {
                        try {
                            addresses = gcd.getFromLocationName(name + "," + city + "," + state, 2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (address != null && addresses.size() > 0) {

                            Address address1 = addresses.get(0);
                            aDouble = address1.getLatitude();
                            aDouble1 = address1.getLongitude();
                            latLong.setAddress(address);
                            latLong.setName(name);
                            latLong.setLat(aDouble);
                            latLong.setLon(aDouble1);
                            latLongInBackgrounds.add(latLong);

                        }
                    } else {
                        latLong.setAddress(address);
                        latLong.setName(name);
                        latLong.setLat(lat);
                        latLong.setLon(log);
                        latLongInBackgrounds.add(latLong);
                    }
                }
            }

            if (mdb.stateWiseHospitalForBloodBank(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == true) {

                for (int i = 0; i < this.bloodBanks.size(); i++) {
                    bloodBank = this.bloodBanks.get(i);
                    LatLongInBackground latLong = new LatLongInBackground();
                    String address = bloodBank.getAddress();
                    String name = bloodBank.getHospitalName();
                    Double lat = null;
                    Double log = null;
                    if (bloodBank.getLatitude() != null && bloodBank.getLangitude() != null) {
                        lat = Double.valueOf(bloodBank.getLatitude());
                        log = Double.valueOf(bloodBank.getLangitude());
                    }
//                    SharedPreferences pref = getActivity().getSharedPreferences("Status", Context.MODE_PRIVATE);
//                    status = pref.getBoolean("StatusForDB", true);
//                    if (status == false) {
                        if (lat == null || log == null) {
                            try {
                                addresses = gcd.getFromLocationName(address, 2);
                                Log.e("In Normal::", "Mi aalo");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses != null && addresses.size() > 0) {

                                Address address1 = addresses.get(0);
                                aDouble = address1.getLatitude();
                                aDouble1 = address1.getLongitude();
                                latLong.setAddress(address);
                                latLong.setLat(aDouble);
                                latLong.setLon(aDouble1);
                                latLong.setName(name);
                                latLongInBackgrounds.add(latLong);
                            }
//                        }
                    } else {
                        latLong.setAddress(address);
                        latLong.setLat(lat);
                        latLong.setLon(log);
                        latLong.setName(name);
                        latLongInBackgrounds.add(latLong);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            if (mdb.stateWiseHospitalForHospital(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == false) {

                for (int i = 0; i < latLongInBackgrounds.size(); i++) {
                    LatLongInBackground latLongInBackground = latLongInBackgrounds.get(i);
                    pharmacyLocation = new LatLng(latLongInBackground.getLat(), latLongInBackground.getLon());
                    MarkerOptions marker = new MarkerOptions().position(pharmacyLocation)
                            .title(latLongInBackground.getName()).snippet(latLongInBackground.getAddress());
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    marker.alpha(1);
                    googleMap.addMarker(marker);

                }


            }


            if (mdb.stateWiseHospitalForBloodBank(state, city).size()
                    > 0 && HospitalDataBase.bbOrhosp == true) {
                for (int i = 0; i < latLongInBackgrounds.size(); i++) {
                    LatLongInBackground latLongInBackground = latLongInBackgrounds.get(i);

                    pharmacyLocation = new LatLng(latLongInBackground.getLat(), latLongInBackground.getLon());
                    MarkerOptions marker = new MarkerOptions().position(pharmacyLocation)
                            .title(latLongInBackground.getName()).snippet(latLongInBackground.getAddress());
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    googleMap.addMarker(marker);

                }
            }


        }

    }
}

