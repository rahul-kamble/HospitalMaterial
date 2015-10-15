package com.example.root.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.root.myapplication.ModelClass.BloodBank;
import com.example.root.myapplication.ModelClass.Hospital;

import java.util.ArrayList;

/**
 * Created by root on 28/9/15.
 */
public class HospitalDataBase {

    //DataBase name and Version
    private static final String DATABASE_NAME = "hospitalDb";
    private static final int DATABASE_VERSION = 6;

    //Table Name
    private static final String HOSPITAL_TABLE_NAME = "hospital";
    private static final String BLOOD_BANK_TABLE_NAME = "bloodBank";


    //fields for Hospital
    private static final String SYSTEM_OF_MEDICINE = "SystemsOfMedicine";
    private static final String Specializations = "Specializations";
    private static final String SERVICES = "Services";

    //fields for Blood Bank
    private static final String BLOOD_COMPONENT = "blood_component";
    private static final String BLOOD_GROUP = "blood_group";
    private static final String SERVICE_TIME = "service_time";
    private static final String LATITUDE = "latitude";
    private static final String HELPLINE = "helpline";
    private static final String FAX = "fax";
    private static final String LONGITUDE = "longitude";
    private static final String ADDRESS = "address";
    private static final String DISTRICT = "district";

    //common Fields
    private static final String HOSPITAL_NAME = "h_name";
    private static final String CONTACT = "contact";
    private static final String PINCODE = "pincode";
    private static final String EMAIL = "email";
    private static final String WEBSITE = "website";
    private static final String CATEGORY = "category";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String KEY_ROW_ID = "_id";


    private static final String DATABASE_CREATE_HOSPITAL = "create table hospital(_id integer primary key autoincrement default 1, "
            + " state text , city text , h_name text , category text , SystemsOfMedicine text ," +
            "contact text , pincode text , email text ," +
            "website text , Specializations text , Services text);";

    private static final String DATABASE_CREATE_BLOODBANK = "create table bloodBank(_id integer primary key autoincrement default 1,"
            + " state text , city text , district text , h_name text ,  address text  , pincode text , contact text , helpline text ," +
            " fax text , category text ,   website text , email text , blood_component text , blood_group text , service_time text ," +
            " latitude text , longitude text);";
    public static String MAP_FRAGMENT = "mapFragment";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    ArrayList<Hospital> hospitalData = new ArrayList<Hospital>();
    ArrayList<BloodBank> bloodBankData = new ArrayList<BloodBank>();

    private Context context;
    public static boolean bbOrhosp;
    public static boolean bloodOrHosp = false;
    public static boolean checkBundle=false;

    public void insertIntoDbHospital(ArrayList<Hospital> hospitalData) {
        this.hospitalData = hospitalData;
        for (int i = 0; i < hospitalData.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(STATE, hospitalData.get(i).getState());
            values.put(CITY, hospitalData.get(i).getCity());
            values.put(HOSPITAL_NAME, hospitalData.get(i).getPvt());
            values.put(CATEGORY, hospitalData.get(i).getCategory());
            values.put(SYSTEM_OF_MEDICINE, hospitalData.get(i).getSystemsOfMedicine());
            values.put(CONTACT, hospitalData.get(i).getContact());
            values.put(PINCODE, hospitalData.get(i).getPincode());
            values.put(EMAIL, hospitalData.get(i).getEmail());
            values.put(WEBSITE, hospitalData.get(i).getWebsite());
            values.put(Specializations, hospitalData.get(i).getSpecializations());
            values.put(SERVICES, hospitalData.get(i).getServices());
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
            values.put(LATITUDE, bloodBankData.get(i).getLatitude());
            values.put(LONGITUDE, bloodBankData.get(i).getLangitude());
            open();
            db.insert(BLOOD_BANK_TABLE_NAME, null, values);
            close();
        }
    }

    public HospitalDataBase(Context context) {
        this.context = context;
    }

    public HospitalDataBase open() {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
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
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    Hospital hospital = new Hospital();
                    hospital.setRowId(_id);
                    hospital.setPvt(hospitalName);
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
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    BloodBank bloodBank = new BloodBank();
                    bloodBank.setRowid(_id);
                    bloodBank.setHospitalName(hospitalName);
                    bloodBankArrayList.add(bloodBank);

                } while (cursor.moveToNext());
            cursor.close();
            close();

        }
        return bloodBankArrayList;
    }

    public ArrayList<Hospital> stateWiseHospitalForHospital(String userState, String userCity) {
        ArrayList<Hospital> hospitalList = new ArrayList<>();
        String readData = "";
        readData += "select * from " + HOSPITAL_TABLE_NAME + " where city='" + userCity + "' AND state='" + userState + "'";
        open();
        Cursor cursor = db.rawQuery(readData, null);
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
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String specialization = cursor.getString(cursor.getColumnIndex(Specializations));
                    String services = cursor.getString(cursor.getColumnIndex(SERVICES));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
                    Hospital hospital = new Hospital();
                    hospital.setState(state);
                    hospital.setCity(city);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setContact(contact);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setServices(services);
                    hospital.setWebsite(website);
//                    hospital.setTimestamp(timestamp);
                    hospital.setEmail(email);
                    hospital.setRowId(hospId);
                    hospital.setPvt(hospitalName);
                    hospitalList.add(hospital);
                } while (cursor.moveToNext());
            cursor.close();
            close();
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
                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
                    String contact = cursor.getString(cursor.getColumnIndex(CONTACT));
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String bloodCompo = cursor.getString(cursor.getColumnIndex(BLOOD_COMPONENT));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String blood_group = cursor.getString(cursor.getColumnIndex(BLOOD_GROUP));
                    String service_time = cursor.getString(cursor.getColumnIndex(SERVICE_TIME));

                    String longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    String latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
                    String helpline = cursor.getString(cursor.getColumnIndex(HELPLINE));
                    String fax = cursor.getString(cursor.getColumnIndex(FAX));
                    String district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));

                    BloodBank bloodBank = new BloodBank();
                    bloodBank.setState(state);
                    bloodBank.setCity(city);
                    bloodBank.setCategory(category);
                    bloodBank.setBloodGroup(blood_group);
                    bloodBank.setPincode(pincode);
                    bloodBank.setContact(contact);
                    bloodBank.setBloodComponent(bloodCompo);
                    bloodBank.setServiceTime(service_time);
                    bloodBank.setWebsite(website);
                    bloodBank.setEmail(email);
                    bloodBank.setLangitude(longitude);
                    bloodBank.setLatitude(latitude);
                    bloodBank.setHelpline(helpline);
                    bloodBank.setFax(fax);
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
//                    String timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(STATE));
                    String city = cursor.getString(cursor.getColumnIndex(CITY));
                    String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                    String website = cursor.getString(cursor.getColumnIndex(WEBSITE));
                    String contact = cursor.getString(cursor.getColumnIndex(CONTACT));
                    String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(PINCODE));
                    String specialization = cursor.getString(cursor.getColumnIndex(Specializations));
                    String services = cursor.getString(cursor.getColumnIndex(SERVICES));

                    hospital.setState(state);
                    hospital.setCity(city);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setContact(contact);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setServices(services);
                    hospital.setWebsite(website);
                    hospital.setEmail(email);
                    hospital.setPvt(hospitalName);
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
                    String lang = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    String latt = cursor.getString(cursor.getColumnIndex(LATITUDE));
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
        Log.e("arraylistInDb::",""+stateList.size());
        return stateList;
    }


}
