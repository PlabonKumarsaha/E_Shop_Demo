package com.example.e_shop_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button RegisterLogInbtn;
    EditText RegisterpassEdittText,RegisterphonenoEdittText,RegisterNameEdittText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterLogInbtn = findViewById(R.id.RegisterLogInbtn);
        RegisterphonenoEdittText = findViewById(R.id.RegisterphonenoEdittText);
        RegisterNameEdittText = findViewById(R.id.RegisterNameEdittText);
        RegisterpassEdittText = findViewById(R.id.RegisterpassEdittText);


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

        }
    }
}
