package com.utouu.easyjoke;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by 黄思程 on 2016/12/16   10:36
 * Function：
 * Desc：
 */
public class RecommendFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 110;
    @BindView(R.id.tv_indicate) TextView tvIndicate;

    @BindView(R.id.ban_camera) Button banCamera;
    @BindView(R.id.ban_write) Button banWrite;
    private IServiceManager iServiceManager;

    private Context context;

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
                        Logger.e(videoJsonStr.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

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
