package io.anuke.procgdx.generators.planets.types;

import com.badlogic.gdx.graphics.Color;

import io.anuke.procgdx.generators.planets.ShaderAdapter;
import io.anuke.ucore.core.Timers;

public class GasGiant extends Planet{
	static String alpha = "ff";
	float compression = 6f;
	
	public GasGiant() {
		super(true, 4f, 4.15f, 120, 50, new Color[]{
			Color.valueOf("ffbd54"+alpha),
			Color.valueOf("f4c882"+alpha),
			Color.valueOf("f4a582"+alpha),
			Color.valueOf("ecbaa4"+alpha),
			Color.valueOf("ef8e84"+alpha),
			Color.valueOf("eae8a9"+alpha)
		});
		
		octavesClouds = 4;
		cloudColor = Color.valueOf("fffa6c");
		cloudColor.a = 0.2f;
		scaleClouds = 1f*2f;
		falloffClouds = 0.5f;
		thresholdClouds = 0.5f;
		
		octaves = 3;
		scale = 1.4f*2f;
		power = 1f;
		magnitude = 0f;
		
		planetShader = new ShaderAdapter("gasgiant", (shader, renderable)->{
			shader.setUniformi("u_octaves", octaves);
			shader.setUniformf("u_falloff", falloff);
			shader.setUniformf("u_scale", scale);
			shader.setUniformf("u_power", power);
			shader.setUniformf("u_compression", compression);
			shader.setUniformf("u_magnitude", magnitude);
			shader.setUniformf("u_time", Timers.time() / 800f);
			shader.setUniformf("u_seed", seed);
			
			shader.setUniformi("u_colornum", colors.length);
			shader.setUniform4fv("u_colors[0]", colorValues, 0, colorValues.length);
		});
	}

}
