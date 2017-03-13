package com.nandity.disastersystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.FillInfoActivity;
import com.nandity.disastersystem.adapter.PictureAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.database.AudioPathBean;
import com.nandity.disastersystem.database.AudioPathBeanDao;
import com.nandity.disastersystem.database.PicturePathBean;
import com.nandity.disastersystem.database.PicturePathBeanDao;
import com.nandity.disastersystem.database.VideoPathBean;
import com.nandity.disastersystem.database.VideoPathBeanDao;
import com.nandity.disastersystem.utils.MyUtils;
import com.nandity.disastersystem.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ChenPeng on 2017/3/6.
 */

public class MediaInfoFragment extends Fragment {
    @BindView(R.id.iv_mediainfo_photo)
    ImageView ivMediainfoPhoto;
    @BindView(R.id.iv_mediainfo_video)
    ImageView ivMediainfoVideo;
    @BindView(R.id.iv_mediainfo_audio)
    ImageView ivMediainfoAudio;
    @BindView(R.id.iv_mediainfo_folder)
    ImageView ivMediainfoFolder;
    @BindView(R.id.btn_mediainfo_save)
    Button btnMediainfoSave;
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_VIDEO = 2;
    public static final int TAKE_AUDIO = 3;
    @BindView(R.id.rv_mediainfo_photo)
    RecyclerView rvMediainfoPhoto;
    @BindView(R.id.btn_baseinfo_photo_upload)
    Button btnBaseinfoPhotoUpload;
    @BindView(R.id.btn_baseinfo_video_upload)
    Button btnBaseinfoVideoUpload;
    @BindView(R.id.btn_baseinfo_audio_upload)
    Button btnBaseinfoAudioUpload;
    @BindView(R.id.vv_mediainfo_video)
    VideoView vvMediainfoVideo;
    @BindView(R.id.tv_mediainfo_audio)
    TextView tvMediainfoAudio;
    @BindView(R.id.btn_baseinfo_video_delete)
    Button btnBaseinfoVideoDelete;
    @BindView(R.id.btn_baseinfo_audio_delete)
    Button btnBaseinfoAudioDelete;
    private Context context;
    private TaskInfoBean taskInfoBean;
    private File pictureFile;
    private File videoFile;
    private String audioFilePath;
    private PictureAdapter picAdapter;
    private List<PicturePathBean> picturePathList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<PicturePathBean> removeList = new ArrayList<>();
    private final String TAG = "MediaInfoFragment";
    private PicturePathBeanDao picturePathBeanDao;
    private VideoPathBeanDao videoPathBeanDao;
    private AudioPathBeanDao audioPathBeanDao;
    private VideoPathBean videoPathBean;
    private AudioPathBean audioPathBean;
    private MediaPlayer player;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_info, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        taskInfoBean = ((FillInfoActivity) getActivity()).taskInfoBean;
        picturePathBeanDao = MyApplication.getDaoSession().getPicturePathBeanDao();
        videoPathBeanDao = MyApplication.getDaoSession().getVideoPathBeanDao();
        audioPathBeanDao = MyApplication.getDaoSession().getAudioPathBeanDao();
        initView();
        setAdapter();
        setListener();
        Log.d(TAG, "执行了onCreateView()方法");
        return view;
    }


    private void initView() {
        audioPathBean = new AudioPathBean();
        videoPathBean = new VideoPathBean();
        bitmapList.clear();
        List<PicturePathBean> list = picturePathBeanDao.queryBuilder().where(PicturePathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).list();
        if (list != null && list.size() > 0) {
            for (PicturePathBean pathBean : list) {
                bitmapList.add(MyUtils.getSmallBitmap(pathBean.getPath(), 50, 100));
            }
            picturePathList = list;
        }
        VideoPathBean unique = videoPathBeanDao.queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (unique != null) {
            this.videoPathBean = unique;
            vvMediainfoVideo.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(getActivity());
            vvMediainfoVideo.setMediaController(mediaController);
            mediaController.setMediaPlayer(vvMediainfoVideo);
            vvMediainfoVideo.setVideoPath(unique.getPath());
            vvMediainfoVideo.requestFocus();
        }
        AudioPathBean audioPathBean = audioPathBeanDao.queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (audioPathBean != null) {
            this.audioPathBean = audioPathBean;
            tvMediainfoAudio.setText(audioPathBean.getPath());
        }
    }

    private void setAdapter() {
        picAdapter = new PictureAdapter(context, bitmapList);
        rvMediainfoPhoto.setLayoutManager(new GridLayoutManager(context, 3));
        rvMediainfoPhoto.setAdapter(picAdapter);
    }

    private void setListener() {
        ivMediainfoPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        ivMediainfoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });
        ivMediainfoAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAudio();
            }
        });
        btnMediainfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto();
                saveVideo();
                saveAudio();
            }
        });
        picAdapter.setOnItemViewClickListener(new PictureAdapter.OnItemViewClickListener() {
            @Override
            public void onPictureClick(int position) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_photo, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_dialog_picture);
                imageView.setImageBitmap(MyUtils.getSmallBitmap(picturePathList.get(position).getPath(), 600, 800));
                new MaterialDialog.Builder(getActivity())
                        .cancelable(true)
                        .customView(view, false)
                        .show();
            }

            @Override
            public void onDeleteClick(int position) {
                removeList.add(picturePathList.remove(position));
                bitmapList.remove(position);
                picAdapter.notifyDataSetChanged();
            }
        });
        btnBaseinfoVideoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vvMediainfoVideo.getVisibility() == View.VISIBLE) {
                    new MaterialDialog.Builder(getActivity())
                            .title("删除视频")
                            .content("确定要删除视频吗？")
                            .positiveText("确定")
                            .positiveColor(Color.RED)
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    vvMediainfoVideo.setVisibility(View.GONE);
                                    videoPathBeanDao.deleteAll();
                                    File file = new File(videoPathBean.getPath());
                                    if (file.isFile() && file.exists()) {
                                        file.delete();
                                    }
                                    videoPathBean = null;
                                }
                            }).show();
                }
            }
        });
        btnBaseinfoAudioDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tvMediainfoAudio.getText().toString())) {
                    new MaterialDialog.Builder(getActivity())
                            .title("删除录音")
                            .content("确定要删除录音吗？")
                            .positiveText("确定")
                            .positiveColor(Color.RED)
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    tvMediainfoAudio.setText("");
                                    audioPathBeanDao.deleteAll();
                                    File file = new File(audioPathBean.getPath());
                                    if (file.exists() && file.isFile()) {
                                        file.delete();
                                    }
                                    audioPathBean = null;
                                }
                            }).show();
                }
            }
        });
        tvMediainfoAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player = new MediaPlayer();
                try {
                    player.setDataSource(audioPathBean.getPath());
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                View view = LayoutInflater.from(context).inflate(R.layout.diaolog_play_audio, null);
                Button btnStart = (Button) view.findViewById(R.id.btn_dialog_play);
                Button btnPause = (Button) view.findViewById(R.id.btn_dialog_pause);
                Button btnStop = (Button) view.findViewById(R.id.btn_dialog_stop);
                MyOnClickListener clickListener = new MyOnClickListener();
                btnStart.setOnClickListener(clickListener);
                btnPause.setOnClickListener(clickListener);
                btnStop.setOnClickListener(clickListener);
                new MaterialDialog.Builder(getActivity())
                        .title("播放录音")
                        .customView(view, false)
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .positiveText("关闭")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                player.stop();
                            }
                        }).show();

            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_dialog_play:
                    player.start();
                    break;
                case R.id.btn_dialog_pause:
                    if (player.isPlaying()) {
                        player.pause();
                    }
                    break;
                case R.id.btn_dialog_stop:
                    player.stop();
                    break;
            }
        }
    }

    private void saveVideo() {
        VideoPathBean unique = videoPathBeanDao.queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (unique != null) {
            unique.setPath(videoPathBean.getPath());
            videoPathBeanDao.update(unique);
        } else {
            if (videoPathBean != null) {
                videoPathBeanDao.insertOrReplace(videoPathBean);
            }
        }
    }

    private void savePhoto() {
        for (PicturePathBean pathBean : removeList) {
            PicturePathBean unique = picturePathBeanDao.queryBuilder().where(PicturePathBeanDao.Properties.Path.eq(pathBean.getPath())).unique();
            if (unique != null) {
                picturePathBeanDao.delete(unique);
                File file = new File(unique.getPath());
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }
        for (PicturePathBean pathBean : picturePathList) {
            picturePathBeanDao.insertOrReplace(pathBean);
        }
        ToastUtils.showShortToast("保存成功");
    }

    private void saveAudio() {
        AudioPathBean unique = audioPathBeanDao.queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (unique == null) {
            if (audioPathBean != null) {
                audioPathBeanDao.insertOrReplace(audioPathBean);
            }
        } else {
            unique.setPath(audioPathBean.getPath());
            audioPathBeanDao.update(unique);
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(getSdPath("picture"));
        if (!dir.exists()) dir.mkdir();
        pictureFile = new File(dir, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        Log.d(TAG, "图片保存路径：" + pictureFile.getAbsolutePath());
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void takeVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File dir = new File(getSdPath("video"));
        if (!dir.exists()) dir.mkdir();
        videoFile = new File(dir, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".MP4");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        startActivityForResult(intent, TAKE_VIDEO);
    }

    private void takeAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, TAKE_AUDIO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FillInfoActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    showPhoto();
                    break;
                case TAKE_VIDEO:
                    showVideo();
                    break;
                case TAKE_AUDIO:
                    playAudio(data);
                    break;
            }
        }
    }

    private void playAudio(Intent data) {
        Uri uri = data.getData();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        audioFilePath = cursor.getString(index);
        tvMediainfoAudio.setText(audioFilePath);
        Log.d(TAG, "录音保存路径:" + audioFilePath);
        audioPathBean.setTaskId(taskInfoBean.getmTaskId());
        audioPathBean.setPath(audioFilePath);
    }

    private void showVideo() {
        videoPathBean.setTaskId(taskInfoBean.getmTaskId());
        videoPathBean.setPath(videoFile.getAbsolutePath());
        Log.d(TAG, "视频保存路径：" + videoFile.getAbsolutePath());
        vvMediainfoVideo.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(getActivity());
        vvMediainfoVideo.setMediaController(mediaController);
        mediaController.setMediaPlayer(vvMediainfoVideo);
        vvMediainfoVideo.setVideoPath(videoFile.getAbsolutePath());
        vvMediainfoVideo.requestFocus();
    }

    private void showPhoto() {
        Bitmap bitmap = MyUtils.getSmallBitmap(pictureFile.getAbsolutePath(), 50, 100);
        bitmapList.add(bitmap);
        picAdapter.notifyDataSetChanged();
        PicturePathBean pathBean = new PicturePathBean();
        pathBean.setTaskId(taskInfoBean.getmTaskId());
        pathBean.setPath(pictureFile.getAbsolutePath());
        picturePathList.add(pathBean);
        Log.d(TAG, "图片路径集合数据：" + picturePathList.toString());
    }


    private String getSdPath(String folder) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/disaster/" + folder;
        }
        return "";
    }
}
