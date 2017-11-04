package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class Planet{
	PlanetShader planetShader;
	CloudShader cloudShader;
	Renderable planet, clouds;
	
	int planetDivis = 100;
	int cloudDivis = 90;
	
	float planetSize = 2f;
	float cloudSize = 2f;
	
	int octaves = 5;
	float falloff = 0.5f;
	float scale = 1.5f;
	float power = 1.3f;
	float magnitude = 0.6f;
	
	Color[] colors;
	boolean hasClouds = true;
	
	public Planet(){
		planetShader = new PlanetShader("planet");
		cloudShader = new CloudShader("cloud");
	}
	
	public void render(Camera cam, RenderContext renderContext){
		planetShader.begin(cam, renderContext);
		planetShader.render(planet);
		planetShader.end();
		
		cloudShader.begin(cam, renderContext);
		cloudShader.render(clouds);
		cloudShader.end();
	}
}
