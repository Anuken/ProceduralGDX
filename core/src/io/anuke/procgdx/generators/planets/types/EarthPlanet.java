package io.anuke.procgdx.generators.planets.types;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.graphics.Hue;

public class EarthPlanet extends Planet{

	public EarthPlanet() {
		super(true, 2.5f, 3.1f, 130, 50, new Color[]{
			Hue.rgb(58, 94, 198), //water
			Hue.rgb(234, 211, 184), //sand
			Hue.rgb(80, 160, 74), //grass
			Hue.rgb(154, 191, 147), //more grass
			Hue.lightness(200 / 256f), //gray stuff
			Hue.rgb(255, 255, 255)  //snow
		});
	}

}
