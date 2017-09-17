package io.anuke.procgdx.generators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

import io.anuke.procgdx.Generator;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.function.TriPosConsumer;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Mathf;

public abstract class ModelGenerator implements Generator{
	Environment environment;
	ModelBatch batch;
	PerspectiveCamera cam;
	Model model;
	ModelInstance instance;
	int size = 50;
	
	float camscale = 1.0f;
	float camrot = 0;
	
	public ModelGenerator(){
		batch = new ModelBatch();
		environment = new Environment();
		cam = new PerspectiveCamera(66, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		cam.near = 1;
		cam.far = 600;
		
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	@Override
	public void build(Table table){
		update();
		
		table.addRect((x, y, width, height)->{
			
			Gdx.graphics.requestRendering();
			Core.batch.end();
			
			doInput();
			cam.update();
			batch.begin(cam);
			batch.render(instance, environment);
			batch.end();
			
			
			Core.batch.begin();
		}).grow();
		
		table.row();
		
		Table tools = new Table("button");
		tools.pad(8);
		
		table.add(tools).growX();
		
		tools.addButton("Update", ()->{
			update();
		});
	}

	@Override
	public void dispose(Table table){
		table.clear();
		if(model != null){
			model.dispose();
			model = null;
		}
	}
	
	public abstract void update();
	
	public void doInput(){
		float speed = 2f;
		
		if(Inputs.keyDown(Keys.LEFT)){
			camrot += speed * Timers.delta();
		}
		
		if(Inputs.keyDown(Keys.RIGHT)){
			camrot -= speed * Timers.delta();
		}
		
		if(Inputs.scrolled()){
			camscale -= Inputs.scroll()/20f;
			camscale = Mathf.clamp(camscale, 0.5f, 100f);
			cam.position.set(size*camscale, size*camscale, size*camscale);
			cam.lookAt(size/2, size/2, size/2);
		}
		
		instance.transform.setToRotation(Vector3.Y, camrot);
		
		cam.viewportWidth = Gdx.graphics.getWidth();
		cam.viewportHeight = Gdx.graphics.getHeight();
		cam.update();
	}
	
	public void forEach(TriPosConsumer tri){
		for(int x = 0; x < size; x ++){
			for(int y = 0; y < size; y ++){
				for(int z = 0; z < size; z ++){
					if(Mathf.inBounds(x, y, z, size, 2))
						tri.accept(x, y, z);
				}
			}
		}
	}
	

}
