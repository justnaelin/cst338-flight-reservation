package com.cst338.naelin.flightrerservationsystem;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener
{
    // Widgets
    private Button createAccountButton;
    private Button reserveSeatButton;
    private Button manageSystemButton;
    private Button cancelReservationButton;

    // Database
    SQLiteHelper db = SQLiteHelper.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initializeDatabase();

        createAccountButton = (Button) findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(this);

        reserveSeatButton = (Button) findViewById(R.id.reserve_seat_button);
        reserveSeatButton.setOnClickListener(this);

        cancelReservationButton = (Button) findViewById(R.id.cancel_reservation_button);
        cancelReservationButton.setOnClickListener(this);

        manageSystemButton = (Button) findViewById(R.id.manage_system_button);
        manageSystemButton.setOnClickListener(this);

    }

    private void initializeDatabase()
    {
        // Create accounts
        db.addLogin(new Login("A@lice5", "@cSit100"));
        db.addLogin(new Login("$BriAn7", "123aBc##"));
        db.addLogin(new Login("!chriS12", "CHrIS12!!"));
        db.addLogin(new Login("!admiM2", "!admiM2"));

        // Add flights
        db.addFlight(new Flight("Otter101", "Monterey", "Los Angeles", "10:30(AM)", 10, 150));
        db.addFlight(new Flight("Otter102", "Los Angeles", "Monterey", "1:00(PM)", 10, 150));
        db.addFlight(new Flight("Otter201", "Monterey", "Seattle", "11:00(AM)", 5, 200.50));
        db.addFlight(new Flight("Otter205", "Monterey", "Seattle", "3:45(PM)", 15, 150));
        db.addFlight(new Flight("Otter202", "Seattle", "Monterey", "2:10(PM)", 5, 200.50));

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.create_account_button)
        {
            startActivity(new Intent(this, CreateAccountActivity.class));
        }
        else if(view.getId() == R.id.reserve_seat_button)
        {
            startActivity(new Intent(this, ReserveSeatActivity.class));
        }
        else if(view.getId() == R.id.cancel_reservation_button)
        {
            startActivity(new Intent(this, CancelReservationActivity.class));
        }
        else if(view.getId() == R.id.manage_system_button)
        {
            startActivity(new Intent(this, ManageSystemActivity.class));
        }


    }
}
