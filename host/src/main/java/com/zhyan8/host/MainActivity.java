package com.zhyan8.host;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.zhyan8.host.utils.DownLoadUtils;
import com.zhyan8.host.utils.FileUtils;
import com.zhyan8.host.utils.PermissionUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String mStorageDir;
    private Button btn_inner;
    private Button btn_download;
    private TextView tv_progress;
    private Button btn_install;
    private Button btn_outer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_inner = findViewById(R.id.btn_inner);
        btn_download = findViewById(R.id.btn_download);
        tv_progress = findViewById(R.id.tv_progress);
        btn_install = findViewById(R.id.btn_install);
        btn_outer = findViewById(R.id.btn_outer);
        btn_inner.setOnClickListener(listener);
        btn_download.setOnClickListener(listener);
        btn_install.setOnClickListener(listener);
        btn_outer.setOnClickListener(listener);
        PermissionUtils.verifyStoragePermissions(this);
        mStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_inner:
                    RePlugin.startActivity(MainActivity.this,
                            RePlugin.createIntent("com.zhyan8.plugin1", "com.zhyan8.plugin1.MainActivity"));
                    break;
                case R.id.btn_download:
                    String urlPath = "https://github.com/2hyan8/RepluginDemo/raw/main/plugin2/src/main/res/raw/plugin2.apk";
                    download(urlPath);
                    break;
                case R.id.btn_install:
                    String apkName = "plugin2.apk";
                    install(apkName);
                    break;
                case R.id.btn_outer:
                     RePlugin.startActivity(MainActivity.this,
                             RePlugin.createIntent("com.zhyan8.plugin2", "com.zhyan8.plugin2.MainActivity"));
                    break;
            }
        }
    };

    private void download(final String urlPath) {
        new Thread() {
            @Override
            public void run() {
                DownLoadUtils.download(urlPath, mHandler);
            }
        }.start();
    }

    private void install(String apkName) {
        String sourceFilePath = mStorageDir + File.separatorChar + apkName;
        String targetFilePath = mStorageDir + File.separatorChar + "temp" + File.separatorChar + apkName;
        FileUtils.copyFile(sourceFilePath, targetFilePath);
        PluginInfo pi = RePlugin.install(targetFilePath);
        if (pi != null) {
            RePlugin.preload(pi);
            Toast.makeText(this, "安装成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "安装失败", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String progress = (String) msg.obj;
            tv_progress.setText(progress);
        }
    };
}
