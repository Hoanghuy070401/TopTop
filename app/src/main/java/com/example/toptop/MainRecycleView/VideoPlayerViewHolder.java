package com.example.toptop.MainRecycleView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.Models.User;
import com.example.toptop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {
    FrameLayout media_container;
    CircleImageView user_image;
    TextView title,nameUser;
    ImageView thumbnail,volumeControl,soundDick;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    DatabaseReference databaseReference;


    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container=itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        volumeControl = itemView.findViewById(R.id.volumOn);
        title = itemView.findViewById(R.id.textInputUser);;
        progressBar = itemView.findViewById(R.id.progressBar);
        soundDick = itemView.findViewById(R.id.imageDick);
        user_image = itemView.findViewById(R.id.circleImageUser);
        nameUser = itemView.findViewById(R.id.nameuser);
    }


    public void onBind(MediaObject mediaObject, RequestManager requestManager){
        this.requestManager = requestManager;
        parent.setTag(this);
//        Glide.with(itemView.getContext()).load(mediaObject.getUser().getPhotoUrl()).into(user_image);
        title.setText(mediaObject.getDescription()+",\n"+mediaObject.getDatePost());
//        nameUser.setText(mediaObject.getUser().getUser_name());
        this.requestManager.load(mediaObject.getThumbnail()).into(thumbnail);

    }
    DatabaseReference mUserdata = FirebaseDatabase.getInstance().getReference().child("User");

    User us = new User();
    private User getUserFromFirebseByID(String idU)
    {
        mUserdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        User uss = dataSnapshot.getValue(User.class);
                        if(uss.getUser_id().equals(idU))
                        {
                            us = uss;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return us;
    }

}
