package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PlanetShader implements Shader {
	ShaderProgram program;
	Camera camera;
    RenderContext context;
    String vert;
    String frag;
    boolean shift = false;
    
    public PlanetShader(String name){
    	vert = Gdx.files.internal("3dshaders/"+name+".vertex").readString();
	    frag = Gdx.files.internal("3dshaders/"+name+".fragment").readString();
    }
	
    @Override
    public void init () {
	    program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
    }
    
    @Override
    public void dispose () {
    	program.dispose();
    }
    
    @Override
    public void begin (Camera camera, RenderContext context) {  
    	this.camera = camera;
    	this.context = context;
    	
    	program.begin();
    	program.setUniformMatrix("u_projViewTrans", camera.combined);
    	
    	context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    @Override
    public void render (Renderable renderable) {  
    	program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    	program.setUniformf("u_center", renderable.meshPart.center);
    	//program.setUniformf("u_time", shift ? Timers.time() : 0f);
    	renderable.meshPart.render(program);
    }
    
    @Override
    public void end () {    
    	program.end();
    }
    
    @Override
    public int compareTo (Shader other) {
        return 0;
    }
    
    @Override
    public boolean canRender (Renderable instance) {
        return true;
    }
}
