package com.tj.zhuhu.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tj.zhuhu.R;
import com.tj.zhuhu.View.Banner;


/**
 * 文章顶部Banner
 */
public class ArticleListTopHolder extends RecyclerView.ViewHolder {

    public Banner banner;

    public ArticleListTopHolder(View itemView) {
        super(itemView);
        banner = (Banner) itemView.findViewById(R.id.banner);
    }
}

