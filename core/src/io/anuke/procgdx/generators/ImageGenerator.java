package io.anuke.procgdx.generators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Scaling;

import io.anuke.procgdx.Generator;
import io.anuke.ucore.function.BiConsumer;
import io.anuke.ucore.function.StringSupplier;
import io.anuke.ucore.scene.style.TextureRegionDrawable;
import io.anuke.ucore.scene.ui.Image;
import io.anuke.ucore.scene.ui.Slider;
import io.anuke.ucore.scene.ui.layout.Table;

public abstract class ImageGenerator implements Generator{
	Pixmap pixmap;
	int size = 400;
	Color color = Color.WHITE.cpy();
	Slider slider;
	
	Image image;

	@Override
	public void build(Table table){
		image = new Image();
		
		update();
		
		image.setScaling(Scaling.fit);
		
		table.add((StringSupplier)()->"Size: " + (int)slider.getValue()).colspan(2).pad(4);
		
		table.row();
		
		table.add(image).grow().colspan(1).pad(4);
		table.row();
		
		Table basetools = new Table("button");
		basetools.pad(8);
		
		table.add(basetools).growX().padBottom(4);
		
		slider = new Slider(1, 1024, 10, false);
		slider.setValue(size);
		slider.released(()->{
			size = (int)slider.getValue();
			update();
		});
		
		table.bottom();
		table.left();
		
		basetools.defaults().expandX();
		
		basetools.addButton("Regenerate", ()->{
			update();
		});
		
		basetools.addButton("Export", ()->{
			PixmapIO.writePNG(Gdx.files.local("out.png"), pixmap);
		});
		
		basetools.add(slider).width(300);
		
		Table extras = new Table();
		
		table.row();
		table.add(extras).growX();
		
		addOptions(extras);
	}

	@Override
	public void dispose(Table table){
		pixmap.dispose();
		table.clear();
		
		pixmap = null;
	}
	
	public void addOptions(Table table){}
	
	public void update(){
		if(pixmap != null){
			pixmap.dispose();
			((TextureRegionDrawable)image.getDrawable()).getRegion().getTexture().dispose();
		}
		pixmap = new Pixmap(size, size, Format.RGBA8888);
		generate();
		
		image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
	}
	
	public void forEach(BiConsumer<Integer, Integer> cons){
		for(int x = 0; x < size; x ++){
			for(int y = 0; y < size; y ++){
				cons.accept(x, y);
			}
		}
	}
	
	public abstract void generate();

}
