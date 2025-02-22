package com.example.e_shop_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_shop_demo.Model.User;
import com.example.e_shop_demo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

   // https://github.com/rey5137/material

    EditText phonenoEdittText,passEdittText;
    CheckBox remMechkbx;
    TextView forgetPasswordLink;
    TextView adminPanelLink,NotadminPanelLink;
    Button LoginActivityLogInbtn;
    private String parentDBName = "User";

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        phonenoEdittText = findViewById(R.id.phonenoEdittText);
        passEdittText = findViewById(R.id.passEdittText);
        LoginActivityLogInbtn = findViewById(R.id.LoginActivityLogInbtn);
        NotadminPanelLink = findViewById(R.id.NotadminPanelLink);

        adminPanelLink = findViewById(R.id.adminPanelLink);

        remMechkbx = findViewById(R.id.remMechkbx);
        Paper.init(this);


        loadingBar = new ProgressDialog(this);


        LoginActivityLogInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                logInUser();

            }
        });

        adminPanelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginActivityLogInbtn.setText("Login Admin");
                adminPanelLink.setVisibility(View.INVISIBLE);
                NotadminPanelLink.setVisibility(View.VISIBLE);

                //this database is created for the admin
                parentDBName = "Admins";


            }
        });

        NotadminPanelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //

                LoginActivityLogInbtn.setText("Login");
                adminPanelLink.setVisibility(View.VISIBLE);
                NotadminPanelLink.setVisibility(View.INVISIBLE);

                //this dataBase is created for the user
                parentDBName = "User";

            }
        });


    }

    private void logInUser() {

        String phoneNo = phonenoEdittText.getText().toString();
        String password = passEdittText.getText().toString();


        if(TextUtils.isEmpty(phoneNo))
        {
            //RegisterNameEdittText.setCursorVisible(true);
            Toast.makeText(getApplicationContext(),"Phone no is empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            // RegisterNameEdittText.setCursorVisible(true);
            Toast.makeText(getApplicationContext()," Password field is empty",Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Log In Account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            allowAcessToAccount(phoneNo,password);

        }
    }

    private void allowAcessToAccount(final String phoneNo, final String password) {

        if(remMechkbx.isChecked())
        {
            //Prevalant is a class with two member variable - (userPhoneKey,userPasswordKey)

            //if remmber me check box is checked then the user will not have to go through the log in
            Paper.book().write(Prevalent.userPhoneKey,phoneNo);
            Paper.book().write(Prevalent.userPasswordKey,password);

        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDBName).child(phoneNo).exists())
                {

                    User userDate = dataSnapshot.child(parentDBName).child(phoneNo).getValue(User.class);

                    if(userDate.getPhoneNo().equals(phoneNo))
                    {
                        //checks if the user is an admin or regular user!
                        if (userDate.getPassword().equals(password))
                        {
                            //checks which database has to be entered..is it the admin or the regular user!

                            if(parentDBName.equals("Admins"))
                            {

                                Toast.makeText(getApplicationContext(),"Welcome Admin! Log in Sucessful",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LogInActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);

                            }
                            else if(parentDBName.equals("User"))
                            {
                                Toast.makeText(getApplicationContext(),"Log in Sucessful",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(),"wrong Password",Toast.LENGTH_SHORT).show();


                        }
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Account With this "+phoneNo +" doesn't exist!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
