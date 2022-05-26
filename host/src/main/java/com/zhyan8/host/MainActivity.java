package com.zhyan8.host;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {
    Button btn_inner;
    Button btn_outer;
    Button btn_download;
    TextView tv_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_inner = findViewById(R.id.btn_inner);
        btn_outer = findViewById(R.id.btn_outer);
        btn_download = findViewById(R.id.btn_download);
        btn_inner.setOnClickListener(listener);
        btn_outer.setOnClickListener(listener);
        btn_download.setOnClickListener(listener);
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
                    download();
                    break;
                case R.id.btn_outer:
                     RePlugin.startActivity(MainActivity.this,
                             RePlugin.createIntent("com.zhyan8.plugin2", "com.zhyan8.plugin2.MainActivity"));
                    break;
            }
        }
    };

    private void download() {
        new Thread() {
            @Override
            public void run() {
                String urlPath = "https://raw.githubusercontent.com/ZhangZeQiao/ImagePluginDemo/7c5866db83b57c455302fac12ea72af30d9a5364/app/src/main/assets/image.apk";
                DownLoadUtils.download(urlPath);
            }
        }.start();
    }
}
