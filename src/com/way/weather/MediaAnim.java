package com.way.weather;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MediaAnim implements MediaPlayer.OnPreparedListener,
		SurfaceHolder.Callback {
	private static final String TAG = "MediaAnim";
	private String mAudioFileName;
	private MediaPlayer mAudioPlayer;
	private Context mContext;
	private boolean mIsStoped = false;
	private SurfaceHolder mSurfaceHolder;
	private SurfaceView mSurfaceView;
	private String mVideoFileName;
	private MediaPlayer mVideoPlayer;
	private int isEffectMute = 0;

	public MediaAnim(Context context, SurfaceView surfaceView) {
		Log.d(TAG, "MediaAnim: Constructor");
		mContext = context;
		mSurfaceView = surfaceView;
		mSurfaceHolder = mSurfaceView.getHolder();
		// mSurfaceHolder.setType(3);
		mSurfaceHolder.addCallback(this);
	}

	private void initAudioPlayer() {
		if (mAudioPlayer == null) {
			mAudioPlayer = new MediaPlayer();
		}
		mAudioPlayer.setLooping(true);
		mAudioPlayer.setOnPreparedListener(this);
		mAudioPlayer.reset();
	}

	private void initVideoPlayer() {
		if (mVideoPlayer == null) {
			mVideoPlayer = new MediaPlayer();
		}
		mVideoPlayer.setLooping(true);
		mVideoPlayer.setVolume(0.0F, 0.0F);
		mVideoPlayer.setOnPreparedListener(this);
		mVideoPlayer.reset();
	}

	private void prepareAudio() {
		try {
			if ((mAudioPlayer != null) && (isEffectMute != 0)) {
				if (mAudioFileName == null) {
					return;
				}
				mAudioPlayer.reset();
				mAudioPlayer.setLooping(true);
				AssetFileDescriptor assetFileDescriptor = mContext.getAssets()
						.openFd(mAudioFileName);
				if (assetFileDescriptor != null) {
					mAudioPlayer.setDataSource(
							assetFileDescriptor.getFileDescriptor(),
							assetFileDescriptor.getStartOffset(),
							assetFileDescriptor.getLength());
					assetFileDescriptor.close();
					mAudioPlayer.prepare();
				}
			}
		} catch (IOException e) {
			Log.e(TAG,
					"[prepareAudio] Unable to open file; error: "
							+ e.getMessage(), e);
			releaseAudioPlayer();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"[prepareAudio] Audio player is in illegal state; error: "
							+ e.getMessage(), e);
			releaseAudioPlayer();
		}
	}

	private void prepareVideo() {
		try {
			if (mVideoPlayer != null) {
				if (mVideoFileName == null) {
					return;
				}
				mVideoPlayer.reset();
				mVideoPlayer.setLooping(true);
				mVideoPlayer.setVolume(0.0F, 0.0F);
				AssetFileDescriptor assetFileDescriptor = mContext.getAssets()
						.openFd(mVideoFileName);
				if (assetFileDescriptor != null) {
					mVideoPlayer.setDataSource(
							assetFileDescriptor.getFileDescriptor(),
							assetFileDescriptor.getStartOffset(),
							assetFileDescriptor.getLength());
					assetFileDescriptor.close();
					mVideoPlayer.prepare();
				}
			}
		} catch (IOException e) {
			Log.e(TAG,
					"[prepareVideo] Unable to open file; error: "
							+ e.getMessage(), e);
			releaseVideoPlayer();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"[prepareVideo] Video player is in illegal state; error: "
							+ e.getMessage(), e);
			releaseVideoPlayer();
		}
	}

	private void releaseAudioPlayer() {
		if (mAudioPlayer != null) {
			mAudioPlayer.release();
			mAudioPlayer = null;
		}
	}

	private void releaseVideoPlayer() {
		if (mVideoPlayer != null) {
			mVideoPlayer.release();
			mVideoPlayer = null;
		}
	}

	public void destroy() {
		Log.d(TAG, "destroy()");
		release();
		if (mSurfaceHolder != null) {
			mSurfaceHolder.removeCallback(this);
			mSurfaceHolder.getSurface().release();
		}
		mSurfaceHolder = null;
		mSurfaceView = null;
	}

	public void onPrepared(MediaPlayer paramMediaPlayer) {
		Log.d(TAG, "onPrepared ");
		if (!mIsStoped) {
			paramMediaPlayer.start();
		}
	}

	public void play() {
		Log.d(TAG, "play(), mIsStoped = " + mIsStoped);
		prepareVideo();
		prepareAudio();
	}

	public void release() {
		releaseVideoPlayer();
		releaseAudioPlayer();
	}

	public void restart() {
		Log.d(TAG, "restart()");
		if (mVideoPlayer != null) {
			mVideoPlayer.start();
		}
		if (mAudioPlayer != null) {
			mAudioPlayer.start();
		}
	}

	public void resume() {
		Log.d(TAG, "resume()");
		mIsStoped = false;
	}

	public void setAudioFileName(String audioFileName) {
		mAudioFileName = audioFileName;
	}

	public void setEffectMute(int isMute) {
		isEffectMute = isMute;
	}

	public void setVideoFileName(String videoFileName) {
		mVideoFileName = videoFileName;
	}

	public void stop() {
		Log.d(TAG, "stop()");
		mIsStoped = true;
		if (mVideoPlayer != null) {
			mVideoPlayer.stop();
		}
		if (mAudioPlayer != null) {
			mAudioPlayer.stop();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		mSurfaceHolder = holder;
		initVideoPlayer();
		initAudioPlayer();
		mVideoPlayer.setDisplay(this.mSurfaceHolder);
		play();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed called");
	}
}
