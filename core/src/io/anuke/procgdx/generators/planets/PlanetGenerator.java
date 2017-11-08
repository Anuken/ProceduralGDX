package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.utils.ShaderLoader;

import io.anuke.procgdx.Generator;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Mathf;

public class PlanetGenerator implements Generator{
	PerspectiveCamera cam;
	CameraInputController camController;
	RenderContext renderContext;
	PostProcessor post;
	boolean postProcess = true;
	
	Array<RenderObject> objects = new Array<>();
	Array<RenderableProvider> renderables = new Array<>();

	public PlanetGenerator(){
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 200f;
		cam.update();
		
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

		camController = new CameraInputController(cam);
		Inputs.addProcessor(camController);
		
		ShaderLoader.BasePath = "shaders/";
		
		addEffects();
	}

	void update(){
		camController.update();
		
		renderContext.begin();
		
		for(RenderObject object : objects){
			object.render(cam, renderContext);
		}
		
		renderContext.end();
		    
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
		objects.add(new SpaceSphere());
		
		for(int i = 0; i < 6; i ++){
			Asteroid a = new Asteroid();
			a.setOrbit(21f + Mathf.range(0.5f));
			objects.add(a);
		}
		
		objects.add(new EarthPlanet().setOrbit(11f));
		objects.add(new GasGiant().setOrbit(17f));
		objects.add(new LavaPlanet().setOrbit(7f));
		objects.add(new IcePlanet().setOrbit(26f));
		objects.add(new Sun());
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
		
		for(RenderObject planet : objects){
			planet.dispose();
		}
		
		post.dispose();
		objects.clear();
	}
}
