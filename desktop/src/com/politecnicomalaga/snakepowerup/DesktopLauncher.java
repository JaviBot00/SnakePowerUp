package com.politecnicomalaga.snakepowerup;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.politecnicomalaga.snakepowerup.manager.SettingsManager;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Snake");
		config.setWindowedMode(SettingsManager.SCREEN_WIDTH, SettingsManager.SCREEN_HEIGHT);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new GdxSnakePowerUp(), config);
	}
}
