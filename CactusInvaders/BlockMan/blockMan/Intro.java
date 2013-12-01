package blockMan;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class Intro extends JPanel{

	private JFrame f = new JFrame();
	private boolean startGame = false;
	private Timer introT;
	private int introC = 0;
	private String introText = "";
	private String[] intro = { "In This World of Turmoil...",
			"A Hero Must Rise...", "Against...", "Cactus Invaders" };
	private BasicStroke bs = new BasicStroke(10);
	private int fontSize = 100;
	private Random r = new Random();

	public Intro() {

		introT = new Timer(2500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (startGame == false) {
					if (introC < intro.length) {
						introText = intro[introC];
						introC++;
					}
				}
			}

		});
		introT.start();
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				startGame = true;
			}
		});

		f.add(this);
		this.setBackground(Color.black);
		f.setUndecorated(true);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Toolkit.getDefaultToolkit().getScreenSize());

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (startGame == false) {
			g2.setStroke(bs);
			g2.setColor(Color.darkGray);
			g2.setColor(new Color(150, 150, r.nextInt(100)));
			g2.setFont(new Font("Comic Sans", Font.BOLD, 50));
			if (introC == intro.length) {
				g2.setFont(new Font("Comic Sans", Font.BOLD, fontSize));
				g2.drawString(introText, 10, getHeight() / 2);
				if (fontSize <= 150) {
					fontSize++;
				}
				g2.setFont(new Font("Comic Sans", Font.BOLD, 50));
				g2.drawString("Do Not Falter! ---- Click To Begin Game",
						getWidth() / 2 - 300, getHeight() / 3);
			} else {
				g2.setFont(new Font("Comic Sans", Font.BOLD, 50));
				g2.drawString(introText, getWidth() / 2 - 300, getHeight() / 2);
			}
			repaint();
		}
		else{
			CactusInvaders bm = new CactusInvaders();
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setVisible(false);
		}
	}
	
	public boolean runGame (){
		return startGame;
	}

}
