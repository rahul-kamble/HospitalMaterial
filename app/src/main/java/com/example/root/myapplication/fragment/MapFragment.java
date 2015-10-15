package com.example.root.myapplication.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<BloodBank> bloodBanks;
    ArrayList<Hospital> hospitalArrayList;
    GoogleMap googleMap;
    Geocoder gcd;
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
        HospitalDataBase mdb = new HospitalDataBase(getActivity());
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is  */
        }
        mMapFragment = com.google.android.gms.maps.MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.view_main, mMapFragment);
        fragmentTransaction.commit();
        // Inflate the layout for this fragment
//        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Construct a CameraPosition focusing on United States and move the camera to that position.

        Double aDouble = null, aDouble1 = null;
        BloodBank bloodBank = null;
        LatLng pharmacyLocation = null;
        gcd = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addd = gcd.getFromLocationName(city + "," + state, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addd.size() > 0) {
            Address address1 = addd.get(0);
            double1 = address1.getLatitude();
            double2 = address1.getLongitude();
        }
        latLng = new LatLng(double1, double2);
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(9) // Sets the zoom
                .build(); // Creates a CameraPosition from the builder
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (mdb.stateWiseHospitalForHospital(state, city).size()
                > 0 && HospitalDataBase.bbOrhosp == false) {}
        for (int i = 0; i < this.bloodBanks.size(); i++) {
            bloodBank = this.bloodBanks.get(i);
            String address = bloodBank.getAddress();
            try {
                addresses = gcd.getFromLocationName(address, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                Address address1 = addresses.get(0);
                aDouble = address1.getLatitude();
                aDouble1 = address1.getLongitude();
                pharmacyLocation = new LatLng(aDouble, aDouble1);
                MarkerOptions marker = new MarkerOptions().position(pharmacyLocation)
                        .title(bloodBank.getHospitalName()).snippet(bloodBank.getAddress());
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                googleMap.addMarker(marker);

            }


        }

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

}