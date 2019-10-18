package com.example.e_shop_demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    //private  String categoryName;

    ImageView SelectProductImage;
    EditText productName,productDescription,productPrice;
    Button newProductbtn;
    private static final int galleryPic = 1;
    private Uri imageURI;
    private String saveCurrentDate,saveCurrentTime;

    private String productRandomKey;
    //private DatabaseReference productImagesRef;
    private StorageReference ProductImagesRef;

    private String categoryName,Description,price,prodName;
    private String downloadImageUrl;

    private DatabaseReference productRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        //Toast.makeText(getApplicationContext(),"Welcome Admin",Toast.LENGTH_SHORT).show();

        categoryName = getIntent().getExtras().get("category").toString();
        //ProductImagesRef = FirebaseDatabase.getInstance().getReference().child("Product images");
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product images");

       // Toast.makeText(getApplicationContext(),categoryName,Toast.LENGTH_SHORT).show();
        SelectProductImage = findViewById(R.id.SelectProductImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        newProductbtn = findViewById(R.id.newProductbtn);
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loadingBar = new ProgressDialog(this);


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

        loadingBar.setTitle("Adding New product");
        loadingBar.setMessage("please wait!");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        //storing cuurent Date

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd,yyyy");

        saveCurrentDate = simpleDateFormat.format(calendar.getTime());

        //storing current time
        //here a in simpleDateFormate means am/pm in 12 hr format

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime = currentTime.format(calendar.getTime());

        //creating a random usnique key from the date and time
        productRandomKey = saveCurrentDate+saveCurrentTime;

        //imageURI.getLastPathSegment() gets the image name .This is a string type value so we concatinate this with product key

        //DatabaseReference filepath = productImagesRef.child(imageURI.getLastPathSegment()+ productRandomKey + ".jpg");
       // DatabaseReference fileath = productImagesRef.child(imageURI.getLastPathSegment()+productRandomKey+".jpg");

       // StorageReference filePath = P;
       // final UploadTask uploadTask = filePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

            // final UploadTask uploadTask = filepath.push()

       // StorageReference filepath = mstorageRef.child("folder").child(filename);

        //Uri File= Uri.fromFile(new File(mFileName));
        //addding file name as a unique value

         final StorageReference filePath = ProductImagesRef.child(imageURI.getLastPathSegment()+ productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(getApplicationContext(),"error :"+e ,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getApplicationContext(),"product Image upload sucessful" ,Toast.LENGTH_SHORT).show();


                Task<Uri>urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(getApplicationContext()," got the product image" ,Toast.LENGTH_SHORT).show();

                            saveProductinfoToDatabase();
                        }

                    }
                });
            }
        });



    }

    private void saveProductinfoToDatabase() {


        //savig all the data in the hash map
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("pname",prodName);

        //save the data in firebase

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    loadingBar.dismiss();

                    //after sucessful adding a product the admin will be taken back to the admin category activity where he can
                    //store other datas!
                    Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext()," product details added sucessfully!" ,Toast.LENGTH_SHORT).show();

                }
                else {
                    loadingBar.dismiss();

                    //task.getException()
                    Toast.makeText(getApplicationContext()," Error :"+task.getException().toString() ,Toast.LENGTH_SHORT).show();

                }

            }
        });


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

        if(requestCode == galleryPic && resultCode == RESULT_OK && data != null)
        {

            imageURI = data.getData();
            SelectProductImage.setImageURI(imageURI);
        }
    }
}
