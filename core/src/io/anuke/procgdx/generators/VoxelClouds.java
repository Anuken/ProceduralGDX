package io.anuke.procgdx.generators;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import io.anuke.procgdx.g3d.VoxelBuilder;
import io.anuke.ucore.noise.Simplex;

public class VoxelClouds extends VoxelGenerator{
	Simplex plex = new Simplex();
	float scale = 60f;
	
	{
		size = 100;
	}
	
	@Override
	public void generate(){
		VoxelBuilder.splitTriangles = true;
		
		plex.setSeed(MathUtils.random(99999999));
		
		forEach((x, y, z)->{
			float noise = (float)(plex.octave_noise_3d(4f, 0.5f, 1f/scale, x, y*3f, z) + 1f) / 2f - 
					Vector3.dst(x, y, z, size/2, size/2, size/2) / (size * 2);
			
			if(noise > 0.6f){
				
				voxels[x][y][z] = Integer.MAX_VALUE;
			}
		});
	}

}
