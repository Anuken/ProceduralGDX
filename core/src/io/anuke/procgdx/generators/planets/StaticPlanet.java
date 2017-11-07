package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public abstract class StaticPlanet implements RenderableProvider{
	Model model;
	ModelInstance instance;
	
	public StaticPlanet(float size, int divisions){
		model = PlanetCreator.modelBuilder.createSphere(size, size, size, divisions, divisions, 
				new Material(), Usage.Position | Usage.Normal | Usage.ColorPacked);
		instance = new ModelInstance(model);
	}
	
	abstract void transform(float[] vertices);
	
	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool){
		instance.getRenderables(renderables, pool);
	}
}
