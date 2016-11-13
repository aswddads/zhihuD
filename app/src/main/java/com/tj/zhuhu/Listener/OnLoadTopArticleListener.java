package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.TopStories;

import java.util.List;

/**
 * 加载顶部Banner文章事件监听
 */
public interface OnLoadTopArticleListener {

    void onSuccess(List<TopStories> topStoriesList);

    void onFailure();
}
