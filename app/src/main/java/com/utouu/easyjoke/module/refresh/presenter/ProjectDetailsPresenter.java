package com.utouu.easyjoke.module.refresh.presenter;

import android.app.Activity;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.utils.GsonUtils;
import com.utouu.easyjoke.module.refresh.view.ProjectDetailsView;

import org.simple.eventbus.EventBus;

import java.util.HashMap;

import cn.utsoft.xunions.base.BasePresenter;
import cn.utsoft.xunions.data.http.XunionsHttpUtils;
import cn.utsoft.xunions.entity.BaseEntity;
import cn.utsoft.xunions.entity.InvestDetailEntity_HSC;
import cn.utsoft.xunions.entity.ProjectFavourEntity_LLH;
import cn.utsoft.xunions.util.constants.RequestHttpURL;

/**
 * Created by llh on 2017/2/18 13:07.
 * Function: 项目详情业务类
 * Desc:
 */

public class ProjectDetailsPresenter extends BasePresenter<ProjectDetailsView> {


    /**
     * 获取赛事请求
     *
     * @param activity
     * @param status
     */
    public void getProjectDetails(Activity activity, String status, String projectId, String orderId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectId", projectId);

        if ("3".equals(status)) { //转让详情
            params.put("orderId", orderId);
            requestDetail(activity, params, RequestHttpURL.FIND_TRANSFER_DETAIL);
        } else if ("0".equals(status) || "1".equals(status))//投资成约和全额投资
            requestDetail(activity, params, RequestHttpURL.FIND_INVEST_FULL);
        else if ("2".equals(status))//投资预热和投资预热成约
            requestDetail(activity, params, RequestHttpURL.FIND_INVEST_PREHEAT);


//        Observable<BaseEntity<InvestDetailEntity_HSC>> observable = null;
//
//        if ("3".equals(status)) { //转让详情
//            params.put("orderId", orderId);
//            observable = ProjectRepository.getIns().findByEquityTransferDetail(params, RequestHttpURL.FIND_TRANSFER_DETAIL);
//        } else if ("0".equals(status))//投资成约和全额投资
//            observable = ProjectRepository.getIns().findByinvestFull(params, RequestHttpURL.FIND_INVEST_FULL);
//        else if ("2".equals(status))//投资预热和投资预热成约
//        {
//            observable = ProjectRepository.getIns().findByinvestPreheat(params, RequestHttpURL.FIND_INVEST_PREHEAT);
//            ProjectRepository.getIns().findByinvestPreheat1(params, RequestHttpURL.FIND_INVEST_PREHEAT)
//                    .subscribe(new DefaultSubscriber<InvestDetailEntity_HSC>() {
//                        @Override
//                        public void _onNext(InvestDetailEntity_HSC entity) {
//
//                        }
//                    });
//        }
//
//
//        if (observable == null) return;
//
//        observable.subscribeOn(Schedulers.io())
////                .compose(((ProjectDetailsActivity) activity).bindUntilEvent(RxLifeEvent.DESTROY))
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        getView().loading();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultSubscriber<BaseEntity<InvestDetailEntity_HSC>>() {
//                    @Override
//                    public void _onNext(BaseEntity<InvestDetailEntity_HSC> entity) {
//                        Logger.e(entity.toString());
//                        getView().getDetailSuccess(entity.data);
//                        getView().content();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.show(e.toString());
//                        super.onError(e);
//                    }
//                });
    }

    /**
     * @param activity
     * @param params
     * @param url
     */
    private void requestDetail(Activity activity, HashMap<String, String> params, String url) {
        getView().loading();
        XunionsHttpUtils.loadData(activity, url, params, new CheckLoginCallback() {
            @Override
            public void tgtInvalid(String message) {
                getView().content();
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                getView().content();
                try {
                    InvestDetailEntity_HSC entity = GsonUtils.getGson().fromJson(message, InvestDetailEntity_HSC.class);
                    if (entity != null)
                        getView().getDetailSuccess(entity);
                    else
                        getView().empty();

                } catch (Exception e) {

                }

            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
                getView().error(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                getView().handleFailed(message);
                getView().error(message);
            }
        });
    }

    /**
     * 添加收藏
     *
     * @param activity
     * @param projectId
     */
    public void addFavor(Activity activity, String projectId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("projectId", projectId);
        XunionsHttpUtils.loadData(activity, RequestHttpURL.ADD_FAVOR_PROJECT, params, new CheckLoginCallback() {

            @Override
            public void tgtInvalid(String message) {
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                try {
                    ProjectFavourEntity_LLH entity = GsonUtils.getGson().fromJson(message, ProjectFavourEntity_LLH.class);
                    EventBus.getDefault().post("com.xUnions.delete_collect", RequestHttpURL.HOME_PAGE_INFO);

                    if (entity != null)
                        getView().addFavorSuccess(entity);
                    else
                        getView().addFavorSuccess(message);

                } catch (Exception e) {

                }
            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                getView().handleFailed(message);
            }
        });

    }

    /**
     * 取消收藏
     *
     * @param activity
     * @param favourId
     */
    public void deleteFavor(Activity activity, String favourId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("favourId", favourId);
        XunionsHttpUtils.loadData(activity, RequestHttpURL.DEL_FAVOR_PROJECT, params, new CheckLoginCallback() {

            @Override
            public void tgtInvalid(String message) {
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                getView().delFavorSuccess();
                EventBus.getDefault().post("com.xUnions.delete_collect", RequestHttpURL.HOME_PAGE_INFO);
            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                getView().handleFailed(message);
            }
        });

    }

    /**
     * Function:生成认购订单
     *
     * @param
     * @return
     */
    public void generateTransferOrder(Context context, String orderid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderid", orderid);

        XunionsHttpUtils.loadAllData(context, RequestHttpURL.BASE_URL + "/v2/project/saveSubscriptionOrder", params, new CheckLoginCallback() {
            @Override
            public void tgtInvalid(String message) {
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                BaseEntity<String> baseEntity = GsonUtils.getGson().fromJson(message, BaseEntity.class);
                Logger.e("order>>>>>>>>>" + message);
                getView().transferOrderSuccess(baseEntity);
            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                failure(message);
            }
        });
    }
}
