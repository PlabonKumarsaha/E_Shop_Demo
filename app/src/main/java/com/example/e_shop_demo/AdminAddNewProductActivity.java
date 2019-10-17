package com.example.e_shop_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private  String categoryName;

    ImageView SelectProductImage;
    EditText productName,productDescription,productPrice;
    Button newProductbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        //Toast.makeText(getApplicationContext(),"Welcome Admin",Toast.LENGTH_SHORT).show();

        categoryName = getIntent().getExtras().get("category").toString();

       // Toast.makeText(getApplicationContext(),categoryName,Toast.LENGTH_SHORT).show();
        SelectProductImage = findViewById(R.id.SelectProductImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        newProductbtn = findViewById(R.id.newProductbtn);

    }
}
