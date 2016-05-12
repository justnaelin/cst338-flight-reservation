package com.cst338.naelin.flightrerservationsystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class UserLogin {

    // Database
    private static SQLiteHelper db;
    private static Context context;
    private static boolean isSuccessful;
    private static Dialog dialog;
    private static Dialog confirmationDialog;
    private static TextView usernameInput;
    private static TextView passwordInput;
    private static View cardView;

    public static boolean promptLogin(View cV, Context c)
    {
        cardView = cV;
        isSuccessful = false;
        context = c;
        db = SQLiteHelper.getInstance(context);


        dialog = new Dialog(cardView.getContext());
        dialog.setContentView(R.layout.login_layout);
        dialog.setCancelable(true);

        formatDialog(dialog);

        TextView flightNo = (TextView) cardView.findViewById(R.id.flight_no_text_view);
        dialog.show();

        Button loginButton = (Button) dialog.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                // Get username and password to search database
                usernameInput = (TextView) dialog.findViewById(R.id.username_edit_text);
                passwordInput = (TextView) dialog.findViewById(R.id.password_edit_text);

                isSuccessful = verifyLogin(usernameInput.getText().toString(), passwordInput.getText().toString());

                if(isSuccessful)
                {
                    confirmFlight(cardView);
                }
            }
        });
        return isSuccessful;
    }

    private static boolean verifyLogin(String username, String password)
    {
        boolean isVerified = false;

        ArrayList<Login> logins = db.getAllLogins();

        for(Login login : logins)
        {
            if(login.getUsername().equals(username))
            {
                if(login.getPassword().equals(password))
                {
                    isVerified = true;
                }
            }
        }

        if(!isVerified)
        {
            Toast.makeText(context, "Incorrect login credentials!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }

        return isVerified;
    }

    private static void confirmFlight(View view)
    {
        confirmationDialog = new Dialog(view.getContext());
        confirmationDialog.setContentView(R.layout.confirmation_layout);
        confirmationDialog.setTitle("Confirm Reservation");
        confirmationDialog.setCancelable(true);

        formatDialog(confirmationDialog);

        // Get flight information that user clicked on
        final TextView flightNo = (TextView) view.findViewById(R.id.flight_no_text_view);
        final TextView departure = (TextView) view.findViewById(R.id.departure_text_view);
        final TextView arrival = (TextView) view.findViewById(R.id.arrival_text_view);
        final TextView departureTime = (TextView) view.findViewById(R.id.departure_time_text_view);
        final EditText noOfTickets = (EditText)((Activity) context).findViewById(R.id.quantity_edit_text);
        final TextView amountOwed = (TextView) view.findViewById(R.id.price_text_view);

        // Set flight information in confirmationDialog
        TextView usernameTextView = (TextView) confirmationDialog.findViewById(R.id.username_text_view);
        usernameTextView.setText(usernameInput.getText().toString());

        TextView flightNoTextView = (TextView) confirmationDialog.findViewById(R.id.flight_no_text_view);
        flightNoTextView.setText(flightNo.getText().toString());

        TextView departureTextView = (TextView) confirmationDialog.findViewById(R.id.departure_text_view);
        departureTextView.setText(departure.getText().toString());

        TextView arrivalTextView = (TextView) confirmationDialog.findViewById(R.id.arrival_text_view);
        arrivalTextView.setText(arrival.getText().toString());

        TextView departureTimeTextView = (TextView) confirmationDialog.findViewById(R.id.departure_time_text_view);
        departureTimeTextView.setText(departureTime.getText().toString());

        TextView noOfTicketsTextView = (TextView) confirmationDialog.findViewById(R.id.no_of_tickets_text_view);
        noOfTicketsTextView.setText("No. of Tickets: " + noOfTickets.getText().toString());

        // Generate Reservation No.
        final TextView reservationNoTextView = (TextView) confirmationDialog.findViewById(R.id.reservation_no_text_view);
        reservationNoTextView.setText("Reservation No. " + (flightNo.getText().toString() + departure.getText().toString()).hashCode());


        final double total = Integer.parseInt(noOfTickets.getText().toString()) * Double.parseDouble(amountOwed.getText().toString().substring(8));

        TextView amountOwedTextView = (TextView) confirmationDialog.findViewById(R.id.total_owed_text_view);
        amountOwedTextView.setText("Total: $" + String.valueOf(total));


        confirmationDialog.show();

        Button confirmButton = (Button) confirmationDialog.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Reservation reservation = new Reservation();
                reservation.setTransactionType("Reserve Seat");
                reservation.setUsername(usernameInput.getText().toString());
                reservation.setFlightNo(flightNo.getText().toString().substring(11));
                reservation.setDeparture(departure.getText().toString().substring(11));
                reservation.setArrival(arrival.getText().toString().substring(9));
                reservation.setDepartureTime(departureTime.getText().toString().substring(16));
                reservation.setNoOfTickets(Integer.parseInt(noOfTickets.getText().toString()));
                reservation.setReservationNo(reservationNoTextView.getText().toString().substring(16)); // TODO: Where does reservation no. come from?

                Log.d("UserLogin", "confirmFlight->onClick" + reservation.getReservationNo());
                reservation.setTotal(total);
                reservation.setTimestamp(getTimestamp());

                db.addReservation(reservation);
                db.updateFlightCapacity(reservation.getFlightNo(), reservation.getNoOfTickets());
                confirmationDialog.cancel();
                Toast.makeText(context, "Reservation has been made!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MenuActivity.class));
            }
        });

        Button cancelButton = (Button) confirmationDialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                confirmationDialog.cancel();
                context.startActivity(new Intent(context, MenuActivity.class));

            }
        });
    }

    private static void formatDialog(Dialog dialog)
    {
        // Format dialog box real quick
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
    }



    public static String getTimestamp()
    {
        Date date = new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp.toString();
    }

}
