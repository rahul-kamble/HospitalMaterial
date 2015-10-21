package com.example.root.myapplication.DB;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.util.Log;

import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;
import com.example.root.myapplication.ModelClass.LatLong;
import com.example.root.myapplication.R;

import java.util.ArrayList;

/**
 * Created by root on 28/9/15.
 */
public class HospitalDataBase {

    //DataBase name and Version
    private static final String DATABASE_NAME = "hospitalDb";
    private static final int DATABASE_VERSION = 1;

    //Table Name
    private static final String HOSPITAL_TABLE_NAME = "hospital";
    private static final String BLOOD_BANK_TABLE_NAME = "bloodBank";


    //fields for Hospital
    private static final String SYSTEM_OF_MEDICINE = "SystemsOfMedicine";
    private static final String SPECIALIZATIONS = "specialization";
    private static final String FACILITY = "facility";
    private static final String CARETYPE = "caretype";
    private static final String SUBDISTRICT = "subDistrict";
    private static final String TELEPHONE = "telephone";
    private static final String MOBILENO = "mobileNo";
    private static final String EMERGENCYNO = "emergencyNo";
    private static final String AMBULANCE = "ambulance";
    private static final String TOLLFREE = "tollfree";
    private static final String SECONDARYEMAIL = "secondaryEmail";
    private static final String NOOFBEDS = "beds";

    //fields for Blood Bank
    private static final String BLOOD_COMPONENT = "blood_component";
    private static final String BLOOD_GROUP = "blood_group";
    private static final String SERVICE_TIME = "service_time";


    //common Fields
    private static final String HOSPITAL_NAME = "h_name";
    private static final String CONTACT = "contact";
    private static final String PINCODE = "pincode";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    private static final String WEBSITE = "website";
    private static final String CATEGORY = "category";
    private static final String STATE = "state";
    private static final String HELPLINE = "helpline";
    private static final String FAX = "fax";
    private static final String CITY = "city";
    private static final String DISTRICT = "district";
    private static final String KEY_ROW_ID = "_id";
    private static final String LATTITUDE_FOR_MARKER = "lattitudeForMarker";
    private static final String LONGITUDE_FOR_MARKER = "longitudeForMarker";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    private static final String DATABASE_CREATE_HOSPITAL = "create table hospital(_id integer primary key autoincrement default 1, "
            + " h_name text , category text , caretype text , SystemsOfMedicine text , address text ," +
            " state text , district text , subDistrict text ," +
            " pincode text , telephone text , mobileNo text, emergencyNo text , ambulance text ," +
            " tollfree text , helpline text , fax text , email text , secondaryEmail text , website text ," +
            " specialization text , latitude text , longitude text , facility text , beds text );";

    private static final String DATABASE_CREATE_BLOODBANK = "create table bloodBank(_id integer primary key autoincrement default 1,"
            + " state text , city text , district text , h_name text ,  address text  , pincode text , contact text , helpline text ," +
            " fax text , category text ,   website text , email text , blood_component text , blood_group text , service_time text ," +
            " lattitudeForMarker text, longitudeForMarker text);";
    public static String MAP_FRAGMENT = "mapFragment";
    public static boolean bbOrhosp;
    public static boolean bloodOrHosp = false;
    public static boolean checkBundle = false;
    ArrayList<Hospital> hospitalData = new ArrayList<Hospital>();
    ArrayList<BloodBank> bloodBankData = new ArrayList<BloodBank>();
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;
    public boolean gps_enabled;
    private boolean network_enabled;
    private boolean status;

    public HospitalDataBase(Context context) {
        this.context = context;
    }

    public void insertIntoDbHospital(ArrayList<Hospital> hospitalData) {
        this.hospitalData = hospitalData;
        for (int i = 0; i < hospitalData.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(HOSPITAL_NAME, hospitalData.get(i).getHospitalName());
            values.put(CATEGORY, hospitalData.get(i).getCategory());
            values.put(CARETYPE, hospitalData.get(i).getCareType());
            values.put(SYSTEM_OF_MEDICINE, hospitalData.get(i).getSystemsOfMedicine());
            values.put(ADDRESS, hospitalData.get(i).getAddress());
            values.put(STATE, hospitalData.get(i).getState());
            values.put(DISTRICT, hospitalData.get(i).getDistrict());
            values.put(SUBDISTRICT, hospitalData.get(i).getSubDistrict());
            values.put(PINCODE, hospitalData.get(i).getPincode());
            values.put(TELEPHONE, hospitalData.get(i).getTelephone());
            values.put(MOBILENO, hospitalData.get(i).getMobileNo());
            values.put(EMERGENCYNO, hospitalData.get(i).getEmergencyNo());
            values.put(AMBULANCE, hospitalData.get(i).getAmbulanceNo());
            values.put(TOLLFREE, hospitalData.get(i).getTollfree());
            values.put(HELPLINE, hospitalData.get(i).getHelpline());
            values.put(FAX, hospitalData.get(i).getFax());
            values.put(EMAIL, hospitalData.get(i).getPrimaryEmail());
            values.put(SECONDARYEMAIL, hospitalData.get(i).getSecondaryEmail());
            values.put(WEBSITE, hospitalData.get(i).getWebsite());
            values.put(SPECIALIZATIONS, hospitalData.get(i).getSpecializations());
            values.put(LATITUDE, hospitalData.get(i).getLat());
            values.put(LONGITUDE, hospitalData.get(i).getLog());
            values.put(FACILITY, hospitalData.get(i).getFalicilty());
            values.put(NOOFBEDS, hospitalData.get(i).getNoOfBeds());
            open();
            db.insert(HOSPITAL_TABLE_NAME, null, values);
            close();
        }
    }

