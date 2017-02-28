package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.view.AnimationButton;

/**
 * 登录界面
 * Created by baohongyan on 2017/2/23.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText et_name;
    private EditText et_pwd;
    private AnimationButton mBtnLogin;
    private TextView tv_forgetpwd;
    private TextView tv_settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        et_name = (EditText)findViewById(R.id.et_name);
        et_pwd = (EditText)findViewById(R.id.et_pwd);
        mBtnLogin = (AnimationButton)findViewById(R.id.btn_login);
        tv_forgetpwd = (TextView)findViewById(R.id.forgetpwd);
        tv_settings = (TextView)findViewById(R.id.settings);

        mBtnLogin.setText("登录");
        mBtnLogin.setMode(AnimationButton.Mode.Hand_Finish);
        mBtnLogin.setOnAnimationButtonClickListener(new AnimationButton.OnAnimationButtonClickListener() {
            @Override
            public void onClick() {
                mBtnLogin.stopProgress(); //stopProgress方法仅在button.setMode(AnimationButton.Mode.Hand_Finish)之后才有效。
                if (mBtnLogin.isProgressStop()){
                    //跳转主页面
                    //TODO
                }
            }
        });

        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    //登录
    public void login(){
        if (!validate()){
            mBtnLogin.setEnabled(true);
            return;
        }


    }

    //检查输入是否为空
    private boolean validate(){
        boolean valid = true;

        String name = et_name.getText().toString();
        String pwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(name)){
            et_name.setError("账号不能为空");
            valid = false;
        } else {
            et_name.setError(null);
        }
        if (TextUtils.isEmpty(pwd)){
            et_pwd.setError("密码不能为空");
            valid = false;
        } else {
            et_pwd.setError(null);
        }

        return valid;
    }

}
