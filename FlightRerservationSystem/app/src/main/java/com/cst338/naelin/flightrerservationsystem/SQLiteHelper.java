package com.cst338.naelin.flightrerservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper
{
    // Unique instance of SQLiteHelper
    private static SQLiteHelper instance;

    // Database name - DB
    private static final String DATABASE_NAME = "DB";

    // Tables
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_ACCOUNT_LOG = "account_log";
    private static final String TABLE_FLIGHT = "flight";
    private static final String TABLE_RESERVATION_LOG = "reservation_log";
    private static final String TABLE_CANCELLATION_LOG = "cancellation_log";

    // Column names of login table
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // Column names of transaction table
    private static final String KEY_TRANSACTION_TYPE = "transactionType";
    private static final String KEY_TIMESTAMP = "timestamp";

    // Column names of flight table
    private static final String KEY_FLIGHT_NO = "flightNo";
    private static final String KEY_DEPARTURE = "departure";
    private static final String KEY_ARRIVAL = "arrival";
    private static final String KEY_DEPARTURE_TIME = "departureTime";
    private static final String KEY_CAPACITY = "capacity";
    private static final String KEY_PRICE = "price";

    // Column names of reservation log table
    private static final String KEY_NO_TICKETS = "noTickets";
    private static final String KEY_RESERVATION_NO = "reservationNo";
    private static final String KEY_TOTAL = "total";

    // Column names of cancellation log table
    private static final String KEY_CANCELLATION_INFO = "cancellationInfo";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Log TAG for debugging purposes
    private static String TAG = "SQLiteHelper";

    // Constructor
    private SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SQLiteHelper(context);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate");

        // SQL statement to create a table called "books"
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + " ( " +
                 KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 KEY_USERNAME + " TEXT, " +
                 KEY_PASSWORD + " TEXT)";

        String CREATE_ACCOUNT_LOG_TABLE = "CREATE TABLE " + TABLE_ACCOUNT_LOG + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TRANSACTION_TYPE + " TEXT, " +
                KEY_USERNAME + " TEXT, " +
                KEY_TIMESTAMP + " TEXT)";

        String CREATE_FLIGHT_TABLE = "CREATE TABLE " + TABLE_FLIGHT + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_FLIGHT_NO + " TEXT, " +
                KEY_DEPARTURE + " TEXT, " +
                KEY_ARRIVAL + " TEXT, " +
                KEY_DEPARTURE_TIME + " TEXT, " +
                KEY_CAPACITY + " INTERGER, " +
                KEY_PRICE + " REAL)";

        String CREATE_RESERVATION_TABLE = "CREATE TABLE " + TABLE_RESERVATION_LOG + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TRANSACTION_TYPE + " TEXT, " +
                KEY_USERNAME + " TEXT, " +
                KEY_FLIGHT_NO + " TEXT, " +
                KEY_DEPARTURE + " TEXT, " +
                KEY_ARRIVAL + " TEXT, " +
                KEY_DEPARTURE_TIME + " TEXT, " +
                KEY_NO_TICKETS + " INTEGER, " +
                KEY_RESERVATION_NO + " TEXT, " +
                KEY_TOTAL + " REAL, " +
                KEY_TIMESTAMP + " TEXT)";

        String CREATE_CANCELLATION_LOG = "CREATE TABLE " + TABLE_CANCELLATION_LOG + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CANCELLATION_INFO + " TEXT, " +
                KEY_TIMESTAMP + " TEXT)";



        // Execute an SQL statement to create the tables
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_ACCOUNT_LOG_TABLE);
        db.execSQL(CREATE_FLIGHT_TABLE);
        db.execSQL(CREATE_RESERVATION_TABLE);
        db.execSQL(CREATE_CANCELLATION_LOG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANCELLATION_LOG);

        this.onCreate(db);
    }

    public void addLogin(Login login)
    {
        Log.d(TAG, "addLogin() - " + login.toString());

        if(checkExists(login))
            return;

        // 1. Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, login.getUsername()); // Get user
        values.put(KEY_PASSWORD, login.getPassword()); // Get password

        // 3. Insert
        db.insert(TABLE_LOGIN, null, values); // key/value -> keys = column names/ values = column values

        // 4. Close - release the reference of writable DB
        db.close();
    }

    public boolean checkExists(Login login)
    {
        ArrayList<Login> logins = getAllLogins();

        for(Login loginDb : logins)
        {
            if(loginDb.getUsername().equals(login.getUsername()))
            {
                return true;
            }
        }

        return false;

    }

    public ArrayList<Login> getAllLogins()
    {
        ArrayList<Login> logins = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Login login = null;
        if(cursor.moveToFirst())
        {
            do
            {
                login = new Login();
                login.setId(Integer.parseInt(cursor.getString(0)));
                login.setUsername(cursor.getString(1));
                login.setPassword(cursor.getString(2));

                logins.add(login);

            } while(cursor.moveToNext());
        }

        Log.d(TAG, "getAllLogins() - " + logins.toString());

        db.close();

        return logins;
    }

    public void addAccountLog(AccountLog accountLog)
    {
        Log.d(TAG, "addTransaction() - " + accountLog.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_TYPE, accountLog.getTransactionType());
        values.put(KEY_USERNAME, accountLog.getUsername());
        values.put(KEY_TIMESTAMP, accountLog.getTimestamp());

        db.insert(TABLE_ACCOUNT_LOG, null, values);

        db.close();

    }

    public ArrayList<AccountLog> getAllAccountLogs()
    {
        ArrayList<AccountLog> accountLogs = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ACCOUNT_LOG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        AccountLog accountLog = null;
        if(cursor.moveToFirst())
        {
            do
            {
                accountLog = new AccountLog();
                accountLog.setId(Integer.parseInt(cursor.getString(0)));
                accountLog.setTransactionType(cursor.getString(1));
                accountLog.setUsername(cursor.getString(2));
                accountLog.setTimestamp(cursor.getString(3));


                accountLogs.add(accountLog);

            } while(cursor.moveToNext());
        }

        Log.d(TAG, "getAllAccountLogs() - " + accountLogs.toString());

        db.close();

        return accountLogs;
    }

    public boolean addFlight(Flight flight)
    {
        Log.d(TAG, "addFlight() - " + flight.toString());

        if(checkExists(flight))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FLIGHT_NO, flight.getFlightNo());
        values.put(KEY_DEPARTURE, flight.getDeparture());
        values.put(KEY_ARRIVAL, flight.getArrival());
        values.put(KEY_DEPARTURE_TIME, flight.getDepartureTime());
        values.put(KEY_CAPACITY, flight.getCapacity());
        values.put(KEY_PRICE, flight.getPrice());

        db.insert(TABLE_FLIGHT, null, values);

        db.close();

        return true;
    }

    public boolean checkExists(Flight flight)
    {
        ArrayList<Flight> flights = getAllFlights();

        for(Flight flightDb : flights)
        {
            if(flightDb.getFlightNo().equals(flight.getFlightNo()))
            {
                return true;
            }
        }

        return false;

    }

    public ArrayList<Flight> getAllFlights()
    {
        ArrayList<Flight> flights = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_FLIGHT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Flight flight = null;
        if(cursor.moveToFirst())
        {
            do
            {
                flight = new Flight();
                flight.setId(cursor.getInt(0));
                flight.setFlightNo(cursor.getString(1));
                flight.setDeparture(cursor.getString(2));
                flight.setArrival(cursor.getString(3));
                flight.setDepartureTime(cursor.getString(4));
                flight.setCapacity(cursor.getInt(5));
                flight.setPrice(cursor.getDouble(6));

                flights.add(flight);

            } while(cursor.moveToNext());
        }

        Log.d(TAG, "getAllFlights() - " + flights.toString());

        db.close();

        return flights;
    }

    public void updateFlightCapacity(String flightNo, int quantity)
    {
        Log.d(TAG, "updateFlightCapacity() - " + flightNo);

        ArrayList<Flight> flights = getAllFlights();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for(Flight flight : flights)
        {
            if(flight.getFlightNo().equals(flightNo))
            {
                flight.setCapacity(flight.getCapacity() - quantity);
                values.put(KEY_CAPACITY, flight.getCapacity());

                db.update(TABLE_FLIGHT,
                          values,
                          KEY_FLIGHT_NO + " = '" +
                          flight.getFlightNo() + "'",
                          null);

            }
        }

        db.close();
    }

    public void addReservation(Reservation reservation)
    {
        Log.d(TAG, "addReservation() - " + reservation.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_TYPE, reservation.getTransactionType());
        values.put(KEY_USERNAME, reservation.getUsername());
        values.put(KEY_FLIGHT_NO, reservation.getFlightNo());
        values.put(KEY_DEPARTURE, reservation.getDeparture());
        values.put(KEY_ARRIVAL, reservation.getArrival());
        values.put(KEY_DEPARTURE_TIME, reservation.getDepartureTime());
        values.put(KEY_NO_TICKETS, reservation.getNoOfTickets());
        values.put(KEY_RESERVATION_NO, reservation.getReservationNo());
        values.put(KEY_TOTAL, reservation.getTotal());
        values.put(KEY_TIMESTAMP, reservation.getTimestamp());


        db.insert(TABLE_RESERVATION_LOG, null, values);

        db.close();
    }

    public ArrayList<Reservation> getAllReservations()
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_RESERVATION_LOG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Reservation reservation = null;
        if(cursor.moveToFirst())
        {
            do
            {
                reservation = new Reservation();
                reservation.setId(cursor.getInt(0));
                reservation.setTransactionType(cursor.getString(1));
                reservation.setUsername(cursor.getString(2));
                reservation.setFlightNo(cursor.getString(3));
                reservation.setDeparture(cursor.getString(4));
                reservation.setArrival(cursor.getString(5));
                reservation.setDepartureTime(cursor.getString(6));
                reservation.setNoOfTickets(cursor.getInt(7));
                reservation.setReservationNo(cursor.getString(8));
                reservation.setTotal(cursor.getDouble(9));
                reservation.setTimestamp(cursor.getString(10));

                reservations.add(reservation);

            } while(cursor.moveToNext());
        }

        Log.d(TAG, "getAllReservations() - " + reservations.toString());

        db.close();

        return reservations;
    }

    public void deleteReservation(Reservation reservation)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_RESERVATION_LOG,
                  KEY_RESERVATION_NO + " = '" +
                  reservation.getReservationNo() + "'",
                  null);

        db.close();

        Log.d(TAG, "deleteReservation() - " + reservation.toString());
    }
    
    public void addCancellationLog(CancellationLog cancellationLog) 
    {
        Log.d(TAG, "addCancellationLog() - " + cancellationLog.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CANCELLATION_INFO, cancellationLog.getCancellationInfo());
        values.put(KEY_TIMESTAMP, cancellationLog.getTimestamp());

        db.insert(TABLE_CANCELLATION_LOG, null, values);

        db.close(); 
    }

    public ArrayList<CancellationLog> getAllCancellationLogs()
    {
        ArrayList<CancellationLog> cancellationLogs = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CANCELLATION_LOG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        CancellationLog cancellationLog = null;
        if(cursor.moveToFirst())
        {
            do
            {
                cancellationLog = new CancellationLog();
                cancellationLog.setId(cursor.getInt(0));
                cancellationLog.setCancellationInfo(cursor.getString(1));

                cancellationLogs.add(cancellationLog);

            } while(cursor.moveToNext());
        }

        Log.d(TAG, "getAllCancellationLogs() - " + cancellationLogs.toString());

        db.close();

        return cancellationLogs;
    }
}
