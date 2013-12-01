package blockMan;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Bullet {

	private BufferedImage bullet;
	private int x = 0;
	private int y = 0;
	private boolean right = true;
	private Rectangle r;

	public Bullet(int x, int y, boolean right) {
		this.x = x;
		this.y = y;
		this.right = right;
		try {
			bullet = ImageIO.read(new File("bullet.PNG"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You Fucked Up Somewhere...");
		}
	}

	public BufferedImage loadPic() {
		return this.bullet;
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
	
	public Rectangle getRect(){
		r = new Rectangle (x, y, bullet.getWidth(), bullet.getHeight());
		return this.r;
	}
}
