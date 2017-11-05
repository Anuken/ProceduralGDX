package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.graphics.Hue;

public class Asteroid extends Planet{

	public Asteroid() {
		super(1f, 60, new Color[]{
			//Hue.mix(Hue.lightness(0.3f), Color.BROWN, 0.0f),
			Hue.mix(Hue.lightness(0.4f), Color.BROWN, 0.0f),
			Hue.mix(Hue.lightness(0.5f), Color.BROWN, 0.1f),
			Hue.mix(Hue.lightness(0.6f), Color.BROWN, 0.2f),
			Hue.mix(Hue.lightness(0.7f), Color.BROWN, 0.3f),
			Hue.mix(Hue.lightness(0.8f), Color.BROWN, 0.4f),
			Hue.mix(Hue.lightness(0.9f), Color.BROWN, 0.5f),
		});
		
		magnitude = 1.5f;
		power = 1f;
	}

}
