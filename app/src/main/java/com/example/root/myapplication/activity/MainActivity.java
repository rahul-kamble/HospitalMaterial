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
import com.example.root.myapplication.Service.LatLongService;
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
    LocationManager mLocationManager;
    Home homeFragment;
    DrawerLayout drawer;
    private HospitalDataBase hospitalDataBase;
    private ProgressDialog dialog;
    private boolean status;
    private String cityForbundle, stateForBundle;
    private boolean checkStatus, flagForLocation, flag;
    private boolean doubleBackToExitPressedOnce = false;
    private List<Address> addresses;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("location", MODE_PRIVATE);
            boolean locationStatus = pref1.getBoolean("Location", false);

            if (location != null) {
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
    private boolean connectionCheck;
    private int fragmentId;
    private boolean gps_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = (Home) getFragmentManager().findFragmentById(R.id.homeFragment);
        hospitalDataBase = new HospitalDataBase(MainActivity.this);
        connectionCheck = hospitalDataBase.checkConnection();
        if (connectionCheck == true) {
            if (hospitalDataBase.gps_enabled == false) {
                hospitalDataBase.locationCheck();
            }

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
                            Intent intent = new Intent(MainActivity.this, LatLongService.class);
                            startService(intent);
                            homeFragment.updateStateList();

                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = pref1.edit();
                            editor1.putBoolean("Status", true);
                            editor1.commit();

                        }
                        Log.e("size", "" + hospitalDataBase.readHospitalDataFromDatabase().size());
                        if (hospitalDataBase.readHospitalDataFromDatabase().size() >= 995) {
                            checkStatus = true;
                        }
                    } while (hospitalDataBase.readHospitalDataFromDatabase().size() < 1040);
                }

            }).start();
            flagForLocation = false;
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        //location search
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps_enabled == true) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        } else {
            Toast.makeText(MainActivity.this, "Need a Sim Card ", Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyboard();


            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
//        getSupportFragmentManager().beginTransaction().
////                remove(getSupportFragmentManager().findFragmentById().commit();
        FragmentManager fragmentManager = getFragmentManager();
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        else {
            super.onBackPressed();
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
        menu.getItem(0).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
            setTitle("Search Hospital");
            FragmentManager fragmentManager = getFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }
        } else if (id == R.id.findHospital) {
            if (hospitalDataBase.gps_enabled == false) {
                hospitalDataBase.locationCheck();
            }
            HospitalList hospitalList = new HospitalList();
            hideKeyboard();
            setTitle("Hospitals");
            FragmentManager fragmentManager = getFragmentManager();
            SharedPreferences sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);
            Bundle bundle = new Bundle();
            bundle.putString("city", sharedPreferences.getString("city", ""));
            bundle.putString("state", sharedPreferences.getString("state", ""));
            hospitalList.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove()
            fragmentTransaction.replace(R.id.view_main, hospitalList).addToBackStack(null).commit();
//            while (fragmentManager.getBackStackEntryCount() > 1) {
//                fragmentManager.popBackStackImmediate();
//            }


        } else if (id == R.id.bloodBank) {
            if (hospitalDataBase.gps_enabled == false) {
                hospitalDataBase.locationCheck();
            }
            BloodBankList bloodBankList = new BloodBankList();
            hideKeyboard();
            setTitle("Blood Bank");
            SharedPreferences sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);
            Bundle bundle1 = new Bundle();
            bundle1.putString("city", sharedPreferences.getString("city", ""));
            bundle1.putString("state", sharedPreferences.getString("state", ""));
            bloodBankList.setArguments(bundle1);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_main, bloodBankList).addToBackStack(null).commit();
            while (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStackImmediate();
            }
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://www.google.co.in/?gfe_rd=cr&ei=JukcVofYEKfG8Ae_162gBA&gws_rd=ssl";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard();
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
        fragmentTransaction.replace(R.id.view_main, bloodBankDetails).addToBackStack(null).commit();
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
        fragmentTransaction.replace(R.id.view_main, hospitalDetailsFrag).addToBackStack(null).commit();
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
        fragmentTransaction.replace(R.id.view_main, bloodBankDetails).addToBackStack(null).commit();
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
        fragmentTransaction.replace(R.id.view_main, hospitalDetailsFrag).addToBackStack(null).commit();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
