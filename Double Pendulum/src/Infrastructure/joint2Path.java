package Infrastructure;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.ColorModel;

public class joint2Path extends Line2D.Double {
	private int alpha;
	private boolean drawn = false;

	public joint2Path(int alpha, int x1, int y1, int x2, int y2) {
		super.setLine(x1, y1, x2, y2);
		this.alpha = alpha;
	}

	public void drawLine(Graphics2D g2d) {
		this.alpha-=10;
		if (this.alpha <= 0 || this.alpha > 255) {
			pendulum.joint2Lines.remove(this);
			drawn = true;
			System.out.println("removed");
			
		} else {
			Color lineColor = new Color(255, 165, 0, this.getAlpha());
			g2d.setColor(lineColor);
			g2d.draw(this);
		}
	}

	/**
	 * @return the alpha
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;

	}

	/**
	 * @return the drawn
	 */
	public boolean isDrawn() {
		return drawn;
	}

	/**
	 * @param drawn the drawn to set
	 */
	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	@Override
	public String toString() {
		return "joint2Path [alpha=" + alpha + "]";
	}

	
	
}
