package com.utouu.module.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.utouu.module.login.R;
import com.utouu.module.login.bean.AccountNumberBean;

import java.util.List;


/**
 * 账号列表
 * Created by yang on 15/12/17.
 */
public class AccountNumberAdapter extends BaseAdapter {

    private List<AccountNumberBean> accountNumberBeans;

    private ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    public AccountNumberAdapter(List<AccountNumberBean> accountNumberBeans) {
        this.accountNumberBeans = accountNumberBeans;
    }

    @Override
    public int getCount() {
        return null != accountNumberBeans ? accountNumberBeans.size() : 0;
    }

    @Override
    public AccountNumberBean getItem(int position) {
        return (null != accountNumberBeans && accountNumberBeans.size() > position) ? accountNumberBeans.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder itemViewHolder;
        if (null != convertView && null != convertView.getTag() && convertView.getTag() instanceof ViewHolder) {
            itemViewHolder = (ViewHolder)convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_number, null);

            itemViewHolder = new ViewHolder();
            itemViewHolder.textView1 = (TextView)convertView.findViewById(R.id.textView1);
            itemViewHolder.mobileClearImageView = (ImageView)convertView.findViewById(R.id.mobile_clear_imageView);
            convertView.setTag(itemViewHolder);
        }

        final AccountNumberBean itemBean = getItem(position);
        if (itemBean != null) {
            itemViewHolder.textView1.setText(itemBean.loginName);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemCallback != null) {
                        itemCallback.clickItem(itemBean);
                    }
                }
            });
            itemViewHolder.mobileClearImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountNumberBeans.remove(position);
                    notifyDataSetChanged();
                    if (itemCallback != null) {
                        itemCallback.clearItem(itemBean);
                    }
                }
            });
        }
        return convertView;
    }

    private static class ViewHolder {

        public TextView textView1;
        public ImageView mobileClearImageView;
    }

    public interface ItemCallback {

        void clickItem(AccountNumberBean accountNumberBean);

        void clearItem(AccountNumberBean accountNumberBean);
    }
}
