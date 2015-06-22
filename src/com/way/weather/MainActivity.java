package com.way.weather;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.way.observablescrollview.ObservableListView;
import com.way.observablescrollview.ObservableScrollViewCallbacks;
import com.way.observablescrollview.ScrollState;
import com.way.observablescrollview.ScrollUtils;
import com.way.weather.plugin.bean.AQI;
import com.way.weather.plugin.bean.Alerts;
import com.way.weather.plugin.bean.Forecast;
import com.way.weather.plugin.bean.Index;
import com.way.weather.plugin.bean.RealTime;
import com.way.weather.plugin.spider.WeatherController;

public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks{
	private static final String[] AUDIO_LIST = { "sunny_day.mp3",
			"sunny_night.mp3", "cloudy_day.mp3", "mostly_cloudy_day.mp3",
			"mostly_cloudy_night.mp3", "rain_heavy.mp3", "rain_small.mp3",
			"snow.mp3", "dust_day.mp3", "fog_day.mp3", "thunder_day.mp3" };
	private static final String[] VIDEO_LIST = new String[] {
			"sunny_day_loop_1.mp4", "sunny_night_loop_1.mp4",
			"cloudy_day_loop_1.mp4", "mostly_cloudy_day_loop_1.mp4",
			"mostly_cloudy_night_loop_1.mp4", "rain_heavy_loop_1.mp4",
			"rain_small_loop_1.mp4", "snow_loop_1.mp4", "dust_day_loop_1.mp4",
			"fog_day_loop_1.mp4", "thunder_day_loop_1.mp4" };
	//private MediaAnim mMediaAnim;
	private TextureViewMediaAnim mMediaAnim;
	private RequestQueue mVolley;
	private static final String WEATHER_ALL = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=%s";
	   private View mHeaderView;
	    private View mToolbarView;
	    private ObservableListView mListView;
	    private int mBaseTranslationY;
	    public static ArrayList<String> getDummyData(int num) {
	        ArrayList<String> items = new ArrayList<>();
	        for (int i = 1; i <= num; i++) {
	            items.add("Item " + i);
	        }
	        return items;
	    }
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

	        mHeaderView = findViewById(R.id.header);
	        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
	        mToolbarView = findViewById(R.id.toolbar);
	        mListView = (ObservableListView) findViewById(R.id.list);
	        mListView.setScrollViewCallbacks(this);
	        LayoutInflater inflater = LayoutInflater.from(this);
	        mListView.addHeaderView(inflater.inflate(R.layout.padding, mListView, false)); // toolbar
	        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getDummyData(100)));
	        
		mVolley = Volley.newRequestQueue(this);
//		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.weather_background);
//		mMediaAnim = new MediaAnim(this, surfaceView);
		TextureView textureView = (TextureView) findViewById(R.id.weather_background);
		mMediaAnim = new TextureViewMediaAnim(this, textureView); 
		String data = "http://open.weather.com.cn/data/?areaid=101010100&type=forecast_v&date=201506190915&appid=ceaaa48d8046a956";
		String key = "904c35_SmartWeatherAPI_7d8a542";
		String baseKey = javademo.standardURLEncoder(data, key);
		String baseUrl = "http://open.weather.com.cn/data/?areaid=101010100&type=forecast_v&date=201506190915&appid=ceaaa4";
		//Log.i("way", "url = " + baseKey);
		StringRequest sr = new StringRequest(baseUrl + "&key="+baseKey, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.i("way", "response = " + response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.i("way", "error = " + error);
			}
		});
		//mVolley.add(sr);
		final String postID = "101280601";
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(String.format(WEATHER_ALL, postID),null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
//					String language = getResources().getConfiguration().locale
//							.toString();
					String language = Locale.TAIWAN.toString();
					
					Forecast forecast = WeatherController.convertToNewForecast(
							response, language, postID);
					//Log.i("way", "jsonObjectRequest forecast = " + forecast);
					
					RealTime realTime = WeatherController.convertToNewRealTime(
							response.getJSONObject("realtime"), language, postID);
					//Log.i("way", "realTime = " + realTime);
					
					Alerts alerts = WeatherController.convertToNewAlert(response.getJSONArray("alert"), postID);
					//Log.i("way", "alerts = " + alerts);
					
					Index index = WeatherController.convertToNewIndex(response,
							language, postID);
					//Log.i("way", "index = " + index);
					
					AQI aqi = WeatherController.convertToNewAQI(response.getJSONObject("aqi"), language, postID);
					Log.i("way", "aqi = " + aqi);
					//Log.i("way", "/njsonObjectRequest realtime = " + response.getJSONObject("realtime"));
					//Log.i("way", "/njsonObjectRequest alert = " + response.getJSONArray("alert"));
					//Log.i("way", "/njsonObjectRequest aqi = " + response.getJSONObject("aqi"));
					//Log.i("way", "jsonObjectRequest index = " + response.getJSONObject("index"));
					//Log.i("way", "/njsonObjectRequest accu_cc = " + response.getJSONObject("accu_cc"));
					//Log.i("way", "/njsonObjectRequest accu_f5 = " + response.getJSONObject("accu_f5"));
					//Log.i("way", "/njsonObjectRequest today = " + response.getJSONObject("today"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.i("way", "jsonObjectRequest error = " + error);
			}
		});
		mVolley.add(jsonObjectRequest);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMediaAnim != null)
			mMediaAnim.resume();
		if (this.mMediaAnim != null) {
			this.mMediaAnim.setVideoFileName(VIDEO_LIST[2]);
			this.mMediaAnim.setAudioFileName(AUDIO_LIST[2]);
			this.mMediaAnim.setEffectMute(1);
			this.mMediaAnim.play();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMediaAnim != null)
			mMediaAnim.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMediaAnim.destroy();
		mMediaAnim = null;
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging) {
	       if (dragging) {
	            int toolbarHeight = mToolbarView.getHeight();
	            if (firstScroll) {
	                float currentHeaderTranslationY = mHeaderView.getTranslationY();
	                if (-toolbarHeight < currentHeaderTranslationY) {
	                    mBaseTranslationY = scrollY;
	                }
	            }
	            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
	            mHeaderView.animate().cancel();
	            mHeaderView.setTranslationY(headerTranslationY);
	        }		
	}

	@Override
	public void onDownMotionEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
	    mBaseTranslationY = 0;

        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbarView.getHeight();
            int scrollY = mListView.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (!toolbarIsShown() && !toolbarIsHidden()) {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
	}
    private boolean toolbarIsShown() {
        return mHeaderView.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return mHeaderView.getTranslationY() == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = mHeaderView.getTranslationY();
        if (headerTranslationY != 0) {
        	mHeaderView.animate().cancel();
        	mHeaderView.animate().translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = mHeaderView.getTranslationY();
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
        	mHeaderView.animate().cancel();
        	mHeaderView.animate().translationY(-toolbarHeight).setDuration(200).start();
        }
    }
}
