package com.piterwilson.mp3streamplayer;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.piterwilson.audio.MP3RadioStreamDelegate;
import com.piterwilson.audio.MP3RadioStreamPlayer;

public class MainActivity extends Activity implements MP3RadioStreamDelegate {
	
	private Button mPlayButton;
	private Button mStopButton;
	private ProgressBar mProgressBar;
	MP3RadioStreamPlayer player;
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPlayButton = (Button) this.findViewById(R.id.button1);
		mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				play();
			}
		});
		mStopButton = (Button) this.findViewById(R.id.button2);
		mStopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stop();
			}
		});
		mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar1);
		
		showGUIStopped();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void play()
	{
		if(player != null)
		{
			player.stop();
			player.release();
			player = null;
			
		}
		
		player = new MP3RadioStreamPlayer();
		player.setUrlString("http://icecast.omroep.nl:80/radio1-sb-mp3");
		player.setDelegate(this);
		
		showGUIBuffering();
		
		try {
			player.play();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void stop()
	{
		player.stop();
	}
	
	private void showGUIBuffering()
	{
		mProgressBar.setVisibility(View.VISIBLE);
		mPlayButton.setEnabled(false);
		mStopButton.setEnabled(false);
	}
	
	private void showGUIPlaying()
	{
		mProgressBar.setVisibility(View.GONE);
		mPlayButton.setEnabled(false);
		mStopButton.setEnabled(true);
	}
	
	private void showGUIStopped()
	{
		mProgressBar.setVisibility(View.GONE);
		mPlayButton.setEnabled(true);
		mStopButton.setEnabled(false);
	}
	
	
	/****************************************
	*
	*	Delegate methods. These are all fired from a background thread so we have to call any GUI code on the main thread.
	*
	****************************************/
	
	@Override
	public void onRadioPlayerPlaybackStarted(MP3RadioStreamPlayer player) {
		Log.i(TAG, "onRadioPlayerPlaybackStarted");;
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				showGUIPlaying();
			}
		});
	}

	@Override
	public void onRadioPlayerStopped(MP3RadioStreamPlayer player) {
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				showGUIStopped();
			}
		});
		
	}

	@Override
	public void onRadioPlayerError(MP3RadioStreamPlayer player) {
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				showGUIStopped();
			}
		});
		
	}

	@Override
	public void onRadioPlayerBuffering(MP3RadioStreamPlayer player) {
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				showGUIBuffering();
			}
		});
		
	}
	
}
