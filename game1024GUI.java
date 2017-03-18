package game1024;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;                

public class game1024GUI extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private int height, width, winningValue;
	private NumberSlider game;
	
	JLabel[][] tiles;
	
	JLabel up;
	JLabel down;
	JLabel left;
	JLabel right;
	
	JLabel exit;
	JLabel reset;
	JLabel resize;
	JLabel undo;
	
	public game1024GUI()
	{
		game = new NumberGame();
		height = 4;
		width = 4;
		winningValue = 1024;
		
		if (game == null) {
            System.err.println ("*---------------------------------------------*");
            System.err.println ("     You must first modify the GUI program.   *");
            System.err.println ("*---------------------------------------------*");
            System.exit(0xE0);
        }
        game.resizeBoard(height, width, winningValue);
        tiles = new JLabel[height][width];
        
		BorderLayout theLayout = new BorderLayout();
		this.setLayout(theLayout);
		
		JPanel tilePanel = new JPanel();
		GridLayout tileGrid = new GridLayout(height,width);
		tilePanel.setBackground(Color.GRAY);
		tilePanel.setPreferredSize(new Dimension(400,400));
		tilePanel.setLayout(tileGrid);
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				tiles[i][j] = new JLabel("0");
				tilePanel.add(tiles[i][j]);
			}
		}
		
		JPanel buttonPanel = new JPanel();
		
		JButton up = new JButton("Move Up");
		buttonPanel.add(up);
		up.addActionListener(this);
		
		JButton down = new JButton("Move Down");
		buttonPanel.add(down);
		down.addActionListener(this);
		
		JButton left = new JButton("Move Left");
		buttonPanel.add(left);
		left.addActionListener(this);
		
		JButton right = new JButton("Move Right");
		buttonPanel.add(right);
		right.addActionListener(this);
		
		JPanel statusPanel = new JPanel();
		
		JButton exit = new JButton("Exit Game");
		statusPanel.add(exit);
		exit.addActionListener(this);
		
		JButton reset = new JButton("Reset Game");
		statusPanel.add(reset);
		reset.addActionListener(this);
		
		JButton resize = new JButton("Resize Game");
		statusPanel.add(resize);
		resize.addActionListener(this);
		
		JButton undo = new JButton("Undo Move");
		statusPanel.add(undo);
		undo.addActionListener(this);
		
		
		this.add(tilePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.EAST);
		this.add(statusPanel, BorderLayout.WEST);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JComponent buttonPressed = (JComponent) e.getSource();
		
		if(buttonPressed == up)
			game.slide(SlideDirection.UP);

		else if(buttonPressed == down)
			game.slide(SlideDirection.DOWN);

		else if(buttonPressed == left)
			game.slide(SlideDirection.LEFT);

		else if(buttonPressed == right)
			game.slide(SlideDirection.RIGHT);
		
		if(buttonPressed == exit)
		{
			switch (game.getStatus()) {
            case IN_PROGRESS:
                System.out.println ("Thanks for playing!");
                break;
            case USER_WON:
                System.out.println ("Yahtzee! Wait wrong game...");
                break;
            case USER_LOST:
                System.out.println ("Sorry... You Lost!");
                break;
			}
		}
		
		else if(buttonPressed == reset)
			game.reset();
		
		else if(buttonPressed == resize)
			game.resizeBoard(height, width, winningValue);
		
		else if(buttonPressed == undo)
			game.undo();

	}
	
	/**
	 * Main function to start GUI, will set the panel to attach to JFrame,
	 * set the close operation, and pack the contents together. All before setting 
	 * the GUI to visible and allowing it to be used.
	 * @param args
	 */
	public static void main(String args[])
	{
		game1024GUI game = new game1024GUI();
	}

}
