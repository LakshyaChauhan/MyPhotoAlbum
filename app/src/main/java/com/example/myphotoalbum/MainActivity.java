package com.example.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    MyImagesViewModel myImagesViewModel;

    ActivityResultLauncher<Intent> activityResultLauncherforAddImage,activityResultLauncherforUpdateImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        myImagesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyImagesViewModel.class);


        recyclerView = findViewById(R.id.rev);
        fab =findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyImagesAdapter adapter = new MyImagesAdapter();
        recyclerView.setAdapter(adapter);





        registerActivityForAddImage();
        registerActivityForUpdateImage();







        myImagesViewModel.getAllimages().observe(MainActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {
                adapter.setImages(myImages);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddImageActivity.class);
                activityResultLauncherforAddImage.launch(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myImagesViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setListner(new MyImagesAdapter.onImageClickListner() {
            @Override
            public void onImageClick(MyImages myImages) {
                Intent intent =new Intent(MainActivity.this, UpdateImageActivity.class);
                intent.putExtra("id",myImages.getImage_id());
                intent.putExtra("title",myImages.getImage_title());
                intent.putExtra("description",myImages.getImage_description());
                intent.putExtra("image",myImages.getImage());


                activityResultLauncherforUpdateImage.launch(intent);
            }
        });
    }



    public void registerActivityForUpdateImage(){

        activityResultLauncherforUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode ==RESULT_OK && data!=null){
                            String t = data.getStringExtra("Updatetitle");
                            String d = data.getStringExtra("Updatedescription");
                            byte[] image = data.getByteArrayExtra("image");
                            int id = data.getIntExtra("id",-1);
                            MyImages myImages = new MyImages(t,d,image);
                            myImages.setId(id);
                            myImagesViewModel.update(myImages);
                        }
                    }
                });
    }
    public void registerActivityForAddImage(){
        activityResultLauncherforAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode == RESULT_OK && data!= null){
                            String title1 = data.getStringExtra("title");
                            String description1 = data.getStringExtra("description");
                            byte[] image = data.getByteArrayExtra("image");

                            MyImages myImages = new MyImages(title1,description1,image);
                            myImagesViewModel.insert(myImages);
                        }
                    }
                });

    }
}