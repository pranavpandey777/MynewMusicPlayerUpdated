package com.example.mynewmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ProjectViewHolder> {
    private Context context;
    private ArrayList<Music> arraylist;
    MediaPlayer mediaPlayer;


    public MyRecyclerAdapter(Context context, ArrayList<Music> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        ProjectViewHolder viewHolder = new ProjectViewHolder(view, arraylist, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder holder, int position) {
        Music project = arraylist.get(position);

        holder.song.setText(project.getTitle());
        final String path = project.getPath();
        mediaPlayer = new MediaPlayer();


        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(project.getPath());

        byte [] data = mmr.getEmbeddedPicture();
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.image.setImageBitmap(bitmap);
        }
        else
        {
            holder.image.setImageResource(R.drawable.songs);
        }

        holder.image.setAdjustViewBounds(true);


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("custom-message");
                intent.putExtra("path",path);



                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


            }
        });


    }

    public void update(ArrayList<Music> datas) {
        arraylist.clear();
        arraylist.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView song;
        MaterialCardView card;
        ImageView image;

        ArrayList<Music> arrayList;
        Context context;


        public ProjectViewHolder(View itemView, ArrayList<Music> arrayList, Context context) {
            super(itemView);
            song = itemView.findViewById(R.id.name);
            card = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.image);
            this.arrayList = arrayList;
            this.context = context;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

        }
    }




}
