package io.anuke.procgdx.generators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.function.StringSupplier;
import io.anuke.ucore.noise.Simplex;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Tmp;

public class Terrain extends ImageGenerator{
	Color[][] colors = {
		{Color.WHITE, Color.WHITE, Color.SKY, Color.SKY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE, Color.WHITE, Color.WHITE},
		{Color.WHITE, Color.SKY, Color.SKY, Color.SKY, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.GRAY, Color.FOREST, Color.FOREST, Color.GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.DARK_GRAY, Color.OLIVE, Color.TAN, Color.TAN, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.TAN, Color.TAN, Color.BROWN, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE},
		{Color.ROYAL, Color.ROYAL, Color.TAN, Color.TAN, Color.TAN, Color.BROWN, Color.BROWN, Color.GRAY, Color.TAN},
	};
	
	final float sqrt2 = (float)Math.sqrt(2f);
	
	long seed = MathUtils.random(9999999999L);
	int eloct = 5, tempoct = 7;
	float elpers = 0.5f, temppers = 0.6f;
	float scale = 0.8f;
	float elscale = 120f * scale, tempscale = 250f * scale;
	boolean island = false, tempisland, heightmap;
	Simplex sim = new Simplex();

	@Override
	public void generate(){
		
		sim.setSeed(seed);
		
		forEach((x, y)->{
			float elev = (float)(1+sim.octaveNoise2d(eloct, elpers, 1f/elscale, x, y))/2f;
			float temp = (float)(1+sim.octaveNoise2d(tempoct, temppers, 1f/tempscale, x + 40, y + 40))/2f;
			
			float elevOffset = elev*0.5f 
					+ (float)(1+sim.octaveNoise2d(eloct+2, elpers+0.01f, 1f/(elscale+15f), x, y))/4f;
			
			if(island){
				elev = elev * Mathf.clamp(1f - (Vector2.dst(size/2, size/2, x, y) / (size/2)), 0, 0.999f);
				elevOffset = elevOffset * Mathf.clamp(1f - (Vector2.dst(size/2, size/2, x, y) / (size/2)), 0, 0.999f);
			}
			
			if(tempisland){
				temp = Mathf.clamp(temp/2f + (Vector2.dst(size/2, size/2, x, y) / (size/2)) / sqrt2 / 2f, 0, 0.999f);
			}
			
			Color dst = colors[(int)(colors.length*temp)][(int)(colors[0].length*elevOffset)];
			
			if(heightmap){
				dst = Tmp.c1.set(elev, elev, elev, 1f);
			}
			
			pixmap.setColor(dst);
			pixmap.drawPixel(x, y);
		});
	}
	
	@Override
	public void addOptions(Table table){
		
		Table one = new Table("button");
		Table two = new Table("button");
		Table three = new Table("button");
		Table four = new Table("button");
		
		three.defaults().left().pad(2);
		
		three.addCheck("Island", c->{
			island = c;
			update();
		});
		
		three.row();
		
		three.addCheck("Heightmap", c->{
			heightmap = c;
			update();
		});
		
		four.add((StringSupplier)()->"Scale: " + scale).left().width(40).padBottom(5);
		four.row();
		four.addSlider(0.01f, 5f, 0.001f, scale, true, f->{
			scale = f;
			elscale = 120f * scale;
			tempscale = 250f * scale;
			update();
		}).left().growX();
		
		one.add((StringSupplier)()->"Elevation Octaves: " + eloct).left().pad(10).width(100);
		one.row();
		one.addSlider(1, 20, 1, 5f, true, f->{
			eloct = (int)(float)f;
			update();
		}).growX().pad(5);
		
		two.add((StringSupplier)()->"Temperature Octaves: " + tempoct).left().pad(10).width(100);
		two.row();
		two.addSlider(1, 20, 1, 7f, true, f->{
			tempoct = (int)(float)f;
			update();
		}).growX().pad(5);
		
		table.defaults().pad(8).growX().left();
		
		table.add(one);
		table.add(two);
		table.add(three).fillY();
		table.add(four).fillY();
		
		table.addButton("Randomize", ()->{
			seed = MathUtils.random(9999999999L);
			update();
		}).fillY();
	}

}
