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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterLogInbtn;
    private EditText RegisterpassEdittText,RegisterphonenoEdittText,RegisterNameEdittText;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterLogInbtn = findViewById(R.id.RegisterLogInbtn);
        RegisterphonenoEdittText = findViewById(R.id.RegisterphonenoEdittText);
        RegisterNameEdittText = findViewById(R.id.RegisterNameEdittText);
        RegisterpassEdittText = findViewById(R.id.RegisterpassEdittText);
        loadingBar = new ProgressDialog(this);



        RegisterLogInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount();

            }
        });
    }

    private void createAccount() {

        String name = RegisterNameEdittText.getText().toString();
        String phoneNo = RegisterphonenoEdittText.getText().toString();
        String password = RegisterpassEdittText.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            RegisterNameEdittText.setCursorVisible(true);
        }
        else if(TextUtils.isEmpty(phoneNo))
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

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            validatePhoneNo(name,phoneNo,password);
        }
    }

    private void validatePhoneNo(final String name, final String phoneNo, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("User").child(phoneNo).exists()))
                {
                    HashMap<String,Object>userDataMap = new HashMap<>();
                    userDataMap.put("Phone No",phoneNo);
                    userDataMap.put("UserName",name);
                    userDataMap.put("Password",password);
                    RootRef.child("User").child(phoneNo).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Suecssfully created new account!",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                                startActivity(intent);
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(),"UnSuecssfully,please try again!",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                else {
                    Toast.makeText(getApplicationContext(),"This"+phoneNo+" already has account",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Try another phone no",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
