package com.example.toptop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Adapter.SoundAdapter;
import com.example.toptop.Models.SoundModel;
import com.example.toptop.editorVideo.PortraitCameraActivity;

import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private List<SoundModel> soundModelList = new ArrayList<>();
    private SoundAdapter soundAdapter;
    private ImageView btnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        recyclerview = findViewById(R.id.recyclerView);
        btnSound = (ImageView) findViewById(R.id.btnClose);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoundActivity.this, PortraitCameraActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        LinearLayoutManager layoutManager3= new LinearLayoutManager(this);
        layoutManager3.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(new GridLayoutManager(this,1));


        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));
        soundModelList.add(new SoundModel("","",""));

        soundAdapter = new SoundAdapter(soundModelList,getApplicationContext());
        recyclerview.setAdapter(soundAdapter);
        soundAdapter.notifyDataSetChanged();
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SoundActivity.this, PortraitCameraActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}