    public void insertIntoDbBloodBank(ArrayList<BloodBank> bloodBankData) {
        this.bloodBankData = bloodBankData;
        for (int i = 0; i < bloodBankData.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(STATE, bloodBankData.get(i).getState());
            values.put(CITY, bloodBankData.get(i).getCity());
            values.put(DISTRICT, bloodBankData.get(i).getDistrict());
            values.put(HOSPITAL_NAME, bloodBankData.get(i).getHospitalName());
            values.put(ADDRESS, bloodBankData.get(i).getAddress());
            values.put(PINCODE, bloodBankData.get(i).getPincode());
            values.put(CONTACT, bloodBankData.get(i).getContact());
            values.put(HELPLINE, bloodBankData.get(i).getHelpline());
            values.put(FAX, bloodBankData.get(i).getFax());
            values.put(CATEGORY, bloodBankData.get(i).getCategory());
            values.put(WEBSITE, bloodBankData.get(i).getWebsite());
            values.put(EMAIL, bloodBankData.get(i).getEmail());
            values.put(BLOOD_COMPONENT, bloodBankData.get(i).getBloodComponent());
            values.put(BLOOD_GROUP, bloodBankData.get(i).getBloodGroup());
            values.put(SERVICE_TIME, bloodBankData.get(i).getServiceTime());
            values.put(LATTITUDE_FOR_MARKER, bloodBankData.get(i).getLatitude());
            values.put(LONGITUDE_FOR_MARKER, bloodBankData.get(i).getLangitude());
            open();
            db.insert(BLOOD_BANK_TABLE_NAME, null, values);
            close();
        }
    }

