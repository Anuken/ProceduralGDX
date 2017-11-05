package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class SpaceSphere implements RenderObject{
	private static final float radius = 40f;
	private static final int divisions = 100;
	
	private Shader shader;
	private Renderable sphere;
	
	public SpaceSphere(){
		shader = new ShaderAdapter("space", (shader, renderable)->{});
		
		sphere = PlanetCreator.genRenderable(PlanetCreator.modelBuilder.createSphere(radius, radius, radius, 
				divisions, divisions, new Material(), Usage.Position | Usage.Normal));
		
		float[] vertices = sphere.meshPart.mesh.getVertices(new float[sphere.meshPart.mesh.getNumVertices() * 6]);
		for(int i = 0; i < vertices.length/6; i ++){
			vertices[0 + i*6 + 0] *= -1;
			vertices[0 + i*6 + 1] *= -1;
			vertices[0 + i*6 + 2] *= -1;
		}
		sphere.meshPart.mesh.setVertices(vertices);
	}

	@Override
	public void render(Camera cam, RenderContext renderContext){
		shader.begin(cam, renderContext);
		shader.render(sphere);
		shader.end();
	}
	
	@Override
	public void dispose(){
		shader.dispose();
		sphere.meshPart.mesh.dispose();
	}

}
