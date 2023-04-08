package com.example.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.media.Image;
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

public class UpdateImageActivity extends AppCompatActivity {

    EditText title, description;
    ImageView imageView;


    String t,d;
    private int id;
    private byte[]image;
    Button button;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;

    private Bitmap selecectdImage, scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Image");
        setContentView(R.layout.activity_update_image);

        registerActivityForSelectImage();

        title = findViewById(R.id.editTextUpdateImageTitle);
        description = findViewById(R.id.editTextUpdateImageDescription);
        imageView = findViewById(R.id.imageViewUpdateImage);
        button =findViewById(R.id.buttonUpdateImageSave);

        id = getIntent().getIntExtra("id",-1);
        t = getIntent().getStringExtra("title");
        d = getIntent().getStringExtra("description");
        image = getIntent().getByteArrayExtra("image");

        title.setText(t);
        description.setText(d);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);
            }
        });
    }

    public void updateData(){

        if(id == -1){
            Toast.makeText(UpdateImageActivity.this,"There is aproblem",Toast.LENGTH_SHORT).show();
        }else {
            String t = title.getText().toString(), d = description.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("Updatetitle", t);
            intent.putExtra("Updatedescription", d);
            if (selecectdImage == null) {
                intent.putExtra("image",image);
            } else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makesmall(selecectdImage, 300);
                scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] image = outputStream.toByteArray();

                intent.putExtra("image", image);

            }
            setResult(RESULT_OK, intent);
            finish();
        }
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