    public HospitalDataBase open() {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

//    public void insertLatIntoDbHospi(ArrayList<LatLong> latLongArrayList) {
//        for (int i = 0; i < latLongArrayList.size(); i++) {
//            ContentValues values = new ContentValues();
//            values.put(LATTITUDE_FOR_MARKER, latLongArrayList.get(i).getLattitude());
//            values.put(LONGITUDE_FOR_MARKER, latLongArrayList.get(i).getLongitude());
//            open();
//            db.insert(HOSPITAL_TABLE_NAME, null, values);
//            close();
//        }
//    }

//    public void insertLatIntoDbBloodBank(ArrayList<LatLong> latLongArrayList) {
//        for (int i = 0; i < latLongArrayList.size(); i++) {
//            ContentValues values = new ContentValues();
//            values.put(LATTITUDE_FOR_MARKER, latLongArrayList.get(i).getLattitude());
//            values.put(LONGITUDE_FOR_MARKER, latLongArrayList.get(i).getLongitude());
//            open();
//            db.insert(BLOOD_BANK_TABLE_NAME, null, values);
//            close();
//        }
//    }

    public ArrayList<Hospital> readHospitalDataFromDatabase() {
        ArrayList<Hospital> hospitalDataList = new ArrayList<>();
        String read = "";
        read += "select * from " + HOSPITAL_TABLE_NAME;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
                    String district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String add = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    Hospital hospital = new Hospital();
                    hospital.setRowId(_id);
                    hospital.setHospitalName(hospitalName);
                    hospital.setDistrict(district);
                    hospital.setAddress(add);
                    hospital.setState(state);
                    hospitalDataList.add(hospital);

                } while (cursor.moveToNext());
            cursor.close();
            close();

        }
        return hospitalDataList;
    }

    public ArrayList<BloodBank> readBloddBankDataFromDB() {
        ArrayList<BloodBank> bloodBankArrayList = new ArrayList<>();
        String read = "";
        read += "select * from " + BLOOD_BANK_TABLE_NAME;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    BloodBank bloodBank = new BloodBank();
                    bloodBank.setRowid(_id);
                    bloodBank.setHospitalName(hospitalName);
                    bloodBank.setAddress(address);
                    bloodBankArrayList.add(bloodBank);

                } while (cursor.moveToNext());
            cursor.close();

        }
        return bloodBankArrayList;
    }

    public ArrayList<Hospital> stateWiseHospitalForHospital(String userState, String userCity) {
        ArrayList<Hospital> hospitalList = new ArrayList<>();
        String readData = "";
        readData += "select * from " + HOSPITAL_TABLE_NAME + " where district='" + userCity + "' AND state='" + userState + "'";
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String city = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String mobile = cursor.getString(cursor.getColumnIndex(MOBILENO));
                    String telephone = cursor.getString(cursor.getColumnIndex(TELEPHONE));
                    String ambulance = cursor.getString(cursor.getColumnIndex(AMBULANCE));
                    String tollfree = cursor.getString(cursor.getColumnIndex(TOLLFREE));
                    String fax = cursor.getString(cursor.getColumnIndex(FAX));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String specialization = cursor.getString(cursor.getColumnIndex(SPECIALIZATIONS));
                    String services = cursor.getString(cursor.getColumnIndex(FACILITY));
                    String lat=cursor.getString(cursor.getColumnIndex(LATITUDE));
                    String log=cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    Hospital hospital = new Hospital();
                    hospital.setState(state);
                    hospital.setDistrict(city);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setAddress(address);
                    hospital.setLat(lat);
                    hospital.setLog(log);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setFalicilty(services);
                    hospital.setWebsite(website);
                    hospital.setPrimaryEmail(email);
                    hospital.setMobileNo(mobile);
                    hospital.setTelephone(telephone);
                    hospital.setAmbulanceNo(ambulance);
                    hospital.setTollfree(tollfree);
                    hospital.setFax(fax);
                    hospital.setRowId(hospId);
                    hospital.setHospitalName(hospitalName);
                    hospitalList.add(hospital);
                } while (cursor.moveToNext());
            cursor.close();
        }
        return hospitalList;
    }

    public ArrayList<BloodBank> stateWiseHospitalForBloodBank(String userState, String userCity) {
        ArrayList<BloodBank> hospitalList = new ArrayList<>();
        String readData = "";
        readData += "select * from " + BLOOD_BANK_TABLE_NAME + " where city='" + userCity + "' AND state='" + userState + "'";
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String city = cursor.getString(cursor.getColumnIndex(CITY));
                    String lat=cursor.getString(cursor.getColumnIndex(LATTITUDE_FOR_MARKER));
                    String log=cursor.getString(cursor.getColumnIndex(LONGITUDE_FOR_MARKER));
//                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
//                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
//                    String contact = cursor.getString(cursor.getColumnIndex(CONTACT));
//                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
//                    String bloodCompo = cursor.getString(cursor.getColumnIndex(BLOOD_COMPONENT));
//                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
//                    String blood_group = cursor.getString(cursor.getColumnIndex(BLOOD_GROUP));
//                    String service_time = cursor.getString(cursor.getColumnIndex(SERVICE_TIME));
//
//                    String longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
//                    String latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
//                    String helpline = cursor.getString(cursor.getColumnIndex(HELPLINE));
//                    String fax = cursor.getString(cursor.getColumnIndex(FAX));
                    String district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));

                    BloodBank bloodBank = new BloodBank();
                    bloodBank.setState(state);
                    bloodBank.setCity(city);
                    bloodBank.setLatitude(lat);
                    bloodBank.setLangitude(log);
