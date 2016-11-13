package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.ArticleBefore;

/**
 * 加载过去文章事件监听
 */
public interface OnLoadBeforeArticleListener {

    void onSuccess(ArticleBefore articleBefore);

    void onFailure();
}
