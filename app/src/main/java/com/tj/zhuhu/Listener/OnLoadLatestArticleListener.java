package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.ArticleLatest;

/**
 * 加载最新文章事件监听
 */
public interface OnLoadLatestArticleListener {

    void onSuccess(ArticleLatest articleLatest);

    void onFailure();
}
