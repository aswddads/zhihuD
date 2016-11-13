package com.tj.zhuhu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj.zhuhu.Bean.Guide;
import com.tj.zhuhu.ImageLoder;
import com.tj.zhuhu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static com.tj.zhuhu.Utility.Constant.URL1;

/**
 * Created by Jun on 16/11/12.
 */

public class WelcomeActivity extends Activity {
    private TextView mGuidetext;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.layout_main);
        new MyAsyncTask().execute(URL1);
        final Intent it = new Intent(WelcomeActivity.this, MainActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();
            }
        };
        timer.schedule(task, 4000);
    }

    private String readStream(InputStream in) {

        InputStreamReader inr;
        String result = "";
        try {
            String line = "";
            inr = new InputStreamReader(in, "utf-8");
            BufferedReader br = new BufferedReader(inr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Guide getJsonData(String url) {
        Guide guide = new Guide();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            jsonObject = new JSONObject(jsonString);
            guide.mImages = jsonObject.getString("img");
            guide.mText = jsonObject.getString("text");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guide;
    }

    class MyAsyncTask extends AsyncTask<String, Void, Guide> {
        @Override
        protected Guide doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(Guide guide) {
            super.onPostExecute(guide);
            setContentView(R.layout.guide_start);
            mGuidetext = (TextView) findViewById(R.id.guide_text);
            mImageView = (ImageView) findViewById(R.id.guide_view);
            //第一个参数用于显示在页面的imageView,第二个参数用于传进去的解析出来的image的参数
            new ImageLoder().showImageByThread(mImageView, guide.mImages);
            mGuidetext.setText(guide.mText);
        }
    }


}
