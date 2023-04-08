package com.example.myphotoalbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImageHolder> {

    List<MyImages> imagesList = new ArrayList<>();

    private onImageClickListner listner;

    public void setListner(onImageClickListner listner) {
        this.listner = listner;
    }

    public void setImages(List<MyImages> imagesList ){
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public interface  onImageClickListner{
        void onImageClick(MyImages myImages);
    }

    public  MyImages getPosition(int position){
        return  imagesList.get(position);
    }

    @NonNull
    @Override
    public MyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card,parent,false);
        return new MyImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageHolder holder, int position) {
        MyImages myImages = imagesList.get(position);
        holder.title.setText(myImages.image_title);
        holder.description.setText(myImages.image_description);
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(),
                0, myImages.getImage().length));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyImageHolder extends RecyclerView.ViewHolder{

        TextView title,description;
        ImageView imageView;
        public MyImageHolder(@NonNull View itemView) {
            super(itemView);

            title= itemView.findViewById(R.id.textViewTitle);
            description=itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.emage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if(listner!=null&& position !=RecyclerView.NO_POSITION){
                        listner.onImageClick(imagesList.get(position));
                    }
                }
            });
        }
    }

}
