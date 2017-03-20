package com.utouu.easyjoke.data.download;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.utouu.easyjoke.util.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 李波 on 2017/3/7.
 * Function: 下载
 * Desc:
 */
public class DownloadTask extends AsyncTask<String, Long, Boolean> {
    private static final long DEFAULT_INTERVAL = 200;

    private final Context mContext;
    private final String url;

    private long interval = DEFAULT_INTERVAL;
    private Callback callback;

    private String target;
    private String name;

    private String path;
    private PublicHelper mHelper;

    private DownloadTask(Context context, String url) {
        this.url = url;
        this.mContext = context;
    }

    public static DownloadTask newIns(Context context, String url) {
        return new DownloadTask(context, url);
    }

    /**
     * 设置下载文件目标地址
     *
     * @param target
     * @return
     */
    public DownloadTask target(String target) {
        this.target = target;
        return this;
    }

    /**
     * 设置下载文件目标地址
     *
     * @param name
     * @return
     */
    public DownloadTask fileName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置监听
     *
     * @param callback
     * @return
     */
    public DownloadTask listener(Callback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置发布进度间隔
     *
     * @param interval
     * @return
     */
    public DownloadTask interval(long interval) {
        this.interval = interval;
        return this;
    }

    /**
     * 启动下载
     */
    public void start() {
        if (TextUtils.isEmpty(target)) {
            if (TextUtils.isEmpty(name)) {
                path = FileUtils.createPath(mContext, null);
            } else {
                path = FileUtils.createPath(mContext, name);
            }
        } else {
            path = target;
        }
        execute(url, path);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return download(params[0], params[1]);
    }

    /**
     * 执行下载请求
     *
     * @param url
     * @param target
     * @return
     */
    public boolean download(String url, String target) {
        HttpURLConnection connection = null;
        OutputStream out = null;
        InputStream in = null;
        // 文件长度和下载进度
        long length = 0;
        long current = 0;
        // 下载缓存计数
        int count = 0;
        // 创建网络请求连接
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20 * 1000);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-type", "application/pdf");
            // 创建连接检查返回code
            int code = connection.getResponseCode();
            if (code == 200 || code == 206) {
                // 获取网络输入流
                in = connection.getInputStream();
                // 获取文件写入流
                out = new FileOutputStream(target);
                // 获取文本长度
                length = connection.getContentLength();
                // 连接成功时发布一次进度
                publishProgress(current, length);
                // 获取下载缓存
                byte[] temp = new byte[4 * 1024];
                // 循环写入文件
                while (true) {
                    count = in.read(temp);
                    // 判断文件是否写入完成,
                    if (count == -1) return true;
                    // 写入文件
                    out.write(temp, 0, count);
                    // 增量下载进度
                    current += count;
                    // 发布进度
                    publishProgress(current, length);
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                // 关闭流
                in.close();
                out.close();
                // 断开连接
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (callback != null) {
            callback.onFinish(result, path);
        }
        destroy();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        if (mHelper == null) {
            mHelper = new PublicHelper(interval);
        }
        mHelper.publish(values[0], values[1]);
    }

    /**
     * Create By 李波 on 2017/3/7.
     * Function: 发布进度辅助类
     * Desc:
     */
    public class PublicHelper {
        private long interval;
        private long start;
        private long lastProgress;

        public PublicHelper(long interval) {
            this.interval = interval;
        }

        /**
         * 进度发布,
         *
         * @param current
         * @param total
         */
        public void publish(long current, long total) {
            if (callback == null) {
                return;
            }
            long millis = System.currentTimeMillis();
            long offsetTime = millis - start;
            if (interval <= offsetTime || start <= 0) {
                // 记录当前下载时间
                start = millis;
                // 计算两次下载量的差值;
                long offsetProgress = current - lastProgress;
                // 保存当前下载量
                lastProgress = current;
                // 计算下载速度(单位kb/s)
                long velocity = offsetProgress / offsetTime;
                // 回调进度
                callback.onProgress(current, total, velocity);
            }
        }
    }

    /**
     * Create By 李波 on 2017/3/7.
     * Function: 下载进度监听
     * Desc:
     */
    public interface Callback {
        /**
         * 当前下载进度
         *
         * @param current
         * @param total
         * @param velocity
         */
        void onProgress(long current, long total, long velocity);

        /**
         * 下载任务执行完成
         *
         * @param success 下载成功否
         */
        void onFinish(boolean success, String path);
    }

    public void destroy() {
        this.cancel(true);
    }
}
