package com.elegion.tracktor.ui.results;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.R;
import com.elegion.tracktor.common.SingleFragmentActivity;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.ShareTrackInfoEvent;
import com.elegion.tracktor.event.ShowCommentDialogEvent;
import com.elegion.tracktor.event.UpdateTrackEvent;
import com.elegion.tracktor.util.ScreenshotMaker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ResultsActivity extends SingleFragmentActivity {
    public static final String RESULT_KEY = "RESULT_KEY";
    public static final long LIST_ID = -1L;

    public static void start(@NonNull Context context, long resultId) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(RESULT_KEY, resultId);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        long resultId = getIntent().getLongExtra(RESULT_KEY, LIST_ID);
        if (resultId != LIST_ID)
            return ResultsDetailsFragment.newInstance(resultId);
        else
            return ResultsFragment.newInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowCommentDialog(ShowCommentDialogEvent event) {
        Track track = event.getTrack();
        AlertDialog.Builder commentDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.comment_title);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dilaog_add_comment, null);
        EditText comment = view.findViewById(R.id.et_comment);
        comment.setText(track.getComment());
        commentDialog.setView(view);
        commentDialog.setPositiveButton(R.string.comment_button_ok, ((dialog, which) -> {
                    track.setComment(comment.getText().toString());
                    EventBus.getDefault().post(new UpdateTrackEvent(track));
                })
        );
        commentDialog.setNegativeButton(R.string.comment_button_cancel, (dialog, which) -> dialog.cancel());
        commentDialog.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareTrackInfo(ShareTrackInfoEvent event) {
        Track track = event.getTrack();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    ScreenshotMaker.fromBase64(track.getImageBase64()),
                    "Мой маршрут",
                    null);
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, track.getTrackInfo());
            startActivity(Intent.createChooser(intent, "Результаты маршрута"));
        } else {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
        }
    }
}
