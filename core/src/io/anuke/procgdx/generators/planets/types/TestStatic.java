package io.anuke.procgdx.generators.planets.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.procgdx.generators.planets.StaticPlanet;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.util.Mathf;

public class TestStatic extends StaticPlanet{
	
	static Color[] colors = new Color[]{
		Hue.rgb(58, 94, 198), //water
		Hue.rgb(234, 211, 184), //sand
		Hue.rgb(80, 160, 74), //grass
		Hue.rgb(80, 160, 74), //grass
		Hue.rgb(154, 191, 147), //more grass
		Hue.rgb(154, 191, 147), //more grass
		Hue.lightness(200 / 256f), //gray stuff
		Hue.lightness(200 / 256f), //gray stuff
		Hue.rgb(255, 255, 255)  //snow
	};

	public TestStatic() {
		super(9f, 1000, 0.2f);
	}

	@Override
	public float getHeight(Vector3 position){
		return Mathf.pow((float)noise.octaveNoise3D(7, 0.4f, 1f/2.5f, position.x, position.y, position.z), 2f);
	}
	
	@Override
	public float getModHeight(Vector3 position){
		return (float)noise.octaveNoise3D(3, 0.6f, 3.5f, position.x, position.y, position.z);
	}

	@Override
	public Color getColor(Vector3 position, float height){
		return colors[(int)(height * colors.length)];
	}

}
