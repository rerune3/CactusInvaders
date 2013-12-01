package blockMan;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Timer;
import javax.tools.JavaCompiler;

public class CactusInvaders extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame f = new JFrame();
	private Player p;
	private int rate = 3;
	private int enemyRate = 1;
	private int enemyDeployCount = 0;
	private int deployCount = 0;
	private int changeSpeed = 0;
	private boolean readyToDeploy = false;
	private Random r = new Random();
	private boolean jump = false;
	private boolean jumpE = false;
	private int jumpCounter = 0;
	private int lastJumpCount = 0;
	private int jumpCounterE = 0;
	private int lastJumpCountE = 0;
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean spacePressed = false;
	private Stack<Integer> strokes = new Stack<Integer>();
	private ArrayList<Bullet> bList = new ArrayList<Bullet>();
	private Iterator<Bullet> bListItr;// handles all iteration through bList
	private int numberOfEnemies = 2;
	private ArrayList<Enemy> eList = new ArrayList<Enemy>();
	private ArrayList<Enemy> eListReady = new ArrayList<Enemy>();
	private int walkCounter = 0;
	private int levelCount = 1;
	private Image bk;
	private Rectangle[] platforms = new Rectangle[4];
	private Timer gameClock;
	private int minute = 0;
	private int seconds = 0;
	private int milliSeconds = 0;

	public CactusInvaders() {

		try {
			bk = ImageIO.read(new File("background.PNG"));// read background
															// image
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Files not there brah");
		}
		f.setSize(Toolkit.getDefaultToolkit().getScreenSize());// get screen
																// size
		try {
			f.setUndecorated(true);// remove title bars -- full screen
									// essentially
		} catch (IllegalComponentStateException e) {
			JOptionPane.showMessageDialog(null, "Your Computer Does Not"
					+ " Support Full Screen");
		}

		gameClock = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				milliSeconds++;
				if (milliSeconds == 59) {
					seconds++;
					milliSeconds = 0;
				}
				if (seconds == 59) {
					minute++;
					seconds = 0;
				}
			}
		});

		p = new Player();// instantiate a new player
		levelCount = 1;// go back to level 1
		if (!eList.isEmpty())// if the enemy list is not empy
			eList.clear();// clear the enemy list
		if (!bList.isEmpty())// if the bullet list is not empty
			bList.clear();// clear the bullet list
		p.setPoints(0);// set his points to 0
		p.setHealth(100);// set his health back to 100
		// reset the timer
		minute = 0;
		seconds = 0;
		milliSeconds = 0;
		p.setKillCount(0);// set the kill count back to 0
		f.addKeyListener(new KeyAdapter() {// add a key listener
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();// store key code into int variable
				if (key == KeyEvent.VK_LEFT) {// if the key code is 37 or left
					strokes.push(key);// push the key code on a stack
					leftPressed = true;// tell java that the left key has been
										// pressed
					walkCounter++;// add one to the walk counter
				}
				if (key == KeyEvent.VK_RIGHT) {// if the key code is 39 or right
					strokes.push(key);// push the key code on a stack
					rightPressed = true;// tell java that the right key has been
										// pressed
					walkCounter++;// add one to the walk counter
				}
				if (key == KeyEvent.VK_UP) {// if the key code is 38 or up
					strokes.push(key);// push the key code on a stack
				}
				if (key == KeyEvent.VK_SPACE) {// if the key code is the space
												// bar
					// access shooting images
					if (p.getType().equals("gwR01")
							|| p.getType().equals("gwR02"))
						// add a bullet to the array list
						bList.add(new Bullet(p.getX(), p.getY() + 40, true));
					else if (p.getType().equals("gwL01")
							|| p.getType().equals("gwL02"))
						// add a bullet to the array list
						bList.add(new Bullet(p.getX(), p.getY() + 40, false));

					strokes.push(key);// push the key code on a stack
					spacePressed = true;// tell java that the space bar has been
										// pressed
					walkCounter++;// add one to the walk counter
				}
				if (key == KeyEvent.VK_ESCAPE) {// if the key code is the escape
												// key
					System.exit(0);// exit the game
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {// if a key is released
				// TODO Auto-generated method stub
				int key = e.getKeyCode();// store key code in a variable
				if (key == KeyEvent.VK_LEFT) {// if the key code is 37 or left
					leftPressed = false;// the player is no longer pressing the
										// left key
				}
				if (key == KeyEvent.VK_RIGHT) {// if the key code is 39 or right
					rightPressed = false;// the player is no longer pressing the
											// right key
				}
				if (key == KeyEvent.VK_SPACE) {// if the key code is 32 or space
					spacePressed = false;// the player is no longer pressing the
											// space key
				}
			}
		});

		new Timer(25, new ActionListener() {// every 25 milliseconds
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (rightPressed == true) {// if java is told that the
													// right key was pressed
							strokes.push(39);// push the key code 39 to the
												// stack
							walkCounter++;// increment walk counter
						}
						if (leftPressed == true) {// if java is told that the
													// left key was pressed
							strokes.push(37);// push the key code 37 to the
												// stack
							walkCounter++;// increment walk counter
						}
						if (spacePressed == true) {// if java is told that the
													// space bar was pressed
							strokes.push(32);// push the key code 32 to the
												// stack
							walkCounter++;// increment walk counter
						}
					}
				}).start();
		f.add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		bk = bk.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		platforms[0] = new Rectangle(1, bk.getHeight(null) - 240, 340, 70);
		platforms[1] = new Rectangle(910, bk.getHeight(null)
				- (bk.getHeight(null) - 470), 1400 - 950, 525 - 467);
		platforms[2] = new Rectangle(460, bk.getHeight(null)
				- (bk.getHeight(null) - 370), 860 - 460, 421 - 365);
		platforms[3] = new Rectangle(780, bk.getHeight(null)
				- (bk.getHeight(null) - 155), 1200 - 780, 225 - 130);

		new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!eList.isEmpty()) {
					readyToDeploy = true;
				}
			}
		}).start();

		new Timer(300, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (changeSpeed == 0) {
					enemyRate = 0;
					changeSpeed = 1;
				} else if (changeSpeed == 1) {
					enemyRate = 1;
					changeSpeed = 0;
				}
			}
		}).start();

		p.setX(f.getWidth() / 2);
		p.setY(getHeight() - 150);
		this.getGraphics().setColor(Color.ORANGE);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (!gameClock.isRunning())// if the game clock is not already
									// running
			gameClock.start();// start the game clock
		g2.drawImage(bk, 0, 0, null);// paste the background image
		if (p.getHealth() > 0) {// if the players health is greater than 0
			if (eList.isEmpty())// if the enemy list is empty
				createEnemies();// create some more enemies
			printLevelCount(g2);// print the level
			// a while loop is used here because java should be reading the
			// key stroke fairly quickly
			while (!strokes.isEmpty()) {// while the strokes stack is not
										// empty
				checkKeysPressed(strokes.pop());// pop from the stack and
												// check what key that is
			}
			firePlayerBullet(g2);// check if the player is firing a bullet
			if (readyToDeploy == true) {// if it is time to deploy an enemy
										// to the game
				// deployCount is a COUNTER
				// if the deploy count is less than the size of the main
				// enemy list
				if (deployCount < eList.size()) {
					// add an enemy from the main list to the ready list
					eListReady.add(eList.get(deployCount));
					// eListReady is a list that holds enemies READY to be
					// deployed to the game
					deployCount++;// increment the deployCount
				}
				readyToDeploy = false;// enemy is not ready to deploy
			}
			checkEnemyMovement(g2);
			checkPlayerBulletHit(g2);
			displayStats(g2);
			bListItr = bList.iterator();
			while (bListItr.hasNext()) {
				Bullet b = bListItr.next();
				if (b.getDirection() == true)
					b.setX(b.getX() + 15);
				else if (b.getDirection() == false)
					b.setX(b.getX() - 15);
				g2.drawImage(b.loadPic(), b.getX(), b.getY(), null);
			}
			if (jump == true)
				jump();
			else {
				p.setY(p.getY());
				if (!onGround() && !onPlatform()) {
					fall();
				}
			}
			g2.drawImage(p.loadPic(), p.getX(), p.getY(), null);
			if (p.getKillCount() == numberOfEnemies) {
				numberOfEnemies = numberOfEnemies * 2;
				levelCount++;
				createEnemies();
				deployCount = 0;
			}
			repaint();
		} else {
			g2.setFont(new Font("Comic Sans", Font.BOLD, 100));
			g2.drawString("Game Over", getWidth() / 2 - 250, getHeight() / 2);
			g2.setFont(new Font("Comic Sans", Font.BOLD, 20));
			g2.drawString("Points: "
					+ (p.getPoints() * (minute * 60 + seconds)) / 2,
					getWidth() / 2 - 250, getHeight() / 2 - 100);
			g2.drawString("Enemies Killed: " + p.getKillCount(),
					getWidth() / 2 - 250, getHeight() / 2 - 150);
			g2.drawString("You Got To Level " + levelCount + " And Survived "
					+ minute + " : " + seconds + " : " + milliSeconds,
					getWidth() / 2 - 250, getHeight() / 2 - 200);
			g2.drawString("Click Anywhere To Play Again", getWidth() / 2 - 250,
					getHeight() / 2 - 300);
			if (gameClock != null) {
				dump();
			}
		}
	}

	private void dump() {
		gameClock.stop();
		eListReady.clear();
		eList.clear();
		enemyDeployCount = 0;
		deployCount = 0;
		strokes.clear();
		numberOfEnemies = 2;
		bList.clear();
		System.gc();
	}

	private void createEnemies() {
		// TODO Auto-generated method stub
		System.gc();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (eList.size() != numberOfEnemies) {
					if (r.nextInt(50) % 2 == 0)
						eList.add(new Enemy(r.nextInt(getWidth()), -100, true));
					else
						eList.add(new Enemy(r.nextInt(getWidth()), -100, false));
				}
			}

		}).start();

	}

	private void printLevelCount(Graphics2D g2) {
		// TODO Auto-generated method stub
		g2.setFont(new Font("Times New Roman", Font.BOLD, 70));
		g2.drawString("Level " + levelCount, getWidth() / 4, 100);
	}

	private void displayStats(final Graphics2D g2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		g2.setColor(Color.CYAN);
		g2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		g2.drawString("Points: " + p.getPoints(), getWidth() / 2, 50);
		g2.drawString("Health", getWidth() / 2, 70);
		g2.fillRect(getWidth() / 2, 80, p.getHealth(), 20);
		g2.setColor(Color.black);
		g2.drawString(p.getHealth() + "", getWidth() / 2 + 30, 95);
		g2.setColor(Color.CYAN);
		g2.drawString(p.getKillCount() + " Enemies Killed", getWidth() / 2, 140);
		g2.drawString(minute + " : " + seconds + " : " + milliSeconds, 1046, 46);

	}

	private void checkPlayerBulletHit(final Graphics2D g2) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Iterator<Bullet> bListItr = bList.iterator();// get bList
																// iterator
				Iterator<Enemy> eListReadyItr = eListReady.iterator();// get
																		// eList
																		// iterator
				while (bListItr.hasNext()) {
					Bullet b = bListItr.next();
					while (eListReadyItr.hasNext()) {
						Enemy e = eListReadyItr.next();
						if (b.getRect().intersects(e.getRect())) {// if a bullet
																	// hits an
																	// enemy
							bListItr.remove();// remove the bullet
							eListReadyItr.remove();// remove the enemy
							p.setPoints(p.getPoints() + 10);// give player ten
															// points
							p.setKillCount(p.getKillCount() + 1);// increase
																	// kill
																	// count
																	// by 1
						}
					}
				}
				eListReadyItr = eListReady.iterator();// get eListIterator again
				while (eListReadyItr.hasNext()) {
					Enemy e = eListReadyItr.next();
					if (e.getRect().intersects(p.getRect())) {// if the enemy
																// and player
																// hit each
																// other
						g2.setColor(Color.red);// red screen represents getting
												// hit
						g2.fillRect(0, 0, getWidth(), getHeight());
						p.setHealth(p.getHealth() - 10);// decrease health by 10
						// if the heading of the enemy relative to the player is
						// left of
						// the player
						if (e.getHeading(p.getX()) == 1)
							p.setX(p.getX() + 80);// push player to the right
						else
							p.setX(p.getX() - 80);// push player to the left
					}
				}
			}

		}).start();

	}

	private void checkEnemyMovement(final Graphics2D g2) {
		// TODO Auto-generated method stub
		Iterator<Enemy> eListReadyItr = eListReady.iterator();// get
																// eList
																// iterator
		while (eListReadyItr.hasNext()) {
			Enemy e = eListReadyItr.next();
			// if the enemy is NOT on the ground and NOT on the platform
			if (!onGround(e) && !onPlatform(e)) {
				fall(e);// make the enemy fall
			}
			// if the enemy is ON a platform and below the player
			if (onPlatform() && e.getY() > p.getY()) {
				jumpE = true;
				jump(e);// jump
			}
			if (p.getX() >= e.getX()) {// if they're on the same plane
				e.setX(e.getX() + enemyRate);// move enemy x position
			} else {
				e.setX(e.getX() - enemyRate);// move enemy x position
			}
			if (e.getX() < -100)
				e.setX(getWidth());
			else if (e.getX() > getWidth() + 100)
				e.setX(-30);
			g2.drawImage(e.loadPic(), e.getX(), e.getY(), null);// draw
																// the
																// enemy
		}
		eListReadyItr = eListReady.iterator();// get the iterator one
												// more time
		while (eListReadyItr.hasNext()) {
			Enemy e = eListReadyItr.next();
			if (e.getX() <= 0 - e.loadPic().getWidth()
					|| e.getX() >= getWidth()) {// if off screen then
												// remove
												// from list
												// eListReadyItr.remove();
			}
		}

	}

	private void firePlayerBullet(final Graphics2D g2) {
		// TODO Auto-generated method stub
		Thread firePlayerBullet = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Iterator<Bullet> bListItr = bList.iterator();
				while (bListItr.hasNext()) {
					Bullet b = bListItr.next();
					if (b.getDirection() == true)
						b.setX(b.getX() + 15);
					else if (b.getDirection() == false)
						b.setX(b.getX() - 15);
					g2.drawImage(b.loadPic(), b.getX(), b.getY(), null);
				}

				bListItr = bList.iterator();
				while (bListItr.hasNext()) {
					Bullet b = bListItr.next();
					if (b.getX() >= getWidth() || b.getX() <= 0) {
						bListItr.remove();
					}
				}
			}

		});
		firePlayerBullet.start();

	}

	private void fall() {
		int jumpFactor = -(int) (Math.sin(Math.toRadians(lastJumpCount)) * 5);
		// System.out.println(jumpFactor);
		p.setY(p.getY() - jumpFactor);
		if (jumpFactor <= 0)
			lastJumpCount = (int) (lastJumpCount + 1);
		else
			lastJumpCount = 0;
	}

	private void fall(Enemy e) {
		int jumpFactor = -(int) (Math.sin(Math.toRadians(lastJumpCountE)) * 8);
		// System.out.println(jumpFactor);
		e.setY(e.getY() - jumpFactor);
		if (jumpFactor <= 0)
			lastJumpCountE = (int) (lastJumpCountE + 1);
		else
			lastJumpCountE = 0;
	}

	private void jump() {
		// TODO Auto-generated method stub
		int jumpFactor = (int) (Math.sin(Math.toRadians(jumpCounter)) * 4);
		// if (jumpCounter < 360) {
		if (jumpFactor < 0) {
			if (onPlatform() || onGround()) {
				jumpCounter = 360;
			}
		}
		p.setY(p.getY() - jumpFactor);
		jumpCounter = (int) (jumpCounter + 1);
		// }
		if (jumpCounter >= 360) {
			jumpCounter = 0;
			jump = false;
		}
	}

	private void jump(Enemy e) {
		// TODO Auto-generated method stub
		int jumpFactor = (int) (Math.sin(Math.toRadians(jumpCounterE)) * 6);
		// if (jumpCounter < 360) {
		if (jumpFactor < 0) {
			if (onPlatform(e) || onGround(e)) {
				jumpCounterE = 360;
			}
		}
		e.setY(e.getY() - jumpFactor);
		jumpCounterE = (int) (jumpCounterE + 1);
		// }
		if (jumpCounterE >= 360) {
			jumpCounterE = 0;
			jumpE = false;
		}
	}

	private boolean onGround() {
		// TODO Auto-generated method stub
		if (p.getY() >= getHeight() - 150) {
			return true;
		}
		return false;
	}

	private boolean onGround(Enemy e) {
		// TODO Auto-generated method stub
		if (e.getY() >= getHeight() - 150) {
			return true;
		}
		return false;
	}

	private boolean onPlatform() {
		// TODO Auto-generated method stub
		if (p.getY() + p.loadPic().getHeight() >= platforms[0].y
				&& p.getY() + p.loadPic().getHeight() <= platforms[0].y
						+ platforms[0].height
				&& p.getX() >= platforms[0].x
				&& p.getX() + p.loadPic().getWidth() < platforms[0].x
						+ platforms[0].width) {
			return true;
		} else if (p.getY() + p.loadPic().getHeight() >= platforms[1].y
				&& p.getY() + p.loadPic().getHeight() <= platforms[1].y
						+ platforms[1].height
				&& p.getX() >= platforms[1].x
				&& p.getX() + p.loadPic().getWidth() < platforms[1].x
						+ platforms[1].width) {
			return true;
		} else if (p.getY() + p.loadPic().getHeight() >= platforms[2].y
				&& p.getY() + p.loadPic().getHeight() <= platforms[2].y
						+ platforms[2].height
				&& p.getX() >= platforms[2].x
				&& p.getX() + p.loadPic().getWidth() < platforms[2].x
						+ platforms[2].width) {
			return true;
		} else if (p.getY() + p.loadPic().getHeight() >= platforms[3].y
				&& p.getY() + p.loadPic().getHeight() <= platforms[3].y
						+ platforms[3].height
				&& p.getX() >= platforms[3].x
				&& p.getX() + p.loadPic().getWidth() < platforms[3].x
						+ platforms[3].width) {
			return true;
		}
		return false;
	}

	private boolean onPlatform(Enemy e) {
		// TODO Auto-generated method stub
		if (e.getY() + e.loadPic().getHeight() >= platforms[0].y
				&& e.getY() + e.loadPic().getHeight() <= platforms[0].y
						+ platforms[0].height
				&& e.getX() >= platforms[0].x
				&& e.getX() + e.loadPic().getWidth() < platforms[0].x
						+ platforms[0].width) {
			return true;
		} else if (e.getY() + e.loadPic().getHeight() >= platforms[1].y
				&& e.getY() + e.loadPic().getHeight() <= platforms[1].y
						+ platforms[1].height
				&& e.getX() >= platforms[1].x
				&& e.getX() + e.loadPic().getWidth() < platforms[1].x
						+ platforms[1].width) {
			return true;
		} else if (e.getY() + e.loadPic().getHeight() >= platforms[2].y
				&& e.getY() + e.loadPic().getHeight() <= platforms[2].y
						+ platforms[2].height
				&& e.getX() >= platforms[2].x
				&& e.getX() + e.loadPic().getWidth() < platforms[2].x
						+ platforms[2].width) {
			return true;
		} else if (e.getY() + e.loadPic().getHeight() >= platforms[3].y
				&& e.getY() + e.loadPic().getHeight() <= platforms[3].y
						+ platforms[3].height
				&& e.getX() >= platforms[3].x
				&& e.getX() + e.loadPic().getWidth() < platforms[3].x
						+ platforms[3].width) {
			return true;
		}
		return false;
	}

	private void checkKeysPressed(final int key) {
		// TODO Auto-generated method stub
		Thread keysPressed = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (key == 37) {// left
					p.setX(p.getX() - rate);
					if (walkCounter % 2 != 0) {
						p.type("gwL01");
					} else {
						p.type("gwL02");
					}
				}
				if (key == 39) {// right
					p.setX(p.getX() + rate);
					if (walkCounter % 2 != 0)
						p.type("gwR01");
					else
						p.type("gwR02");
				}
				if (key == 37) {// left
					p.setX(p.getX() - rate);
					if (walkCounter % 2 != 0) {
						p.type("gwL01");
					} else {
						p.type("gwL02");
					}
				}
				if (key == 39) {// right
					p.setX(p.getX() + rate);
					if (walkCounter % 2 != 0)
						p.type("gwR01");
					else
						p.type("gwR02");
				}
				if (p.getX() < -100)
					p.setX(getWidth());
				else if (p.getX() > getWidth())
					p.setX(-30);
				if (key == 38)
					jump = true;
			}

		});
		keysPressed.start();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Intro i = new Intro();
	}

}
