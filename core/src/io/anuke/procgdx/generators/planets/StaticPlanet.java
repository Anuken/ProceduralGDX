package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;

import io.anuke.procgdx.generators.planets.MultiMeshBuilder.BuildResult;
import io.anuke.ucore.UCore;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.noise.Simplex;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Tmp;

public abstract class StaticPlanet implements RenderableProvider{
	private static final IntArray tmpIndices = new IntArray();
	private static final VertexInfo vertTmp3 = new VertexInfo();
	private static final Matrix4 matTmp1 = new Matrix4();
	private static final MultiMeshBuilder builder = new MultiMeshBuilder();
	private static final ModelBuilder modelBuilder = new ModelBuilder();
	
	public int seed = Mathf.random(99999);
	public Simplex noise = new Simplex();
	float intensity = 0.5f;
	
	Model model;
	ModelInstance instance;

	public StaticPlanet(float size, int divisions, float intensity) {
		this.intensity = intensity;
		noise.setSeed(seed);
		
		Array<Mesh> meshes = createSphere(size, size, size, divisions, divisions);
		
		modelBuilder.begin();
		for(int i = 0; i < meshes.size; i ++){
			modelBuilder.part("mesh"+i, meshes.get(i), GL20.GL_TRIANGLES, new Material());
		}
		model = modelBuilder.end();
	
		instance = new ModelInstance(model);
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool){
		instance.getRenderables(renderables, pool);
	}
	
	public abstract float getHeight(Vector3 position);
	public abstract Color getColor(Vector3 position, float height);
	
	void fixNormals(int[] indices, float[] vertices, int indiceAmount, int verticeAmount){
		int vs = 7; //vertex size
		
		for(int i = 0; i < indiceAmount/3; i ++){
			int a = indices[i*3+0] * vs;
			int b = indices[i*3+1] * vs;
			int c = indices[i*3+2] * vs;
			
			float x1 = vertices[a + 0];
			float y1 = vertices[a + 1];
			float z1 = vertices[a + 2];

			float x2 = vertices[b + 0];
			float y2 = vertices[b + 1];
			float z2 = vertices[b + 2];

			float x3 = vertices[c + 0];
			float y3 = vertices[c + 1];
			float z3 = vertices[c + 2];
			
			Tmp.v31.set(x3 - x1, y3 - y1, z3 - z1).crs(x2 - x1, y2 - y1, z2 - z1).scl(-1f).nor();
			
			vertices[a + 4] = Tmp.v31.x;
			vertices[a + 5] = Tmp.v31.y;
			vertices[a + 6] = Tmp.v31.z;
			
			vertices[b + 4] = Tmp.v31.x;
			vertices[b + 5] = Tmp.v31.y;
			vertices[b + 6] = Tmp.v31.z;
			
			vertices[c + 4] = Tmp.v31.x;
			vertices[c + 5] = Tmp.v31.y;
			vertices[c + 6] = Tmp.v31.z;
			
			/*
			float color = vertices[a + 3];
			
			vertices[a + 3] = color;
			vertices[b + 3] = color;
			vertices[c + 3] = color; */
		}
	}
	
	void fixTriangles(FloatArray vertices, IntArray indices){
		float[] verticearray = vertices.toArray();
		int[] indicearray = indices.toArray();

		vertices.clear();
		indices.clear();
		
		vertices.ensureCapacity(vertices.size);
		indices.ensureCapacity(indices.size);

		for(int i = 0; i < indicearray.length; i ++){
			int o = indicearray[i] * 7;
			
			for(int j = 0; j < 7; j ++){
				vertices.add(verticearray[o + j]);
			}

			indices.add(i);
		}
	}
	
