package com.cst338.naelin.flightrerservationsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>
{
    private List<Flight> flightsList = new ArrayList<>();

    public FlightsAdapter(List<Flight> flightsList) {
        for(Flight flight : flightsList)
        {
            this.flightsList.add(flight);
        }
    }

    @Override
    public int getItemCount() {
        return flightsList.size();
    }

    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flight_card_layout, viewGroup, false);


        return new FlightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FlightViewHolder flightViewHolder, int i)
    {
        Flight flightInfo = flightsList.get(i);
        flightViewHolder.flightNoTextView.setText("Flight No. " + flightInfo.getFlightNo());
        flightViewHolder.departureTextView.setText("Departure: " + flightInfo.getDeparture());
        flightViewHolder.arrivalTextView.setText("Arrival: " + flightInfo.getArrival());
        flightViewHolder.departureTimeTextView.setText("Departure Time: " + flightInfo.getDepartureTime());
        flightViewHolder.capacityTextView.setText(String.valueOf("No. Tickets: " + flightInfo.getCapacity()));
        flightViewHolder.priceTextView.setText(String.valueOf("Price: $" + flightInfo.getPrice()));

    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder
    {
        public View flightView;
        protected TextView flightNoTextView;
        protected TextView departureTextView;
        protected TextView arrivalTextView;
        protected TextView departureTimeTextView;
        protected TextView capacityTextView;
        protected TextView priceTextView;

        public FlightViewHolder(View v)
        {
            super(v);

            flightView = v;
            flightView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ConfirmReservation.promptLogin(view, view.getContext());
                }
            });

            flightNoTextView = (TextView) v.findViewById(R.id.flight_no_text_view);
            departureTextView = (TextView) v.findViewById(R.id.departure_text_view);
            arrivalTextView = (TextView) v.findViewById(R.id.arrival_text_view);
            departureTimeTextView = (TextView) v.findViewById(R.id.departure_time_text_view);
            capacityTextView = (TextView) v.findViewById(R.id.capacity_text_view);
            priceTextView = (TextView) v.findViewById(R.id.price_text_view);
        }

    }

}
