package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

import io.anuke.ucore.core.Timers;

public class CloudShader extends PlanetShader{

	public CloudShader(String name) {
		super(name);
	}
	
	@Override
    public void begin (Camera camera, RenderContext context) {  
    	this.camera = camera;
    	this.context = context;
    	
    	program.begin();
    	program.setUniformMatrix("u_projViewTrans", camera.combined);
    	
    	context.setDepthTest(GL20.GL_LESS);
        context.setCullFace(GL20.GL_BACK);
        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
	
	@Override
    public void render (Renderable renderable) {  
    	program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    	program.setUniformf("u_time", Timers.time() / 700f);
    	renderable.meshPart.render(program);
    }

}
