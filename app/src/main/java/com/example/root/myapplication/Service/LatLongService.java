package com.example.root.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.ModelClass.LatLong;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 16/10/15.
 */
public class LatLongService  extends Service {

    HospitalDataBase mdb;
    Hospital hospital;

    private List<Address> addresses = null;
    GoogleMap googleMap;
    BloodBank bloodBank;
    AsyncTask<ArrayList<BloodBank>, Void, Void> downloadLatLong;
    Geocoder gcd;
    LatLng hospitalLocation;
    ArrayList<Hospital> hospitalArrayList = new ArrayList<Hospital>();
    ArrayList<BloodBank> bloodBankArrayList = new ArrayList<BloodBank>();
    ArrayList<LatLong> latLongArrayList = new ArrayList<LatLong>();

    private double lat, longg;

    public LatLongService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mdb = new HospitalDataBase(getApplicationContext());
        hospitalArrayList = mdb.readHospitalDataFromDatabase();
        bloodBankArrayList = mdb.readBloddBankDataFromDB();
        gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        downloadLatLong=new DownloadLatLong().execute(bloodBankArrayList);
        return  0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void calcLatLongForHospital() {
        if (mdb.readHospitalDataFromDatabase().size() > 0) {

            for (int i = 0; i < hospitalArrayList.size(); i++) {
                hospital = hospitalArrayList.get(i);
                String city = hospital.getDistrict();
                String hospitalName = hospital.getHospitalName();
                String state = hospital.getState();
                int rowid= (int) hospital.getRowId();
                try {
                    addresses = gcd.getFromLocationName( hospitalName+","+city+","+state, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (addresses !=null && addresses.size() > 0) {
                    Address address1 = addresses.get(0);
                    lat = address1.getLatitude();
                    longg = address1.getLongitude();
                    LatLong latLong = new LatLong();
                    latLong.setLattitude(lat);
                    latLong.setLongitude(longg);
                    latLong.setUserId(rowid);
                    Log.e("ROWID::", "" + rowid);
                    latLongArrayList.add(latLong);
//                    Log.e("latt::", "" + longg);
                    Log.e("arraylistSize::", "" + latLongArrayList.size());

                }
            }
            mdb.updateNote(latLongArrayList);
        }
    }

    private void calcLatLongForBloodBank() {
        if (mdb.readBloddBankDataFromDB().size() > 0) {

            for (int i = 0; i < bloodBankArrayList.size(); i++) {
                bloodBank = bloodBankArrayList.get(i);
                String address = bloodBank.getAddress();
                try {
                    addresses = gcd.getFromLocationName(address, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (addresses!=null && addresses.size() > 0) {
                    Address address1 = addresses.get(0);
                    lat = address1.getLatitude();
                    longg = address1.getLongitude();
                    int rowid= (int) bloodBank.getRowid();
                    LatLong latLong = new LatLong();
                    latLong.setLattitude(lat);
                    latLong.setLongitude(longg);
                    latLong.setUserId(rowid);
                    latLongArrayList.add(latLong);
                    Log.e("latt::", "" + address + lat);
                    Log.e("latt::", "" + longg);
                    Log.e("arraylistSize::", "" + latLongArrayList.size());

//                    mdb.insertLatIntoDbBloodBank(latLongArrayList);
                }
            }
            mdb.updateNote(latLongArrayList);
        }
        HospitalDataBase hospitalDataBase = new HospitalDataBase(getApplicationContext());
        hospitalDataBase.statusCheck();
    }

    private class DownloadLatLong extends AsyncTask<ArrayList<BloodBank>,Void,Void> {

        @Override
        protected Void doInBackground(ArrayList<BloodBank>... params) {
            calcLatLongForBloodBank();
            return null;
        }
    }
}
