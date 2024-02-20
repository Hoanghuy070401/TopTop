package com.example.toptop;

import android.app.Activity;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {
    private ImageView createPost;
    private  ImageView pageUser;

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();

    private VideoPlayerRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        anhxa();
    }

    private void anhxa(){

        createPost = findViewById(R.id.btnadd);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingActivity.this, uploadData.class);
                startActivity(intent);
            }
        });
        pageUser =  findViewById(R.id.btn_user);
        pageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FollowingActivity.this, LoginActivity.class);
                startActivity(intent);
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
        databaseRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(FollowingActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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
    private int  count =0;
    public void btnforyou(View view){
        Intent intent = new Intent(FollowingActivity.this,HomeActivity.class );
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        Animatoo.animateSlideLeft(this);
        finish();
    }

}