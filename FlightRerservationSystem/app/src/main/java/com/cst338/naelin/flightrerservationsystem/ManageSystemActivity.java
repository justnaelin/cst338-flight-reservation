package com.cst338.naelin.flightrerservationsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManageSystemActivity extends AppCompatActivity implements View.OnClickListener
{
    private Dialog dialog;
    private static TextView usernameInput;
    private static TextView passwordInput;
    private RecyclerView accountLogsRecyclerView;
    private RecyclerView reservationLogsRecyclerView;
    private RecyclerView cancellationLogsRecyclerView;
    private Button confirmButton;

    private LinearLayoutManager accountLogsLayoutManager;
    private LinearLayoutManager reservationsLayoutManager;
    private LinearLayoutManager cancellationLogsLayoutManager;

    private AccountLogsAdapter accountLogsAdapter;
    private ReservationsAdapter reservationAdapter;
    private CancellationLogsAdapter cancellationLogAdapter;

    boolean isSuccessful;
    boolean yesClick;
    boolean isValid;


    // Database
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);

        db = SQLiteHelper.getInstance(this);
        promptLogin();

        // Confirm button
        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(this);

        // Recyler view
        accountLogsRecyclerView = (RecyclerView) findViewById(R.id.account_logs_recycler_view);
        reservationLogsRecyclerView = (RecyclerView) findViewById(R.id.reservation_logs_recycler_view);
        cancellationLogsRecyclerView = (RecyclerView) findViewById(R.id.cancellation_logs_recycler_view);

        // Configure LinearLayouts
        accountLogsLayoutManager = new LinearLayoutManager(this);
        accountLogsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reservationsLayoutManager = new LinearLayoutManager(this);
        reservationsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        cancellationLogsLayoutManager = new LinearLayoutManager(this);
        cancellationLogsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set the recycler view's layout
        accountLogsRecyclerView.setLayoutManager(accountLogsLayoutManager);
        reservationLogsRecyclerView.setLayoutManager(reservationsLayoutManager);
        cancellationLogsRecyclerView.setLayoutManager(cancellationLogsLayoutManager);

    }

    public void promptLogin()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.login_layout);
        formatDialog(dialog);

        dialog.show();

        Button loginButton = (Button) dialog.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // Get username and password to search database
                usernameInput = (EditText) dialog.findViewById(R.id.username_edit_text);
                passwordInput = (EditText) dialog.findViewById(R.id.password_edit_text);

                Log.d("ManageSystem", "username="+usernameInput.getText().toString());
                Log.d("ManageSystem", "password="+passwordInput.getText().toString());

                isSuccessful = verifyLogin(usernameInput.getText().toString(), passwordInput.getText().toString());

                if(isSuccessful)
                {
                    Log.d("CancelReservation", "isSuccessful="+isSuccessful);
                   displayTransactionLogs();
                }
            }
        });

        // TODO: Create a "cancel" button when login dialog is prompted
    }

    private boolean verifyLogin(String username, String password)
    {
        boolean isVerified = false;

        if(username.equals("!admiM2") && password.equals("!admiM2"))
        {
            isVerified = true;
        }

        if(!isVerified)
        {
            Toast.makeText(this, "Incorrect username and/or password!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }

        return isVerified;
    }

    public void displayTransactionLogs()
    {

        accountLogsAdapter = new AccountLogsAdapter(db.getAllAccountLogs());
        accountLogsRecyclerView.setAdapter(accountLogsAdapter);

        reservationAdapter = new ReservationsAdapter(db.getAllReservations());
        reservationLogsRecyclerView.setAdapter(reservationAdapter);

        cancellationLogAdapter = new CancellationLogsAdapter(db.getAllCancellationLogs());
        cancellationLogsRecyclerView.setAdapter(cancellationLogAdapter);

        if(db.getAllAccountLogs().isEmpty() && db.getAllReservations().isEmpty() && db.getAllCancellationLogs().isEmpty())
        {
            new AlertDialog.Builder(this)
                    .setTitle("There is no log information!")
                    .setMessage("Return to Menu?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            promptAddNewFlight();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    private void formatDialog(Dialog dialog)
    {
        // Format dialog box real quick
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.confirm_button)
        {
            if(isSuccessful)
                promptAddNewFlight();
            else
            {
                Toast.makeText(this, "Admin access only!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MenuActivity.class));
            }
        }
    }

    public void promptAddNewFlight()
    {
        new AlertDialog.Builder(this)
                .setTitle("Add New Flight")
                .setMessage("Would you like to add a new flight?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final Dialog addFlightDialog = new Dialog(ManageSystemActivity.this);
                        addFlightDialog.setContentView(R.layout.add_flight_layout);
                        formatDialog(addFlightDialog);
                        addFlightDialog.show();

                        // Get inputted information
                        final EditText flightNoEditText = (EditText) addFlightDialog.findViewById(R.id.flight_no_edit_text);
                        final EditText departureEditText = (EditText) addFlightDialog.findViewById(R.id.departure_edit_text);
                        final EditText arrivalEditText = (EditText) addFlightDialog.findViewById(R.id.arrival_edit_text);
                        final EditText departureTimeEditText = (EditText) addFlightDialog.findViewById(R.id.departure_time_edit_text);
                        final EditText flightCapacityEditText = (EditText) addFlightDialog.findViewById(R.id.flight_capacity_edit_text);
                        final EditText priceEditText = (EditText) addFlightDialog.findViewById(R.id.price_edit_text);


                        // Add Flight button
                        final Button addFlightButton = (Button) addFlightDialog.findViewById(R.id.add_flight_button);
                        addFlightButton.setOnClickListener(new View.OnClickListener()
                        {

                            @Override
                            public void onClick(View view)
                            {
                                if((flightNoEditText.getText().toString().isEmpty() || departureEditText.getText().toString().isEmpty() ||
                                        arrivalEditText.getText().toString().isEmpty() || departureTimeEditText.getText().toString().isEmpty() ||
                                    flightCapacityEditText.getText().toString().isEmpty() || priceEditText.getText().toString().isEmpty()) ||
                                checkIfFlightExists(flightNoEditText.getText().toString()))
                                {
                                    isValid = false;
                                }
                                else
                                 isValid = true;

                                if(!isValid)
                                {


                                    new AlertDialog.Builder(ManageSystemActivity.this)
                                            .setTitle("Error!")
                                            .setMessage("Information entered is not valid or [Flight No.] already exists!")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                                }

                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                                else
                                {
                                    final Flight flightToAdd = new Flight();
                                    flightToAdd.setFlightNo(flightNoEditText.getText().toString());
                                    flightToAdd.setDeparture(departureEditText.getText().toString());
                                    flightToAdd.setArrival(arrivalEditText.getText().toString());
                                    flightToAdd.setDepartureTime(departureTimeEditText.getText().toString());
                                    flightToAdd.setCapacity(Integer.parseInt(flightCapacityEditText.getText().toString()));
                                    flightToAdd.setPrice(Double.parseDouble(priceEditText.getText().toString()));

                                    new AlertDialog.Builder(ManageSystemActivity.this)
                                            .setTitle("Confirm Flight Information")
                                            .setMessage(String.valueOf(flightToAdd))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                        db.addFlight(flightToAdd);
                                                        Toast.makeText(getApplicationContext(), "Flight successfully added!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));


                                                }

                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(), "Flight was not added", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }
                        });

                        // Cancel button
                        Button cancelButton = (Button) addFlightDialog.findViewById(R.id.cancel_add_button);
                        cancelButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                addFlightDialog.cancel();
                            }
                        });


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public boolean checkIfFlightExists(String flightNo)
    {
        ArrayList<Flight> flights = db.getAllFlights();
        for(Flight flight : flights)
        {
            if(flight.getFlightNo().equals(flightNo))
            {
                return true;
            }
        }
        return false;
    }
}



