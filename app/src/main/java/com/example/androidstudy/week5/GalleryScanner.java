package com.example.androidstudy.week5;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.androidstudy.AndroidApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mypc on 2017-07-23.
 */

public class GalleryScanner {

    //클래스의 이름을 string value로 가져온다.
    private static final String TAG = GalleryScanner.class.getSimpleName();

    public static final int ALL_PHOTO_BUCKET = 0;

    //projection:어떤 열만 가져오겠따
    private static final String[] projection = {
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
    };

    public static List<Photo> photoScan(int albumId){
        String selection =null;
        //selection은 행을 선택
        if(albumId!= ALL_PHOTO_BUCKET)
            selection = String.format("%s = %d", MediaStore.Images.Media.BUCKET_ID,albumId);

        Cursor cursor = MediaStore.Images.Media.query(//쿼리:table에서 요청하는 문장만 가져올 수 있게 하는 함수
                AndroidApplication.getContext().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,//열
                selection,//행
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC" //날짜별로 내림차순 정렬
        );
        Log.d(TAG,"cursor is initialized and cursor total size:"+cursor.getCount());

        int photoIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        List<Photo> photoList = new ArrayList<>();

        while(cursor.moveToNext()){
            int bucketId = cursor.getInt(bucketIdColumn);
            String bucketName  = cursor.getColumnName(bucketNameColumn);
            int photoId = cursor.getInt(photoIdColumn);
            String path = cursor.getString(pathColumn);

            Photo photo = new Photo();
            photo.setBucketId(bucketId);
            photo.setPath(path);

            photoList.add(photo);
        }
        return  photoList;
    }
}
