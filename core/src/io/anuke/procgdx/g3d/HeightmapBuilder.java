package io.anuke.procgdx.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class HeightmapBuilder{
	private static final VertexInfo vertTmp1 = new VertexInfo();
	private static final VertexInfo vertTmp2 = new VertexInfo();
	private static final VertexInfo vertTmp3 = new VertexInfo();
	private static final VertexInfo vertTmp4 = new VertexInfo();
	private static final Color color = new Color(1, 0, 0, 1);
	private static final Vector3[] vectors = new Vector3[8];
	private static final Vector3 temp1 = new Vector3(), temp2 = new Vector3();
	
	private static final MeshBuilder meshBuilder = new MeshBuilder();
	private static final ModelBuilder modelBuilder = new ModelBuilder();
	private static Array<Mesh> meshes;
	
	static{
		for(int i = 0; i < vectors.length; i ++){
			vectors[i] = new Vector3();
		}
	}
	
	public static Model createHeightmapModel(float[][] heights, int[][] colors, float heightscale, float yoffset){
		Mesh[] meshes = createHeightmapMesh(heights, colors, heightscale, yoffset);
		
		modelBuilder.begin();
		int i = 0;

		for(Mesh mesh : meshes){
			modelBuilder.part("mesh" + i ++, mesh, GL20.GL_TRIANGLES, new Material());
		}

		return modelBuilder.end();
	}
	
	private static Mesh[] createHeightmapMesh(float[][] heights, int[][] colors, float heightscale, float yoffset){
		begin();
		
		float offsetx = -heights.length/2f;
		float offsetz = -heights[0].length/2f;
		
		for(int x = 0; x < heights.length - 1; x ++){
			for(int z = 0; z < heights[0].length - 1; z ++){
				int vcolor = colors[x][z];
				
				float height1 = heights[x][z];
				float height2 = heights[x][z + 1];
				float height3 = heights[x + 1][z + 1];
				float height4 = heights[x + 1][z];
				
				vectors[0].set(x + offsetx, height1 * heightscale + yoffset, z + offsetz);
				vectors[1].set(x + offsetx, height2 * heightscale + yoffset, z + 1 + offsetz);
				vectors[2].set(x + 1 + offsetx, height3 * heightscale + yoffset, z + 1 + offsetz);
				vectors[3].set(x + 1 + offsetx, height4 * heightscale + yoffset, z + offsetz);
				
				color.set(vcolor);
				
				rect(vectors[0], vectors[1], vectors[2], vectors[3]);
			}
		}
		return end();
	}

	private static void begin(){
		if(meshes != null) throw new IllegalArgumentException("Call end() first.");
		meshes = new Array<Mesh>();
		meshBuilder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
	}

	private static Mesh[] end(){
		if(meshes == null) throw new IllegalArgumentException("Call begin() first.");

		if(meshBuilder.getAttributes() != null){
			endMesh();
		}

		Mesh[] array = meshes.toArray(Mesh.class);

		meshes = null;
		
		System.out.println("ending");

		return array;
	}
	
	private static void rect(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		rect(a, b, c, d, color, color, color, color);
	}
	
	private static void rect(Vector3 a, Vector3 b, Vector3 c, Vector3 d, Color ca, Color cb, Color cc, Color cd){
		
		checkMesh();
		
		temp1.set(c).sub(b);
		temp2.set(a).sub(b);
		
		Vector3 normal = temp1.crs(temp2);

		meshBuilder.rect(
			vertTmp1.set(a, normal, ca, null).setUV(0f, 1f), 
			vertTmp2.set(b, normal, cb, null).setUV(1f, 1f), 
			vertTmp3.set(c, normal, cc, null).setUV(1f, 0f), 
			vertTmp4.set(d, normal, cd, null).setUV(0f, 0f)
		);
	}

	private static void checkMesh(){
		if(meshBuilder.getAttributes() == null){

			meshBuilder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
			//System.out.println("Beginning first mesh build");

		}else if(meshBuilder.getNumIndices() >= Short.MAX_VALUE + 16000 /*if the vertices will exceed max vertices soon*/){
			endMesh();

			//System.out.println("Adding new mesh.");

			meshBuilder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		}
	}

	private static void endMesh(){
		Mesh mesh = meshBuilder.end();
		meshes.add(mesh);
		System.out.println("End mesh. Adding to array.");
	}
}
