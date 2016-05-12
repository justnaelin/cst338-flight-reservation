package com.cst338.naelin.flightrerservationsystem;

import android.app.Dialog;
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

import java.util.ArrayList;

public class ManageSystemActivity extends AppCompatActivity
{
    private Dialog dialog;
    private static TextView usernameInput;
    private static TextView passwordInput;
    private RecyclerView reservationsRecyclerView;
    private LinearLayoutManager reservationsLayoutManager;
    private ReservationsAdapter reservationAdapter;

    boolean isSuccessful;

    // Database
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);

        promptLogin();
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
                    // TODO: displayTransactionLogs();
                }
            }
        });

        // TODO: Create a "cancel" button when login dialog is prompted
    }

    private boolean verifyLogin(String username, String password)
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
            Toast.makeText(this, "Incorrect username and/or password!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }

        return isVerified;
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
}
