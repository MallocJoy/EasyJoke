package com.utouu.easyjoke.module.main.presenter;

import com.utouu.easyjoke.base.BasePresenter;
import com.utouu.easyjoke.data.retrofit.RetrofitHelper;
import com.utouu.easyjoke.data.service.IMainPage;
import com.utouu.easyjoke.entity.BaseEntity;
import com.utouu.easyjoke.entity.main.MainEntity;
import com.utouu.easyjoke.module.main.view.IMainView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by 黄思程 on 2017/3/20   17:29
 * Function：
 * Desc：
 */
public class MainPresenter extends BasePresenter<IMainView> {
    /**
     * 获取主页面的数据
     */
    public void requestMainPage() {
        IMainView view = getView();

        RetrofitHelper.create(IMainPage.class)
                .GetMainPage()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<MainEntity>>>() {
                    @Override
                    public void onCompleted() {
                        if (view != null) {
                            view.onComplete("获取数据完成");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.onFailed("获取数据失败");
                        }
                    }

                    @Override
                    public void onNext(BaseEntity<List<MainEntity>> entity) {
                        if (view != null) {
                            view.onGetMainPageSuccess(entity);
                        }
                    }
                });
    }
}
