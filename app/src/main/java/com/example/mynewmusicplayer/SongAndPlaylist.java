package com.example.mynewmusicplayer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

@SuppressLint("ValidFragment")
public class SongAndPlaylist extends Fragment {

    String val;

    @SuppressLint("ValidFragment")
    public SongAndPlaylist(String val) {
        this.val = val;

    }

    ArrayList<Music> arrayList;
    RecyclerView recyclerView;
    Music music;
    MediaPlayer mediaPlayer;
    ListView listView;
    private BottomSheetBehavior mBottomSheetBehaviour;
    String path;
    String ipath;
    SeekBar seekBar;
    Button back, play, forward;
    MyRecyclerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songandplaylist, container, false);


        if (val.equals("songs")) {
            View nestedScrollView = view.findViewById(R.id.nestedScrollView);
            mBottomSheetBehaviour = BottomSheetBehavior.from(nestedScrollView);

            recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mediaPlayer = new MediaPlayer();
            seekBar = view.findViewById(R.id.seekbar);
            back = view.findViewById(R.id.back);
            play = view.findViewById(R.id.play);
            forward = view.findViewById(R.id.forward);
            seekBar.setClickable(false);
            play.setBackgroundResource(R.drawable.pause);
            forward.setBackgroundResource(R.drawable.ford);
            back.setBackgroundResource(R.drawable.back);


            mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:

                            mediaPlayer.reset();

                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:

                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:

                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:

                            mediaPlayer.reset();

                            break;
                        case BottomSheetBehavior.STATE_SETTLING:

                            break;
                    }
                }

                // Toast.makeText(getActivity(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();


                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        play.setBackgroundResource(R.drawable.play);
                    } else {
                        mediaPlayer.start();
                        play.setBackgroundResource(R.drawable.pause);
                    }


                }
            });


            new MyBack().execute();


        } else if (val.equals("playlist")) {



        }


        return view;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            path = intent.getStringExtra("path");

            // Toast.makeText(getActivity()," "+path ,Toast.LENGTH_SHORT).show();
            /*ipath=path;*/

            /*     mediaPlayer.reset();*/

            play.setBackgroundResource(R.drawable.pause);


            try {
                playMySong(path);
            } catch (IOException e) {
                e.printStackTrace();
            }


            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    };

    void playMySong(String songPath) throws IOException {


        mediaPlayer.reset();
        mediaPlayer.setDataSource(songPath);
        mediaPlayer.prepare();

        mediaPlayer.start();
    }


    public class MyBack extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            arrayList = new ArrayList<>();

            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = contentResolver.query(song, null, null, null, null);

            while (cursor.moveToNext()) {

                int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songPath = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                String currentTtrack = cursor.getString(songTitle);
                String curretArtist = cursor.getString(songArtist);
                String currentTrackPath = cursor.getString(songPath);
                music = new Music(currentTtrack, curretArtist, currentTrackPath);
                arrayList.add(music);


                // Toast.makeText(getActivity(), "" + arrayList, Toast.LENGTH_SHORT).show();


            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new MyRecyclerAdapter(getActivity(), arrayList);
                    recyclerView.setAdapter(adapter);
                    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                            new IntentFilter("custom-message"));


                }
            });

            return null;
        }
    }


}


