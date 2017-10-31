package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.*;

import io.anuke.procgdx.Generator;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.scene.ui.layout.Table;

public class PlanetGenerator implements Generator{
	PerspectiveCamera cam;
	CameraInputController camController;
	Shader shader;
	Model model;
	RenderContext renderContext;
	Environment environment;
	Renderable renderable;

	public PlanetGenerator(){
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 1000f;
		cam.update();

		camController = new CameraInputController(cam);
		Inputs.addProcessor(camController);

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createSphere(2f, 2f, 2f, 100, 100, new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		NodePart blockPart = model.nodes.get(0).parts.get(0);

		renderable = new Renderable();
		blockPart.setRenderable(renderable);
		renderable.environment = null;
		renderable.worldTransform.idt();
		//renderable.meshPart.primitiveType = GL20.GL_LINES;
		renderable.meshPart.update();

		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
	    shader = new PlanetShader("planet");
		shader.init();
	}

	public void update(){
		camController.update();

		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		

		renderContext.begin();
		shader.begin(cam, renderContext);
		shader.render(renderable);
		shader.end();
		renderContext.end();
		    
		
		Timers.update();
	}
	
	@Override
	public void resize(int width, int height){
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
	}

	@Override
	public void build(Table table){
		table.addRect((x, y, width, height)->{
			
			Gdx.graphics.requestRendering();
			Core.batch.end();
			update();
			Core.batch.begin();
		}).grow();
	}

	@Override
	public void dispose(Table table){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//shader.dispose();
		//model.dispose();
	}
}
