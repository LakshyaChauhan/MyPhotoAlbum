package com.example.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AddImageActivity extends AppCompatActivity {

    EditText title,description;
    ImageView imageView;
    Button button;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;

    private  Bitmap selecectdImage, scaledImage;

    private  static  final  String [] permission = {"android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Image");
        setContentView(R.layout.activity_add_image);

        registerActivityForSelectImage();

        //register activity
        registerActivityForSelectImage();

        title = findViewById(R.id.editTextAddImageTitle);
        description = findViewById(R.id.editTextAddImageDescription);
        imageView = findViewById(R.id.imageViewAddImage);
        button = findViewById(R.id.buttonAddImageSave);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                   if(checkSelfPermission(permission[0])!= PackageManager.PERMISSION_GRANTED){
                       requestPermissions(permission,1);
                   }
                   else{
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncherForSelectImage.launch(intent);
                   }
               }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selecectdImage == null){
                    Toast.makeText(AddImageActivity.this,"Please select an image!",Toast.LENGTH_SHORT).show();
                }
                else{
                    String t = title.getText().toString(), d = description.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImage = makesmall(selecectdImage,300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    byte[] image = outputStream.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("title",t);
                    intent.putExtra("description",d);
                    intent.putExtra("image",image);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });
    }

    public void registerActivityForSelectImage(){
        activityResultLauncherForSelectImage =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data =result.getData();

                        if(resultCode == RESULT_OK && data!=null){
                            try {
                                selecectdImage = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                                imageView.setImageBitmap(selecectdImage);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImage.launch(intent);
        }
    }

    public  Bitmap makesmall(Bitmap image, int maxsize){
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width/ (float) height;

        if(ratio>1){
            width = maxsize;
            height = (int)(width/ratio);
        }
        else{
            height= maxsize;
            width = (int)(height *ratio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}