package io.anuke.procgdx.generators;

import java.awt.geom.Line2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Tmp;

public class CanyonGenerator extends BasicNoise{
	float scale = 50f;
	float falloff = 0.5f;
	int octaves = 6;
	Array<Line2D.Float> lines = new Array<>();

	double radius = 0.1;
	double linepow = 1.2;
	double nscl = 0.5;

	public CanyonGenerator() {
		//lines.add(new Line2D.Float(0, 0, 1f, 1f));
		//lines.add(new Line2D.Float(1, 0, 0f, 1f));

		int sides = 20;
		float rad = 0.4f;

		for(int i = 0; i < sides; i++){
			Angles.translation(i * 360f / sides, rad);
			float x1 = Angles.x(), y1 = Angles.y();
			Angles.translation((i + 1) * 360f / sides, rad);
			lines.add(new Line2D.Float(x1 + 0.5f, y1 + 0.5f, Angles.x() + 0.5f, Angles.y() + 0.5f));
		}
	}

	@Override
	public void generate(){

		forEach((x, y) -> {
			double noise = sim.octaveNoise2D(octaves, falloff, 1 / scale / ((float) size / 400f), x, y) * nscl;

			double min = 1f;

			for(Line2D.Float line : lines){
				double dst = Math.pow(dist(line.x1, line.y1, line.x2, line.y2, (float) x / size, (float) y / size), linepow) / radius + noise;
				min = Math.min(dst, min);
			}

			Color dst = Hue.lightness((float) min);

			pixmap.setColor(dst);
			pixmap.drawPixel(x, y);
		});
	}

	float dist(float x, float y, float x2, float y2, float px, float py){
		float l2 = Vector2.dst2(x, y, x2, y2);
		float t = Math.max(0, Math.min(1, Vector2.dot(px - x, py - y, x2 - x, y2 - y) / l2));
		Vector2 projection = Tmp.v1.set(x, y).add(Tmp.v2.set(x2, y2).sub(x, y).scl(t)); // Projection falls on the segment
		return projection.dst(px, py);
	}

}
