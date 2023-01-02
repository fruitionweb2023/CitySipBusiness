//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Solutions Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.direct2web.citysip.Utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.coremedia.iso.boxes.Container;
import com.direct2web.citysip.R;
import com.direct2web.citysip.customVideoViews.OnVideoTrimListener;
import com.googlecode.mp4parser.FileDataSourceViaHeapImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Utility {
    public static final String VIDEO_FORMAT = ".mp4";
    private static final String TAG = Utility.class.getSimpleName();

    public static void startTrim(Activity activity, @NonNull File src, @NonNull String dst, long startMs, long endMs,
                                 @NonNull OnVideoTrimListener callback) throws IOException {
        File file1 = create(activity, dst);
        if (file1 != null)
            generateVideo(src, file1, startMs, endMs, callback);
    }

    private static void generateVideo(@NonNull File src, @NonNull File dst, long startMs,
                                      long endMs, @NonNull OnVideoTrimListener callback) throws IOException {


        // NOTE: Switched to using FileDataSourceViaHeapImpl since it does not use memory mapping (VM).
        // Otherwise we get OOM with large movie files.
        Movie movie = MovieCreator.build(new FileDataSourceViaHeapImpl(src.getAbsolutePath()));

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        // remove all tracks we will create new tracks from the old

        long startTime1 = startMs/1000;
        long endTime1 = endMs / 1000;

        boolean timeCorrected = false;

        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)

                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
//                startTime1 = correctTimeToSyncSample(track, startTime1, false);
//                endTime1 = correctTimeToSyncSample(track, endTime1, true);
                timeCorrected = true;
            }
        }

        for (Track track : tracks) {
            long currentSample = 0;
            long ednCurrentSample = 0;
            long currentTime = 0;
            long lastTime = -1;
            long startSample1 = -1;
            long endSample1 = -1;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
                long delta = track.getSampleDurations()[i];


                if (currentTime > lastTime && currentTime <= startTime1) {
                    // current sample is still before the new starttime
                    startSample1 = currentSample;
                }
                if (currentTime > lastTime && currentTime <= endTime1) {
                    // current sample is after the new start time and still before the new endtime
                    endSample1 = ednCurrentSample;
                }
                lastTime = currentTime;
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
                ednCurrentSample++;
            }
            movie.addTrack(new AppendTrack(new CroppedTrack(track, startSample1, endSample1)));
        }



        try {
            Container out = new DefaultMp4Builder().build(movie);
            FileOutputStream fos = new FileOutputStream(dst);
            FileChannel fc = fos.getChannel();
            out.writeContainer(fc);
            fc.close();
            fos.close();
            if (callback != null)
                callback.getResult(Uri.parse(dst.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static File create(Activity activity, String dst) {
        File file = new File(dst);
        file.mkdirs();

//        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = activity.getExternalFilesDir(file.getAbsolutePath());
        Log.e(TAG, "Generated file path " + file.getAbsolutePath() + " ----< 123123  file1 " + storageDir.exists());
        Log.e(TAG, "Generated file path " + Environment.DIRECTORY_PICTURES + " ----< 123123  file1 " + storageDir.exists());

        try {
            return File.createTempFile(
                    activity.getResources().getString(R.string.app_provider) + new Date().getTime(), /* prefix */
                    ".mp4", /* suffix */
                    storageDir /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}