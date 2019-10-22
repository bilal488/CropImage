package com.example.bilal.cropimage;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////
        b = (Button)findViewById(R.id.btn_crop);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkandroidversion();
            }
        });

    }

    //Crop Image
    public void checkandroidversion(){

        //If Android version M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            }
            catch (Exception e) {

            }
        } else {
            pickImage();
        }
    }

    //PICK IMAGE METHOD
    public void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    //CROP REQUEST JAVA
    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    //FOR ACTIVITY RESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            /*Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height);


            Uri imgUri=Uri.parse("android.resource://your.package.name/"+R.drawable.image);

            */
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());

                    ((ImageView)findViewById(R.id.imageview)).setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //FOR ACTIVITY RESULT PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkandroidversion();
        }
    }


    @Override
    public void onBackPressed() {
        open();
    }

    //Dialog box
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage("Are you really want to exit");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //to close app
                finish();//to close app
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }//dialog box end

}
