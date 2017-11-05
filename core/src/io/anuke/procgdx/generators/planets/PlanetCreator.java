package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PlanetCreator{
	public static ModelBuilder modelBuilder = new ModelBuilder();

	public static Renderable genRenderable(Model model){
		NodePart blockPart = model.nodes.get(0).parts.get(0);
	
		Renderable renderable = new Renderable();
		blockPart.setRenderable(renderable);
		renderable.environment = null;
		renderable.worldTransform.idt();
		renderable.meshPart.update();
		return renderable;
	}
}
