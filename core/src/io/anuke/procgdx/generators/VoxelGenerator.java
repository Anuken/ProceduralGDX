package io.anuke.procgdx.generators;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import io.anuke.procgdx.g3d.VoxelBuilder;

public abstract class VoxelGenerator extends ModelGenerator{
	int[][][] voxels;
	
	public abstract void generate();
	
	@Override
	public void update(){
		if(model != null){
			model.dispose();
			model = null;
		}
		
		voxels = new int[size][size][size];
		generate();
		
		model = VoxelBuilder.createVoxelModel(voxels);
		instance = new ModelInstance(model);
		
		cam.position.set(size*camscale, size*camscale, size*camscale);
		cam.lookAt(size/2, size/2, size/2);
		cam.near = 1f;
        cam.far = 300f;
        cam.update();
	}
}
