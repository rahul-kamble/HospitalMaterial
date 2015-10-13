package com.example.root.myapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.R;
import com.example.root.myapplication.Service.DownloadService;
import com.example.root.myapplication.fragment.BloodBankDetails;
import com.example.root.myapplication.fragment.BloodBankList;
import com.example.root.myapplication.fragment.GotoUserHospDetails;
import com.example.root.myapplication.fragment.Home;
import com.example.root.myapplication.fragment.HospitalDetailsFrag;
import com.example.root.myapplication.fragment.HospitalList;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String HOME_FRAGMENT = "home_fragment";
    private HospitalDataBase hospitalDataBase;
    private ProgressDialog dialog;
    private boolean status;
    private String cityForbundle, stateForBundle;
    private boolean checkStatus, flagForLocation, flag;
    LocationManager mLocationManager;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hospitalDataBase = new HospitalDataBase(MainActivity.this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Hospital", MODE_PRIVATE);
        status = pref.getBoolean("Status", false);

        if (hospitalDataBase.readHospitalDataFromDatabase().size() == 0) {
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            startService(intent);
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                do {
                    if (checkStatus == true && status == false) {
                        dialog.dismiss();
                        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref1.edit();
                        editor1.putBoolean("Status", true);
                        editor1.commit();

                    }
                    Log.e("size", "" + hospitalDataBase.readBloddBankDataFromDB().size());
                    if (hospitalDataBase.readBloddBankDataFromDB().size() >= 2900) {
                        checkStatus = true;
                    }
                } while (hospitalDataBase.readBloddBankDataFromDB().size() < 2946);
            }

        }).start();
        flagForLocation = false;

        //location search
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, mLocationListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Home home = new Home();
        hideKeyboard();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_main, home, HOME_FRAGMENT).addToBackStack(null).commit();

    }

    private List<Address> addresses;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("location", MODE_PRIVATE);

            boolean locationStatus = pref1.getBoolean("Location", false);

            if (location != null && locationStatus == false) {
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putBoolean("Location", true);


                //your code here
                Double lat = location.getLatitude();
                Double longg = location.getLongitude();
                Log.e("location", "" + location.getLatitude());
                Log.e("location", "" + location.getLongitude());
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = gcd.getFromLocation(lat, longg, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    flag = true;
                    HospitalDataBase.checkBundle = true;
                    cityForbundle = addresses.get(0).getLocality();
                    stateForBundle = addresses.get(0).getAdminArea();
                    editor1.putString("city", cityForbundle);
                    editor1.putString("state", stateForBundle);
                    editor1.commit();

                    Log.e("hi", "" + addresses.get(0).getLocality());//city
                    Log.e("hi", "" + addresses.get(0).getAddressLine(1));//local area
                    Log.e("hi", "" + addresses.get(0).getAdminArea());//state
                }
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onBackPressed() {
//        //super.onBackPressed();
        Home home = new Home();
        FragmentManager listManager = getFragmentManager();
        Home myFragment = (Home) getFragmentManager().findFragmentByTag(HOME_FRAGMENT);
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please Press Back Button Again...", Toast.LENGTH_SHORT).show();
        } else {
            if (getFragmentManager().findFragmentByTag(HOME_FRAGMENT) != null) {
                FragmentTransaction listTransaction = listManager.beginTransaction();
                listTransaction.remove(getFragmentManager().findFragmentByTag(HOME_FRAGMENT));
                listTransaction.replace(R.id.view_main, home, HOME_FRAGMENT);
                listTransaction.commit();

            } else {
                FragmentTransaction listTransaction = listManager.beginTransaction();
                listTransaction.replace(R.id.view_main, home, HOME_FRAGMENT);
//            listTransaction.addToBackStack(null);
                listTransaction.commit();
            }

        }
    }

    protected void onResume() {
        super.onResume();
        // .... other stuff in my onResume ....
        this.doubleBackToExitPressedOnce = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            Home home = new Home();
            hideKeyboard();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_main, home, HOME_FRAGMENT).addToBackStack(null).commit();

        } else if (id == R.id.findHospital) {

            HospitalList hospitalList = new HospitalList();
            hideKeyboard();
            FragmentManager fragmentManager = getFragmentManager();
            SharedPreferences sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);
            Bundle bundle = new Bundle();
            bundle.putString("city", sharedPreferences.getString("city", ""));
            bundle.putString("state", sharedPreferences.getString("state", ""));
            hospitalList.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_main, hospitalList).commit();


        } else if (id == R.id.bloodBank) {
            BloodBankList bloodBankList = new BloodBankList();
            hideKeyboard();
            SharedPreferences sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);
            Bundle bundle1 = new Bundle();
            bundle1.putString("city", sharedPreferences.getString("city", ""));
            bundle1.putString("state", sharedPreferences.getString("state", ""));
            bloodBankList.setArguments(bundle1);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_main, bloodBankList).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotoBloodBankDeatails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        BloodBankDetails bloodBankDetails = new BloodBankDetails();
        bloodBankDetails.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_main, bloodBankDetails).commit();
    }

    public void hospitalDetailsFrag(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        HospitalDetailsFrag hospitalDetailsFrag = new HospitalDetailsFrag();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_main, hospitalDetailsFrag).commit();
    }

    public void gotoUserBloodBankDetails(int position, String state, String city) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        BloodBankDetails bloodBankDetails = new BloodBankDetails();
        bloodBankDetails.setArguments(bundle);
        FragmentManager fragmentManagerForBlood = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManagerForBlood.beginTransaction();
        fragmentTransaction.replace(R.id.view_main, bloodBankDetails).commit();
    }

    public void gotoUserHospDetails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        GotoUserHospDetails hospitalDetailsFrag = new GotoUserHospDetails();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_main, hospitalDetailsFrag).commit();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
