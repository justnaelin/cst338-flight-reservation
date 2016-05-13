package com.cst338.naelin.flightrerservationsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by naelin on 5/12/16.
 */
public class CancelReservation
{
    private static View view;
    private static Context context;

    private static SQLiteHelper db;

    public static void confirmCancellation(View v, Context c)
    {
        view = v;
        context = c;
        db = SQLiteHelper.getInstance(context);

        final EditText usernameEditText = (EditText) ((CancelReservationActivity) c).dialog.findViewById(R.id.username_edit_text);


        new AlertDialog.Builder(context)
                .setTitle("Confirm Cancellation")
                .setMessage("Are you sure you want to cancel this reservation?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Reservation reservationToDelete = new Reservation();

                        TextView reservationNoTextView = (TextView) view.findViewById(R.id.reservation_no_text_view);

                        // Retrieve reservation no. and delete it from the database
                        reservationToDelete.setReservationNo(reservationNoTextView.getText().toString().substring(18));
                        db.deleteReservation(reservationToDelete);

                        TextView departureTextView = (TextView) view.findViewById(R.id.departure_text_view);
                        TextView arrivalTextView = (TextView) view.findViewById(R.id.arrival_text_view);
                        TextView departureTimeTextView = (TextView) view.findViewById(R.id.departure_time_text_view);

                        // Update the quantity (put back the no. of tickets available) of the flight to the database
                        // Get flight no. to see which flight to update
                        TextView flightNoTextView = (TextView) view.findViewById(R.id.flight_no_text_view);
                        TextView noOfTicketsTextView = (TextView) view.findViewById(R.id.no_of_tickets_text_view);

                        Flight flightToUpdate = new Flight();
                        flightToUpdate.setFlightNo(flightNoTextView.getText().toString());

                        flightToUpdate.setCapacity(Integer.parseInt(noOfTicketsTextView.getText().toString().substring(13)) * -1);
                        db.updateFlightCapacity(flightToUpdate.getFlightNo().substring(11), flightToUpdate.getCapacity());

                        Toast.makeText(context, "Your reservation has been cancelled!", Toast.LENGTH_SHORT).show();

                        CancellationLog cancellationLog = new CancellationLog();
                        String timestamp = getTimestamp();
                        String cancellationLogString = "Transaction type: Cancellation\n" +
                                                        "Customer's username: " + usernameEditText.getText().toString() + "\n" +
                                                        flightNoTextView.getText().toString() + "\n" +
                                                        departureTextView.getText().toString() + ", " + departureTimeTextView.getText().toString() + "\n" +
                                                        arrivalTextView.getText().toString() + "\n" +
                                                        noOfTicketsTextView.getText().toString() + "\n" +
                                                        "Reservation No.: " + reservationToDelete.getReservationNo() + "\n" +
                                                        "Transaction date: " + timestamp.substring(0, 10) + "\n" +
                                                        "Transaction time: " + timestamp.substring(12);


                        cancellationLog.setCancellationInfo(cancellationLogString);

                        db.addCancellationLog(cancellationLog);
                        context.startActivity(new Intent(context, MenuActivity.class));

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        AlertDialog.Builder failedCancellation = new AlertDialog.Builder(context);
                        failedCancellation.setTitle("Failed to Cancel");
                        failedCancellation.setMessage("Return to the menu");
                        failedCancellation.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                context.startActivity(new Intent(context, MenuActivity.class));
                            }

                        });
                        failedCancellation.show();
                    }
                }).show();
         }


    public static String getTimestamp()
    {
        Date date = new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp.toString();
    }
}
