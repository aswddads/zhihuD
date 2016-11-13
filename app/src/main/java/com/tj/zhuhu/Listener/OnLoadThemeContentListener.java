package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.ArticleTheme;

/**
 * 加载特定主题下文章列表事件监听
 */
public interface OnLoadThemeContentListener {

    void onSuccess(ArticleTheme articleTheme);

    void onFailure();
}
