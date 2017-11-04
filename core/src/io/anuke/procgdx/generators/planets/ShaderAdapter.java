package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.anuke.ucore.function.BiConsumer;

public class ShaderAdapter implements Shader{
	ShaderProgram program;
	BiConsumer<ShaderProgram, Renderable> renderer;
	
	public ShaderAdapter(String name, BiConsumer<ShaderProgram, Renderable> renderer){
		this.renderer = renderer;
		
		String vert = Gdx.files.internal("3dshaders/"+name+".vertex").readString();
		String frag = Gdx.files.internal("3dshaders/"+name+".fragment").readString();
		
		program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
	}

	@Override
	public void dispose(){
		program.dispose();
	}

	@Override
	public void init(){
		
	}

	@Override
	public int compareTo(Shader other){
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance){
		return true;
	}

	@Override
	public void begin(Camera camera, RenderContext context){
		program.begin();
    	program.setUniformMatrix("u_projViewTrans", camera.combined);
    	
    	context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render(Renderable renderable){
		program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
		renderer.accept(program, renderable);
		renderable.meshPart.render(program);
	}

	@Override
	public void end(){
		program.end();
	}

}
