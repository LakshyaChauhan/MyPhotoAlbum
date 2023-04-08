package com.example.myphotoalbum;

import android.app.Application;
//import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDao myImagesDao;
    private LiveData<List<MyImages>> images;



        public MyImagesRepository (Application application){
        MyImagesDatabase database = MyImagesDatabase.getInstance(application);
        myImagesDao = database.myImagesDao();
        images = myImagesDao.getAllImages();
    }

//    ExecutorService executorService = Executors.newSingleThreadExecutor();


    public void insert(MyImages myImages){
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                myImagesDao.insert(myImages);
//            }
//        });

        new InsertImageAsyncAask(myImagesDao).execute(myImages);
    }

    public void update(MyImages myImages){
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                myImagesDao.update(myImages);
//            }
//        });
        new UpdateImageAsyncAask(myImagesDao).execute(myImages);
    }

    public void delete(MyImages myImages){
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                myImagesDao.delete(myImages);
//            }
//        });
        new DeleteImageAsyncAask(myImagesDao).execute(myImages);
    }

    public LiveData<List<MyImages>> getAllImages(){

        return images;
    }

    private  static  class InsertImageAsyncAask extends AsyncTask<MyImages,Void,Void>{


        MyImagesDao  imagesDao;
        public InsertImageAsyncAask(MyImagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(MyImages... myImages) {

            imagesDao.insert(myImages[0]);
            return null;
        }
    }
    private  static  class UpdateImageAsyncAask extends AsyncTask<MyImages,Void,Void>{


        MyImagesDao  imagesDao;
        public UpdateImageAsyncAask(MyImagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(MyImages... myImages) {

            imagesDao.update(myImages[0]);
            return null;
        }
    }
    private  static  class DeleteImageAsyncAask extends AsyncTask<MyImages,Void,Void>{


        MyImagesDao  imagesDao;
        public DeleteImageAsyncAask(MyImagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(MyImages... myImages) {

            imagesDao.delete(myImages[0]);
            return null;
        }
    }
}
