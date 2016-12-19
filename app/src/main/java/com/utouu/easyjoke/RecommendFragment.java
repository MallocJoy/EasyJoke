package com.utouu.easyjoke;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by 黄思程 on 2016/12/16   10:36
 * Function：
 * Desc：
 */
public class RecommendFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 110;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    @BindView(R.id.tv_indicate) TextView tvIndicate;

    @BindView(R.id.ban_camera) Button banCamera;
    @BindView(R.id.ban_write) Button banWrite;
    private IServiceManager iServiceManager;

    private Context context;
    private String netUrl = " http://lol.data.shiwan.com/score";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_recommend, container, false);
        context = getContext();

        loadData();

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void loadData() {


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://ic.snssdk.com/neihan/")
                .build();

        iServiceManager = retrofit.create(IServiceManager.class);
        iServiceManager.getVideoJsonStr(-104)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(videoJsonStr -> {
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoJsonStr -> {
                    //json数据处理
                    try {
                        Logger.d(videoJsonStr.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        iServiceManager.login("Vince","vince")
                .flatMap(new Func1<String, Observable<User>>() {
                    @Override
                    public Observable<User> call(String s) {
                        return iServiceManager.getUser(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(user -> {/*在这里保存用户信息*/})
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {

                    }
                });


        //OkHttp的GET使用
        OkHttpClient  client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(netUrl)
                .get()
                .build();
        Call  call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.e("请求成功:  OkHttp-Get---> "+response.body().string());
            }
        });

        //OkHttp的POST使用
        RequestBody postBody = new FormBody.Builder()
//                .add()
//                .add()   可以添加参数
                .build();
        Request postRequest = new Request.Builder()
                .url(netUrl)
                .post(postBody)
                .build();
        client.newCall(postRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.e("请求成功:  OkHttp-Post---> "+response.body().string());
            }
        });

        //OkHttp文件上传
        File  file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/MUSIC/test.mp3");
        Request fileRequest = new Request.Builder()
                .url(netUrl)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,file))
                .build();
        client.newCall(fileRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("请求失败  OkHttp-File");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.e("请求成功:  OkHttp-File---> "+response.body().string());
            }
        });


        //OkHttp文件下载
        Request downRequest = new Request.Builder()
                .url(netUrl)
                .build();
        client.newCall(downRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });


        //异步上传Multipart文件
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title","wangshu")
                .addFormDataPart("image","wangshu.jpg",RequestBody.create(MEDIA_TYPE_PNG,new File("/sdcard/wangshu.jpg")))
                .build();

        Request  multRequest = new Request.Builder()
                .header("Authorization","Client-ID")
                .url(netUrl)
                .post(requestBody)
                .build();

        client.newCall(multRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });


        //设置超时时间和缓存
        int  cacheSize = 10*1024*1024;
        OkHttpClient  httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .cache(new Cache(Environment.getExternalStorageDirectory().getAbsoluteFile(),cacheSize))
                .build();



    }

    @OnClick({R.id.ban_camera, R.id.ban_write})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ban_camera:
                btnForCameraPermission();
                break;
            case R.id.ban_write:
                btnForWritePermission();
                break;
        }
    }

    private void btnForWritePermission() {
        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,Manifest.permission_group.CALENDAR}
                    ,CAMERA_PERMISSION_CODE);
        }
    }

    private void btnForCameraPermission() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            int  grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Logger.e(granted?"你获取了权限":"你没有获取权限");
        }
    }

}
