package com.utouu.easyjoke.module.refresh;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.progress.UIProgressView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.easyutilcode.DimensUtil;
import com.marno.easyutilcode.IntentUtil;
import com.marno.easyutilcode.ScreenUtil;
import com.marno.easyutilcode.ToastUtil;
import com.utouu.android.commons.utils.DisplayUtil;
import com.utouu.easyjoke.module.refresh.presenter.ProjectDetailsPresenter;
import com.utouu.easyjoke.module.refresh.view.ProjectDetailsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.utsoft.xunions.R;
import cn.utsoft.xunions.adapter.InvestCommentAdapter;
import cn.utsoft.xunions.adapter.InvestIntroduceAdapter;
import cn.utsoft.xunions.adapter.InvestNewsAdapter;
import cn.utsoft.xunions.adapter.InvestorAdapter;
import cn.utsoft.xunions.adapter.ProjectAnalyseAdapter;
import cn.utsoft.xunions.base.BaseMVPActivity;
import cn.utsoft.xunions.data.http.XunionsHttpUtils;
import cn.utsoft.xunions.entity.AnalyseEntity_LLH;
import cn.utsoft.xunions.entity.BaseEntity;
import cn.utsoft.xunions.entity.InvestCommentEntity_HSC;
import cn.utsoft.xunions.entity.InvestDetailEntity_HSC;
import cn.utsoft.xunions.entity.InvestFormEntity_HSC;
import cn.utsoft.xunions.entity.InvestIntroduceEntity_HSC;
import cn.utsoft.xunions.entity.InvestNewsEntity_HSC;
import cn.utsoft.xunions.entity.InvestorEntity_HSC;
import cn.utsoft.xunions.entity.ProjectFavourEntity_LLH;
import cn.utsoft.xunions.manager.GlideManager;
import cn.utsoft.xunions.module.PdfActivity;
import cn.utsoft.xunions.module.invest.view.FixedListView;
import cn.utsoft.xunions.module.personal.OrderPayActivity;
import cn.utsoft.xunions.module.space.SpaceShowActivity;
import cn.utsoft.xunions.util.FormatUtil;
import cn.utsoft.xunions.util.LineChartUtils;
import cn.utsoft.xunions.util.LoadingUtil;
import cn.utsoft.xunions.util.ProgressBarStyleUtil;
import cn.utsoft.xunions.util.ProgressBarUtils;
import cn.utsoft.xunions.util.ScreenUtils;
import cn.utsoft.xunions.util.SpannableStringUtils;
import cn.utsoft.xunions.util.StringUtils;
import cn.utsoft.xunions.util.TimeUtils;
import cn.utsoft.xunions.widgets.FixedTextView;
import cn.utsoft.xunions.widgets.GradationScrollView;
import cn.utsoft.xunions.widgets.JCPlayerLayout;
import cn.utsoft.xunions.widgets.TextDrawable;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;