//                    bloodBank.setCategory(category);
//                    bloodBank.setBloodGroup(blood_group);
//                    bloodBank.setPincode(pincode);
//                    bloodBank.setContact(contact);
//                    bloodBank.setBloodComponent(bloodCompo);
//                    bloodBank.setServiceTime(service_time);
//                    bloodBank.setWebsite(website);
//                    bloodBank.setEmail(email);
//                    bloodBank.setLangitude(longitude);
//                    bloodBank.setLatitude(latitude);
//                    bloodBank.setHelpline(helpline);
//                    bloodBank.setFax(fax);
                    bloodBank.setAddress(address);
                    bloodBank.setDistrict(district);
                    bloodBank.setRowid(hospId);
                    bloodBank.setHospitalName(hospitalName);
                    hospitalList.add(bloodBank);

                } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        return hospitalList;
    }

    public Hospital getSinglerecordForHosp(long rowId) {
        Hospital hospital = new Hospital();
        String read = "";
        read += "select * from " + HOSPITAL_TABLE_NAME + " where _id =" + rowId;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String mobile = cursor.getString(cursor.getColumnIndex(MOBILENO));
                    String telephone = cursor.getString(cursor.getColumnIndex(TELEPHONE));
                    String ambulance = cursor.getString(cursor.getColumnIndex(AMBULANCE));
                    String tollfree = cursor.getString(cursor.getColumnIndex(TOLLFREE));
                    String fax = cursor.getString(cursor.getColumnIndex(FAX));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String specialization = cursor.getString(cursor.getColumnIndex(SPECIALIZATIONS));
                    String services = cursor.getString(cursor.getColumnIndex(FACILITY));
                    hospital.setState(state);
                    hospital.setDistrict(district);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setAddress(address);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setFalicilty(services);
                    hospital.setWebsite(website);
                    hospital.setPrimaryEmail(email);
                    hospital.setMobileNo(mobile);
                    hospital.setTelephone(telephone);
                    hospital.setAmbulanceNo(ambulance);
                    hospital.setTollfree(tollfree);
                    hospital.setFax(fax);
                    hospital.setHospitalName(hospitalName);
//                    hospitalDetails.add(hospital);
                } while (cursor.moveToNext());
            cursor.close();

        }
        return hospital;
    }

    public BloodBank getSinglerecord(long rowId) {
        BloodBank bloodBank = new BloodBank();
        String read = "";
        read += "select * from " + BLOOD_BANK_TABLE_NAME + " where _id =" + rowId;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(HOSPITAL_NAME));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String city = cursor.getString(cursor.getColumnIndex(CITY));
                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
                    String contact = cursor.getString(cursor.getColumnIndex(CONTACT));
                    String district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    String bloodComp = cursor.getString(cursor.getColumnIndex(BLOOD_COMPONENT));
                    String bloodGroup = cursor.getString(cursor.getColumnIndex(BLOOD_GROUP));
                    String helpline = cursor.getString(cursor.getColumnIndex(HELPLINE));
                    String lang = cursor.getString(cursor.getColumnIndex(LATTITUDE_FOR_MARKER));
                    String latt = cursor.getString(cursor.getColumnIndex(LONGITUDE_FOR_MARKER));
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String fax = cursor.getString(cursor.getColumnIndex(FAX));
                    String servicesTime = cursor.getString(cursor.getColumnIndex(SERVICE_TIME));

                    bloodBank.setState(state);
                    bloodBank.setCity(city);
                    bloodBank.setCategory(category);
                    bloodBank.setFax(fax);
                    bloodBank.setPincode(pincode);
                    bloodBank.setContact(contact);
                    bloodBank.setAddress(address);
                    bloodBank.setServiceTime(servicesTime);
                    bloodBank.setWebsite(website);
                    bloodBank.setEmail(email);
                    bloodBank.setHospitalName(hospitalName);

                    bloodBank.setDistrict(district);
                    bloodBank.setBloodComponent(bloodComp);
                    bloodBank.setHelpline(helpline);
                    bloodBank.setBloodGroup(bloodGroup);
                    bloodBank.setLangitude(lang);
                    bloodBank.setLatitude(latt);


                } while (cursor.moveToNext());
            cursor.close();

        }
        return bloodBank;
    }

    public ArrayList<String> cityFromDb(String userState) {
        ArrayList<String> cityList = new ArrayList<>();
        String readData = "";
        readData += "select DISTINCT city from " + BLOOD_BANK_TABLE_NAME + " where state='" + userState + "'";
//        readData +="select bloodBank.city AS ID , hospital.city AS c2 from hospital INNER JOIN bloodBank ";
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String city = cursor.getString(cursor.getColumnIndex(CITY));
//                    String state = cursor.getString(cursor.getColumnIndex("c2"));
                    cityList.add(city);
                } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        return cityList;
    }

    public ArrayList<String> stateFromDB() {
        ArrayList<String> stateList = new ArrayList<>();
        String readData = "";
        readData += "select DISTINCT state from " + HOSPITAL_TABLE_NAME;
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    stateList.add(state);
                } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        Log.e("arraylistInDb::", "" + stateList.size());
        return stateList;
    }

    public void updateNote(ArrayList<LatLong> latLongArrayList) {
        open();
        for (int i = 0; i < latLongArrayList.size(); i++) {
            LatLong latLong = latLongArrayList.get(i);
            ContentValues args = new ContentValues();
            args.put(LATTITUDE_FOR_MARKER, latLong.getLattitude());
            args.put(LONGITUDE_FOR_MARKER, latLong.getLongitude());
            Log.e("userid", "" + latLong.getUserId());
            db.update(BLOOD_BANK_TABLE_NAME, args, KEY_ROW_ID + "=" + latLong.getUserId(), null);
        }
        close();
    }

    public void locationCheck() {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

    public boolean checkConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    public void statusCheck() {
        SharedPreferences pref1 = context.getSharedPreferences("Status12",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref1.edit();
        editor1.putBoolean("StatusForDB", false);
        editor1.commit();

    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_HOSPITAL);
            db.execSQL(DATABASE_CREATE_BLOODBANK);
            Log.e("dbdb", "created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HOSPITAL_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BLOOD_BANK_TABLE_NAME);
            onCreate(db);
            Log.e("dbdb", "upgraded");
        }
    }
}
