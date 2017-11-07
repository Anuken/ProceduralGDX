package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Color;

public class Sun extends Planet{
	
	public Sun(){
		super(false, 7f, 0f, 140, 0, new Color[]{
			Color.valueOf("ff7a38"),
			Color.valueOf("ff9638"),
			Color.valueOf("ffc64c"),
			Color.valueOf("ffc64c"),
			Color.valueOf("ffe371"),
			Color.valueOf("f4ee8e"),
		});
		
		scale = 1.3f;
		speed = 700f;
		falloff = 0.3f;
		octaves = 4;
		spread = 1.2f;
		magnitude = 0f;
	}
}
