package com.zpan.okhttp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_get).setOnClickListener(view -> {
            getRequest();
        });
        findViewById(R.id.btn_post).setOnClickListener(view -> {
            postRequest();
        });
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Log.e("222222222222", message.obj.toString());
            return false;
        }
    });

    private void getRequest() {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://wanandroid.com/wxarticle/chapters/json";

        Request request = new Request.Builder().url(url).get().build();

        Call call = httpClient.newCall(request);

        new Thread(() -> {
            try {
                Response response = call.execute();
                Log.e("1111111111111111", response.body().toString());

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = response.body().toString();
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void postRequest() {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://www.wanandroid.com/user/login";

        //MediaType contentType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
            .add("username", "zpan")
            .add("password", "123456")
            .build();

        Request request = new Request.Builder().url(url).post(body).build();

        Call call = httpClient.newCall(request);

        new Thread(() -> {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("4444444", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.e("333333333", response.body().toString());

                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = response.body().toString();
                    mHandler.sendMessage(msg);
                }
            });
        }).start();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtra("sss", bundle);

    }
}