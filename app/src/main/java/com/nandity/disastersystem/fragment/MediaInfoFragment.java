package com.nandity.disastersystem.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.adapter.PictureAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.AudioPathBean;
import com.nandity.disastersystem.database.AudioPathBeanDao;
import com.nandity.disastersystem.database.PicturePathBean;
import com.nandity.disastersystem.database.PicturePathBeanDao;
import com.nandity.disastersystem.database.VideoPathBean;
import com.nandity.disastersystem.database.VideoPathBeanDao;
import com.nandity.disastersystem.utils.MyUtils;
import com.nandity.disastersystem.utils.ToastUtils;
import com.nandity.disastersystem.utils.UriToPath;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 反馈信息填报---媒体信息页面
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
    public static final int TAKE_FOLDER = 4;
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
    @BindView(R.id.btn_baseinfo_folder_upload)
    Button btnBaseinfoFolderUpload;
    @BindView(R.id.tv_mediainfo_folder)
    TextView tvMediainfoFolder;
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
    private SharedPreferences sp;
    private String sessionId;
    private int number;
    private ProgressDialog uploadProgress;
    private String folderPath;
    private MediaRecorder recorder;
    private MaterialDialog recorderDialog;
    private TextView tv;
    private String audioPath;
    private Button btnStart;
    private Button btnStop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mediainfo, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
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
        uploadProgress = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        uploadProgress.setMessage("正在上传");
        uploadProgress.setCancelable(false);
        uploadProgress.setCanceledOnTouchOutside(false);
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
        ivMediainfoFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeFolder();
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

                                    if (videoPathBeanDao.queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique() != null) {
                                        videoPathBeanDao.delete(videoPathBean);
                                    }
                                    File file = new File(videoPathBean.getPath());
                                    if (file.isFile() && file.exists()) {
                                        file.delete();
                                    }
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
                                    if (audioPathBeanDao.queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique() != null) {
                                        audioPathBeanDao.delete(audioPathBean);

                                    }
                                    File file = new File(audioPathBean.getPath());
                                    if (file.exists() && file.isFile()) {
                                        file.delete();
                                    }
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
        btnBaseinfoPhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<File> picFiles = new ArrayList<File>();
                List<PicturePathBean> list = picturePathBeanDao.queryBuilder().where(PicturePathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).list();
                if (list != null && list.size() > 0) {
                    for (PicturePathBean pathBean : list) {
                        File file = new File(pathBean.getPath());
                        if (file.isFile() && file.exists()) {
                            picFiles.add(file);
                        }
                    }
                    uploadPhoto(picFiles);
                } else {
                    ToastUtils.showShortToast("请先保存后再上传！");
                }
            }
        });
        btnBaseinfoVideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPathBean unique = videoPathBeanDao.queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
                if (unique != null) {
                    File file = new File(unique.getPath());
                    if (file.isFile() && file.exists()) {

                        uploadData(file, "2");
                    } else {
                        ToastUtils.showShortToast("视频文件不存在！");
                    }
                } else {
                    ToastUtils.showShortToast("请先保存后再上传！");
                }
            }
        });
        btnBaseinfoAudioUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPathBean unique = audioPathBeanDao.queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
                if (unique != null) {
                    File file = new File(unique.getPath());
                    if (file.isFile() && file.exists()) {
                        uploadData(file, "3");
                    } else {
                        ToastUtils.showShortToast("音频文件不存在！");
                    }
                } else {
                    ToastUtils.showShortToast("请先保存后再上传！");
                }
            }
        });
        btnBaseinfoFolderUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(folderPath)) {
                    File file = new File(folderPath);
                    if (file.isFile() && file.exists()) {
                        uploadData(file, "4");
                    } else {
                        ToastUtils.showShortToast("文件不存在！");
                    }
                } else {
                    ToastUtils.showShortToast("请先选择文件！");
                }
            }
        });
    }

    private void uploadPhoto(final List<File> files) {
        OkHttpUtils.get().url(new ConnectUrl().getTaskStatusUrl())
                .addParams("taskId", taskInfoBean.getmTaskId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            if ("300".equals(status)) {
                                uploadPicture(files);
                            } else if ("200".equals(status)) {
                                ToastUtils.showShortToast("该任务已完成无需上传信息！");
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void uploadData(final File file, final String type) {
        OkHttpUtils.get().url(new ConnectUrl().getTaskStatusUrl())
                .addParams("taskId", taskInfoBean.getmTaskId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            if ("300".equals(status)) {
                                if ("2".equals(type)) {
                                    upload(file, getFileName() + ".mp4", "2");
                                } else if ("3".equals(type)) {
                                    upload(file, getFileName() + ".wav", "3");
                                } else {
                                    upload(file, file.getName(), "4");
                                }

                            } else if ("200".equals(status)) {
                                ToastUtils.showShortToast("该任务已完成无需上传信息！");
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void upload(final File file, String name, final String type) {
        uploadProgress.show();
        OkHttpUtils.post().url(new ConnectUrl().getMediaUploadUrl())
                .addParams("sessionId", sessionId)
                .addParams("type", type)
                .addParams("taskId", taskInfoBean.getmTaskId())
                .addFile("files", name, file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        uploadProgress.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                                deleteFile(file, null);
                                deleteDao(type);
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(context, LoginActivity.class));
                                getActivity().finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void deleteDao(String type) {
        if ("1".equals(type)) {
            List<PicturePathBean> list = picturePathBeanDao.queryBuilder().where(PicturePathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).list();
            for (PicturePathBean pathBean : list) {
                picturePathBeanDao.delete(pathBean);
            }
            bitmapList.clear();
            picAdapter.notifyDataSetChanged();
        } else if ("2".equals(type)) {
            VideoPathBean unique = videoPathBeanDao.queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
            videoPathBeanDao.delete(unique);
            vvMediainfoVideo.setVisibility(View.GONE);
        } else if ("3".equals(type)) {
            AudioPathBean unique = audioPathBeanDao.queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
            audioPathBeanDao.delete(unique);
            tvMediainfoAudio.setText("");
        } else {
            tvMediainfoFolder.setText("");
        }


    }


    private void deleteFile(final File file, final List<File> fileList) {
        new MaterialDialog.Builder(context)
                .title("删除文件")
                .content("是否删除已上传文件?")
                .positiveColor(Color.RED)
                .positiveText("删除")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (file != null) {
                            file.delete();
                        }
                        if (fileList != null) {
                            for (File file1 : fileList) {
                                file1.delete();
                            }
                        }

                    }
                }).show();

    }

    private void uploadPicture(final List<File> picFiles) {
        uploadProgress.show();
        OkHttpUtils.post().url(new ConnectUrl().getMediaUploadUrl())
                .addParams("sessionId", sessionId)
                .addParams("type", "1")
                .addParams("taskId", taskInfoBean.getmTaskId())
                .addFile("files", getFileName() + ".jpg", picFiles.get(number))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        uploadProgress.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                if (number == picFiles.size() - 1) {
                                    uploadProgress.dismiss();
                                    ToastUtils.showShortToast(msg);
                                    deleteFile(null, picFiles);
                                    deleteDao("1");
                                } else if (number < picFiles.size() - 1) {
                                    number++;
                                    uploadPicture(picFiles);
                                }

                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(context, LoginActivity.class));
                                getActivity().finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String getFileName() {

        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            Log.d(TAG, "文件夹是否创建成功：" + mkdir);
        }
        pictureFile = new File(dir, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg");
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(getActivity(), "com.nandity.disastersystem", pictureFile);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(pictureFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.d(TAG, "图片保存路径：" + pictureFile.getAbsolutePath());
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void takeVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File dir = new File(getSdPath("video"));
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            Log.d(TAG, "文件夹是否创建成功：" + mkdir);
        }
        videoFile = new File(dir, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".MP4");
        Uri videoUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            videoUri = FileProvider.getUriForFile(getActivity(), "com.nandity.disastersystem", videoFile);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            videoUri = Uri.fromFile(videoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        startActivityForResult(intent, TAKE_VIDEO);
    }

    private void takeAudio() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_recoder, null);
        btnStart = (Button) view.findViewById(R.id.btn_start_recode);
        btnStop = (Button) view.findViewById(R.id.btn_stop_recode);
        btnStop.setEnabled(false);
        tv = (TextView) view.findViewById(R.id.tv_time);
        RecorderOnclickListener listener = new RecorderOnclickListener();
        btnStart.setOnClickListener(listener);
        btnStop.setOnClickListener(listener);
        recorderDialog = new MaterialDialog.Builder(getActivity())
                .title("录音")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .customView(view, false)
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (TextUtils.isEmpty(audioPath)) {
                            return;
                        }
                        File file2 = new File(audioPath);
                        if (file2.isFile() && file2.exists()) {
                            file2.delete();
                        }
                        if (recorder != null) {
                            recorder.stop();
                            recorder.release();
                            recorder = null;
                        }
                    }
                })
                .show();
    }

    private class RecorderOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start_recode:
                    File file = new File(getSdPath("audio"));
                    if (!file.exists()) file.mkdirs();
                    audioPath = getSdPath("audio") + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".wav";
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setOutputFile(audioPath);
                    //设置编码格式
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        ToastUtils.showShortToast("录音机使用失败！");
                    }
                    recorder.start();
                    tv.setVisibility(View.VISIBLE);
                    btnStop.setEnabled(true);
                    btnStart.setEnabled(false);
                    break;
                case R.id.btn_stop_recode:
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    audioFilePath = audioPath;
                    tvMediainfoAudio.setText(audioFilePath);
                    audioPathBean.setTaskId(taskInfoBean.getmTaskId());
                    audioPathBean.setPath(audioFilePath);
                    recorderDialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recorder = null;
    }

    private void takeFolder() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, TAKE_FOLDER);
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
                case TAKE_FOLDER:
                    showFolder(data);
                    break;
            }
        }
    }

    private void showFolder(Intent data) {
        Uri uri = data.getData();
        folderPath = UriToPath.getPath(context, uri);
        Log.d(TAG, "选择文件的路径：" + folderPath);
        tvMediainfoFolder.setText(folderPath);
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
