import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Main JFrame program for the entire game
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class GameMain extends JFrame
{
	GamePanel game;

	// Declares the constants for the game
	// Variables for any runtime errors
	public static final int MISSING_SAVE_FILE = 0;
	public static final int MISSING_SECOND_PORTAL = 1;
	public static final int MISSING_IMAGE = 2;
	public static final int WORLD_FILE_NOT_FOUND = 3;
	public static final int NO_PLAYER = 4;
	public static final int CORRUPT_SAVE_FILE = 5;

	// Constants which adjust the gameplay
	public static final int LASER_RANGE = 3;
	public static final int ANIMATE_SPEED = 60;
	public static final double IMAGE_SCALE_FACTOR = 1;

	// Reference variables which correspond with the chosen difficulty
	public static final int EASY = 1;
	public static final int NORMAL = 2;
	public static final int HARD = 3;

	/**
	 * Creates a GameMain object
	 */
	public GameMain()
	{
		// Sets up the frame and grid, defines the name and icon for the game
		super("Lab Escape");
		setIconImage(new ImageIcon("export" + File.separator
				+ "chemicalspillnight.png").getImage());
		setResizable(false);
		setLocation(500, 10);

		// Generates a new main menu and displays the game on the screen
		game = new GamePanel(this);
		add(game, BorderLayout.CENTER);
		this.addKeyListener(game);
		this.requestFocusInWindow();
		this.setFocusable(true);
		this.setVisible(true);
	}

	public static void main(String[] args) throws IOException
	{
		// Sets up the main frame for the game
		// Loads a MainMenu by default
		GameMain theFrame = new GameMain();
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setPreferredSize(new Dimension(
				(int) (800 * IMAGE_SCALE_FACTOR),
				(int) (800 * IMAGE_SCALE_FACTOR)));
		theFrame.pack();
		theFrame.setVisible(true);
	}

	/**
	 * Creates a JOptionPane to show error messages in runtime errors. Typically
	 * when a file is corrupt or cannot be found
	 * @param errorCode the specific type of error
	 */
	public static void runtimeError(int errorCode)
	{
		// Handles any run time errors (We incorporated some mildly humorous
		// messages for fun, and player's enjoyment)
		if (errorCode == MISSING_SAVE_FILE)
			JOptionPane
					.showMessageDialog(
							null,
							"Your save file has vanished in an accident involving monkeys and a Delete key.\nCreate a file named userdata.txt in the root directory and try again.",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
		else if (errorCode == MISSING_SECOND_PORTAL)
			JOptionPane
					.showMessageDialog(
							null,
							"The second portal has vanished in an accident involving cheesecake and a rip in spacetime.\nNo worries, contact developers ASAP, or fix the map yourself.",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
		else if (errorCode == MISSING_IMAGE)
			JOptionPane
					.showMessageDialog(
							null,
							"An image (or more) went missing in an existential crisis.\nPlease recover the images as soon possible.\nAt no point in time are the creators liable for psychiatric fees.",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
		else if (errorCode == WORLD_FILE_NOT_FOUND)
			JOptionPane
					.showMessageDialog(
							null,
							"Our surveillance cameras have detected the theft of a world file in an incident involving a camel and a chandelier.\nPlease take greater care to secure your property in the future.",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
		else if (errorCode == NO_PLAYER)
			JOptionPane
					.showMessageDialog(
							null,
							"The level file that you are trying to load contains no player.\nThis could be due to experiments with the drone robots that are going to take over the world.\nIf you have been messing with the files, please stop. If else, sorry.",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
		else if (errorCode == CORRUPT_SAVE_FILE)
			JOptionPane
					.showMessageDialog(
							null,
							"The level file you are trying to load is corrupt. I told you not to let the cat touch it!\nYou never listen... sighs. Oh well. What's there to do other than weep?\nWe are not responsible for any lost progress",
							"WHOOPS, Something Went Wrong!",
							JOptionPane.ERROR_MESSAGE);
	}
}
