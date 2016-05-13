package com.cst338.naelin.flightrerservationsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AccountLogsAdapter extends RecyclerView.Adapter<AccountLogsAdapter.AccountLogViewHolder>
{
    private List<AccountLog> accountLogs = new ArrayList<>();

    public AccountLogsAdapter (List<AccountLog> accountLogs) {
        for(AccountLog accountLog : accountLogs)
        {
            this.accountLogs.add(accountLog);
        }
    }

    @Override
    public int getItemCount() {
        return accountLogs.size();
    }

    @Override
    public AccountLogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.account_log_layout, viewGroup, false);

        return new AccountLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AccountLogViewHolder accountLogViewHolder, int i)
    {
        AccountLog accountLogInfo = accountLogs.get(i);
        accountLogViewHolder.accountLogTextView.setText(String.valueOf(accountLogInfo));
    }

    public static class AccountLogViewHolder extends RecyclerView.ViewHolder
    {
        public View accountLogView;
        protected TextView accountLogTextView;

        public AccountLogViewHolder(View v)
        {
            super(v);

            accountLogView = v;

            accountLogTextView = (TextView) v.findViewById(R.id.transaction_type_text_view);

        }

    }

}
