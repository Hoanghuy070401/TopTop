package com.example.toptop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.SoundModel;
import com.example.toptop.R;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {

    List<SoundModel> soundModelList;
    Context context;

    public SoundAdapter(List<SoundModel> soundModelList, Context context) {
        this.soundModelList = soundModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sound,parent,false);


        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {

        SoundModel soundModel = soundModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return soundModelList.size();
    }

    public class SoundViewHolder extends RecyclerView.ViewHolder {
        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
