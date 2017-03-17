package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.fragment.MediaInfoFragment;
import com.nandity.disastersystem.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecoderActivity extends AppCompatActivity {
    @BindView(R.id.btn_start_recode)
    Button btnStartRecode;
    @BindView(R.id.btn_stop_recode)
    Button btnStopRecode;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private MediaRecorder recorder;
    private String audioPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recoder);
        ButterKnife.bind(this);
        File file=new File(getSdPath("audio"));
        if (!file.exists())file.mkdirs();
        audioPath=getSdPath("audio")+"/"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp3";
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioPath);
        //设置编码格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShortToast("录音机使用失败！");
        }
        setListener();
    }

    private void setListener() {
        btnStartRecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.start();
                tvTime.setVisibility(View.VISIBLE);
            }
        });
        btnStopRecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                recorder.release();
                Log.d("ssss",audioPath);
                Intent intent=new Intent();
                intent.putExtra("PATH",audioPath);
                setResult(MediaInfoFragment.TAKE_AUDIO,intent);
                finish();
            }
        });
    }
    private String getSdPath(String folder) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/disaster/" + folder;
        }
        return "";
    }
}
