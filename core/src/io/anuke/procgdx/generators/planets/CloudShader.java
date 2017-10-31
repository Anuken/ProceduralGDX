package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.g3d.Renderable;

import io.anuke.ucore.core.Timers;

public class CloudShader extends PlanetShader{

	public CloudShader(String name) {
		super(name);
	}
	
	@Override
    public void render (Renderable renderable) {  
    	program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    	program.setUniformf("u_time", Timers.time() / 700f);
    	renderable.meshPart.render(program);
    }

}
