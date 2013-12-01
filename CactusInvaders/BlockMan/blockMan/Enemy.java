package blockMan;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Enemy {
	private BufferedImage enemy;
	private int x = 0;
	private int y = 0;
	private boolean right = true;
	private Rectangle r;
	
	public Enemy(int x, int y, boolean right) {
		this.x = x;
		this.y = y;
		this.right = right;
		try {
			enemy = ImageIO.read(new File("enemy.PNG"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could Not Find The File...");
		}
	}

	public BufferedImage loadPic() {
		return this.enemy;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean getDirection() {
		return right;
	}

	public Rectangle getRect() {
		r = new Rectangle(x, y, enemy.getWidth(), enemy.getHeight());
		return this.r;
	}

	public int getHeading(int pX) {
		if (pX > this.getX())
			return 1;
		else
			return 2;
	}

}
