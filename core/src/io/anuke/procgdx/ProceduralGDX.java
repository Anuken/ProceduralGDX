package io.anuke.procgdx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.modules.ModuleCore;

public class ProceduralGDX extends ModuleCore {
	
	@Override
	public void init(){
		Core.batch = new SpriteBatch();
		add(new UI());
	}
	
}
