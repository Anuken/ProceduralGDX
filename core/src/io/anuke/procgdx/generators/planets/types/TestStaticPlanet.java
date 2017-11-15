package io.anuke.procgdx.generators.planets.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

import io.anuke.procgdx.generators.planets.StaticPlanet;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Tmp;

public class TestStaticPlanet extends StaticPlanet{
	static Pixmap pix = new Pixmap(Gdx.files.internal("sprites/colors.png"));;
	static int waterLevel = 5;
	static float water = waterLevel / (float)(pix.getHeight());
	
	static Color[] colors = new Color[]{
		Hue.rgb(58, 94, 198), //water
		Hue.rgb(58, 94, 198), //water
		Hue.rgb(234, 211, 184), //sand
		Hue.rgb(80, 160, 74), //grass
		Hue.rgb(80, 160, 74), //grass
		Hue.rgb(154, 191, 147), //more grass
		Hue.rgb(154, 191, 147), //more grass
		Hue.lightness(200 / 256f), //gray stuff
		Hue.lightness(200 / 256f), //gray stuff
		Hue.rgb(255, 255, 255),  //snow
		Hue.rgb(255, 255, 255)  //snow
	};

	public TestStaticPlanet() {
		super(11f, 1200, 0.3f);
	}

	@Override
	public float getHeight(Vector3 position){
		float height = Mathf.pow((float)noise.octaveNoise3D(7, 0.48f, 1f/3f, position.x, position.y, position.z), 2.4f);
		if(height <= water){
			return water;
		}
		return height;
	}
	
	@Override
	public float getModHeight(Vector3 position){
		float height = getHeight(position);
		if(height <= water){
			return 0f;
		}
		return (float)noise.octaveNoise3D(3, 0.6f, 3.5f, position.x, position.y, position.z);
	}

	@Override
	public Color getColor(Vector3 position, float height){
		float rad = 11f;
		float temp = Mathf.clamp(Math.abs(position.y * 2f) / (rad));
		float tnoise = (float)noise.octaveNoise3D(7, 0.48f, 1f/3f, position.x, position.y + 999f, position.z);
		temp = Mathf.lerp(temp, tnoise, 0.5f);
		height *= 1.2f;
		height = Mathf.clamp(height);
		
		return Tmp.c1.set(pix.getPixel((int)(temp * (pix.getWidth()-1)), 
				(int)((1f-height) * (pix.getHeight()-1))));//colors[(int)(height * colors.length)];
	}

}
