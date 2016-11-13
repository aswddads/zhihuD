package com.tj.zhuhu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tj.zhuhu.Bean.Gradle;
import com.tj.zhuhu.Fragment.BaseFragment;
import com.tj.zhuhu.Fragment.MainFragment;
import com.tj.zhuhu.Fragment.ThemeFragment;
import com.tj.zhuhu.Net.HttpUtil;
import com.tj.zhuhu.R;
import com.tj.zhuhu.Utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private String mMsg;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;
    //文章的发布日期
    private String date;
    //用来实现再按一次退出程序的效果
    private boolean isExit;
    private int currentId;
    public boolean isHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        currentId = -1;
        getTransition().add(R.id.fl_content, new MainFragment(), "Fragment" + currentId).commit();
        isHomepage = true;

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        new MyGradleAsyncTask().execute(Constant.URL2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //drawerContent = (LinearLayout) findViewById(R.id.drawerContent);
        // 设置Toolbar
        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        // 设置toolbar支持actionbar
        setSupportActionBar(toolbar);
        // 使用ActionBarDrawerToggle，配合DrawerLayout和ActionBar,以实现推荐的抽屉功能。
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,R.string.close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isHomepage) {
                    MainFragment mainFragment = (MainFragment) getFragmentByTag("Fragment" + currentId);
                    mainFragment.getLatestArticleList();
                } else {
                    ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
                    themeFragment.refreshData();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id. main_toolbar_shuffle) {
            Toast.makeText(MainActivity.this,"随机看",Toast.LENGTH_LONG).show();
            return true;
        }
        else if(id== R.id.main_toolbar_search){
            Toast.makeText(MainActivity.this,"查询",Toast.LENGTH_SHORT).show();
        }
        else if (id== R.id.main_toolbar_about){
            new MyGradleAsyncTask().execute(Constant.URL2);
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     @Override
     public void onClick(View v) {
     // 关闭DrawerLayout
     drawerLayout.closeDrawer(drawerContent);
     switch (v.getId()) {
     case R.id.tv1:
     Toast.makeText(MainViewActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
     break;
     case R.id.tv2:
     Toast.makeText(MainViewActivity.this, "离线下载", Toast.LENGTH_SHORT).show();
     break;
     //case R.id.gradle:
     //  showNormalDialog();
     //break;
     }
     }*/
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("版本信息");
        normalDialog.setMessage("已是最新版本！");
        normalDialog.setPositiveButton("我知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }




    private String readStream(InputStream in) {
        InputStreamReader inr;
        String result = "";
        try {
            String line="";
            inr=new InputStreamReader(in,"utf-8");
            BufferedReader br=new BufferedReader(inr);
            while((line=br.readLine())!=null){
                result+=line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private Gradle getJsonData(String url){
        Gradle gradle=new Gradle();
        try {
            String jsonString= readStream(new URL(url).openStream());
            JSONObject jsonObject;
            jsonObject=new JSONObject(jsonString);
            gradle.msg=jsonObject.getString("msg");
            gradle.latest=jsonObject.getString("latest");
            gradle.status=jsonObject.getString("status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gradle;
    }
    class MyGradleAsyncTask extends AsyncTask<String,Void,Gradle> {
        @Override
        protected Gradle doInBackground(String... params) {
            return getJsonData(params[0]);
        }
        @Override
        protected void onPostExecute(final Gradle gradle){
            super.onPostExecute(gradle);
            mMsg=gradle.msg;
            if(gradle.status=="0"){
                showNormalDialog();
            }
            else{
                showGradleNormalDialog();
            }
        }
    }

    private void showGradleNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("更新信息");
        normalDialog.setMessage(mMsg);
        normalDialog.setPositiveButton("更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"准备更新中...",Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.show();
    }






    //获取主页
    public void getHomepage() {
        MainFragment mainFragment = (MainFragment) getFragmentByTag("Fragment" + "-1");
        ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
        FragmentTransaction transition = getTransition();
        transition.hide(themeFragment);
        if (mainFragment == null) {
            transition.add(R.id.fl_content, new MainFragment(), "Fragment" + "-1").commit();
        } else {
            transition.show(mainFragment).commit();
        }
        currentId = -1;
        isHomepage = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
        }
    }

    //获取主题Fragment
    public void getThemeFragment(int id, Bundle bundle) {
        ThemeFragment toFragment = (ThemeFragment) getFragmentByTag("Fragment" + id);
        BaseFragment nowFragment;
        if (isHomepage) {
            nowFragment = (MainFragment) getFragmentByTag("Fragment" + currentId);
        } else {
            nowFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
        }
        FragmentTransaction transition = getTransition();
        transition.hide(nowFragment);
        if (toFragment == null) {
            ThemeFragment themeFragment = new ThemeFragment();
            themeFragment.setArguments(bundle);
            transition.add(R.id.fl_content, themeFragment, "Fragment" + id).commit();
        } else {
            transition.show(toFragment).commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bundle.getString("Title"));
            }
        }
        currentId = id;
        isHomepage = false;
        setRefresh(false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawerLayout();
            return;
        }
        if (!isHomepage) {
            getHomepage();
            return;
        }
        if (isExit) {
            refreshLayout.setRefreshing(false);
            HttpUtil.client.cancelAllRequests(true);
            super.onBackPressed();
        } else {
            hint();
        }
    }

    private void hint() {
        Snackbar snackbar = Snackbar.make(refreshLayout, "再按一次退出", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#0099CC"));
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                isExit = false;
            }

            @Override
            public void onShown(Snackbar snackbar) {
                isExit = true;
            }
        }).show();
    }

    private FragmentTransaction getTransition() {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        return transition;
    }

    public Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public int getCurrentId() {
        return currentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRefresh(boolean flag) {
        refreshLayout.setRefreshing(flag);
    }

    public void closeDrawerLayout() {
        this.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void joke(View view) {
        Snackbar snackbar = Snackbar.make(refreshLayout, "其实并没有该功能，只是为了占个地方。。。", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#0099CC"));
        snackbar.show();
    }

}
