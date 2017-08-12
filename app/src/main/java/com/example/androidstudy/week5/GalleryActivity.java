package com.example.androidstudy.week5;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.androidstudy.R;

import java.util.List;

/**
 * Created by mypc on 2017-07-23.
 */

public class GalleryActivity extends AppCompatActivity implements PhotoAdapter.PhotoClickListener {
    private static final String TAG = GalleryActivity.class.getSimpleName();

    private RecyclerView photoListView;
    private PhotoAdapter adapter;

    private final int STORAGE_PERMISSION_REQUEST = 2017;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        photoListView = (RecyclerView) findViewById(R.id.photo_list_view);

        //listview나 recyclerview는 불특정 n개를 다루므로 xml을 만들어 줄 수 없어서 adapter이용
        //recyclerview는 adapter,layoutmanager꼭 필요
        adapter = new PhotoAdapter(this);
        adapter.setOnItemClickListener(this);

        //어떤 layout으로 될지 선언     grid->는 바둑판 모양 3->열이 3줄
        //this는 context : activity, receive, serivice, p? 는 application의 context를 이용하여 만들어진 것이므로 this만 써도 context를 인자로 받을 수 있다.
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);

        photoListView.setAdapter(adapter);
        photoListView.setLayoutManager(layoutManager);

        Log.d(TAG,"permission request is called");

        //승낙 여부가 grantResults 배열에 저장됨
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        //stoage_permission_request : 대충 1000대의 높은숫자
        ActivityCompat.requestPermissions(this,permissions,STORAGE_PERMISSION_REQUEST);

    }

    //권한체크가 끝났을 때 안드로이드 내부적으로 부르는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_PERMISSION_REQUEST:
                //승낙 여부
                if(grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                    runGalleryScan();
                }
        }
    }
    private void runGalleryScan(){
        new Thread(new Runnable() {//mainthread대신 작업을 할 놈 만들어줌,thread 하나당 하나의 일을 함
            @Override
            public void run() {//무슨 작업을 할지 써줌
                final List<Photo> photoList = GalleryScanner.photoScan(GalleryScanner.ALL_PHOTO_BUCKET);
                Log.d(TAG,"photoScan is done");
                Log.d(TAG,"photo list size:"+photoList.size());
                runOnUiThread(new Runnable() {//main ui thread로 다시 돌아감
                    @Override
                    public void run() {
                        Log.d(TAG,"runOnUiThread run is called");
                        adapter.addPhotoList(photoList);
                        adapter.notifyDataSetChanged();//bind가 다시 됨
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClickPhoto(Photo photo) {
        Toast.makeText(this,photo.getPath(),Toast.LENGTH_SHORT).show();
    }
}

