package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Video;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.DeletevideoitemBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import java.util.List;

public class DeleteVideoAdapter extends RecyclerView.Adapter<DeleteVideoAdapter.ViewHolder> {

    Context context;
    List<Video> stringArrayList;
    Uri vid_url;
    private DefaultTrackSelector trackSelector;
    ExoPlayer simpleExoPlayer;
    int flag=0;

    public DeleteVideoAdapter(Context context, List<Video> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DeletevideoitemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.deletevideoitem, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        holder.binding.relativeLayout.setAlpha(0.50f);
        holder.binding.imgSelect.setVisibility(View.VISIBLE);

        if (stringArrayList.get(position).getUri() == null) {
            vid_url = Uri.parse(stringArrayList.get(position).getVideo());
        } else {
            vid_url = stringArrayList.get(position).getUri();
        }
        trackSelector = new DefaultTrackSelector(context);
        simpleExoPlayer = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
        //playerView = findViewById(R.id.exoPlayerView);
        simpleExoPlayer.addListener(new PlayerEventListener());
        holder.binding.videoView.setPlayer(simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(vid_url);

        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();

//        holder.binding.videoView.setPlayer(simpleExoPlayer);

        holder.binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0){
                    flag = 1;
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_selected_images))
                            .error(R.drawable.ic_group_1755)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.binding.imgSelect);

                    stringArrayList.get(position).setSelected(true);
                }else {

                    flag = 0;
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.ic_non_select))
                            .error(R.drawable.ic_group_1755)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.binding.imgSelect);
                    stringArrayList.get(position).setSelected(false);

                }

            }
        });
        /*holder.binding.imgSelect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.relativeLayout.setAlpha(0.50f);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_non_select))
                        .error(R.drawable.ic_group_1755)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.binding.imgSelect);
                stringArrayList.get(position).setSelected(false);
                return true;
            }
        });*/

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

    @Override
    public int getItemCount() {
        return(stringArrayList != null) ? stringArrayList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        DeletevideoitemBinding binding;

        public ViewHolder(@NonNull DeletevideoitemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}