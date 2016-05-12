package com.cst338.naelin.flightrerservationsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener
{
    // Widgets
    private Button createAccountButton;
    private EditText usernameInput;
    private EditText passwordInput;

    // Database
    SQLiteHelper db;

    // Backend logic
    private int numTimesInvalidFormat;
    private int numTimesUsernameExists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Get database
        db = SQLiteHelper.getInstance(this);

        // Reset the number of times the user has failed to create an account
        numTimesInvalidFormat = 0;
        numTimesUsernameExists = 0;

        createAccountButton = (Button) findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.create_account_button)
        {
            usernameInput = (EditText) findViewById(R.id.username_edit_text);
            passwordInput = (EditText) findViewById(R.id.password_edit_text);

            if(usernameInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Missing a username and/or password!", Toast.LENGTH_LONG).show();
            }
            else
            {
                final View v = view;
                if(!checkExistingUsername(usernameInput.getText().toString()))
                {
                    if(verifyUsernameAndPassword())
                    {
                        // Add new account to database
                        Login account = new Login(usernameInput.getText().toString(), passwordInput.getText().toString());
                        db.addLogin(account);

                        // Add new account accountLog to database
                        AccountLog accountLog = new AccountLog("New Account", usernameInput.getText().toString(), getTimestamp());
                        db.addAccountLog(accountLog);

                        new AlertDialog.Builder(this)
                                .setTitle("Success!")
                                .setMessage("Your account has been created")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(v.getContext(), MenuActivity.class));
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                }
                else
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Error!")
                            .setMessage("Username already exists")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    numTimesUsernameExists++;
                                    if(numTimesUsernameExists > 1)
                                        startActivity(new Intent(v.getContext(), MenuActivity.class));                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
        }
    }

    public boolean checkExistingUsername(String username)
    {
        // Get all accounts from database
        ArrayList<Login> logins = db.getAllLogins();

        // Check if username already exists in database
        boolean doesExist = false;

        for(int i = 0; i < logins.size(); i++)
        {
            if(logins.get(i).getUsername().equals(username))
            {
                doesExist = true;
                break;
            }
        }

        return doesExist;
    }

    public boolean verifyUsernameAndPassword()
    {
        boolean isVerified = true;

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(username);
        boolean usernameHasSpecial = matcher.matches();
        matcher = pattern.matcher(password);
        boolean passwordHasSpecial = matcher.matches();

        boolean usernameHasNumbers = username.matches(".*\\d+.*");
        boolean passwordHasNumbers = password.matches(".*\\d+.*");
        boolean usernameHasUppercase = !username.equals(username.toLowerCase());
        boolean usernameHasLowercase = !username.equals(username.toUpperCase());
        boolean passwordHasUppercase = !password.equals(password.toLowerCase());
        boolean passwordHasLowercase = !password.equals(password.toUpperCase());

        if(!usernameHasNumbers || !passwordHasNumbers|| !usernameHasUppercase || !usernameHasLowercase || usernameHasSpecial || passwordHasSpecial || !passwordHasUppercase || !passwordHasLowercase)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Invalid format for username and/or password!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            numTimesInvalidFormat++;
                            if(numTimesInvalidFormat > 1)
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));                                }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            isVerified = false;
        }

        return isVerified;
    }

    public String getTimestamp()
    {
        Date date = new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp.toString();
    }
}
