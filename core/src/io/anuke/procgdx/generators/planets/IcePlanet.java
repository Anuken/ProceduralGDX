package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Color;

public class IcePlanet extends Planet{

	public IcePlanet(){
		super(true, 2.5f, 3.0f, 120, 40, new Color[]{
			Color.valueOf("6b6e76"),
			Color.valueOf("878ea2"),
			Color.valueOf("97a5bc"),
			Color.valueOf("aac5e1"),
			Color.valueOf("bcdaef"),
			Color.valueOf("ddf4fb"),
		});
		
		cloudColor = Color.valueOf("a5d4f1");
		cloudColor.a = 0.8f;
		
		magnitude = 0.5f;
		spread = 1.5f;
		power = 1f;
		falloff = 0.4f;
	}
}
