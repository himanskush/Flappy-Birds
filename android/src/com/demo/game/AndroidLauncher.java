package com.demo.game;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.demo.game.Flappy_Birds;

public class AndroidLauncher extends AndroidApplication {

	SharedPreferences sharedPreferences ;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Flappy_Birds(), config);
	}
}