	void splitMeshes(Array<Mesh> meshes, float[] vertices, int[] indices){
		VertexAttributes attributes = MeshBuilder.createAttributes(Usage.Position | Usage.Normal | Usage.ColorPacked);
		
		int offset = 0;
		int loffset = 0;
		
		while(offset < indices.length/3){
			FloatArray currentvertices = new FloatArray();
			ShortArray currentindices = new ShortArray();
			
			loffset = offset;
			
			for(int i = 0; i < Short.MAX_VALUE/3 && offset < indices.length/3; i ++){
				
				for(int s = 0; s < 3; s ++){
					currentindices.add(indices[offset*3 + s] - loffset*3);
					int block = indices[offset*3 + s]*7;
					
					for(int j = 0; j < 7; j ++){
						currentvertices.add(vertices[block + j]);
					}
					//currentvertices.set(currentvertices.size - 4, 7457235235.26236236f);
				}
				
				offset ++;
			}
			
			Mesh mesh = new Mesh(true, currentvertices.size, currentindices.size, attributes);
			mesh.setVertices(currentvertices.toArray());
			mesh.setIndices(currentindices.toArray());
			meshes.add(mesh);
		}
	}

	Array<Mesh> createSphere(float width, float height, float depth, int divisionsU, int divisionsV){
		Array<Mesh> meshes = new Array<>();
		
		Timers.mark();
		
		builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		
		final Matrix4 transform = matTmp1.idt();
		final float angleUFrom = 0, angleUTo = 360f, angleVFrom = 0f, angleVTo = 180f;
		final float hw = width * 0.5f;
		final float hh = height * 0.5f;
		final float hd = depth * 0.5f;
		final float auo = MathUtils.degreesToRadians * angleUFrom;
		final float stepU = (MathUtils.degreesToRadians * (angleUTo - angleUFrom)) / divisionsU;
		final float avo = MathUtils.degreesToRadians * angleVFrom;
		final float stepV = (MathUtils.degreesToRadians * (angleVTo - angleVFrom)) / divisionsV;
		final float us = 1f / divisionsU;
		final float vs = 1f / divisionsV;
		float u = 0f;
		float v = 0f;
		float angleU = 0f;
		float angleV = 0f;
		VertexInfo curr1 = vertTmp3.set(null, null, null, null);
		curr1.hasUV = curr1.hasPosition = curr1.hasNormal = true;

		final int s = divisionsU + 3;
		tmpIndices.clear();
		tmpIndices.ensureCapacity(divisionsU * 2);
		tmpIndices.size = s;
		int tempOffset = 0;

		builder.ensureVertices((divisionsV + 1) * (divisionsU + 1));
		builder.ensureRectangleIndices(divisionsU);
		for(int iv = 0; iv <= divisionsV; iv++){
			angleV = avo + stepV * iv;
			v = vs * iv;
			final float t = MathUtils.sin(angleV);
			final float h = MathUtils.cos(angleV) * hh;
			for(int iu = 0; iu <= divisionsU; iu++){
				angleU = auo + stepU * iu;
				u = 1f - us * iu;
				curr1.position.set(MathUtils.cos(angleU) * hw * t, h, MathUtils.sin(angleU) * hd * t).mul(transform);
				curr1.normal.set(curr1.position).nor();
				curr1.uv.set(u, v);
				
				Vector3 sp = Tmp.v31.set(curr1.position).add(width*2f);
				
				float sHeight = getHeight(sp);
				Color sColor = getColor(curr1.position, sHeight);
				
				curr1.position.scl(1f + sHeight*intensity);
				
				curr1.setCol(sColor);
				
				tmpIndices.set(tempOffset, builder.vertex(curr1));
				final int o = tempOffset + s;
				if((iv > 0) && (iu > 0)) // FIXME don't duplicate lines and points
					builder.rect(tmpIndices.get(tempOffset), 
							tmpIndices.get((o - 1) % s), 
							tmpIndices.get((o - (divisionsU + 2)) % s), 
							tmpIndices.get((o - (divisionsU + 1)) % s));
				tempOffset = (tempOffset + 1) % tmpIndices.size;
			}
		}
		
		BuildResult result = builder.end();
		UCore.log("Done building raw vertices: " + Timers.elapsed() + "ms.");
		
		Timers.mark();
		fixTriangles(result.vertices, result.indices);
		UCore.log("Fixed triangles: " + Timers.elapsed() + "ms.");
		
		Timers.mark();
		fixNormals(result.indices.items, result.vertices.items, result.indices.size, result.vertices.size);
		UCore.log("Fixed normals: " + Timers.elapsed() + "ms.");
		
		Timers.mark();
		splitMeshes(meshes, result.vertices.toArray(), result.indices.toArray());
		UCore.log("Split meshes: " + Timers.elapsed() + "ms.");
		
		return meshes;
	}
}
