package com.example.androidstudy.week5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidstudy.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by mypc on 2017-07-23.
 */

public class PhotoAdapter extends  RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{


    private Context mContext;
    private LayoutInflater mInflater;

    private List<Photo> photoList = new ArrayList<>();//scanner에서 가져오는 그림을 가질 배열
    private PhotoClickListener mClickListener;

    public PhotoAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);//xml을 뷰로 그려줌
    }

    public void setOnItemClickListener(PhotoClickListener listener){
        mClickListener=listener;
    }
    @Override
    //view를 받아서 viewholder로 리턴
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHoler is called");
        return new PhotoViewHolder(mInflater.inflate(R.layout.item_photo,parent,false));
    }

    //그림을 view에 실제로 그리는 부분
    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder holder, int position) {

        Log.d(TAG,"onBindViewHolder is called");
        Photo photo = photoList.get(position);
        holder.bind(photo);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"getItemCount is called with cound:" + photoList.size());
        return photoList.size();
    }
    public void addPhotoList(List<Photo> photoList){
        this.photoList.addAll(photoList);
    }

    class PhotoViewHolder extends  RecyclerView.ViewHolder{
        private ImageView photoImage;
        private TextView photoPath;

        private Photo photo;

        PhotoViewHolder(View itemView){//n+3개의 뷰를 만듬,view를 담는 친구
            super(itemView);
            photoImage = (ImageView)itemView.findViewById(R.id.imageView);
            photoPath = (TextView)itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClickPhoto(photo);
                }
            });
        }
        private void bind(Photo photo){
            this.photo = photo;

            Glide.with(mContext).load(photo.getPath()).into(photoImage);
            photoPath.setText(photo.getPath());
        }
    }

    public interface PhotoClickListener{
        public void onClickPhoto(Photo photo);
    }
}
