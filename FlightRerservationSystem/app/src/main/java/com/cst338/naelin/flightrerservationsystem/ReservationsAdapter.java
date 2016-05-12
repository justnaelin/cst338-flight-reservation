package com.cst338.naelin.flightrerservationsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>
{
    private List<Reservation> reservationsList = new ArrayList<>();

    public ReservationsAdapter(List<Reservation> reservationsList) {
        for(Reservation reservation : reservationsList)
        {
            this.reservationsList.add(reservation);
        }
    }

    @Override
    public int getItemCount() {
        return reservationsList.size();
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_card_layout, viewGroup, false);

        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder reservationViewHolder, int i)
    {
        Reservation reservationInfo = reservationsList.get(i);
        reservationViewHolder.reservationNo.setText("Reservation No. : " + reservationInfo.getReservationNo());
        reservationViewHolder.flightNoTextView.setText("Flight No. " + reservationInfo.getFlightNo());
        reservationViewHolder.departureTextView.setText("Departure: " + reservationInfo.getDeparture());
        reservationViewHolder.arrivalTextView.setText("Arrival: " + reservationInfo.getArrival());
        reservationViewHolder.departureTimeTextView.setText("Departure Time: " + reservationInfo.getDepartureTime());
        reservationViewHolder.noOfTickets.setText(String.valueOf("No. Tickets: " + reservationInfo.getNoOfTickets()));

    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder
    {
        public View reservationView;
        protected TextView reservationNo;
        protected TextView flightNoTextView;
        protected TextView departureTextView;
        protected TextView arrivalTextView;
        protected TextView departureTimeTextView;
        protected TextView noOfTickets;

        public ReservationViewHolder(View v)
        {
            super(v);

            reservationView = v;
            reservationView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    CancelReservation.confirmCancellation(view, view.getContext());
                }
            });

            reservationNo = (TextView) v.findViewById(R.id.reservation_no_text_view);
            flightNoTextView = (TextView) v.findViewById(R.id.flight_no_text_view);
            departureTextView = (TextView) v.findViewById(R.id.departure_text_view);
            arrivalTextView = (TextView) v.findViewById(R.id.arrival_text_view);
            departureTimeTextView = (TextView) v.findViewById(R.id.departure_time_text_view);
            noOfTickets = (TextView) v.findViewById(R.id.no_of_tickets_text_view);
        }

    }

}
