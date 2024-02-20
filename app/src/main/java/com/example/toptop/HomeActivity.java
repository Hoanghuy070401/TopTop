package com.example.toptop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.toptop.MainRecycleView.VerticalSpacingItemDecorator;
import com.example.toptop.MainRecycleView.VideoPlayerRecyclerAdapter;
import com.example.toptop.MainRecycleView.VideoPlayerRecyclerView;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.Models.User;
import com.example.toptop.editorVideo.PortraitCameraActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

      private ImageView createPost;
      private  ImageView pageUser;
      private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();
      private  ArrayList<User> user= new ArrayList<>();
      private VideoPlayerRecyclerView recyclerView;
      private static final int CAMERA_PERMISSION_REQUEST_CODE = 88888;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();

    }
    private void anhxa(){

        createPost = findViewById(R.id.btnadd);

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                Intent intent = new Intent(HomeActivity.this, PortraitCameraActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        pageUser =  findViewById(R.id.btn_user);
        pageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        //lam mo trang thai
        if(Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23){
          setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,true);
        }
        if(Build.VERSION.SDK_INT >= 19 ){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //m
        if(Build.VERSION.SDK_INT >= 23){
          setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
          getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //recycleview
        recyclerView = (VideoPlayerRecyclerView) findViewById(R.id.recyclerViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(0);
        recyclerView.addItemDecoration(itemDecorator);

        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerView);

        LoadPost();
    }


    private void LoadPost() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MediaObject");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mediaObjectList = new ArrayList<>();
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MediaObject mediaObject = dataSnapshot.getValue(MediaObject.class);
                        mediaObjectList.add(mediaObject);
                    }
                    recyclerView.setMediaObjects(mediaObjectList);
                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjectList, initGlide());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setDrawingCacheEnabled(true);
                    adapter.notifyDataSetChanged();
                    recyclerView.setKeepScreenOn(true);
                    recyclerView.smoothScrollToPosition(mediaObjectList.size() + 1);
                }
            }@Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public static void setWindowFlag(@NotNull Activity activity, final int bits , boolean on){
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on){
            winParams.flags |= bits;
        }else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.black)
                .error(R.color.black);
        return  Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onDestroy (){
        if (recyclerView!=null)
            recyclerView.releasePlayer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recyclerView!=null)
            recyclerView.releasePlayer();
        finish();
    }



    public void folowingBtn(View view){
        Intent intent = new Intent(HomeActivity.this,FollowingActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        // request camera permission if it has not been grunted.
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HomeActivity.this, "permission has been grunted.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(HomeActivity.this, "[WARN] permission is not grunted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private int  count =0;
    public void messengerPage(View view){
        Intent intent = new Intent(HomeActivity.this,MesengerActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    public void discoverPage(View view){
        Intent intent = new Intent(HomeActivity.this,DiscoverActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    public void homePage(View view){

    }

    @Override
    public void onBackPressed() {


        if(count<1)
        {
            Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
            count++;
        }else {
            finishAffinity();
        }
    }
}
