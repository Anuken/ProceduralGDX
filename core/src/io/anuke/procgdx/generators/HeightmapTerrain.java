package io.anuke.procgdx.generators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.procgdx.g3d.HeightmapBuilder;
import io.anuke.ucore.noise.Simplex;
import io.anuke.ucore.util.Mathf;

public class HeightmapTerrain extends ModelGenerator{
	Color[][] colors = {
		{Color.ROYAL, Color.ROYAL, Color.DARK_GRAY, Color.FOREST, Color.FOREST, Color.OLIVE, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.DARK_GRAY, Color.FOREST, Color.OLIVE, Color.OLIVE, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.BROWN, Color.TAN, Color.FOREST, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.TAN, Color.TAN, Color.TAN, Color.BROWN, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
	};
		
	int eloct = 5, tempoct = 7;
	float elpers = 0.5f, temppers = 0.6f;
	float scale = 0.8f;
	float elscale = 120f * scale, tempscale = 250f * scale;
	boolean island = false;
	Simplex sim = new Simplex();
	
	float[][] height;
	int[][] heightcolors;
	
	{
		size = 200;
	}

	@Override
	public void update(){
		if(model != null){
			model.dispose();
			model = null;
		}
		
		heightcolors = new int[size][size];
		height = new float[size][size];
		generate();
		
		model = HeightmapBuilder.createHeightmapModel(height, heightcolors, 70f, -20f);
		//TODO
		instance = new ModelInstance(model);
		
		cam.position.set(size*camscale, size*camscale, size*camscale);
		cam.lookAt(size/2, size/2, size/2);
        cam.update();
	}
	
	public void generate(){
		sim.setSeed(MathUtils.random(9999999999L));
		
		for(int x = 0; x < size; x ++){
			for(int z = 0; z < size; z ++){
				float elev = (float)Math.pow((1+sim.octaveNoise2d(eloct, elpers, 1f/elscale, x, z))/2f, 1.3f);
				float temp = (float)(1+sim.octaveNoise2d(tempoct, temppers, 1f/tempscale, x + 40, z + 40))/2f;
				
				float elevOffset = elev*0.5f 
						+ (float)(1+sim.octaveNoise2d(eloct+2, elpers+0.01f, 1f/(elscale+15f), x, z))/4f;
				
				if(island){
					elev = elev * Mathf.clamp(1f - (Vector2.dst(size/2, size/2, x, z) / (size/2)), 0, 0.999f);
				}
					
				Color color = colors[(int)(colors.length*temp)][(int)(colors[0].length*elevOffset)];
				
				height[x][z] = elev;
				heightcolors[x][z] = Color.rgba8888(color);
			}
		}
	}
}
