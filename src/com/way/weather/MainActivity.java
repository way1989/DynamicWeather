package com.way.weather;

import org.apache.http.protocol.ResponseConnControl;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
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
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mVolley = Volley.newRequestQueue(this);
//		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.weather_background);
//		mMediaAnim = new MediaAnim(this, surfaceView);
		TextureView textureView = (TextureView) findViewById(R.id.weather_background);
		mMediaAnim = new TextureViewMediaAnim(this, textureView); 
		String data = "http://open.weather.com.cn/data/?areaid=101010100&type=forecast_v&date=201506190915&appid=ceaaa48d8046a956";
		String key = "904c35_SmartWeatherAPI_7d8a542";
		String baseKey = javademo.standardURLEncoder(data, key);
		String baseUrl = "http://open.weather.com.cn/data/?areaid=101010100&type=forecast_v&date=201506190915&appid=ceaaa4";
		Log.i("way", "url = " + baseKey);
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
		mVolley.add(sr);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
