package com.cst338.naelin.flightrerservationsystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReserveSeatActivity extends Activity implements View.OnClickListener
{
    // Widgets
    private Button findFlightsButton;
    private EditText departureEditText;
    private EditText arrivalEditText;
    private EditText quantityEditText;
    private RecyclerView flightsRecyclerView;
    private LinearLayoutManager flightsLayoutManager;
    private FlightsAdapter flightsAdapter;

    // Database
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        // Get database
        db = SQLiteHelper.getInstance(this);

        // Find flights button
        findFlightsButton = (Button) findViewById(R.id.find_flights_button);
        findFlightsButton.setOnClickListener(this);

        // Input text boxes
        departureEditText = (EditText) findViewById(R.id.departure_edit_text);
        arrivalEditText = (EditText) findViewById(R.id.arrival_edit_text);
        quantityEditText = (EditText) findViewById(R.id.quantity_edit_text);

        // Recyler view
        flightsRecyclerView = (RecyclerView) findViewById(R.id.flights_recycler_view);

        // Configure LinearLayout
        flightsLayoutManager = new LinearLayoutManager(this);
        flightsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set the recycler view's layout
        flightsRecyclerView.setLayoutManager(flightsLayoutManager);
    }

    @Override
    public void onClick(final View view)
    {
        if(view.getId() == R.id.find_flights_button)
        {
            if(checkRequiredFields())
            {
                List<Flight> desiredFlights = displayFlights();

                if(!desiredFlights.isEmpty())
                {
                    flightsAdapter = new FlightsAdapter(desiredFlights);
                    flightsRecyclerView.setAdapter(flightsAdapter);
                }
                else
                {
                    final View v = view;

                    new AlertDialog.Builder(this)
                            .setTitle("There are no available flights!")
                            .setMessage("Return to Menu?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(v.getContext(), MenuActivity.class));
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
        }

    }

    public boolean checkRequiredFields()
    {
        boolean isFilled = true;

        boolean isDepartureEmpty = departureEditText.getText().toString().isEmpty();
        boolean isArrivalEmpty = arrivalEditText.getText().toString().isEmpty();
        boolean isQuantityEmpty = quantityEditText.getText().toString().isEmpty();

        if(isDepartureEmpty || isArrivalEmpty || isQuantityEmpty)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all the fields")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            isFilled = false;
        }
        else if(Integer.parseInt(quantityEditText.getText().toString()) <= 0 || Integer.parseInt(quantityEditText.getText().toString()) > 7)
        {
            new AlertDialog.Builder(this)
                    .setTitle("System Restriction")
                    .setMessage("Please only enter a quantity from 1-7")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


            isFilled = false;
        }

        return isFilled;
    }

    public List<Flight> displayFlights()
    {
        ArrayList<Flight> flights = db.getAllFlights();
        ArrayList<Flight> desiredFlights = new ArrayList<>();

        int quantity = Integer.parseInt(quantityEditText.getText().toString());
        String departureLocation = departureEditText.getText().toString();
        String arrivalLocation = arrivalEditText.getText().toString();

        for(Flight flight : flights)
        {
            if(flight.getDeparture().equalsIgnoreCase(departureLocation) && flight.getArrival().equalsIgnoreCase(arrivalLocation) && flight.getCapacity() >= quantity)
            {
                desiredFlights.add(flight);
                Log.d("TEST", String.valueOf(flight));
            }
        }
        return desiredFlights;

    }
}
