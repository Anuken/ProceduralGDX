package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.utils.ShaderLoader;

import io.anuke.procgdx.Generator;
import io.anuke.procgdx.generators.planets.types.TestStatic;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.scene.ui.layout.Table;

public class StaticPlanetGenerator implements Generator{
	PerspectiveCamera cam;
	CameraInputController camController;
	Environment environment;
	ModelBatch batch;
	
	PostProcessor post;
	boolean postProcess = true;
	
	Array<RenderableProvider> renderables = new Array<>();

	public StaticPlanetGenerator(){
		batch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 200f;
		cam.update();
		
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		camController = new CameraInputController(cam);
		Inputs.addProcessor(camController);
		
		ShaderLoader.BasePath = "shaders/";
		ShaderLoader.Pedantic = false;
		
		addEffects();
	}

	void update(){
		camController.update();
		
		batch.begin(cam);
		for(RenderableProvider r : renderables){
			batch.render(r, environment);
		}
		batch.end();
		    
		Timers.update();
	}
	
	void addEffects(){
		post = new PostProcessor(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, true, true);
		
		Bloom bloom = new Bloom(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
		bloom.setThreshold(0.8f);
		bloom.setBlurPasses(5);
		bloom.setBaseSaturation(1f);
		post.addEffect(bloom);
	}
	
	void addPlanets(){
		renderables.add(new TestStatic());
	}
	
	@Override
	public void resize(int width, int height){
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update(true);
		
		post.dispose();
		addEffects();
	}

	@Override
	public void build(Table table){
		addPlanets();
		
		table.addRect((x, y, width, height)->{
			
			Gdx.graphics.requestRendering();
			Core.batch.end();
			if(postProcess) post.capture();
			update();
			if(postProcess) post.render();
			Core.batch.begin();
		}).grow();
	}

	@Override
	public void dispose(Table table){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		post.dispose();
		renderables.clear();
	}
}
