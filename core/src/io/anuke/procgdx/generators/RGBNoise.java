package io.anuke.procgdx.generators;

import io.anuke.ucore.graphics.Hue;

public class RGBNoise extends ImageGenerator{

	@Override
	public void generate(){
		
		forEach((x, y)->{
			Hue.random(color);
			pixmap.setColor(color);
			pixmap.drawPixel(x, y);
		});
	}

}
