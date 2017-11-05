package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;

import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Mathf;

public class Planet implements Disposable, RenderObject{
	ShaderAdapter planetShader, cloudShader;
	Renderable planet, clouds;
	
	int octaves = 5;
	float falloff = 0.5f;
	float scale = 1f;
	float power = 1.3f;
	float magnitude = 0.6f;
	
	int octavesClouds = 5;
	float falloffClouds = 0.5f;
	float scaleClouds = 1.5f;
	float thresholdClouds = 0.6f;
	float speedClouds = 700f;
	
	float seed = Mathf.random(9999f);
	
	Color[] colors;
	float[] colorValues;
	
	final boolean hasClouds;
	Color cloudColor = Color.WHITE;
	
	/**Creates a planet with no clouds.*/
	public Planet(float planetSize, int planetDivis, Color[] colors){
		this(false, planetSize, 0f, planetDivis, 0, colors);
	}
	
	public Planet(boolean hasClouds, float planetSize, float cloudSize, int planetDivis, int cloudDivis, 
			Color[] colors){
		
		this.hasClouds = hasClouds;
		this.colors = colors;
		
		colorValues = new float[colors.length*3];
		
		for(int i = 0; i < colors.length; i ++){
			colorValues[i*3 + 0] = colors[i].r; 
			colorValues[i*3 + 1] = colors[i].g; 
			colorValues[i*3 + 2] = colors[i].b; 
		}
		
		planetShader = new ShaderAdapter("planet", (shader, renderable)->{
			shader.setUniformi("u_octaves", octaves);
			shader.setUniformf("u_falloff", falloff);
			shader.setUniformf("u_scale", scale);
			shader.setUniformf("u_power", power);
			shader.setUniformf("u_magnitude", magnitude);
			shader.setUniformf("u_time", 0f);
			shader.setUniformf("u_seed", seed);
			
			shader.setUniformi("u_colornum", colors.length);
			shader.setUniform3fv("u_colors[0]", colorValues, 0, colorValues.length);
		});
		
		planet = PlanetCreator.genRenderable(PlanetCreator.modelBuilder.createSphere(planetSize, planetSize, planetSize, 
				planetDivis, planetDivis, new Material(), Usage.Position));
		
		if(hasClouds){
			cloudShader = new ShaderAdapter("cloud", (shader, renderable)->{
				shader.setUniformf("u_time", Timers.time() / speedClouds);
				shader.setUniformi("u_octaves", octavesClouds);
				shader.setUniformf("u_falloff", falloffClouds);
				shader.setUniformf("u_scale", scaleClouds);
				shader.setUniformf("u_threshold", thresholdClouds);
				shader.setUniformf("u_seed", seed);
				shader.setUniformf("u_color", cloudColor);
			});
			
			clouds = PlanetCreator.genRenderable(PlanetCreator.modelBuilder.createSphere(cloudSize, cloudSize, cloudSize, 
					cloudDivis, cloudDivis, new Material(), Usage.Position));
		}
		
	}
	
	public void setPosition(float x, float y, float z){
		planet.worldTransform.setToTranslation(x, y, z);
		if(hasClouds)
			clouds.worldTransform.setToTranslation(x, y, z);
	}
	
	public void render(Camera cam, RenderContext renderContext){
		planetShader.begin(cam, renderContext);
		planetShader.render(planet);
		planetShader.end();
		
		if(hasClouds){
			cloudShader.begin(cam, renderContext);
			cloudShader.render(clouds);
			cloudShader.end();
		}
	}
	
	@Override
	public void dispose(){
		planetShader.dispose();
		planet.meshPart.mesh.dispose();
		
		if(hasClouds){
			cloudShader.dispose();
			clouds.meshPart.mesh.dispose();
		}
	}
}
