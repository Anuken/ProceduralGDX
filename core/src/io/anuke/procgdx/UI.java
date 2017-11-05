package io.anuke.procgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.gif.GifRecorder;
import io.anuke.procgdx.generators.*;
import io.anuke.procgdx.generators.planets.PlanetGenerator;
import io.anuke.ucore.UCore;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.build;
import io.anuke.ucore.scene.builders.table;
import io.anuke.ucore.scene.ui.ButtonGroup;
import io.anuke.ucore.scene.ui.TextButton;
import io.anuke.ucore.scene.ui.layout.Table;

public class UI extends SceneModule{
	Generator[] generators = {new PlanetGenerator(), new Terrain(), new VoxelClouds(), new VoxelTerrain(), new HeightmapTerrain(), new RGBNoise()};
	Generator current = generators[0];
	Table genTable;
	GifRecorder recorder = new GifRecorder(new SpriteBatch());
	
	@Override
	public void init(){
		Gdx.graphics.setContinuousRendering(false);
		
		build.begin();
		
		new table(){{
			
			new table(){{
				growX();
				atop();
				aright();
				
				ButtonGroup<TextButton> group = new ButtonGroup<>();
				
				for(int i = 0; i < generators.length; i ++){
					Generator gen = generators[i];
					
					TextButton button = new TextButton(ClassReflection.getSimpleName(gen.getClass()), "toggle");
					button.setChecked(current == gen);
					button.clicked(()->{
						if(gen != current){
							current.dispose(genTable);
							current = gen;
							current.build(genTable);
						}
					});
					
					group.add(button);
					
					add(button).left().growX();
				}
			}}.end();
			
			row();
			
			new table("button"){{
				genTable = get();
				
				grow();
			}}.end();
		}}.end();
		
		build.end();
		
		current.build(genTable);
	}
	
	@Override
	public void update(){
		Graphics.clear(Color.BLACK);
		super.update();
		
		if(Timers.get(this, "fps", 60)){
			UCore.log(Gdx.graphics.getFramesPerSecond());
		}
		
		recorder.update();
		
		if(Inputs.keyUp(Keys.ESCAPE)){
			Gdx.app.exit();
		}
		
		Inputs.update();
	}
	
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
		current.resize(width, height);
	}
	
}
