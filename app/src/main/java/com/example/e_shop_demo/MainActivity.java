package com.example.e_shop_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_shop_demo.Model.User;
import com.example.e_shop_demo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button joinNowbtn,mainLoginbtn;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadingBar = new ProgressDialog(this);

        mainLoginbtn = findViewById(R.id.mainLoginbtn);
        joinNowbtn = findViewById(R.id.joinNowbtn);



        mainLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        joinNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //using Paper library
        Paper.init(this);
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userpasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey != "" || userpasswordKey!= "")
        {
            if(!TextUtils.isEmpty(userpasswordKey) && !TextUtils.isEmpty(userpasswordKey))
            {
                AllowAcess(userPhoneKey,userpasswordKey);

                loadingBar.setTitle("Already logged In!Welcome sir!");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }



    }

    private void AllowAcess(final String phoneNo, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("User").child(phoneNo).exists())
                {

                    User userDate = dataSnapshot.child("User").child(phoneNo).getValue(User.class);
                    if(userDate.getPhoneNo().equals(phoneNo))
                    {
                        if (userDate.getPassword().equals(password))
                        {
                            Toast.makeText(getApplicationContext(),"Log in Sucessful",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);

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
