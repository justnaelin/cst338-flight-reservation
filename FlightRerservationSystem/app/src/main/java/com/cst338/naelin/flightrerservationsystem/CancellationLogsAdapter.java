package com.cst338.naelin.flightrerservationsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CancellationLogsAdapter extends RecyclerView.Adapter<CancellationLogsAdapter.CancellationLogViewHolder>
{
    private List<CancellationLog> cancellationLogs = new ArrayList<>();

    public CancellationLogsAdapter(List<CancellationLog> cancellationLogs) {
        for(CancellationLog cancellationLog : cancellationLogs)
        {
            this.cancellationLogs.add(cancellationLog);
        }
    }

    @Override
    public int getItemCount() {
        return cancellationLogs.size();
    }

    @Override
    public CancellationLogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancellation_log_layout, viewGroup, false);

        return new CancellationLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CancellationLogViewHolder cancellationLogViewHolder, int i)
    {
        CancellationLog cancellationLogInfo = cancellationLogs.get(i);
        cancellationLogViewHolder.cancellationLogTextView.setText(String.valueOf(cancellationLogInfo));
    }

    public static class CancellationLogViewHolder extends RecyclerView.ViewHolder
    {
        public View cancellationLogView;
        protected TextView cancellationLogTextView;

        public CancellationLogViewHolder(View v)
        {
            super(v);

            cancellationLogView= v;

            cancellationLogTextView = (TextView) v.findViewById(R.id.cancellation_log_text_view);

        }

    }

}
