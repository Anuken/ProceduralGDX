package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Color;

public class LavaPlanet extends Planet{

	public LavaPlanet() {
		super(true, 2.6f, 3f, 120, 50, new Color[]{
			Color.valueOf("f6b072"),
			Color.valueOf("f68872"),
			Color.valueOf("b88273"),
			Color.valueOf("646464"),
			Color.valueOf("888585"),
			Color.valueOf("9a9196"),
			Color.valueOf("baa4b4"),
		});
		
		cloudColor = Color.valueOf("5a5a5a");
		cloudColor.a = 0.8f;
		
		spread = 1.6f;
		falloff = 0.5f;
		octaves = 6;
		scale = 1.7f;
		magnitude = 0.6f;
		
		falloffClouds = 0.7f;
		thresholdClouds = 0.56f;
		speed = 2000f;
	}

}
