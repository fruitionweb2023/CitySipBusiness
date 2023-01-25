package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Video;
import com.direct2web.citysip.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class SelectedVideoAdapter extends RecyclerView.Adapter<SelectedVideoAdapter.ViewHolder> {

    Context context;
    List<Video> stringArrayList;
    Uri vid_url;
    private DefaultTrackSelector trackSelector;
    SimpleExoPlayer simpleExoPlayer;
    OnItemClickListner addVedio;
    OnVideoItemClickListner playVedio;

    public SelectedVideoAdapter(Context context, List<Video> stringArrayList,OnItemClickListner addVedio,OnVideoItemClickListner playVedio) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.addVedio = addVedio;
        this.playVedio= playVedio;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == R.layout.property_add_image) {
            itemView = LayoutInflater.from(context).inflate(R.layout.property_add_image,viewGroup,false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_video_rclr,viewGroup,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == stringArrayList.size()) ? R.layout.property_add_image : R.layout.raw_video_rclr;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        /*Glide.with(context)
                .load(stringArrayList.get(position).getPhoto())
                .error(R.drawable.ic_group_1755)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.image);*/
        if (position == stringArrayList.size()) {
            holder.btnAddVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addVedio.onAddButtonClick(position);
                }
            });
        } else {

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playVedio.onPlayVideoButtonClick(position);
                    Log.e("VideoBtnClicked : ", "URL : " + stringArrayList.get(position).getVideo() );
                }

            });
//            vid_url = stringArrayList.get(position).getUri();
//
//            holder.videoView.setVideoURI(vid_url);
//
//            MediaController mediaController = new MediaController(context);
//            mediaController.setAnchorView(holder.videoView);
//
//            holder.videoView.setMediaController(mediaController);

            if (stringArrayList.get(position).getUri() == null){
                vid_url = Uri.parse(stringArrayList.get(position).getVideo());
            }else {
                vid_url = stringArrayList.get(position).getUri();
            }
            trackSelector = new DefaultTrackSelector(context);
            simpleExoPlayer = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
            //playerView = findViewById(R.id.exoPlayerView);
            simpleExoPlayer.addListener(new PlayerEventListener());
            holder.videoView.setPlayer(simpleExoPlayer);

            MediaItem mediaItem = MediaItem.fromUri(vid_url);

            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();


            //simpleExoPlayer.play();


            /*holder.farwordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 5000);

                }
            });
            holder.rewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    long num = simpleExoPlayer.getCurrentPosition() - 5000;
                    if (num < 0) {

                        simpleExoPlayer.seekTo(0);


                    } else {

                        simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 5000);

                    }


                }
            });

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    simpleExoPlayer.play();


                }
            });
            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    simpleExoPlayer.pause();

                }
            });*/

        }

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView videoView;
        //VideoView videoView;
        ImageView farwordBtn;
        ImageView rewBtn;

        ImageView play;
        ImageView pause;
        ImageView btnAddVideo;
        RelativeLayout btnCard;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = (PlayerView) itemView.findViewById(R.id.videoView);
            btnCard = (RelativeLayout) itemView.findViewById(R.id.rl_main);
//            farwordBtn = (ImageView) videoView.findViewById(R.id.fwd);
//            rewBtn = (ImageView) videoView.findViewById(R.id.rew);
//
//            play = (ImageView) videoView.findViewById(R.id.exo_play);
//            pause = (ImageView) videoView.findViewById(R.id.exo_pause);
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

    public interface OnVideoItemClickListner{
        public void onPlayVideoButtonClick(int postion);

    }
}