public class ProjectDetailsActivity extends BaseMVPActivity<ProjectDetailsView, ProjectDetailsPresenter> implements
        GradationScrollView.ScrollViewListener, ProjectDetailsView, RadioGroup.OnCheckedChangeListener {

    /*-----------------Top------------------*/
    @BindView(R.id.iv_banner_ProjectDetailActivity)
    ImageView mIvBanner;
    @BindView(R.id.sv_outer_ProjectDetailActivity)
    GradationScrollView mScrollView;
    @BindView(R.id.titleBar)
    TitleBarView mToolBar;
    @BindView(R.id.tv_countdown_ProjectDetailActivity)
    TextView mCountDownTv;
    @BindView(R.id.tv_title1_long_ProjectDetailActivity)
    FixedTextView mLongTitleTv;
    @BindView(R.id.tv_title1_short_ProjectDetailActivity)
    FixedTextView mShortTitleTv;
    @BindView(R.id.tv_title2_long_ProjectDetailActivity)
    TextView mLongTitleTagTv;
    @BindView(R.id.tv_title2_short_ProjectDetailActivity)
    TextView mShortTitleTagTv;
    @BindView(R.id.tv_proportion_ProjectDetailActivity)
    TextView mPropoetionTv;
    @BindView(R.id.tv_lowest_ProjectDetailActivity)
    TextView mLowestTv;
    @BindView(R.id.tv_fundraised_ProjestDetailActivity)
    TextView mFundraisedTv;
    @BindView(R.id.iv_logo_ProjectDetailActivity)
    ImageView mLogoIv;
    @BindView(R.id.tv_percentum_ProjestDetailActivity)
    TextView mFinPercentumTv;
    @BindView(R.id.pb_progress_ProjestDetailActivity)
    ProgressBar mFinPb;
    @BindView(R.id.tv_financing_stage_ProjectDetailAcitivity)
    TextView mFinStageTv;
    @BindView(R.id.tv_share_ProjectDetailAcitivity)
    TextView mShareTv;
    @BindView(R.id.tv_max_ProjectDetailAcitivity)
    TextView mMaxFinTv;
    @BindView(R.id.tv_total_ProjectDetailAcitivity)
    TextView mTotalFinTv;
    @BindView(R.id.st_company_ProjectDetailsActivity)
    SuperTextView mCompanyNameSuperTv;
    @BindView(R.id.rlayout_super_ProjectDetailsActivity)
    RelativeLayout mProjectSuperRl;
    @BindView(R.id.tv_super_left_ProjectDetailsActivity)
    TextView mProjectSuperTv;
    @BindView(R.id.iv_super_left_ProjectDetailsActivity)
    ImageView mProjectSuperIv;
    @BindView(R.id.st_space_ProjectDetailsActivity)
    SuperTextView mSpaceSuperTv;
    @BindView(R.id.st_value_ProjectDetailsActivity)
    SuperTextView mValueSuperTv;
    @BindView(R.id.tv_percentum_introduction_ProjestDetailActivity)
    TextView mBluePercentumTv;
    @BindView(R.id.pb_progress_introduction_ProjestDetailActivity)
    ProgressBar mBluePb;
    @BindView(R.id.jcLayout_videoPlayer_ProjectDetailsActivity)
    JCPlayerLayout mJCPlayerLayout;
    @BindView(R.id.cv_analyse_ProjectDetailActivity)
    RecyclerView mAnalyseCv;
    @BindView(R.id.esv_ProjectDetailsActivity)
    EasyStatusView mEasyStatusView;
    @BindView(R.id.rLayout_progress_ProjectDetailsActivity)
    RelativeLayout mProgressRl;
    @BindView(R.id.rlayout_title_long_ProjectDetailActivity)
    RelativeLayout mTitleLongRl;
    @BindView(R.id.rlayout_title_short_ProjectDetailActivity)
    RelativeLayout mTitleShortRl;
    @BindView(R.id.rlayout_share_ProjectDetailAcitivity)
    RelativeLayout mShareRl;
    @BindView(R.id.rlayout_max_ProjectDetailAcitivity)
    RelativeLayout mMaxFinRl;
    @BindView(R.id.tv_total_fin_ProjectDetailAcitivity)
    TextView mTotalTv;
    @BindView(R.id.tv_industryName_ProjectDetailsActivity)
    TextView mIndustryNameTv;
    @BindView(R.id.tv_des_ProjectDetailActivity)
    FixedTextView mDescriptionTv;
    @BindView(R.id.rlayout_video_ProjectDetailsProject)
    RelativeLayout mVideoRv;

    /*-----------------Bottom------------------*/
    @BindView(R.id.tv_invest_increaseNumber)
    TextView mtvInvestIncreaseNumber; //用户增长表的增长人数
    @BindView(R.id.tv_invest_incomeNumber)
    TextView mtvInvestIncomeNumber;  //项目收入表的收入金额
    @BindView(R.id.tv_invest_invest)
    TextView mtvInvest; //投资按钮
    @BindView(R.id.tv_invest_collect_text)
    TextView mtvInvestCollect;//收藏按钮

    @BindView(R.id.lv_invest_introduce)
    FixedListView mlvInvestIntroduce; //项目团队的支持团队列表List
    @BindView(R.id.lv_invest_news)
    FixedListView mlvInvestNews;  //项目新闻的List
    @BindView(R.id.lv_invest_comment)
    FixedListView mlvInvestComment; //跟投人评论的List
    @BindView(R.id.rv_invest_investors)
    RecyclerView mrvInvestInvestors; //团队成员横向展示的RecyclerView

    @BindView(R.id.lc_invest_increase)
    LineChart mlcInvestIncrease; //用户增长图表
    @BindView(R.id.lc_invest_deal)
    LineChart mlcInvestDeal;  //成交价情况图表
    @BindView(R.id.lc_invest_expend)
    LineChart mlcInvestExpend; //项目支出图表
    @BindView(R.id.lc_invest_income)
    LineChart mlcInvestIncome; //项目收入图表

    @BindView(R.id.ll_invest_comment)
    LinearLayout mLLayoutComment; //跟投人评论布局
    @BindView(R.id.ll_invest_form)
    LinearLayout mLLayoutForm; //表单布局
    @BindView(R.id.ll_collect)
    LinearLayout mLLayoutCollect; //收藏布局img+TextView
    @BindView(R.id.ll_invest_news) //项目新闻布局
            LinearLayout mLLayoutNews;

    @BindView(R.id.rg_invest_expend)
    RadioGroup mrgInvestExpend; //表单的年月周切换RadioGroup
    @BindView(R.id.iv_invest_collect_img)
    ImageView mivCollectImg; //收藏图片状态

    /**
     * 项目支出报表的标记
     */
    private static final int EXPENSE_WEEK = 0x01;
    private static final int EXPENSE_MONTH = 0x02;
    private static final int EXPENSE_YEAR = 0x03;

    /**
     * X轴和Y轴的Label标签数组
     */
    private List<String> mYLabels = new ArrayList<>();
    private List<String> mXLabelsExpense = new ArrayList<>();
    private List<String> mXLabelsEarn = new ArrayList<>();
    private List<String> mXLabelsUser = new ArrayList<>();
    private List<String> mXLabelsBargain = new ArrayList<>();

    /**
     * 表格数据集合
     */
    private List<Float> ExpenseFormWeek = new ArrayList<>();
    private List<Float> ExpenseFormMonth = new ArrayList<>();
    private List<Float> ExpenseFormYear = new ArrayList<>();
    private List<Float> EarningsForm = new ArrayList<>();
    private List<Float> UserIncreaseForm = new ArrayList<>();
    private List<Float> BargainForm = new ArrayList<>();

    private List<InvestorEntity_HSC> investorData = new ArrayList<>();
    private List<InvestIntroduceEntity_HSC> introduceData = new ArrayList<>();
    private List<InvestNewsEntity_HSC> newsData = new ArrayList<>();
    private List<InvestCommentEntity_HSC> commentData = new ArrayList<>();

    private InvestorAdapter investorAdapter;
    private InvestIntroduceAdapter introduceAdapter;
    private InvestNewsAdapter newsAdapter;
    private InvestCommentAdapter commentAdapter;

    private float valueMax = 100; //图表的最大数值
    private int increaseNum = 0; //用户增长总人数
    private int income = 0; //项目收入总额
    private InvestFormEntity_HSC tuBiao; //图表数据类

    /*---------------------------*/
    private ProjectAnalyseAdapter mAnalyseAdapter;//项目分析适配器

    private int ivBannerHeight = 0;//banner高度

    private String status;//状态
    private String orderId;//订单id
    private String projectId;//项目id
    private String projectName;//项目名称

    private int isDeposit;//支持成约
    private String isDepositOrder;//支持成约
    private int isFavour = 1;//没收藏
    private String favourId;//收藏id
    private String mInterspaceId;//空间id

    private CountDownTimer mCountDownTimer;//倒计时
    private UIProgressView mLoadingProgress;
    private String type;

    /**
     * 调起项目详情界面,投资项目跳转可不传orderId,股权转让跳转需要传orderId
     * 并且status传“3”
     *
     * @param context
     * @param orderId
     */
    public static void startThisActivity(Context context, String status, String projectId, String projectName, String orderId) {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("projectId", projectId);
        intent.putExtra("projectName", projectName);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_project_details;
    }

    @Override
    protected ProjectDetailsPresenter createPresenter() {
        return new ProjectDetailsPresenter();
    }

    @Override
    protected void _initView(Bundle bundle) {
        setEasyStatusView(mEasyStatusView);
        mToolBar.setBackgroundColor(Color.TRANSPARENT);
        //设置图片和视屏播放高度
        int height = (int) ((4f / 6f) * ScreenUtil.getScreenWidth(mContext));
        ivBannerHeight = height;
        mIvBanner.getLayoutParams().height = height;

        mJCPlayerLayout.getLayoutParams().height = height;
        mScrollView.setScrollViewListener(ProjectDetailsActivity.this);

        //项目分析设配器
        mAnalyseAdapter = new ProjectAnalyseAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mAnalyseCv.setLayoutManager(layoutManager);
        mAnalyseCv.setAdapter(mAnalyseAdapter);

        initBottomView();
    }

    @OnClick({R.id.st_space_ProjectDetailsActivity, R.id.ll_collect, R.id.tv_invest_invest, R.id.st_authorization_ProjectDetailsActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.st_space_ProjectDetailsActivity:
                Intent intent = new Intent(mContext, SpaceShowActivity.class);
                intent.putExtra("spaceId", mInterspaceId);
                IntentUtil.to(mContext, intent);
                break;
            case R.id.ll_collect:
                //收藏逻辑实现
                progressShow();
                if (isFavour == 1)
                    mPresenter.addFavor(mContext, projectId);
                //取消收藏
                if (isFavour == 2)
                    mPresenter.deleteFavor(mContext, favourId);
                break;
            case R.id.tv_invest_invest:
                if ("0".equals(status)) {//跳转到投资确认界面
                    Intent investIntent = new Intent(mContext, IntoGoldBuyActivity.class);
                    investIntent.putExtra("type", isDeposit);
                    investIntent.putExtra("projectId", projectId);
                    IntentUtil.to(mContext, investIntent);
                } else if ("3".equals(status)) {//跳转到订单支付
                    type = "3";
                    if (isDepositOrder.equals("2"))
                        type = "3";
                    else if (isDepositOrder.equals("1"))
                        type = "4";
                    progressShow();
                    mPresenter.generateTransferOrder(mContext, orderId);
                }
                break;

            case R.id.st_authorization_ProjectDetailsActivity:
                PdfActivity.start(this, projectId, 0);
//                IntentUtil.to(mContext, MadeByBookActivity.class);
                break;
        }
    }

    @Override
    protected void loadData() {
        status = getIntent().getStringExtra("status");
        orderId = getIntent().getStringExtra("orderId");
        projectId = getIntent().getStringExtra("projectId");
        isDepositOrder = getIntent().getStringExtra("isDeposit");
        projectName = getIntent().getStringExtra("projectName");
        mToolBar.setTitleMainText(projectName);
        mToolBar.setFocusable(true);
        mToolBar.setFocusableInTouchMode(true);
        mToolBar.requestFocus();
        mScrollView.smoothScrollTo(0,0);
        mPresenter.getProjectDetails(mContext, status, projectId, orderId);
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
            mToolBar.setBackgroundColor(Color.TRANSPARENT);
        } else if (y > 0 && y <= ivBannerHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / ivBannerHeight;
            float alpha = (255 * scale);
            mToolBar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            mToolBar.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }

        //视频播放控件在不屏幕内，停止播放。
        if (!ScreenUtils.checkIsVisible(mContext, mJCPlayerLayout) && mJCPlayerLayout.currentState == CURRENT_STATE_PLAYING)
            mJCPlayerLayout.startButton.performClick();
    }

    private void initBottomView() {
        //设置团队成员信息
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        investorAdapter = new InvestorAdapter(investorData);
        mrvInvestInvestors.setLayoutManager(layoutManager);
        mrvInvestInvestors.setAdapter(investorAdapter);

        //设置团队介绍
        introduceAdapter = new InvestIntroduceAdapter(this, introduceData, R.layout.item_invest_introduce);
        mlvInvestIntroduce.setAdapter(introduceAdapter);

        //设置项目动态
        newsAdapter = new InvestNewsAdapter(this, newsData, R.layout.item_invest_news);
        mlvInvestNews.setAdapter(newsAdapter);

        //设置跟投人评论
        commentAdapter = new InvestCommentAdapter(this, commentData, R.layout.item_invest_comment);
        mlvInvestComment.setAdapter(commentAdapter);

        //设置监听器
        mrgInvestExpend.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        XunionsHttpUtils.cancelRequests(mContext);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (mLoadingProgress != null) {
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }

        super.onDestroy();
    }

    @Override
    public void getDetailSuccess(InvestDetailEntity_HSC entity) {
        /**

         顶部大图                   字段：project_fm
         项目小图                   字段：logo
         项目名称                   字段：projectName   + （status&&isDeposit的判断）“成”、“预”
         倒计时                     字段：projectDelayReleaseDate
         ¥----.--起购 最低%@%%"     起购：startUpQuantity * singleStockPrice  最低：startUpQuantity
         成约金占总金额:--%          字段：depositLowAmount
         募资进度                   字段：( transferStockNum -surplusStockNum )/transferStockNum
         已募资                     字段：nowRaiseSum
         融资阶段                   字段：financingNumId
         出让份额                   字段：transferStockNum
         起购份额                   字段：transferStockNum * singleStockPrice
         募资总额                   字段：predictRaiseSum

         */
//        加载封面
        GlideManager.setCommonPlaceholder(R.drawable.ic_place_holder_750_500);
        GlideManager.loadImg(entity.project_fm, mIvBanner);

//      加载视频
        if (TextUtils.isEmpty(entity.projectVideo))
            mVideoRv.setVisibility(View.GONE);
        else{
            GlideManager.loadImg(entity.project_fm,mJCPlayerLayout.thumbImageView);
            mJCPlayerLayout.setUp(entity.projectVideo, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        }


        isDeposit = entity.isDeposit;
        isFavour = entity.isFavour;
        mInterspaceId = entity.interspaceId;
        if (entity.favour != null)
            favourId = entity.favour.id;

        if (isFavour == 2) {//已收藏
            mivCollectImg.setImageResource(R.drawable.small_star_sel);
        }

        mDescriptionTv.setText(entity.projectDescribe);
/*-----------------------------------------------------*/
        if ("3".equals(status)) {//股权转让
            mtvInvest.setText("认购");

            if (entity.projectStock != null) {
                GlideManager.setCirclePlaceholder(R.drawable.ic_place_holder_circle_120_120);
                GlideManager.loadCircleImg(entity.projectStock.headImg, mLogoIv);
                mTitleShortRl.setVisibility(View.VISIBLE);
                mShortTitleTagTv.setVisibility(View.GONE);
                mShortTitleTv.setText(entity.projectStock.userName);

                mShareRl.setVisibility(View.GONE);
                mMaxFinRl.setVisibility(View.GONE);

                mLowestTv.setText(SpannableStringUtils
                        .getBuilder("￥").setTextSize(13)
                        .append(FormatUtil.formatCommaString((int) (entity.projectStock.ownStock * entity.projectStock.sigleStock), false, false))
                        .setTextSize(34)
                        .create()
                );

                if (isDepositOrder.equals("1")) {//支持成约
                    mPropoetionTv.setVisibility(View.VISIBLE);
                    mPropoetionTv.setText(entity.depositLowAmount + "%的成约金");
                }

                //        entity.financingNumId 融资轮次
                mFinStageTv.setText(entity.financingNumId);
                mTotalTv.setText("出让股权");
                mTotalFinTv.setText(entity.projectStock.ownStock + "%");

                mProjectSuperRl.setVisibility(View.VISIBLE);
                mProjectSuperTv.setText(entity.projectName);
                mIndustryNameTv.setText(entity.industryName);
                GlideManager.setsRoundPlaceholder(R.drawable.ic_place_holder_120_120);
                GlideManager.loadRoundImg(entity.project_fm, mProjectSuperIv, 12);

            }

        } else {
            GlideManager.setsRoundPlaceholder(R.drawable.ic_place_holder_120_120);
            GlideManager.loadRoundImg(entity.logo, mLogoIv, 10);

            setProjectTitle(entity.projectName, entity.isDeposit, entity.industryName);
//         最低起购价
//        entity.depositLowAmount : 最低定金最低金额
//            entity.singleStockPrice
//            startUpQuantity : 起投数量,

            double account = 0f;
            double accountTem = 0f;

            if (entity.singleStockPrice != 0) {
                account = (entity.startUpQuantity * entity.singleStockPrice);
            }
            accountTem = account;
            if (account >= 10000000) {
                accountTem = account / 10000000;
            }
            String[] splitLowest = FormatUtil.formatCommaString(accountTem).split("\\.");
            mLowestTv.setText(SpannableStringUtils
                    .getBuilder("¥ ")
                    .append(splitLowest[0]).setTextSize(34)
                    .append(".").append(splitLowest[1])
                    .append(account >= 10000000 ? "千万" : "")
                    .append("起购 最低").append(entity.startUpQuantity + "").append("%").create()
            );

            if (1 == entity.isDeposit) {//支持成约
                mPropoetionTv.setVisibility(View.VISIBLE);
                mPropoetionTv.setText(String.format(getResources().getString(R.string.desc_propoetion_transfer), entity.depositLowAmount + ""));
            }

            if ("0".equals(status) || "1".equals(status)) {//投资成约和投资全额
                mProgressRl.setVisibility(View.VISIBLE);

//        entity.nowRaiseSum现在募资总额,predictRaiseSum : 预计募资总金额
                float temp = (float) (entity.transferStockNum - entity.surplusStockNum) / entity.transferStockNum;
                if (temp < 0.01f && temp != 0)
                    temp = 1;
                int progress = entity.transferStockNum == 0 ? 0 : (int) (temp * 100);
                ProgressBarStyleUtil.setStyle(progress, mFinPb, mFinPercentumTv);
                ProgressBarUtils.setProgressAnim(mFinPb, progress, 2000, true);
                mFundraisedTv.setText(SpannableStringUtils
                        .getBuilder("已募资：")
                        .append(FormatUtil.formatCommaString(Double.parseDouble(entity.nowRaiseSum)).replace(",", "")).setForegroundColor(Color.parseColor("#666666"))
                        .create()
                );

            } else if ("2".equals(status)) {//投资预热和投资预热成约
                setCountDown(entity.projectDelayReleaseDate);
            }

            /*-----------------------------------------------------*/

//        entity.financingNumId 融资轮次
            mFinStageTv.setText(entity.financingNumId);
//        entity.transferStockNum 出让股总数量(百分比的分子)
            mShareTv.setText(entity.transferStockNum + "%");
//            起购份额
//            double startUpTem = entity.startUpQuantity * entity.singleStockPrice;
//            mMaxFinTv.setText(startUpTem >= 10000000 ? (int) (startUpTem / 10000000) + "千万" : (startUpTem >= 1000 ? (int) (startUpTem / 10000) + "万" : ""));
//            预募资人数
            mMaxFinTv.setText(entity.subscribeNum + "人");

//        entity.predictRaiseSum : 预计募资总金额

            if (entity.predictRaiseSum < 10000)
                mTotalFinTv.setText(entity.predictRaiseSum + "");

            else
                mTotalFinTv.setText((long) (entity.predictRaiseSum / 10000) + "万");


        }

        //          entity.projectSchedule项目进度
        ProgressBarStyleUtil.setStyle(entity.projectSchedule, mBluePb, mBluePercentumTv);
        ProgressBarUtils.setProgressAnim(mBluePb, entity.projectSchedule, 2000);

//        entity.companies公司名称
        mCompanyNameSuperTv.setRightString(entity.companies);
//        entity.interspaceName所属空间名称
        mSpaceSuperTv.setRightString(entity.interspaceName);
//        entity.projectValuation项目估值

        mValueSuperTv.setRightString(FormatUtil.formatCommaString(entity.projectValuation) + "元");

//        项目分析数据
//        marketAnalyze : 市场分析,
//        commercePatternAnalyze : 商业模式分析,
//                painAppealAnalyze : 痛点与述求分析,
//                pitchingInAnalyze : 切入点分析,
//                productDirectionAnalyze : 产品方向分析
        mAnalyseAdapter.setNewData(new ArrayList<AnalyseEntity_LLH>());
        mAnalyseAdapter.addData(new AnalyseEntity_LLH(R.drawable.ic_detail_analyse_market, "市场分析", entity.marketAnalyze));
        mAnalyseAdapter.addData(new AnalyseEntity_LLH(R.drawable.ic_detail_analyse_appeal, "痛点及诉求分析", entity.painAppealAnalyze));
        mAnalyseAdapter.addData(new AnalyseEntity_LLH(R.drawable.ic_detail_analyse_pointcut, "切入点分析", entity.pitchingInAnalyze));
        mAnalyseAdapter.addData(new AnalyseEntity_LLH(R.drawable.ic_detail_analyse_bus, "商业模式分析", entity.commercePatternAnalyze));
        mAnalyseAdapter.addData(new AnalyseEntity_LLH(R.drawable.ic_detail_analyse_derection, "产品方向分析", entity.productDirectionAnalyze));

        /***************项目团队，项目报表，项目动态，跟投人评论模块的数据设置************/
        //TODO 首先根据一个字段判断是那种类型的Project来确定是否显示项目报表和跟投人评论模块
        /**
         * status=2，isDeposit=0 ---->项目预热
         * status=2，isDeposit=1 ---->项目预热成约
         * status=0，isDeposit=0 ---->投资全额
         * status=0，isDeposit=1 ---->投资成约
         * status=3，       项目转让
         */
        if (status.equals("3")) {
            //转让项目详情取消收藏功能
            mLLayoutCollect.setVisibility(View.GONE);
        }
        if (!"3".equals(status) && entity.status == 1) {
            mtvInvest.setBackgroundColor(Color.parseColor("#DDDDDD"));
            mtvInvest.setText("募资完成");
            mtvInvest.setClickable(false);
        }
        if (status.equals("2")) {
            //为预热和预热成约项目，隐藏项目报表和跟投人评论，设置按钮为灰色，不可点击
            mLLayoutComment.setVisibility(View.GONE);
            mLLayoutForm.setVisibility(View.GONE);
            mtvInvest.setBackgroundColor(Color.parseColor("#DDDDDD"));
            mtvInvest.setClickable(false);
        } else {
            //项目报表数据设置
            tuBiao = entity.tuBiao;
            loadExpenseFormData(tuBiao, EXPENSE_MONTH);
            loadLineChartData(tuBiao);
            //跟投人评论数据设置
            commentAdapter.clear(); //清除数据
            if (entity.checkComment.isEmpty()) {
                mLLayoutComment.setVisibility(View.GONE);
            } else {
                commentAdapter.addAll(entity.checkComment);
                commentAdapter.notifyDataSetChanged();
            }
//            mtvInvest.setBackgroundColor(Color.parseColor("#6666ff"));
            mtvInvest.setClickable(true);
        }
        //项目团队成员数据设置
        investorData.clear();   //清楚数据
        investorData.addAll(entity.projectTeam);
        investorAdapter.notifyDataSetChanged();
        //项目承接方数据设置
        introduceAdapter.clear(); //清除数据
        introduceAdapter.addAll(entity.projectIntroduceTeam);
        introduceAdapter.notifyDataSetChanged();
        //项目动态数据设置
        newsAdapter.clear(); //清除数据
        if (entity.projectNews.isEmpty()) {
            mLLayoutNews.setVisibility(View.GONE);
        } else {
            for (InvestNewsEntity_HSC projectNew : entity.projectNews) {
                projectNew.logo = entity.logo;
            }
            newsAdapter.addAll(entity.projectNews);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addFavorSuccess(Object message) {
        progressDissmiss();
        if (message instanceof ProjectFavourEntity_LLH) {
            ProjectFavourEntity_LLH entity = (ProjectFavourEntity_LLH) message;
            favourId = entity.id;
        }
        isFavour = 2;
        mivCollectImg.setImageResource(R.drawable.small_star_sel);
    }

    @Override
    public void delFavorSuccess() {
        progressDissmiss();
        isFavour = 1;
        mivCollectImg.setImageResource(R.drawable.small_star_nor);
    }

    @Override
    public void handleFailed(String message) {
        progressDissmiss();
        ToastUtil.show(message);
    }

    @Override
    public void tgtInvalid(String message) {
        progressDissmiss();
        super.tgtInvalid(message);
    }

    /**
     * 根据服务器返回的数据设置图表数据
     *
     * @param tuBiao 图表实体类
     */
    private void loadLineChartData(InvestFormEntity_HSC tuBiao) {

        List<InvestFormEntity_HSC.Bargain> bargains = tuBiao.bargain;
        List<InvestFormEntity_HSC.Earnings> earnings = tuBiao.earnings;
        List<InvestFormEntity_HSC.UserIncrease> userIncreases = tuBiao.userIncrease;

        //项目收入数据设置
        EarningsForm.clear();
        mXLabelsEarn.clear();
        valueMax = 0;
        for (InvestFormEntity_HSC.Earnings earning : earnings) {
            income += earning.sumAmount;
            EarningsForm.add((float) earning.sumAmount);
            valueMax = valueMax > earning.sumAmount ? valueMax : earning.sumAmount;
            mXLabelsEarn.add(earning.strTime);
        }
        LineChartUtils.initLineChart(this, mlcInvestIncome, setValueMax(valueMax), 0, true, false);
        LineChartUtils.setLineData(this, mlcInvestIncome, EarningsForm,
                LineDataSet.Mode.HORIZONTAL_BEZIER, R.drawable.fade_green, Color.parseColor("#37F990"));
        //设置X轴的Label
        mlcInvestIncome.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsEarn)); //设置标签内容
        mlcInvestIncome.getXAxis().setLabelCount(earnings.size() - 1); //设置标签数量
        if (income > 1000000) {
            mtvInvestIncomeNumber.setText("" + income / 10000 + "万");
        } else if (income > 10000) {
            int wan = income / 10000;
            int qian = income / 1000 % 10;
            int bai = income / 100 % 10;
            mtvInvestIncomeNumber.setText("" + wan + "." + qian + bai + "万");
        } else {
            mtvInvestIncomeNumber.setText("" + income + "");
        }

        //用户增长数据设置
        UserIncreaseForm.clear();
        mXLabelsUser.clear();
        valueMax = 0;
        for (InvestFormEntity_HSC.UserIncrease userIncrease : userIncreases) {
            increaseNum += userIncrease.sumAmount;
            UserIncreaseForm.add((float) userIncrease.sumAmount);
            valueMax = valueMax > userIncrease.sumAmount ? valueMax : userIncrease.sumAmount;
            mXLabelsUser.add(userIncrease.strTime);
        }
        LineChartUtils.initLineChart(this, mlcInvestIncrease, setValueMax(valueMax), 0, true, false);
        LineChartUtils.setLineData(this, mlcInvestIncrease, UserIncreaseForm,
                LineDataSet.Mode.HORIZONTAL_BEZIER, R.drawable.fade_blue, Color.parseColor("#1FC3FF"));
        //设置Y轴的Label
        mlcInvestIncrease.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsUser));
        mlcInvestIncrease.getXAxis().setLabelCount(userIncreases.size() - 1);
        if (increaseNum > 1000000) {
            mtvInvestIncreaseNumber.setText("" + increaseNum / 10000 + "万人");
        } else if (increaseNum > 10000) {
            int wan = increaseNum / 10000;
            int qian = increaseNum / 1000 % 10;
            int bai = increaseNum / 100 % 10;
            mtvInvestIncreaseNumber.setText("" + wan + "." + qian + bai + "万人");
        } else {
            mtvInvestIncreaseNumber.setText("" + increaseNum + (increaseNum > 0 ? "人" : ""));
        }

        //设置成交价情况
        BargainForm.clear();
        mXLabelsBargain.clear();
        valueMax = 0;
        for (InvestFormEntity_HSC.Bargain bargain : bargains) {
            BargainForm.add((float) bargain.sumAmount);
            valueMax = valueMax > bargain.sumAmount ? valueMax : bargain.sumAmount;
            mXLabelsBargain.add(bargain.strTime);
        }
        LineChartUtils.initLineChart(this, mlcInvestDeal, setValueMax(valueMax), 0, true, true);
        LineChartUtils.setLineData(this, mlcInvestDeal, BargainForm,
                LineDataSet.Mode.LINEAR, R.drawable.fade_light_blue, Color.parseColor("#2298F1"));
        mlcInvestDeal.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsBargain));
        mlcInvestDeal.getXAxis().setLabelCount(bargains.size() - 1);
    }

    /**
     * 项目支出情况报表的数据加载
     *
     * @param tuBiao
     * @param expenseType
     */
    private void loadExpenseFormData(InvestFormEntity_HSC tuBiao, int expenseType) {
        List<InvestFormEntity_HSC.ExpenseWeek> expenseWeek = tuBiao.expenseWeek;
        List<InvestFormEntity_HSC.ExpenseMonth> expenseMonth = tuBiao.expenseMonth;
        List<InvestFormEntity_HSC.ExpenseYear> expenseYear = tuBiao.expenseYear;

        switch (expenseType) {
            case EXPENSE_WEEK:
                //设置项目周支出数据
                try {
                    ExpenseFormWeek.clear();
                    mXLabelsExpense.clear();
                    valueMax = 0;
                    for (InvestFormEntity_HSC.ExpenseWeek expense1 : expenseWeek) {
                        ExpenseFormWeek.add((float) expense1.sumAmount);
                        valueMax = valueMax > expense1.sumAmount ? valueMax : expense1.sumAmount;
                        mXLabelsExpense.add(expense1.strTime);
                    }
                    LineChartUtils.initLineChart(this, mlcInvestExpend, setValueMax(valueMax), 0, true, true);
                    LineChartUtils.setLineData(this, mlcInvestExpend, ExpenseFormWeek,
                            LineDataSet.Mode.LINEAR, R.drawable.fade_light_blue, Color.parseColor("#2298F1"));
                    mlcInvestExpend.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsExpense));
                    mlcInvestExpend.getXAxis().setLabelCount(expenseWeek.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mlcInvestExpend.clear();
                }
                break;
            case EXPENSE_MONTH:
                //设置项目月支出数据
                try {
                    ExpenseFormMonth.clear();
                    mXLabelsExpense.clear();
                    valueMax = 0;
                    for (InvestFormEntity_HSC.ExpenseMonth expense1 : expenseMonth) {
                        ExpenseFormMonth.add((float) expense1.sumAmount);
                        valueMax = valueMax > expense1.sumAmount ? valueMax : expense1.sumAmount;
                        mXLabelsExpense.add(expense1.strTime);
                    }
                    LineChartUtils.initLineChart(this, mlcInvestExpend, setValueMax(valueMax), 0, true, true);
                    LineChartUtils.setLineData(this, mlcInvestExpend, ExpenseFormMonth,
                            LineDataSet.Mode.LINEAR, R.drawable.fade_light_blue, Color.parseColor("#2298F1"));
                    mlcInvestExpend.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsExpense));
                    mlcInvestExpend.getXAxis().setLabelCount(expenseMonth.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mlcInvestExpend.clear();
                }
                break;
            case EXPENSE_YEAR:
                //设置项目年支出数据
                try {
                    ExpenseFormYear.clear();
                    mXLabelsExpense.clear();
                    valueMax = 0;
                    for (InvestFormEntity_HSC.ExpenseYear expense1 : expenseYear) {
                        ExpenseFormYear.add((float) expense1.sumAmount);
                        valueMax = valueMax > expense1.sumAmount ? valueMax : expense1.sumAmount;
                        mXLabelsExpense.add(expense1.strTime);
                    }
                    LineChartUtils.initLineChart(this, mlcInvestExpend, setValueMax(valueMax), 0, true, true);
                    LineChartUtils.setLineData(this, mlcInvestExpend, ExpenseFormYear,
                            LineDataSet.Mode.LINEAR, R.drawable.fade_light_blue, Color.parseColor("#2298F1"));
                    mlcInvestExpend.getXAxis().setValueFormatter(new CustomXValueFormatter(mXLabelsExpense));
                    mlcInvestExpend.getXAxis().setLabelCount(expenseYear.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mlcInvestExpend.clear();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置项目名称和标识
     *
     * @param projectName
     * @param isDeposit
     */
    private void setProjectTitle(String projectName, int isDeposit, String industory) {
        if (StringUtils.getLength(projectName) <= 10) {//长度小于10，只有一行，标识在第二行居中显示
            mTitleShortRl.setVisibility(View.VISIBLE);
            mShortTitleTv.setText(projectName);
            mShortTitleTagTv.setText(getTitleTag("", 0, isDeposit, industory));
        } else {//长度大于10，多行，左对齐，标识跟在最后一行后面显示
            mTitleLongRl.setVisibility(View.VISIBLE);
            String[] strings;
            if (StringUtils.getLength(projectName) % 10 == 0) {//长度是10的倍数
                strings = StringUtils.splitByLength(projectName, StringUtils.getLength(projectName) - 10, "");

            } else {
                strings = StringUtils.splitByLength(projectName, StringUtils.getLength(projectName) - StringUtils.getLength(projectName) % 10, "");
            }
            mLongTitleTv.setText(strings[0]);
            mLongTitleTagTv.setText(getTitleTag(strings[1], 4, isDeposit, industory));
        }
    }

    /**
     * 获取标题右边的“互联网”“预”“成”
     *
     * @param str
     * @return
     */
    private SpannableStringBuilder getTitleTag(String str, int xOffset, int isDeposit, String industory) {

        if (xOffset == 0) {
            xOffset -= 2;
            if ("2".equals(status) && 1 == isDeposit)
                xOffset -= 2;

        }
        //所属产业标识
        TextDrawable industoryDrawable = TextDrawableHelper.getTagDrawable(industory, Color.parseColor("#979797"), Color.parseColor("#979797"), Paint.Style.STROKE,
                10, (int) DisplayUtil.dp2px(mContext, 1), (int) DisplayUtil.dp2px(mContext, 0.5f));

        SpannableStringUtils.Builder builder = SpannableStringUtils
                .getBuilder(str)
                .append("i").setDrawable(industoryDrawable,
                        DimensUtil.dp2px(xOffset, mContext),
                        -DimensUtil.dp2px(2, mContext));

        TextDrawable agreeDrawable = TextDrawableHelper.getTagDrawable("成", Color.WHITE, Color.parseColor("#ff9c1f"), Paint.Style.FILL,
                10, (int) DisplayUtil.dp2px(mContext, 1), 0);

        if ("2".equals(status)) {//预热
            xOffset += 4;
            TextDrawable preDrawable = TextDrawableHelper.getTagDrawable("预", Color.WHITE, Color.parseColor("#fe6666"), Paint.Style.FILL,
                    10, (int) DisplayUtil.dp2px(mContext, 1), 0);
            builder.append("p").setDrawable(preDrawable,
                    DimensUtil.dp2px(xOffset, mContext),
                    -DimensUtil.dp2px(2, mContext));
            if (1 == isDeposit) {
                xOffset += 4;
                builder.append("a").setDrawable(agreeDrawable,
                        DimensUtil.dp2px(xOffset, mContext),
                        -DimensUtil.dp2px(2, mContext));
            }
        }

        if ("0".equals(status) || "1".equals(status)) {//全额和成约  募资完成
            xOffset += 4;
            if (1 == isDeposit) {
                builder.append("a").setDrawable(agreeDrawable,
                        DimensUtil.dp2px(xOffset, mContext),
                        -DimensUtil.dp2px(2, mContext));
            } else {
                TextDrawable fullDrawable = TextDrawableHelper.getTagDrawable("全", Color.WHITE, Color.parseColor("#5c7cfa"), Paint.Style.FILL,
                        10, (int) DisplayUtil.dp2px(mContext, 1), 0);
                builder.append("f").setDrawable(fullDrawable,
                        DimensUtil.dp2px(xOffset, mContext),
                        -DimensUtil.dp2px(2, mContext));
            }
        }
        return builder.create();
    }

    /**
     * 设置倒计时
     *
     * @param commingDate
     */
    private void setCountDown(long commingDate) {

        //项目延迟发布的日期
        mCountDownTimer = new CountDownTimer(10000/*commingDate - System.currentTimeMillis()*/, 1000) {
            @Override
            public void onTick(long l) {
                setCountDownText(TimeUtils.long2Hms(l));
            }

            @Override
            public void onFinish() {
                setCountDownText("00:00:00");
                status = "0";
                loadData();
                /*mPresenter.getProjectDetails(mContext,"0",projectId,orderId);*/
                /*startThisActivity(mContext,"0",projectId,projectName,orderId);
                finish();*/
            }
        }.start();
    }

    /**
     * 设置倒计时文本
     *
     * @param str
     */
    private void setCountDownText(String str) {
        if (mCountDownTv == null && str == null) return;

        SpannableStringUtils.Builder builder = SpannableStringUtils
                .getBuilder("倒计时：");

        String[] splitTime = str.split(":");
        for (int i = 0; i < splitTime.length; i++) {
            builder.append(splitTime[i]).setBackgroundColor(Color.parseColor("#ff7d00")).setForegroundColor(Color.WHITE);
            if (i != splitTime.length - 1)
                builder.append("：");
        }
        mCountDownTv.setText(builder.create());
    }


    /**
     * 项目报表按钮切换设置不同的数据（年，月，周）
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mlcInvestExpend.clear();
        switch (checkedId) {
            case R.id.rb_invest_week:
                loadExpenseFormData(tuBiao, EXPENSE_WEEK);
                break;
            case R.id.rb_invest_month:
                loadExpenseFormData(tuBiao, EXPENSE_MONTH);
                break;
            case R.id.rb_invest_year:
                loadExpenseFormData(tuBiao, EXPENSE_YEAR);
                break;
            default:
                break;
        }
        mlcInvestExpend.animateXY(1500, 1500);
        mlcInvestExpend.invalidate();
    }

    /**
     * 显示加载进度
     */
    private void progressShow() {
        if (mLoadingProgress == null) {
            mLoadingProgress = LoadingUtil.show(mContext, "");
            mLoadingProgress.setCancelable(false);
        } else {
            mLoadingProgress.show();
        }
    }

    /**
     * 显示加载进度
     */
    private void progressDissmiss() {
        if (mLoadingProgress != null) {
            mLoadingProgress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if (JCVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 确定图表Y轴显示值得范围
     *
     * @param valueMax 最大值
     * @return 最大值范围
     */
    public float setValueMax(float valueMax) {
        if (valueMax < 100000000) {
            if (valueMax > 50000000) {
                valueMax = 110000000;
            } else if (valueMax > 10000000) {
                valueMax = 52000000;
            } else if (valueMax > 5000000) {
                valueMax = 11000000;
            } else if (valueMax > 1000000) {
                valueMax = 5200000;
            } else if (valueMax > 500000) {
                valueMax = 1100000;
            } else if (valueMax > 100000) {
                valueMax = 520000;
            } else if (valueMax > 50000) {
                valueMax = 110000;
            } else if (valueMax > 10000) {
                valueMax = 52000;
            } else if (valueMax > 5000) {
                valueMax = 11000;
            } else if (valueMax > 1000) {
                valueMax = 5200;
            } else if (valueMax > 500) {
                valueMax = 1100;
            } else if (valueMax > 100) {
                valueMax = 520;
            } else if (valueMax > 50) {
                valueMax = 110;
            } else if (valueMax > 10) {
                valueMax = 52;
            } else {
                valueMax = 11;
            }
        } else if (valueMax > 5000000000f) {
            valueMax = 11000000000f;
        } else if (valueMax > 1000000000f) {
            valueMax = 5200000000f;
        } else if (valueMax > 500000000f) {
            valueMax = 1100000000f;
        } else if (valueMax >= 100000000) {
            valueMax = 520000000;
        }
        return valueMax;
    }

    /**
     * Function:生成转让订单
     *
     * @param
     * @return
     */
    @Override
    public void transferOrderSuccess(BaseEntity<String> baseEntity) {
        progressDissmiss();
        if (baseEntity.success) {
            OrderPayActivity.startOrderPay(mContext, orderId, type);
        } else {
            ToastUtil.show(baseEntity.msg);
        }
    }
}
