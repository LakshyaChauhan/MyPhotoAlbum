package com.example.myphotoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyImagesViewModel extends AndroidViewModel {

    MyImagesRepository repository;
    LiveData<List<MyImages>> imagelist;

    public MyImagesViewModel(@NonNull Application application) {
        super(application);
        repository = new MyImagesRepository(application);
        imagelist = repository.getAllImages();
    }

    public void insert(MyImages myImages){
        repository.insert(myImages);

    }
    public void update(MyImages myImages){
        repository.update(myImages);
    }
    public void delete(MyImages myImages){
        repository.delete(myImages);
    }
    public LiveData<List<MyImages>> getAllimages(){
        return imagelist;
    }
}
