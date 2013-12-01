package blockMan;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player {
	private BufferedImage gPR01;
	private BufferedImage gPR02;
	private BufferedImage gPL01;
	private BufferedImage gPL02;
	private String type = "gwR01";
	private Rectangle r;
	private int x = 0;
	private int y = 0;
	private int health = 100;
	private int points = 0;
	private int killCount = 0;

	public Player() {
		try {
			gPR01 = ImageIO.read(new File("gwalk01right.PNG"));
			gPR02 = ImageIO.read(new File("gwalk02right.PNG"));
			gPL01 = ImageIO.read(new File("gwalk01left.PNG"));
			gPL02 = ImageIO.read(new File("gwalk02left.PNG"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You Fucked Up Somewhere...");
		}
	}

	public BufferedImage loadPic() {
		if (type.equals("gwR01"))
			return gPR01;
		else if (type.equals("gwR02"))
			return gPR02;
		else if (type.equals("gwL01"))
			return gPL01;
		else if (type.equals("gwL02"))
			return gPL02;
		return null;
	}

	public void type(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setHealth(int h) {
		this.health = h;
	}

	public void setPoints(int p) {
		this.points = p;
	}

	public void setKillCount(int kC) {
		this.killCount = kC;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getHealth() {
		return this.health;
	}

	public int getPoints() {
		return this.points;
	}

	public int getKillCount() {
		return this.killCount;
	}

	public Rectangle getRect() {
		r = new Rectangle(x, y, gPR01.getWidth(), gPR01.getHeight());
		return this.r;
	}

}
