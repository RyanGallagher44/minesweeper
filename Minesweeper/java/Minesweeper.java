import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Minesweeper extends JPanel implements ActionListener,MouseListener {
	
	JFrame frame;
	JMenuBar menuBar;
	JMenu game;
	JMenuItem beginner;
	JMenuItem intermediate;
	JMenuItem expert;
	JMenu icons;
	JMenuItem msIcons;
	JMenuItem sbIcons;
	JMenuItem hIcons;
	JButton controls;
	int dimRow = 9, dimCol = 9;
	int numMines = 10;
	JPanel panel;
	JToggleButton[][] togglers;
	JPanel scoreboard;
	JLabel score;
	JButton reset;
	ImageIcon smile;
	JLabel time;
	ImageIcon flag;
	ImageIcon mine;
	boolean firstClick = true;
	boolean end = false;
	int[][] mines;
	JPanel topPanel;
	int startRow;
	int startCol;
	int difficulty = 0;
	Timer timer;
	int seconds;
	int selectedIcons = 0;
	int numToggled;
	boolean won = false;
	
	public Minesweeper() {
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(dimRow*60,dimCol*60);
		menuBar = new JMenuBar();
		game = new JMenu("Game");
		icons = new JMenu("Icons");
		controls = new JButton("Controls");
		controls.addActionListener(this);
		beginner = new JMenuItem("Beginner");
		beginner.addActionListener(this);
		intermediate = new JMenuItem("Intermediate");
		intermediate.addActionListener(this);
		expert = new JMenuItem("Expert");
		expert.addActionListener(this);
		msIcons = new JMenuItem("Microsoft Icons");
		msIcons.addActionListener(this);
		sbIcons = new JMenuItem("Spongebob Icons");
		sbIcons.addActionListener(this);
		hIcons = new JMenuItem("Hockey Icons");
		hIcons.addActionListener(this);
		game.add(beginner);
		game.add(intermediate);
		game.add(expert);
		icons.add(msIcons);
		icons.add(sbIcons);
		icons.add(hIcons);
		menuBar.add(game);
		menuBar.add(icons);
		menuBar.add(controls);
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2,1));
		scoreboard = new JPanel();
		scoreboard.setLayout(new GridLayout(1,3));
		score = new JLabel("                           "+numMines+"");
		reset = new JButton();
		timer = new Timer(1000,this);
		seconds = 0;
		time = new JLabel("                           "+seconds+"");
		scoreboard.add(score);
		reset.addActionListener(this);
		scoreboard.add(reset);
		scoreboard.add(time);
		topPanel.add(menuBar);
		topPanel.add(scoreboard);
		frame.add(topPanel,BorderLayout.NORTH);
		togglers = new JToggleButton[dimRow][dimCol];
		panel = new JPanel();
		panel.setLayout(new GridLayout(dimRow,dimCol));
		for(int i = 0; i < togglers.length; i++) {
			for(int j = 0; j < togglers[0].length; j++) {
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		mines = new int[dimRow][dimCol];
		mine = new ImageIcon("mine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance((frame.getWidth()/dimCol)+1, (frame.getHeight()/dimRow)+1, Image.SCALE_SMOOTH));
		frame.add(panel,BorderLayout.CENTER);
		flag = new ImageIcon("flag.png");
		flag = new ImageIcon(flag.getImage().getScaledInstance((frame.getWidth()/dimCol)+1, (frame.getHeight()/dimRow)+1, Image.SCALE_SMOOTH));
		smile = new ImageIcon("smile.png");
		smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
		reset.setIcon(smile);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Minesweeper mineSweeper = new Minesweeper();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	public void expand(int i, int j) {
		if(togglers[i][j].getIcon() == null) {
			togglers[i][j].setSelected(true);
			if(mines[i][j] > 0) {
				togglers[i][j].setText(mines[i][j]+"");
			}
			if(mines[i][j] == 0) {
				for(int k = i-1; k <= i+1; k++) {
					for(int l = j-1; l <= j+1; l++) {
						if(k > -1 && l > -1 && k < dimRow && l < dimCol) {
							if(!togglers[k][l].isSelected()) {
								expand(k,l);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!end) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				for(int i = 0; i < togglers.length; i++) {
					for(int j = 0; j < togglers[0].length; j++) {
						if(e.getSource()==togglers[i][j]) {
							if(!togglers[i][j].isSelected() && togglers[i][j].getIcon() == null) {
								if(numMines > 0) {
									togglers[i][j].setSelected(true);
									togglers[i][j].setIcon(flag);
									numMines--;
								}
							}else {
								togglers[i][j].setSelected(false);
								togglers[i][j].setIcon(null);
								numMines++;
							}
							score.setText("                           "+numMines+"");
						}
					}
				}
			}
			numToggled = 0;
			for(int i = 0; i < togglers.length; i++) {
				for(int j = 0; j < togglers[0].length; j++) {
					if(togglers[i][j].isSelected()) {
						numToggled++;
					}
				}
			}
			int minesFlagged = 0;
			if(numToggled == togglers.length*togglers[0].length) {
				for(int i = 0; i < mines.length; i++) {
					for(int j = 0; j < mines[0].length; j++) {
						if(mines[i][j] == 9 && togglers[i][j].getIcon() == flag) {
							minesFlagged++;
						}
					}
				}
			}
			if(minesFlagged == numMines) {
				won = true;
			}
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(firstClick) {
					timer.start();
					for(int i = 0; i < togglers.length; i++) {
						for(int j = 0; j < togglers[0].length; j++) {
							if(e.getSource() == togglers[i][j]) {
								startRow = i;
								startCol = j;
							}
						}
					}
					int minesPlanted = 0;
					while(minesPlanted < numMines) {
						int randRow = (int)(Math.random()*dimRow);
						int randCol = (int)(Math.random()*dimCol);
						if(mines[randRow][randCol] == 0 && randRow != startRow && randCol != startCol) {
							mines[randRow][randCol] = 9;
							minesPlanted++;
						}
					}
					firstClick = false;
				}
				for(int i = 0; i < mines.length; i++) {
					for(int j = 0; j < mines[0].length; j++) {
						if(mines[i][j] != 9) {
							int numSurroundingMines = 0;
							for(int k = i-1; k <= i+1; k++) {
								for(int l = j-1; l <= j+1; l++) {
									if(k > -1 && l > -1 && k < dimRow && l < dimCol && mines[k][l] == 9) {
										numSurroundingMines++;
									}
								}
							}
							mines[i][j] = numSurroundingMines;
						}
					}
				}
				for(int i = 0; i < togglers.length; i++) {
					for(int j = 0; j < togglers[0].length; j++) {
						if(e.getSource()==togglers[i][j]) {
							if(mines[i][j] != 9) {
								expand(i,j);
							}
							if(mines[i][j] == 9) {
								togglers[i][j].setIcon(mine);
								timer.stop();
								if(selectedIcons == 0) {
									smile = new ImageIcon("smile.png");
									smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
								}
								if(selectedIcons == 1) {
									smile = new ImageIcon("planktonsad.png");
									smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
								}
								if(selectedIcons == 2) {
									smile = new ImageIcon("hockeysad.png");
									smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
								}
								reset.setIcon(smile);
								end = true;
							}
						}
					}
				}
			}
			if(end) {
				for(int i = 0; i < togglers.length; i++) {
					for(int j = 0; j < togglers[0].length; j++) {
						togglers[i][j].setSelected(true);
						if(mines[i][j] != 9) {
							togglers[i][j].setEnabled(false);
						}
						if(mines[i][j] == 9) {
							togglers[i][j].setIcon(mine);
						}
					}
				}
			}
			if(won) {
				if(selectedIcons == 1) {
					smile = new ImageIcon("planktonhappy.png");
					smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
				}
				if(selectedIcons == 2) {
					smile = new ImageIcon("hockeyhappy.png");
					smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
				}
				reset.setIcon(smile);
				score.setText("                           YOU WIN!");
			}
		}
	}
	
	public void changeDifficulty(int r, int c, int m, int w, int h) {
		timer.stop();
		firstClick = true;
		end = false;
		dimRow = r;
		dimCol = c;
		numMines = m;
		score.setText("                           "+numMines+"");
		seconds = 0;
		time.setText("                           "+seconds+"");
		frame.setSize(dimRow*w,dimCol*h);
		frame.remove(panel);
		mines = new int[dimRow][dimCol];
		togglers = new JToggleButton[dimRow][dimCol];
		panel = new JPanel();
		panel.setLayout(new GridLayout(dimRow,dimCol));
		for(int i = 0; i < togglers.length; i++) {
			for(int j = 0; j < togglers[0].length; j++) {
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		frame.add(panel,BorderLayout.CENTER);
		if(selectedIcons == 0) {
			setTheme("flag.png","mine.png","smile.png");
		}
		if(selectedIcons == 1) {
			setTheme("trap.png","krabs.jpg","planktonnormal.png");
		}
		if(selectedIcons == 2) {
			setTheme("net.png","puck.png","hockeynormal.png");
		}
		frame.revalidate();
	}
	
	public void beginner() {
		changeDifficulty(9,9,10,60,60);
	}

	public void intermediate() {
		changeDifficulty(16,16,40,60,60);
	}
	
	public void expert() {
		changeDifficulty(16,30,99,120,32);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == beginner) {
			beginner();
			difficulty = 0;
		}
		if(e.getSource() == intermediate) {
			intermediate();
			difficulty = 1;
		}
		if(e.getSource() == expert) {
			expert();
			difficulty = 2;
		}
		if(e.getSource() == reset) {
			timer.restart();
			if(difficulty == 0) {
				beginner();
			}
			if(difficulty == 1) {
				intermediate();
			}
			if(difficulty == 2) {
				expert();
			}
			if(selectedIcons == 0) {
				setTheme("flag.png","mine.png","smile.png");
			}
			if(selectedIcons == 1) {
				setTheme("trap.png","krabs.jpg","planktonnormal.png");
			}
			if(selectedIcons == 2) {
				setTheme("net.png","puck.png","hockeynormal.png");
			}
		}
		if(e.getSource() == timer) {
			seconds++;
			time.setText("                           "+seconds+"");
		}
		if(e.getSource() == msIcons) {
			selectedIcons = 0;
			setTheme("flag.png","mine.png","smile.png");
		}
		if(e.getSource() == sbIcons) {
			selectedIcons = 1;
			setTheme("trap.png","krabs.jpg","planktonnormal.png");
		}
		if(e.getSource() == hIcons) {
			selectedIcons = 2;
			setTheme("net.png","puck.png","hockeynormal.png");
		}
		if(e.getSource() == controls) {
			JOptionPane.showMessageDialog(frame, "Reveal Empty Square/Expand - Left Click\nFlag Potential Mine - Right Click", "Controls", JOptionPane.INFORMATION_MESSAGE);
		}
		repaint();
	}
	
	public void setTheme(String f, String m, String s) {
		flag = new ImageIcon(f);
		flag = new ImageIcon(flag.getImage().getScaledInstance(frame.getWidth()/dimCol, frame.getHeight()/dimRow, Image.SCALE_SMOOTH));
		mine = new ImageIcon(m);
		mine = new ImageIcon(mine.getImage().getScaledInstance((frame.getWidth()/dimCol)+1, (frame.getHeight()/dimRow)+1, Image.SCALE_SMOOTH));
		smile = new ImageIcon(s);
		smile = new ImageIcon(smile.getImage().getScaledInstance((frame.getWidth()/(dimCol*2))+1, (frame.getHeight()/(dimRow*2))+1, Image.SCALE_SMOOTH));
		reset.setIcon(smile);
		frame.revalidate();
	}
	
}