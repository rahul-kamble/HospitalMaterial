package com.example.root.myapplication.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.example.root.myapplication.DB.HospitalDataBase;
import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.Parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadService extends Service {
    JSONParser jsonParser = new JSONParser();
    JSONObject bloodBankJson = null;
    JSONObject hospitalJson = null;
    HospitalDataBase hospitalDataBase;
    SharedPreferences sharedPreferences;
    private boolean status;


    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hospitalDataBase = new HospitalDataBase(getApplicationContext());
        hospitalDataBase.readHospitalDataFromDatabase();
//        sharedPreferences = getSharedPreferences("status", MODE_PRIVATE);
//        status = sharedPreferences.getBoolean("s", false);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Looper.prepare();
//                if (isNetworkAvailable(getApplicationContext())) {
//                    for (int i = 0; i <=8; i++) {
                if(hospitalDataBase.readBloddBankDataFromDB().size()==0 && hospitalDataBase.readHospitalDataFromDatabase().size()==0) {
                    bloodBankJson = jsonParser.openHttpConnectionForBloodBank("https://data.gov.in/node/356981/datastore/export/json");
                    hospitalJson = jsonParser.openHttpConnection("https://data.gov.in/node/323921/datastore/export/json");
//                        if (status == false) {
                    addHospitaltoDb(hospitalJson);
                    addToBloodBankDB(bloodBankJson);
////                        }
//                    }
//                    Looper.loop();
//
                }
                }

//            }

        }).start();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("s", true).commit();
//        ArrayList<Hospital> arrayList = hospitalDataBase.readHospitalDataFromDatabase();
//        Log.e("sss", "" + arrayList.size());
        return 0;
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void addHospitaltoDb(JSONObject bloodBank) {
        ArrayList<Hospital> arrayList = new ArrayList<>();
        if (bloodBank != null) {
            try {
                JSONArray jsonArray = this.hospitalJson.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray array = jsonArray.getJSONArray(i);
                    ArrayList<String> strings = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        String details = array.getString(j);
                        strings.add(details);
                    }
                    Hospital hospital = new Hospital();
                    hospital.setState(strings.get(0).toString());
                    hospital.setCity(strings.get(1).toString());
                    hospital.setPvt(strings.get(2).toString());
                    hospital.setCategory(strings.get(3).toString());
                    hospital.setSystemsOfMedicine(strings.get(4).toString());
                    hospital.setContact(strings.get(5).toString());
                    hospital.setPincode(strings.get(6).toString());
                    hospital.setEmail(strings.get(7).toString());
                    hospital.setWebsite(strings.get(8).toString());
                    hospital.setSpecializations(strings.get(9).toString());
                    hospital.setServices(strings.get(10).toString());
                    arrayList.add(hospital);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Size ::::", "" + arrayList.size());
//            hospitalDataBase.open();
            if (arrayList.size() > 0) {

// To dismiss the d
                hospitalDataBase.open();
                hospitalDataBase.insertIntoDbHospital(arrayList);

            }

        }
    }

    private void addToBloodBankDB(JSONObject jsonObject1) {
        ArrayList<BloodBank> arrayList = new ArrayList<>();
        if (jsonObject1 != null) {
            try {
                JSONArray jsonArray = this.bloodBankJson.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray array = jsonArray.getJSONArray(i);
                    ArrayList<String> strings = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        String details = array.getString(j);
                        strings.add(details);
                    }
//                    Hospital modelClassDB = new Hospital();
//                    modelClassDB.setHospitalId(array.getString("id"));
//                    modelClassDB.setState(array.getString("State"));
//                    modelClassDB.setCity(array.getString("City"));
//                    modelClassDB.setPvt(array.getString("Hospital / Private"));
//                    modelClassDB.setCategory(array.getString("Category"));
//                    modelClassDB.setSpecializations(array.getString("Specializations"));
//                    modelClassDB.setPincode(array.getString("AREA Pin CODE"));
//                    modelClassDB.setContact(array.getString("Contact Details"));
//                    modelClassDB.setSystemsOfMedicine(array.getString("Systems of Medicine"));
//                    modelClassDB.setServices(array.getString("Services"));
//                    modelClassDB.setWebsite(array.getString("Website link"));
//                    modelClassDB.setTimestamp(array.getString("timestamp"));
//                    modelClassDB.setEmail(array.getString("Email address"));
//                    arrayList.add(modelClassDB);
                    BloodBank bloodBank = new BloodBank();
                    bloodBank.setState(strings.get(1).toString());
                    bloodBank.setCity(strings.get(2).toString());
                    bloodBank.setDistrict(strings.get(3).toString());
                    bloodBank.setHospitalName(strings.get(4).toString());
                    bloodBank.setAddress(strings.get(5).toString());
                    bloodBank.setPincode(strings.get(6).toString());
                    bloodBank.setContact(strings.get(7).toString());
                    bloodBank.setHelpline(strings.get(8).toString());
                    bloodBank.setFax(strings.get(9).toString());
                    bloodBank.setCategory(strings.get(10).toString());
                    bloodBank.setWebsite(strings.get(11).toString());
                    bloodBank.setEmail(strings.get(12).toString());
                    bloodBank.setBloodComponent(strings.get(13).toString());
                    bloodBank.setBloodGroup(strings.get(14).toString());
                    bloodBank.setServiceTime(strings.get(15).toString());
                    bloodBank.setLatitude(strings.get(16).toString());
                    bloodBank.setLangitude(strings.get(17).toString());
                    arrayList.add(bloodBank);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Size ::::", "" + arrayList.size());
//            hospitalDataBase.open();
            if (arrayList.size() > 0) {
                hospitalDataBase.open();
                hospitalDataBase.insertIntoDbBloodBank(arrayList);
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
