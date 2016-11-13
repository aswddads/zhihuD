package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.ArticleContent;

/**
 * 加载文章内容事件监听
 */
public interface OnLoadArticleContentListener {

    void onSuccess(ArticleContent content);

    void onFailure();

}
