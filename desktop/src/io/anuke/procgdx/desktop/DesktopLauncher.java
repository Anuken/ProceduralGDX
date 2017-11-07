package io.anuke.procgdx.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.anuke.procgdx.ProceduralGDX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("ProceduralGDX");
		config.setMaximized(true);
		config.useVsync(false);
		new Lwjgl3Application(new ProceduralGDX(), config);
	}
}
