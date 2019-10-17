package com.example.e_shop_demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminAddNewProductActivity extends AppCompatActivity {

    //private  String categoryName;

    ImageView SelectProductImage;
    EditText productName,productDescription,productPrice;
    Button newProductbtn;
    private static final int galleryPic = 1;
    private Uri imageURI;
    private String saveCurrentDate,saveCurrentTime;

    private String productRandomKey;

    private String categoryName,Description,price,prodName;

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

        SelectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                openGallery();
            }


        });


        newProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateProductData();
            }
        });

    }

    private void validateProductData() {

        Description = productDescription.getText().toString();
        price = productPrice.getText().toString();
        prodName =productName.getText().toString();

        if(imageURI == null)
        {
            Toast.makeText(getApplicationContext(),"Must add product Image",Toast.LENGTH_SHORT).show();

        }
        else if(Description.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Must Write product Description",Toast.LENGTH_SHORT).show();

        }
        else if(price.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Must Write price ",Toast.LENGTH_SHORT).show();

        }
        else if(prodName.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Must Write product Name ",Toast.LENGTH_SHORT).show();

        }
        else {
            storeproductInformation();
        }
    }

    private void storeproductInformation() {

        Calendar calendar = Calendar.getInstance();

        //storing cuurent Date

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd,yyyy");

        saveCurrentDate = simpleDateFormat.format(calendar.getTime());

        //storing current time
        //here a in simpleDateFormate means am/pm in 12 hr format

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;


    }


    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPic && requestCode == RESULT_OK && data != null)
        {

            imageURI = data.getData();
            SelectProductImage.setImageURI(imageURI);
        }
    }
}
