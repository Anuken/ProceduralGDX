package io.anuke.procgdx.generators.planets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;

public interface RenderObject extends Disposable{
	public void render(Camera cam, RenderContext renderContext);
}
