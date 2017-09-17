package io.anuke.procgdx;

import io.anuke.ucore.scene.ui.layout.Table;

public interface Generator{
	
	public void build(Table table);
	public void dispose(Table table);
}
