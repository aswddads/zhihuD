package com.tj.zhuhu.Listener;


import com.tj.zhuhu.Bean.Others;

import java.util.List;

/**
 * 加载文章主题列表事件监听
 */
public interface OnLoadThemesListener {

    void onSuccess(List<Others> othersList);

    void onFailure();
}
