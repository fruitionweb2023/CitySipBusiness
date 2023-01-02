package com.direct2web.citysip.Adapter.LawyerAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.LawyerModels.LawyerAddImage.ImageAndVedio.Video;
import com.direct2web.citysip.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class AddAndSetVedioListLawyerAdapter extends RecyclerView.Adapter<AddAndSetVedioListLawyerAdapter.ViewHolder> {

    Context context;
    List<Video> videoList;
    Uri vid_url;
    private DefaultTrackSelector trackSelector;
    SimpleExoPlayer simpleExoPlayer;
    OnItemClickListner addVedio;

    public AddAndSetVedioListLawyerAdapter(Context context, List<Video> videoList, OnItemClickListner addVedio) {
        this.context = context;
        this.videoList = videoList;
        this.addVedio = addVedio;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == R.layout.property_lawyer_add_image) {
            itemView = LayoutInflater.from(context).inflate(R.layout.property_lawyer_add_image,viewGroup,false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_video_rclr,viewGroup,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == videoList.size()) ? R.layout.property_lawyer_add_image : R.layout.raw_video_rclr;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (position == videoList.size()) {
            holder.btnAddVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addVedio.onAddButtonClick(position);
                }
            });

        } else {

            if (videoList.get(position).getUri() == null){
                vid_url = Uri.parse(videoList.get(position).getVideo());
            }else {
                vid_url = videoList.get(position).getUri();
            }
            trackSelector = new DefaultTrackSelector(context);
            simpleExoPlayer = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
            //playerView = findViewById(R.id.exoPlayerView);
            simpleExoPlayer.addListener(new PlayerEventListener());
            holder.videoView.setPlayer(simpleExoPlayer);

            MediaItem mediaItem = MediaItem.fromUri(vid_url);

            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();

        }

    }

    @Override
    public int getItemCount() {
        return videoList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView videoView;
        ImageView btnAddVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = (PlayerView) itemView.findViewById(R.id.videoView);
            btnAddVideo = (ImageView) itemView.findViewById(R.id.btn_addItem);
        }
    }

    private class PlayerEventListener implements Player.Listener {

        @Override
        public void onPlaybackStateChanged(int state) {
            switch (state) {


                case Player.STATE_READY:

                    break;
                case Player.STATE_ENDED:


                    break;
            }

        }

    }

    public interface OnItemClickListner{
        public void onAddButtonClick(int postion);

    }
}