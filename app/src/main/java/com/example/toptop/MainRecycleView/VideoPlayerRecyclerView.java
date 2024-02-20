package com.example.toptop.MainRecycleView;


import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerRecyclerView extends RecyclerView {

    private static final String TAG = "VideoPlayerRecyclerView";
    private View itemView;


    private enum VolumeState{ON,OFF};

    //ui
    private ImageView thumbnail,volumeControl,soundDick;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private CircleImageView user_image;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;
    //vars
    private ArrayList<MediaObject> mediaObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight =0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;
    public  CircleImageView profileBtn;

    //controlling playback state
    private  VolumeState volumeState;



    public VideoPlayerRecyclerView(@NonNull Context context) {

        super(context);
        intit(context);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        intit(context);
    }

    private void intit(Context context){
        progressBar = new ProgressBar(getContext());
        progressBar.setVisibility(GONE);
        this.context = context.getApplicationContext();
        Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView =new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        AdaptiveTrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //2 create the player
        videoPlayer =  ExoPlayerFactory.newSimpleInstance(context,trackSelector);
        //bind the player to the view
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        setVolumeControl(VolumeState.ON);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged:called.");
                    if (thumbnail != null) {
//                        thumbnail.setVisibility(VISIBLE);
                    }

                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true);
                    } else {
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (viewHolderParent != null && viewHolderParent.equals(view)){
                    resetVideoView();
                }
            }
        });

        videoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline,@Nullable Object manifest, int reason) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video");
                        if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: ENDED video");
                        videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play");
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }
                        if (!isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
             }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });

    }
    private VideoPlayerViewHolder holder;
    public void playVideo(boolean isEndOfList ){
        int targetPosition;

        if (!isEndOfList){
            int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();

            //if there is more them 2 list-items on the screen , set the difference to be 1
            if(endPosition-startPosition>1){
                endPosition = startPosition+1;

            }

            //something is wrong return
            if (startPosition<0 || endPosition<0){
                return;
            }

            //if there is more than 1 list-item on the screen
            if(startPosition!= endPosition){
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight >endPositionVideoHeight ? startPosition:endPosition;

            }
            else{
                targetPosition=startPosition;
            }
        }
        else {
              targetPosition= mediaObjects.size()-1;
        }
        Log.d(TAG,"playVideo :target position:"+targetPosition);

        //video is already playing so return
        if (targetPosition == playPosition){
            return;
        }
        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView ==null){
            return;
        }
        //remove any old surfaces views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition = targetPosition-((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null){
            return;
        }
        holder = (VideoPlayerViewHolder) child.getTag();
        if(holder==null){
            playPosition=-1;
            return;
        }

        soundDick = holder.soundDick;
        thumbnail = holder.thumbnail;
        progressBar = holder.progressBar;
        volumeControl = holder.volumeControl;
        viewHolderParent = holder.itemView;
        requestManager = holder.requestManager;
        user_image = holder.user_image;
        frameLayout = holder.itemView.findViewById(R.id.media_container);

        Glide.with(context).load(R.drawable.recording).into(soundDick);
        ///
        videoSurfaceView.setPlayer(videoPlayer);
        viewHolderParent.setOnClickListener(videoViewClickListenner);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context," RecyclerView VideoPlayer"));
        String mediaUrld = mediaObjects.get(targetPosition).getMedia_url();
        if (mediaUrld !=null){
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mediaUrld));
            videoPlayer.prepare(videoSource);
            videoPlayer.setPlayWhenReady(true);
        }
    }
    private OnClickListener videoViewClickListenner = new OnClickListener() {
        @Override
        public void onClick(View view) {
            toggleVolume();
        }
    };

    private int getVisibleVideoSurfaceHeight(int playPosition){
        int at=playPosition -((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG,"getVisibleVideoSurfaceHeight: at:"+at);

        View child = getChildAt(at);
        if (child==null){
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1]<0){
            return  location[1] + videoSurfaceDefaultHeight;

        }else {
            return screenDefaultHeight - location[1];
        }
    }

    //remove the old player
    private void removeVideoView(PlayerView videoView){
        ViewGroup parent = ( ViewGroup) videoView.getParent();
        if (parent==null){
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index>=0){
            parent.removeViewAt(index);
            isVideoViewAdded= false;
            viewHolderParent.setOnClickListener(null);
        }
    }

    public void addVideoView(){
        frameLayout.addView(videoSurfaceView);
        isVideoViewAdded= true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        thumbnail.setVisibility(GONE);

    }

    private  void resetVideoView(){
        if (isVideoViewAdded){
            removeVideoView(videoSurfaceView);
            playPosition=-1;
            videoSurfaceView.setVisibility(INVISIBLE);
            thumbnail.setVisibility(VISIBLE);
        }
    }
    public  void releasePlayer(){
        if (videoPlayer!=null){
            videoPlayer.release();
            videoPlayer=null;
        }
        viewHolderParent = null;
    }
    private void toggleVolume(){
        if (videoPlayer !=null){
            if (volumeState==volumeState.OFF){
                Log.d(TAG,"togglePlaybackState: enabling volume");
                setVolumeControl(VolumeState.ON);
            }else if (volumeState== VolumeState.ON){
                Log.d(TAG,"togglePlaybackState: disabling volume");
                setVolumeControl(VolumeState.OFF);
            }
        }
    }
    private void setVolumeControl(VolumeState state){
        volumeState = state;
        if (state==VolumeState.OFF){
            videoPlayer.setVolume(0f);
            animateVolumeControl();
        } else if (state == VolumeState.ON) {
            videoPlayer.setVolume(1f);
            animateVolumeControl();

        }
    }
    private  void animateVolumeControl(){
        if (volumeControl !=null){
            volumeControl.bringToFront();
            if (volumeState ==VolumeState.OFF){
                requestManager.load(R.drawable.ic_volume_off).into(volumeControl);
            } else if (volumeState ==VolumeState.ON) {
                requestManager.load(R.drawable.ic_volume_on).into(volumeControl);
            }
            volumeControl.animate().cancel();
            volumeControl.setAlpha(1f);
            volumeControl.animate().alpha(0f)
                    .setDuration(600).setStartDelay(10000);

        }
    }
    public void setMediaObjects(ArrayList<MediaObject> mediaObjects ){
        this.mediaObjects = mediaObjects;
    }
}
