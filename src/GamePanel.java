import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.ReadPendingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * JPanel, main drawing area, and main operation code of the game.
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class GamePanel extends JPanel implements KeyListener
{
	private GameMain theMain;
	private Player player;

	// Program constants
	// Defines the rectangular hot spot for main menu buttons
	private final Rectangle NEW_GAME_BUTTON = new Rectangle(
			(int) (458 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (283 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (55 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle LOAD_GAME_BUTTON = new Rectangle(
			(int) (459 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (374 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (55 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle INSTRUCTIONS = new Rectangle(
			(int) (459 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (467 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (55 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle EXIT_BUTTON = new Rectangle(
			(int) (460 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (563 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (55 * GameMain.IMAGE_SCALE_FACTOR));

	// Defines hot spots for the next and previous button graphics used on the
	// instructions menu
	private final Rectangle NEXT_BUTTON = new Rectangle(
			(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (38 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle PREV_BUTTON = new Rectangle(
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (38 * GameMain.IMAGE_SCALE_FACTOR));

	// Defines hot spots for the back button use by the level selection,
	// difficulty selection and instruction menus
	final Rectangle BACK_BUTTON = new Rectangle(
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (700 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (227 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (53 * GameMain.IMAGE_SCALE_FACTOR));

	// Defines hot spots for the difficulty selection's button graphics
	private final Rectangle BEGINNER = new Rectangle(
			(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (184 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (466 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (77 * GameMain.IMAGE_SCALE_FACTOR)
			);
	private final Rectangle NORMAL = new Rectangle(
			(int) (361 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (361 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (466 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (77 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle HARD = new Rectangle(
			(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (538 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (466 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (77 * GameMain.IMAGE_SCALE_FACTOR));

	// Defines hot spots for for pause/game over menu
	private final Rectangle RESUME = new Rectangle(
			(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (150 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle RESTART_RETRY = new Rectangle(
			(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (300 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle LEVELS = new Rectangle(
			(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (450 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR));
	private final Rectangle MAIN_MENU = new Rectangle(
			(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
			(int) (100 * GameMain.IMAGE_SCALE_FACTOR));

	final static int NO_OF_LEVELS = 20;

	// The directions used within the game
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	// Program variables
	// Declares all main menu graphic variables
	private static Image imageBackground, imageNewGame2, imageLoadGame2,
			imageHelp2, imageNext, imageNext2, imagePrevious, imagePrevious2,
			imageExitScreen2, imageBack,
			imageBack2, imageLevelSelectBG, endScreen;
	private boolean highlightNewGame, highlightLoadGame, highlightHelp,
			highlightExit, highlightNext, highlightPrev,
			highlightBeginner, highlightMedium, highlightHard, highlightBack,
			levelSelect, difficultySelect, menuMode, paused, gameOver,
			resumeHighlight, restartRetryHighlight,
			levelsHighlight, mMenuHighlight, displayEndScreen;
	private static boolean[] unlockedLevels = new boolean[21];
	private int helpScreen;

	// Declares all arrays, (for loading the world, images, images for menus,
	// and images for the side bar), which will be used throughout the program
	// Keeps track of board contents
	private char[][] world;
	private Image[] images = new Image[26];
	private Image[] sidebarImages = new Image[9];
	private Image[] pauseMenuImages = new Image[12];
	private ArrayList<Point> spills = new ArrayList<Point>();
	private static Image[] helpImages = new Image[6];
	private static Image[] difficultyImages = new Image[7];
	private boolean[] hasStoryline = new boolean[21];
	// It is highly unlikely that a map will be Integer.MAX_VALUE tiles large.
	// Therefore, these are set as such so that if a second portal cannot be
	// found, entities will not be teleported to 0,0
	private int[][] portalDirectory = {
			{ Integer.MAX_VALUE, Integer.MAX_VALUE },
			{ Integer.MAX_VALUE, Integer.MAX_VALUE } };
	private int currentLevel, difficulty, iconSize, achievedLevel;

	/**
	 * Creates a new GamePanel Object
	 * @param theMain the JFrame to reference
	 */
	public GamePanel(GameMain theMain)
	{
		// Setup
		this.theMain = theMain;
		menuMode = true;
		theMain.setPreferredSize(new Dimension(
				(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
				(int) (800 * GameMain.IMAGE_SCALE_FACTOR)));

		// Loads the array for unlocked levels
		readUserData();

		/*
		 * Loads images. The images' indexes correspond to their letter codes on
		 * the array and file. File.seperator is used here instead of \ or / to
		 * resolve complications between UNIX and DOS systems
		 */
		imageBackground = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "OpeningScreen.png")
				.getImage();
		imageNewGame2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "NewGame2.png").getImage();
		imageLoadGame2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "loadGame2.png").getImage();
		imageHelp2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "help2.png").getImage();
		imageExitScreen2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "exitScreen2.png")
				.getImage();
		imageNext = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "NextArrow.png").getImage();
		imageNext2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "NextArrowGlow.png")
				.getImage();
		imagePrevious = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "PrevArrow.png").getImage();
		imagePrevious2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "prevArrowGlow.png")
				.getImage();
		imageLevelSelectBG = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "SelectionBG.png")
				.getImage();
		imageBack = new ImageIcon("export" + File.separator + "Menu Images"
				+ File.separator + "backButton.png").getImage();
		imageBack2 = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "backButton2.png")
				.getImage();
		endScreen = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "endScreen.png")
				.getImage();

		// Difficulty setting Images
		difficultyImages[0] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "BlackBG.png").getImage();
		difficultyImages[1] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "BeginnerOpt.png")
				.getImage();
		difficultyImages[2] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "BeginnerOpt2.png")
				.getImage();
		difficultyImages[3] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "MediumOpt.png").getImage();
		difficultyImages[4] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "MediumOpt2.png").getImage();
		difficultyImages[5] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "HardOpt.png").getImage();
		difficultyImages[6] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "HardOpt2.png").getImage();

		// Instructions screen Images
		helpImages[1] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "Instructions"
				+ File.separator + "instructions1.png")
				.getImage();
		helpImages[2] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "Instructions"
				+ File.separator + "instructions2.png")
				.getImage();
		helpImages[3] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "Instructions"
				+ File.separator + "instructions3.png")
				.getImage();
		helpImages[4] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "Instructions"
				+ File.separator + "instructions4.png")
				.getImage();
		helpImages[5] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "Instructions"
				+ File.separator + "instructions5.png")
				.getImage();

		// Declares which levels have animations
		hasStoryline[1] = true;
		hasStoryline[2] = true;
		hasStoryline[3] = true;
		hasStoryline[5] = true;
		hasStoryline[10] = true;
		hasStoryline[15] = true;
		hasStoryline[20] = true;

		// Creates each Level Button as an Object and adds it to the list of
		// Level Buttons
		for (int createLevelButton = 1; createLevelButton < NO_OF_LEVELS + 1; createLevelButton++)
		{
			LevelButton levelButton = new LevelButton(createLevelButton,
					unlockedLevels[createLevelButton]);
			LevelButton.getButtonDirectory().add(levelButton);
		}

		// Imports all images into an array for day and night levels,
		// respectively.
		if (currentLevel > 5 && currentLevel < 11)
		{
			images[0] = new ImageIcon("export" + File.separator + "aNight.png")
					.getImage();
			images[1] = new ImageIcon("export" + File.separator + "b.png")
					.getImage();
			images[2] = new ImageIcon("export" + File.separator
					+ "bBottomNight.png").getImage();
			images[3] = new ImageIcon("export" + File.separator
					+ "laserbeam.png").getImage();
			images[4] = new ImageIcon("export" + File.separator + "laser0.png")
					.getImage();
			images[5] = new ImageIcon("export" + File.separator + "laser1.png")
					.getImage();
			images[6] = new ImageIcon("export" + File.separator + "laser2.png")
					.getImage();
			images[7] = new ImageIcon("export" + File.separator + "laser3.png")
					.getImage();
			images[8] = new ImageIcon("export" + File.separator
					+ "chemicalspillnight.png").getImage();
			images[9] = new ImageIcon("export" + File.separator
					+ "mopnight.png")
					.getImage();
			images[10] = new ImageIcon("export" + File.separator
					+ "shieldnight.png").getImage();
			images[11] = new ImageIcon("export" + File.separator + "k2.png")
					.getImage();
			images[12] = new ImageIcon("export" + File.separator +
					"lever2night.png").getImage();
			images[13] = new ImageIcon("export" + File.separator
					+ "portal1.png").getImage();
			images[14] = new ImageIcon("export" + File.separator
					+ "laserbeamVert.png").getImage();
			images[23] = new ImageIcon("export" + File.separator
					+ "batteryIcon.png").getImage();
			images[25] = new ImageIcon("export" + File.separator
					+ "zNight.png").getImage();
		}
		else
		{
			images[0] = new ImageIcon("export" + File.separator + "aDay.png")
					.getImage();
			images[1] = new ImageIcon("export" + File.separator + "b.png")
					.getImage();
			images[2] = new ImageIcon("export" + File.separator
					+ "bBottomDay.png").getImage();
			images[3] = new ImageIcon("export" + File.separator
					+ "laserbeam.png").getImage();
			images[4] = new ImageIcon("export" + File.separator + "laser0.png")
					.getImage();
			images[5] = new ImageIcon("export" + File.separator + "laser1.png")
					.getImage();
			images[6] = new ImageIcon("export" + File.separator + "laser2.png")
					.getImage();
			images[7] = new ImageIcon("export" + File.separator + "laser3.png")
					.getImage();
			images[8] = new ImageIcon("export" + File.separator
					+ "chemicalspillday.png").getImage();
			images[9] = new ImageIcon("export" + File.separator + "mopday.png")
					.getImage();
			images[10] = new ImageIcon("export" + File.separator
					+ "shield day.png").getImage();
			images[11] = new ImageIcon("export" + File.separator + "k1.png")
					.getImage();
			images[12] = new ImageIcon("export" + File.separator +
					"lever2day.png").getImage();
			images[13] = new ImageIcon("export" + File.separator
					+ "portal1.png").getImage();
			images[14] = new ImageIcon("export" + File.separator
					+ "laserbeamVert.png").getImage();
			images[23] = new ImageIcon("export" + File.separator
					+ "batteryIcon.png").getImage();
			images[25] = new ImageIcon("export" + File.separator
					+ "zDay.png").getImage();
		}

		// Does the same for sidebar images
		sidebarImages[0] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "RobotBackward.png").getImage();
		sidebarImages[1] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "RobotRight.png").getImage();
		sidebarImages[2] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "RobotForward.png").getImage();
		sidebarImages[3] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "RobotLeft.png").getImage();
		sidebarImages[4] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "SideBarBG.png").getImage();
		sidebarImages[5] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "LGKey.png").getImage();
		sidebarImages[6] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "LGMop.png").getImage();
		sidebarImages[7] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "LGShield.png").getImage();
		sidebarImages[8] = new ImageIcon("export" + File.separator
				+ "Sidebar" + File.separator
				+ "slidingThing.png").getImage();

		// Does the same for game over/pause menu images
		pauseMenuImages[0] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "GamePauseBG.png")
				.getImage();
		pauseMenuImages[1] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "resume.png").getImage();
		pauseMenuImages[2] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "resume2.png").getImage();
		pauseMenuImages[3] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "backToLevels.png")
				.getImage();
		pauseMenuImages[4] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "backToLevels2.png")
				.getImage();
		pauseMenuImages[5] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "backToMain.png").getImage();
		pauseMenuImages[6] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "backToMain2.png")
				.getImage();
		pauseMenuImages[7] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "GameOverBG.png").getImage();
		pauseMenuImages[8] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "restart.png").getImage();
		pauseMenuImages[9] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "restart2.png").getImage();
		pauseMenuImages[10] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "retryButton.png")
				.getImage();
		pauseMenuImages[11] = new ImageIcon("export" + File.separator
				+ "Menu Images" + File.separator + "retryButton2.png")
				.getImage();

		// Gets the icon size from the first image to properly size the world,
		// so that images can be replaced and resized as needed
		iconSize = (int) (images[0].getWidth(null));

		// Initiates mouse listeners to the drawing panel
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());
	}

	/**
	 * Reads in the UserData file which is used to store player progress in the
	 * game
	 */
	public void readUserData()
	{
		Scanner dataFile;

		// Try/catch block in case file doesn't exist
		try
		{
			// The first digit of the text file is the difficulty, and the
			// following numbers is the level number
			dataFile = new Scanner(new File("userdata.txt"));
			String line = dataFile.nextLine();
			difficulty = Integer.parseInt(line.charAt(0) + "");
			achievedLevel = Integer.parseInt(line.substring(1, line.length()));
			dataFile.close();
		}
		catch (FileNotFoundException e)
		{
			GameMain.runtimeError(GameMain.MISSING_SAVE_FILE);
		}
		// Catches if the parseInt for the level number is not a number...
		// corrupt save file
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			GameMain.runtimeError(GameMain.CORRUPT_SAVE_FILE);
		}

		// Sets the booleans for unlocked to true as necessary
		for (int level = 1; level <= achievedLevel; level++)
		{
			unlockedLevels[level] = true;
		}
	}

	/**
	 * Loads the world in from file and sets up everything (creates player and
	 * guard objects, etc.) to be ready to play
	 * @param currentLvl level number; finds the appropriate world file from
	 *            folder
	 */
	private void loadWorld(int currentLvl)
	{
		// Resets arrays to make sure everything loads properly and there are no
		// artifacts from previous levels (it's complicated)
		Guard.setGuardDirectory(new ArrayList<Guard>());
		Door.setDoorDirectory(new ArrayList<Door>());
		Lever.setLeverDirectory(new ArrayList<Lever>());
		spills = new ArrayList<Point>();
		LaserTimer.stopLasers();
		Guard.stopGuardTimers();

		// Makes sure to display the game
		levelSelect = false;
		menuMode = false;
		helpScreen = 0;
		difficultySelect = false;

		// Finds the width and height to create the 2-D array
		Scanner lineCounter;
		try
		{
			// Imports the file to count lines
			lineCounter = new Scanner(new File("worlds" + File.separator
					+ "level"
					+ Integer.toString(currentLvl) + ".txt"));

			// Columns' count
			int width = lineCounter.nextLine().length();

			// Line count
			int lines = 1;
			while (lineCounter.hasNextLine())
			{
				lines++;
				lineCounter.nextLine();
			}
			world = new char[lines][width];
			lineCounter.close();

			// Imports the world's data into an array
			Scanner file = new Scanner(new File("worlds" + File.separator
					+ "level" + currentLvl
					+ ".txt"));

			/*
			 * The world will be surrounded by a border of walls, to make
			 * collision check easier, so the actual playable area is from 1, 1
			 * to (width-1), (length-1)
			 */
			// Used to store the locations of the portals
			int portalNo = 0;

			/*
			 * Because the file reader gets the data in lines then columns, it
			 * may read in a lever before its corresponding door. The ArrayList
			 * keeps track of these variables, in this order: ROW of the lever,
			 * COLUMN of the lever, ROW of its door, COLUMN of its door. The
			 * levers are then created in bulk after the entire file has been
			 * scanned, and the directories of the doors can then be found.
			 */
			ArrayList<int[]> leverCache = new ArrayList<int[]>();

			// Loops through every tile
			for (int line = 0; line < world.length; line++)
			{
				String fileLine = file.nextLine();

				for (int col = 0; col < fileLine.length(); col++)
				{
					// Uses the letter codes in the text file and reads them
					// into array. Tiles that do not require any action are
					// simply imported.
					if (fileLine.charAt(col) == 'a'
							|| fileLine.charAt(col) == 'b'
							|| fileLine.charAt(col) == 'z'
							|| fileLine.charAt(col) == 'k'
							|| fileLine.charAt(col) == 'j'
							|| fileLine.charAt(col) == 'l'
							|| fileLine.charAt(col) == 'x')
						world[line][col] = fileLine.charAt(col);

					// Each direction of a laser transmitter
					else if (fileLine.charAt(col) >= 'e'
							&& fileLine.charAt(col) <= 'h')
					{
						Entity laser;

						// Laser going up
						if (fileLine.charAt(col) == 'e')
						{
							// Creates a new laser for each direction
							laser = new Entity(this, line, col, UP);
							world[line][col] = 'e';
						}

						// Laser going right
						else if (fileLine.charAt(col) == 'f')
						{
							laser = new Entity(this, line, col, RIGHT);
							world[line][col] = 'f';
						}

						// Laser going down
						else if (fileLine.charAt(col) == 'g')
						{
							laser = new Entity(this, line, col, DOWN);
							world[line][col] = 'g';
						}

						// Laser going left
						else
						{
							laser = new Entity(this, line, col, LEFT);
							world[line][col] = 'h';
						}

						// Creates and starts the timer for each laser
						Timer timer = new Timer(5000 / currentLvl + 500
								/ difficulty, new LaserTimer(laser, this));
						LaserTimer.getLaserDirectory().add(timer);
						timer.start();

					}

					// "Chemical spill" - sets the array to blank and adds the
					// coordinates of the spill to the "spills" array
					else if (fileLine.charAt(col) == 'i')
					{
						spills.add(new Point(line, col));
						world[line][col] = 'a';
					}

					// Reads in levers
					else if (fileLine.charAt(col) == 'm')
					{
						// Imports the letter so that graphics can be rendered
						world[line][col] = 'm';

						// Support for "dummy levers" is included (these may not
						// control any doors)
						if (!(fileLine.charAt(col + 1) < '0' && fileLine
								.charAt(col + 1) > '9'))
						{
							/*
							 * The syntax for a lever's corresponding Door's
							 * location is as follows: aaamROW,COLUMN]aaa with a
							 * comma and square bracket separating values
							 */

							// Finds the row of the Door
							int findComma = 0;
							int findBracket = 0;
							try
							{
								while (fileLine.charAt(col + findComma) != ',')
									findComma++;

								// Finds the column of the door
								findBracket = findComma;
								while (fileLine.charAt(col + findBracket) != ']')
									findBracket++;
							}
							catch (StringIndexOutOfBoundsException e)
							{
								GameMain.runtimeError(GameMain.CORRUPT_SAVE_FILE);
								e.printStackTrace();
							}

							// Inserts this into the leverCache ArrayList to be
							// processed later
							int[] tempArray = {
									line,
									col,
									Integer.parseInt(fileLine.substring(
											col + 1, col + findComma)),
									Integer.parseInt(fileLine.substring(col
											+ findComma + 1, col + findBracket)) };
							leverCache.add(tempArray);
							fileLine = fileLine.substring(0, col)
									+ fileLine.substring(col + findBracket);
						}
					}

					// Adds portals to the directory and world (there are only
					// two)
					else if (fileLine.charAt(col) == 'n')
					{
						// Try/catch in case there are more than two portals in
						// the world
						try
						{
							portalDirectory[portalNo][0] = line;
							portalDirectory[portalNo][1] = col;
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							GameMain.runtimeError(GameMain.CORRUPT_SAVE_FILE);
						}
						portalNo++;
						world[line][col] = 'n';
					}

					// Loads doors into the world according to their direction
					// and state
					else if (fileLine.charAt(col) >= 'p'
							&& fileLine.charAt(col) <= 'w')
					{
						Door doorFromFile;
						if (fileLine.charAt(col) == 'p')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col), true);
						}
						else if (fileLine.charAt(col) == 'q')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col),
									true);
						}
						else if (fileLine.charAt(col) == 'r')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col), true);
						}
						else if (fileLine.charAt(col) == 's')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col), true);
						}
						else if (fileLine.charAt(col) == 't')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col), false);
						}
						else if (fileLine.charAt(col) == 'u')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col),
									false);
						}
						else if (fileLine.charAt(col) == 'v')
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col),
									false);
						}
						else
						{
							doorFromFile = new Door(this, line, col,
									fileLine.charAt(col),
									false);
						}

						// Adds doors to a directory for access later
						Door.getDoorDirectory().add(doorFromFile);
						world[line][col] = fileLine.charAt(col);
					}

					// Creates a new player and sets them at the start point
					else if (fileLine.charAt(col) == 'y')
					{
						player = new Player(this, line, col);

						// The start point is reset to a blank tile, even though
						// in the file, the start point is marked with 'y'
						world[line][col] = 'a';

						// Code to calculate where to draw the player at start
						player.setDrawX((int) (col * iconSize * GameMain.IMAGE_SCALE_FACTOR));
						player.setDrawY((int) (line * iconSize * GameMain.IMAGE_SCALE_FACTOR));
					}

					// Handles guards
					else if (fileLine.charAt(col) <= '9'
							&& fileLine.charAt(col) >= '1')
					{
						// New guard object created
						Guard guard = new Guard(this, line, col);
						Guard.getGuardDirectory().add(guard);

						// Starts the timer for calculating auto-movement
						Timer timer = new Timer(
								100 / (fileLine.charAt(col) - '0' + difficulty),
								guard);
						Guard.getGuardTimers().add(timer);
						timer.start();
						world[line][col] = 'a';

						// Finds the X,Y coordinates to paint the guards at
						// creation
						guard.setDrawX((int) (col * iconSize * GameMain.IMAGE_SCALE_FACTOR));
						guard.setDrawY((int) (line * iconSize * GameMain.IMAGE_SCALE_FACTOR));
					}
					// If there are any other characters (errors) in the text
					// document, then it defaults to a blank tile to be inserted
					else
						world[line][col] = 'a';

					// Refreshes the screen after every tile has been drawn
					repaint();
				}
			}

			// See top for explanation of why this is here
			for (int leverCreate = 0; leverCreate < leverCache.size(); leverCreate++)
			{
				// Goes through the DoorDirectory to find the corresponding
				// Door's coordinates
				int rowToFind = leverCache.get(leverCreate)[2];
				int colToFind = leverCache.get(leverCreate)[3];
				int doorsIndex = 0;
				while (doorsIndex < Door.getDoorDirectory().size()
						&& Door.getDoorDirectory().get(doorsIndex).getRow() != rowToFind
						&& Door.getDoorDirectory().get(doorsIndex).getCol() != colToFind)
					doorsIndex++;
				doorsIndex--;

				// Creates a new Lever object to handle the Door's toggling
				// movement
				Door doorToCreate = Door.getDoorDirectory().get(doorsIndex);
				Lever lever = new Lever(this, leverCache.get(leverCreate)[0],
						leverCache.get(leverCreate)[1], doorToCreate);
				doorToCreate.setLockState(true);
				Lever.getLeverDirectory().add(lever);
			}

			// Ensures the security of the save file
			file.close();
			repaint();
		}
		catch (FileNotFoundException e)
		{
			GameMain.runtimeError(GameMain.CORRUPT_SAVE_FILE);
		}

		// setPower needs the dimensions of the world to work properly, so it
		// has to be done after world has loaded

		player.setPower(world);
		// Resizes the world in order to fit the sidebar
		theMain.setPreferredSize(new Dimension(
				(int) ((world[0].length * iconSize + 300) * GameMain.IMAGE_SCALE_FACTOR),
				(int) ((world.length + 1) * iconSize * GameMain.IMAGE_SCALE_FACTOR)));
		theMain.pack();

		// Preps the world to be played
		paused = false;
		gameOver = false;
		repaint();
	}

	/**
	 * "Extends" and "retracts" lasers, all of length 3 (editing the LASER_RANGE
	 * constant is not recommended)
	 * @param laser specific laser object to operate on
	 * @param extending extending or retracting (boolean)
	 */
	public void extendRetractLaser(Entity laser, boolean extending)
	{
		// Changes tiles that are three ahead of the transmitter
		for (int extend = 1; extend <= GameMain.LASER_RANGE; extend++)
		{
			// Extending lasers (require different images depending on vertical
			// or horizontal
			if (extending)
			{
				// Laser pointing up
				if (laser.getDir() == UP)
				{
					// Try/catch block in case the laser goes out of the world
					// array's bounds
					try
					{
						// 'o' is the vertical laser image
						world[laser.getRow() - extend][laser.getCol()] = 'o';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing right
				else if (laser.getDir() == RIGHT)
				{
					try
					{
						// 'd' is the horizontal laser image
						world[laser.getRow()][laser.getCol() + extend] = 'd';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing down
				else if (laser.getDir() == DOWN)
				{
					try
					{
						world[laser.getRow() + extend][laser.getCol()] = 'o';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing left
				else if (laser.getDir() == LEFT)
				{
					try
					{
						world[laser.getRow()][laser.getCol() - extend] = 'd';
					}
					catch (Exception e)
					{
					}
				}
			}

			// For retracting lasers (sets the character to 'a')
			else
			{
				// Laser pointing up
				if (laser.getDir() == GamePanel.UP)
				{
					// Again, try/catch blocks in case the world file is not
					// working as intended
					try
					{
						world[laser.getRow() - extend][laser.getCol()] = 'a';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing right
				else if (laser.getDir() == GamePanel.RIGHT)
				{
					try
					{
						world[laser.getRow()][laser.getCol() + extend] = 'a';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing down
				else if (laser.getDir() == GamePanel.DOWN)
				{
					try
					{
						world[laser.getRow() + extend][laser.getCol()] = 'a';
					}
					catch (Exception e)
					{
					}
				}

				// Laser pointing left
				else if (laser.getDir() == GamePanel.LEFT)
				{
					try
					{
						world[laser.getRow()][laser.getCol() - extend] = 'a';
					}
					catch (Exception e)
					{
					}
				}
			}
			repaint();
		}

		// Checks for any collisions with the player (as long as the player
		// object exists to prevent red-text errors)
		if (player != null && (world[player.getRow()][player.getCol()] == 'd'
				|| world[player.getRow()][player.getCol()] == 'o')
				&& !player.hasArmour())

			// If collision detected and no armour is worn, the player fails the
			// level
			levelFail();
	}

	/**
	 * Uses the "teleport pads" to transfer any Entity instantly between two
	 * points
	 * @param entity the entity to teleport (this can be a guard or player)
	 */
	public void teleport(Entity entity)
	{
		// Gets the current row and column, and checks in the portalsDirectory
		// to find the other set of row+column
		int row = entity.getRow();
		int col = entity.getCol();

		// In case a second portal doesn't exist in the world (see the
		// declaration for this portalDirectory array for more information)
		try
		{
			if (portalDirectory[0][0] == row && portalDirectory[0][1] == col)
			{
				entity.setRow(portalDirectory[1][0]);
				entity.setCol(portalDirectory[1][1]);
			}
			else
			{
				entity.setRow(portalDirectory[0][0]);
				entity.setCol(portalDirectory[0][1]);
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			GameMain.runtimeError(GameMain.MISSING_SECOND_PORTAL);
		}

		// Recalculates the new coordinates on the grid and refreshes the JPanel
		entity.setDrawX((int) (entity.getCol() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
		entity.setDrawY((int) (entity.getRow() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
		repaint();
	}

	/**
	 * Method called upon successful completion of a level (advances to next
	 * level if appropriate). Edits the text file that contains the current
	 * progress of the player
	 */
	public void levelComplete()
	{
		// Stops movement in the world
		paused = true;

		// If the user has completed the game (at the end of level twenty, but
		// some support provided for adding your own)
		if (currentLevel == NO_OF_LEVELS)
		{
			displayEndScreen = true;
		}

		// If the user has not reached the end of the game...
		else
		{
			currentLevel++;

			// Only changes next text file if the record is greater than the
			// current
			// level
			if (achievedLevel <= currentLevel)
			{

				// Try/catch block in case the file cannot be found
				try
				{
					PrintWriter outFile = new PrintWriter(
							new FileWriter("userdata.txt"));
					outFile.print(difficulty
							+ Integer.toString(this.currentLevel++));
					outFile.close();
				}
				catch (IOException e)
				{
					GameMain.runtimeError(GameMain.MISSING_SAVE_FILE);
				}

				achievedLevel++;
			}

			// Loads next world
			loadWorld(currentLevel);
		}
	}

	/**
	 * Method called upon failure to complete a level, usually resulting in the
	 * destruction of the player's robot.
	 */
	public void levelFail()
	{
		repaint();
		// Stops movement
		gameOver = true;
		paused = true;
		// Pauses for a bit to allow the player to see the horrid mistake that
		// they made
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		repaint();
	}

	/**
	 * Activated every time a player or guard moves to check if a player has
	 * collided with a guard (and ends the level in the process)
	 */
	public void checkForGuardCollision()
	{
		// Loops through the ArrayList of guards and checks each for collision
		// with the player
		for (int guardNo = 0; guardNo < Guard.getGuardDirectory().size(); guardNo++)
		{
			// Fails the player if the row and column of the player matches
			// that of any guard
			if (player != null
					&& player.getCol() == Guard.getGuardDirectory()
							.get(guardNo)
							.getCol()
					&& player.getRow() == Guard.getGuardDirectory()
							.get(guardNo).getRow())
			{
				// Armour check
				if (player.hasArmour())
				{
					player.setArmour(false);
				}
//				else
//					levelFail();
			}
		}
	}

	/**
	 * Finds a direction for the guard to face the player using a basic slope
	 * calculation (WARNING: Do not put too many guards in one map, because this
	 * calculation could significantly slow the computer down)
	 * @param guard the guard to find its row and column
	 * @return a direction that the guard should turn to face the player
	 */
	public int facePlayer(Guard guard)
	{
		// In case division by zero
		double slope;
		try
		{
			slope = (guard.getRow() - player.getRow())
					/ (guard.getCol() - player.getCol());
		}
		catch (ArithmeticException e)
		{
			// If it will divide by zero, then randomly picks a slope
			slope = Math.random() * 2 - 1;
		}

		// Depending on the positioning of the guard vs the player and the
		// slope, it will choose a direction
		if (guard.getRow() < player.getRow() && Math.abs(slope) > 1)
			return UP;
		else if (guard.getCol() < player.getCol() && Math.abs(slope) < 1)
			return RIGHT;
		else if (guard.getRow() > player.getRow() && Math.abs(slope) > 1)
			return UP;
		else if (guard.getCol() > player.getCol() && Math.abs(slope) < 1)
			return LEFT;

		// If the row and/or column are the same
		else if (guard.getRow() == player.getRow())
		{
			if (guard.getCol() > player.getCol())
				return RIGHT;
			else
				return LEFT;
		}
		else
		{
			if (guard.getRow() > player.getRow())
				return UP;

			// Okay, if nothing works, then...
			else
				return DOWN;
		}
	}

	/**
	 * Graphics, paints all elements on the JPanel with try/catch blocks on all
	 * drawing code in case there are missing images
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (displayEndScreen)
			g.drawImage(endScreen, 0, 0,
					(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
					(int) (800 * GameMain.IMAGE_SCALE_FACTOR), this);

		// Draws for when the screen is displaying the main menu
		if (menuMode)
		{
			// Draws the background images first so that everything else is
			// above it
			g.drawImage(imageBackground, 0, 0,
					(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
					(int) (800 * GameMain.IMAGE_SCALE_FACTOR), this);

			// Draws the main screen buttons in either their highlighted form or
			// default (no change) form depending on where the mouse is situated
			if (highlightNewGame)
			{
				g.drawImage(imageNewGame2,
						(int) (458 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (283 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (55 * GameMain.IMAGE_SCALE_FACTOR), this);
			}
			if (highlightLoadGame)
			{
				g.drawImage(imageLoadGame2,
						(int) (459 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (374 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (55 * GameMain.IMAGE_SCALE_FACTOR), this);
			}
			if (highlightHelp)
			{
				g.drawImage(imageHelp2,
						(int) (459 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (467 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (55 * GameMain.IMAGE_SCALE_FACTOR), this);
			}
			if (highlightExit)
			{
				g.drawImage(imageExitScreen2,
						(int) (460 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (563 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (259 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (55 * GameMain.IMAGE_SCALE_FACTOR), this);
			}

			// Draws instructions screen
			// Instructions screen of 0 would indicate a screen that is not
			// displayed
			if (helpScreen > 0)
			{
				g.drawImage(helpImages[helpScreen], 0,
						(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (578 * GameMain.IMAGE_SCALE_FACTOR), null);
			}
			// Next button for the instructions screen
			if (helpScreen < 5 && helpScreen > 0)
			{
				if (highlightNext)
					g.drawImage(imageNext2,
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (38 * GameMain.IMAGE_SCALE_FACTOR), this);
				else
					g.drawImage(imageNext,
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (38 * GameMain.IMAGE_SCALE_FACTOR), this);
			}
			// Previous button for the instructions screen
			if (helpScreen <= 5 && helpScreen > 1)
			{
				if (highlightPrev)
				{
					g.drawImage(imagePrevious2,
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (38 * GameMain.IMAGE_SCALE_FACTOR), this);
				}
				else
					g.drawImage(imagePrevious,
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (38 * GameMain.IMAGE_SCALE_FACTOR), this);
			}

			// Difficulty selection screen
			if (difficultySelect)
			{
				// Draws background
				g.drawImage(
						difficultyImages[0],
						0,
						0,
						(int) (difficultyImages[0].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[0].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
						null);

				// Non-selected buttons for easy, normal, and hard respectively
				g.drawImage(
						difficultyImages[1],
						(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (184 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[1].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[1].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
						null);
				g.drawImage(
						difficultyImages[3],
						(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (361 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[3].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[3].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
						null);
				g.drawImage(
						difficultyImages[5],
						(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (538 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[5].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (difficultyImages[5].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
						null);

				// When mouse is in the rectangle area of these, then they will
				// "highlight", so draws the images for selected over top of the
				// normal buttons
				if (highlightBeginner)
					g.drawImage(
							difficultyImages[2],
							(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (184 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[2].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[2].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
							null);
				else if (highlightMedium)
					g.drawImage(
							difficultyImages[4],
							(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (361 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[4].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[4].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
							null);
				else if (highlightHard)
					g.drawImage(
							difficultyImages[6],
							(int) (167 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (538 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[6].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
							(int) (difficultyImages[6].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
							null);
			}

			// Level selection screen (load game)
			else if (levelSelect)
			{
				// Background drawn first
				g.drawImage(imageLevelSelectBG, 0, 0,
						(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (800 * GameMain.IMAGE_SCALE_FACTOR), null);

				// Each button in the buttonDirectory is drawn separately
				for (int buttonIndex = 0; buttonIndex < LevelButton
						.getButtonDirectory().size(); buttonIndex++)
				{
					// References so the code is easier to read
					LevelButton button = LevelButton
							.getButtonDirectory()
							.get(buttonIndex);

					// Arranges the numbers as a "base"
					g.drawImage(
							button.getImages()[0],
							(int) (button.getDrawX() * GameMain.IMAGE_SCALE_FACTOR),
							(int) (button.getDrawY() * GameMain.IMAGE_SCALE_FACTOR),
							(int) (80 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (50 * GameMain.IMAGE_SCALE_FACTOR),
							this);

					// Draws the "clicked" button based on the mouse on/off
					// state
					if (button.isHighlighted())
						g.drawImage(
								button.getImages()[1],
								(int) (button.getDrawX() * GameMain.IMAGE_SCALE_FACTOR),
								(int) (button.getDrawY() * GameMain.IMAGE_SCALE_FACTOR),
								(int) (80 * GameMain.IMAGE_SCALE_FACTOR),
								(int) (50 * GameMain.IMAGE_SCALE_FACTOR),
								this);

					// Draws the "locked" image over-top the level number if
					// needed
					if (!button.isUnlocked())
					{
						g.drawImage(
								button.getImages()[2],
								(int) (button.getDrawX() * GameMain.IMAGE_SCALE_FACTOR),
								(int) (button.getDrawY() * GameMain.IMAGE_SCALE_FACTOR),
								(int) (80 * GameMain.IMAGE_SCALE_FACTOR),
								(int) (50 * GameMain.IMAGE_SCALE_FACTOR),
								this);
					}
				}
			}
			// If the user is NOT on the Main Menu (elsewhere) it will draw
			// "back"
			// to return to the Main Menu
			if (levelSelect || helpScreen > 0 || difficultySelect)
			{
				if (highlightBack)
					g.drawImage(imageBack,
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (700 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (227 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (53 * GameMain.IMAGE_SCALE_FACTOR), null);
				else
					g.drawImage(imageBack2,
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (700 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (227 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (53 * GameMain.IMAGE_SCALE_FACTOR), null);
			}
		}

		// Means the game is actually running
		else
		{
			// Paints every tile
			for (int rows = 0; rows < world.length; rows++)
			{
				for (int columns = 0; columns < world[rows].length; columns++)
				{
					// Draw the empty tile first for everything on top,
					// according to
					// scale factor for smaller screens
					g.drawImage(
							images[0],
							(int) (columns * iconSize * GameMain.IMAGE_SCALE_FACTOR),
							(int) (rows
									* iconSize * GameMain.IMAGE_SCALE_FACTOR),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							null);
					// Special code to handle the bottom side of walls, so that
					// there is an oblique look to the game
					if (world[rows][columns] == 'b')
					{
						// So that ArrayOutOfBoundsException isn't thrown
						if (rows < world.length - 1)
							// These are possible tiles that there WOULD be a
							// special wall tile
							if (world[rows + 1][columns] == 'a'
									|| world[rows + 1][columns] == 'd'
									|| world[rows + 1][columns] == 'e'
									|| world[rows + 1][columns] == 'f' ||
									(world[rows + 1][columns] >= 'h'
									&& world[rows + 1][columns] <= 'o')
									|| world[rows + 1][columns] == 'r'
									|| world[rows + 1][columns] == 'x'
									|| world[rows + 1][columns] == 'z')

								g.drawImage(
										images[2],
										(int) (columns * iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (rows
												* iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
										null);

							// Paints regular (black) wall tiles
							else
								g.drawImage(
										images[1],
										(int) (columns * iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (rows
												* iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
										(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
										null);
						// Again, paints regular wall tiles for those at the
						// bottom
						// row of the world
						else
							g.drawImage(
									images[1],
									(int) (columns * iconSize * GameMain.IMAGE_SCALE_FACTOR),
									(int) (rows
											* iconSize * GameMain.IMAGE_SCALE_FACTOR),
									(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
									(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
									null);
					}

					// Other (non-wall and non-door) tiles are drawn here
					else if (world[rows][columns] < 'p'
							|| world[rows][columns] > 'w')
					{
						g.drawImage(
								images[(int) (world[rows][columns] - 'a')],
								(int) (columns * iconSize * GameMain.IMAGE_SCALE_FACTOR),
								(int) (rows
										* iconSize * GameMain.IMAGE_SCALE_FACTOR),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								null);
					}
				}
			}

			if (player != null)
			{
				// Draws doors
				for (int doorIndex = 0; doorIndex < Door.getDoorDirectory()
						.size(); doorIndex++)
				{
					Door currentDoor = Door.getDoorDirectory().get(doorIndex);

					// Draws the door if it's closed
					if (currentDoor.isClosed())
						g.drawImage(currentDoor.getDoorImages()[0],
								currentDoor.getDrawX(), currentDoor.getDrawY(),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								null);
					// ... and if it's opened
					else
						g.drawImage(currentDoor.getDoorImages()[1],
								currentDoor.getDrawX(), currentDoor.getDrawY(),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
								null);
				}

				// Draws player
				{
					g.drawImage(player.getImages()[player.getDir()],
							player.getDrawX(),
							player.getDrawY(),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							this);
				}

				// Draws guards
				for (int guardIndex = 0; guardIndex < Guard.getGuardDirectory()
						.size(); guardIndex++)
				{
					g.drawImage(
							Guard.getGuardDirectory().get(guardIndex)
									.getImages()[Guard
									.getGuardDirectory().get(guardIndex)
									.getDir()],
							Guard.getGuardDirectory().get(guardIndex)
									.getDrawX(),
							Guard.getGuardDirectory()
									.get(guardIndex).getDrawY(),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							(int) (iconSize * GameMain.IMAGE_SCALE_FACTOR),
							this);
				}
			}

			// Draws the game sidebar background
			g.drawImage(
					sidebarImages[4],
					(int) (world[0].length * iconSize * GameMain.IMAGE_SCALE_FACTOR),
					0,
					(int) (sidebarImages[4].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
					(int) (sidebarImages[4].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
					null);
			// Draws the player icon
			g.drawImage(
					sidebarImages[player.getDir()],
					(int) ((world[0].length * iconSize + 79) * GameMain.IMAGE_SCALE_FACTOR),
					(int) (67 * GameMain.IMAGE_SCALE_FACTOR),
					(int) (sidebarImages[player.getDir()].getWidth(null) * GameMain.IMAGE_SCALE_FACTOR),
					(int) (sidebarImages[player.getDir()].getHeight(null) * GameMain.IMAGE_SCALE_FACTOR),
					null);

			// Draws the sliding indicator for battery remaining
			g.drawImage(
					sidebarImages[8],
					(int) (((world[0].length * iconSize + 25 + 300.0
							* player.getPowerLevel() / player.getStartPower()) * GameMain.IMAGE_SCALE_FACTOR)),
					(int) (332 * GameMain.IMAGE_SCALE_FACTOR), 250,
					(int) (16 * GameMain.IMAGE_SCALE_FACTOR), null);

			// Draws the level indicator
			g.drawImage(
					new ImageIcon("export" + File.separator + "Menu Images"
							+ File.separator
							+ "Level Selection Buttons" + File.separator
							+ "lvl"
							+ currentLevel + ".png")
							.getImage(),
					(int) ((world[0].length * iconSize + 110) * GameMain.IMAGE_SCALE_FACTOR),
					(int) (410 * GameMain.IMAGE_SCALE_FACTOR),
					(int) (80 * GameMain.IMAGE_SCALE_FACTOR),
					(int) (50 * GameMain.IMAGE_SCALE_FACTOR), null);

			// Draws inventory items
			if (player.hasKey())
				g.drawImage(
						sidebarImages[5],
						(int) ((world[0].length * iconSize + 27) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (72 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (112 * GameMain.IMAGE_SCALE_FACTOR), null);
			if (player.hasMop())
				g.drawImage(
						sidebarImages[6],
						(int) ((world[0].length * iconSize + 114) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (72 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (112 * GameMain.IMAGE_SCALE_FACTOR), null);
			if (player.hasArmour())
				g.drawImage(
						sidebarImages[7],
						(int) ((world[0].length * iconSize + 201) * GameMain.IMAGE_SCALE_FACTOR),
						(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (72 * GameMain.IMAGE_SCALE_FACTOR),
						(int) (112 * GameMain.IMAGE_SCALE_FACTOR), null);

			// Draws game's pause menu
			if (paused || gameOver)
			{
				if (!displayEndScreen)
				{
					// Draws background first depending on game over/paused
					// state
					if (gameOver)
					{
						// "Game Over" header
						g.drawImage(pauseMenuImages[7], 0, 0,
								(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
								(int) (800 * GameMain.IMAGE_SCALE_FACTOR), null);

						// If game over, then "retry" is displayed
						if (!restartRetryHighlight)
							g.drawImage(pauseMenuImages[10],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (300 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);
						else
							g.drawImage(pauseMenuImages[11],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (300 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);
					}

					// Only if game is paused
					else
					{
						// "Paused" header
						g.drawImage(pauseMenuImages[0], 0, 0,
								(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
								(int) (800 * GameMain.IMAGE_SCALE_FACTOR), null);

						// Resume is only drawn if game is paused
						if (!resumeHighlight)
							g.drawImage(pauseMenuImages[1],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (150 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);
						else
							g.drawImage(pauseMenuImages[2],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (150 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);

						// Restart game is then drawn if it is merely paused
						if (!restartRetryHighlight)
							g.drawImage(pauseMenuImages[8],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (300 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);
						else
							g.drawImage(pauseMenuImages[9],
									(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (300 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
									(int) (100 * GameMain.IMAGE_SCALE_FACTOR),
									null);
					}
				}

				// Return to level selection screen
				if (!levelsHighlight)
					g.drawImage(pauseMenuImages[3],
							(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (450 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR), null);
				else
					g.drawImage(pauseMenuImages[4],
							(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (450 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR), null);

				// Go to main menu
				if (!mMenuHighlight)
					g.drawImage(pauseMenuImages[5],
							(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR), null);
				else
					g.drawImage(pauseMenuImages[6],
							(int) (105 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (600 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (590 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (100 * GameMain.IMAGE_SCALE_FACTOR), null);
			}
		}
	}

	/**
	 * Handles animation for the player
	 * @param entity the entity's motion to be animated
	 */
	public void animate(Entity entity)
	{
		// Loops through ten "frames"
		for (double move = 0; move < 1.0; move += 0.1)
		{
			// Calculates position of entity while moving, depending on the
			// direction of movement and icon size
			if (entity.getDir() == UP)
			{
				entity.setDrawX((int) (entity.getCol() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
				entity.setDrawY((int) ((entity.getRow() - move) * iconSize * GameMain.IMAGE_SCALE_FACTOR));
			}
			else if (entity.getDir() == RIGHT)
			{
				entity.setDrawX((int) ((entity.getCol() + move) * iconSize * GameMain.IMAGE_SCALE_FACTOR));
				entity.setDrawY((int) (entity.getRow() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
			}
			if (entity.getDir() == DOWN)
			{
				entity.setDrawX((int) (entity.getCol() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
				entity.setDrawY((int) ((entity.getRow() + move) * iconSize * GameMain.IMAGE_SCALE_FACTOR));
			}
			else if (entity.getDir() == LEFT)
			{
				entity.setDrawX((int) ((entity.getCol() - move) * iconSize * GameMain.IMAGE_SCALE_FACTOR));
				entity.setDrawY((int) (entity.getRow() * iconSize * GameMain.IMAGE_SCALE_FACTOR));
			}

			// Repaint area around moving thing
			paintImmediately(entity.getDrawX(), entity.getDrawY() - 2,
					(int) (iconSize
					* GameMain.IMAGE_SCALE_FACTOR),
					(int) ((iconSize + 8) * GameMain.IMAGE_SCALE_FACTOR));

			// Pause to properly animate images
			try
			{
				Thread.sleep(800 / GameMain.ANIMATE_SPEED);
			}
			catch (InterruptedException ex)
			{
			}
		}
	}

	/**
	 * Checks area around player each time it is moved to "reveal"
	 * chemical-spills, using the spills ArrayList
	 */
	public void updateSpills()
	{
		// In essence, checks in a 7x7 square around the player, and in case
		// anything weird happens with the array, try/catch as necessary
		try
		{
			for (int rowCheck = player.getRow() - 3; rowCheck < player.getRow() + 3; rowCheck++)
			{
				for (int colCheck = player.getCol() - 3; colCheck < player
						.getCol() + 3; colCheck++)
				{
					// Then looks in the spillDirectory to see if there are any
					// to "reveal"
					for (int spillIndex = 0; spillIndex < spills.size(); spillIndex++)
					{
						if (spills.get(spillIndex).getX() == rowCheck
								&& spills.get(spillIndex).getY() == colCheck)
						{
							// If there are, then it sets the tile from the
							// original "blank" to a "spilled" tile and
							// refreshes
							world[rowCheck][colCheck] = 'i';
							repaint();
						}
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}
	}

	/**
	 * Eliminates a spill from the spills directory once the player has
	 * "cleaned" them up
	 */
	public void wipeSpill(int row, int col)
	{
		// Searches for the spill tile that matches with the row and column
		// provided
		for (int spillIndex = 0; spillIndex < spills.size(); spillIndex++)
		{
			// Removes the spill from the ArrayList
			if (spills.get(spillIndex).getX() == row
					&& spills.get(spillIndex).getY() == col)
				spills.remove(spillIndex);
		}
		repaint();
	}

	/**
	 * Player controlled by arrow keys or WASD or ,AOE (Dvorak support) Spacebar
	 * is to toggle any levers or open any doors in the vicinity of the player
	 */
	public void keyPressed(KeyEvent e)
	{
		// Each key code corresponds to a certain direction

		// UP key code
		if (e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_W
				|| e.getKeyCode() == KeyEvent.VK_COMMA)
		{
			// Everything relating to movement is dealt with under the Player
			// object
			player.move(UP);
		}

		// RIGHT
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_D
				|| e.getKeyCode() == KeyEvent.VK_E)
		{
			player.move(RIGHT);
		}

		// DOWN
		else if (e.getKeyCode() == KeyEvent.VK_DOWN
				|| e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_O)
		{
			player.move(DOWN);
		}

		// LEFT (A is the same in QWERTY and Dvorak)
		else if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_A)
		{
			player.move(LEFT);
		}

		// Spacebar toggles doors in the vicinity of the player
		// It also checks if the player is on any levers to toggle
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			// Directly changes the doors in vicinity
			player.toggleDoorsInVicinity();
			// Activates levers if applicable
			if (world[player.getRow()][player.getCol()] == 'm')
			{
				for (int leverIndex = 0; leverIndex < Lever.getLeverDirectory()
						.size(); leverIndex++)
				{
					if (Lever.getLeverDirectory().get(leverIndex).getRow() == player
							.getRow()
							&& Lever.getLeverDirectory().get(leverIndex)
									.getCol() == player.getCol())
					{
						// Toggles related door of the lever
						Lever.getLeverDirectory().get(leverIndex).toggleDoor();
					}

				}
			}
		}

		// Activates the pause menu, and prohibits the player from continuing if
		// the game is over
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE
				|| e.getKeyCode() == KeyEvent.VK_P && !gameOver)
		{
			paused = true;
			repaint();
		}
	}

	/**
	 * Necessary method
	 */
	public void keyReleased(KeyEvent e)
	{
	}

	/**
	 * Necessary method
	 */
	public void keyTyped(KeyEvent e)
	{
	}

	/**
	 * Gets the level number
	 * @return level
	 */
	public int getLevel()
	{
		return currentLevel;
	}

	/**
	 * Gets the world array
	 * @return "world" character array
	 */
	public char[][] getWorld()
	{
		return world;
	}

	/**
	 * Gets difficulty for calculations of powerLevel
	 * @return
	 */
	public int getDifficulty()
	{
		return difficulty;
	}

	/**
	 * @return the iconSize
	 */
	public int getIconSize()
	{
		return iconSize;
	}

	/**
	 * @return the paused state
	 */
	public boolean isPaused()
	{
		return paused;
	}

	/**
	 * Sets the pause to false, thereby unpausing the game
	 */
	public void resume()
	{
		paused = false;
		repaint();
	}

	/**
	 * Allows for user navigation on the in-game menus (such as the pause or
	 * game-over menu)
	 * @author Vince Ou and Barbara Guo
	 * @version January 2015
	 */
	public class MouseHandler extends MouseAdapter
	{
		// Mouse pressed
		public void mousePressed(MouseEvent event)
		{
			Point pressed = event.getPoint();

			// Main menus
			if (menuMode)
			{
				// Main menu buttons
				if (helpScreen == 0 && !levelSelect && !difficultySelect)
				{
					// Responds if a mouse button was pressed over the New Game
					// image
					if (NEW_GAME_BUTTON.contains(pressed))
					{
						menuMode = true;
						// Opens the difficulty selection menu if it wasn't
						// already
						// opened
						difficultySelect = true;
					}

					// Does the same for the load game button
					if (LOAD_GAME_BUTTON.contains(pressed))
					{
						menuMode = true;
						levelSelect = true;
						setCursor(Cursor.getDefaultCursor());
					}

					// Does the same for the instructions button but only if the
					// instructions screen isn't already displayed
					if (INSTRUCTIONS.contains(pressed))
					{
						menuMode = true;
						helpScreen = 1;
						setCursor(Cursor.getDefaultCursor());
					}

					// If the exit button is pressed close the screen
					if (EXIT_BUTTON.contains(pressed))
					{
						System.exit(0);
					}
				}

				// Instructions screens
				else if (helpScreen > 0 && !difficultySelect
						&& !levelSelect)
				{
					menuMode = true;
					// Next or previous screens' buttons
					if (NEXT_BUTTON.contains(pressed) && helpScreen < 5)
					{
						helpScreen++;
					}
					else if (PREV_BUTTON.contains(pressed) && helpScreen > 1)
					{
						helpScreen--;
					}
				}

				// Level select screens
				else if (helpScreen == 0 && !difficultySelect
						&& levelSelect)
				{
					menuMode = true;
					for (int button = 0; button < LevelButton
							.getButtonDirectory().size(); button++)
					{
						// Checks if level button is unlocked, and then loads
						// the
						// game based on the level button's data
						if (LevelButton.getButtonDirectory().get(button)
								.contains(pressed)
								&& LevelButton.getButtonDirectory().get(button)
										.isUnlocked())
						{
							menuMode = false;
							currentLevel = LevelButton.getButtonDirectory()
									.get(button)
									.getLevel();
							loadWorld(currentLevel);
						}
					}
				}

				// Difficulty select screen
				else if (helpScreen == 0 && difficultySelect
						&& !levelSelect)
				{
					// If ANY button is pressed
					if (BEGINNER.contains(pressed) || NORMAL.contains(pressed)
							|| HARD.contains(pressed))
					{
						// Beginner
						if (BEGINNER.contains(pressed))
						{
							difficulty = GameMain.EASY;
						}
						// Normal
						else if (NORMAL.contains(pressed))
						{
							difficulty = GameMain.NORMAL;
						}
						// Hard
						else
						{
							difficulty = GameMain.HARD;
						}

						// Starts a new game ONLY IF any button is pressed
						// Try/catch block in case the file cannot be found
						try
						{
							PrintWriter outFile = new PrintWriter(
									new FileWriter("userdata.txt"));
							outFile.print(difficulty + Integer.toString(1));
							outFile.close();
						}
						catch (IOException e)
						{
							GameMain.runtimeError(GameMain.MISSING_SAVE_FILE);
						}
						achievedLevel = 1;
						currentLevel = achievedLevel;
						loadWorld(currentLevel);
					}
				}

				// Back button press on any screen returns it to main menu
				if (BACK_BUTTON.contains(pressed))
				{
					difficultySelect = false;
					helpScreen = 0;
					levelSelect = false;
				}
			}

			// In game menus
			if ((paused || gameOver) && !menuMode)
			{
				// Resume game (un-pauses the game)
				if (RESUME.contains(pressed) && !gameOver)
				{
					resume();
				}

				// Retry or restart the level (different text depending on game
				// over or pausing)
				else if (RESTART_RETRY.contains(pressed))
				{
					loadWorld(currentLevel);
				}

				// Return to the levels selection screen (closes the current
				// GameWorld JPanel as well)
				else if (LEVELS.contains(pressed))
				{
					menuMode = true;
					levelSelect = true;
					theMain.setPreferredSize(new Dimension(
							(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (800 * GameMain.IMAGE_SCALE_FACTOR)));
					theMain.pack();
				}

				// Returns to the main menu (closes the current GameWorld JPanel
				// as well)
				else if (MAIN_MENU.contains(pressed))
				{
					theMain.setPreferredSize(new Dimension(
							(int) (800 * GameMain.IMAGE_SCALE_FACTOR),
							(int) (800 * GameMain.IMAGE_SCALE_FACTOR)));
					theMain.pack();
					menuMode = true;
				}
			}

			repaint();
		}
	}

	/**
	 * Animates buttons on the in-game menus
	 * @author Vince Ou and Barbara Guo
	 * @version January 2015
	 */
	public class MouseMotionHandler extends MouseMotionAdapter
	{
		// If mouse is moved onto a button...
		public void mouseMoved(MouseEvent event)
		{
			Point mousePos = event.getPoint();

			if (paused || gameOver)
			{
				// The resume game button from the pause menu
				if (RESUME.contains(mousePos))
				{
					resumeHighlight = true;

					// change the mouse cursor to the hand image
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					resumeHighlight = false;
					// change the mouse cursor to the normal image if mouse is
					// not inside its Rectangle
					setCursor(Cursor.getDefaultCursor());
				}

				// The restart/retry button (depending on the paused/game over
				// state)
				if (RESTART_RETRY.contains(mousePos))
				{
					restartRetryHighlight = true;

					// change the mouse cursor to the hand image
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					restartRetryHighlight = false;
					// change the mouse cursor to the normal image if mouse is
					// not inside its Rectangle
					setCursor(Cursor.getDefaultCursor());
				}

				// Return to the levels selection screen button
				if (LEVELS.contains(mousePos))
				{
					levelsHighlight = true;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					levelsHighlight = false;
					setCursor(Cursor.getDefaultCursor());
				}

				// Return to Main Menu button
				if (MAIN_MENU.contains(mousePos))
				{
					mMenuHighlight = true;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					mMenuHighlight = false;
					setCursor(Cursor.getDefaultCursor());
				}
			}

			// Main menu buttons activate
			if (helpScreen == 0 && !difficultySelect && !levelSelect)
			{
				// If the mouse is over the "imageNewGame" image on the main
				// screen, then highlight/change it to the "imageNewGame2"
				// image
				if (NEW_GAME_BUTTON.contains(mousePos))
					highlightNewGame = true;
				else
					highlightNewGame = false;

				// Do the same with the other buttons
				// Load game
				if (LOAD_GAME_BUTTON.contains(mousePos))

					highlightLoadGame = true;
				else
					highlightLoadGame = false;

				// Instructions button
				if (INSTRUCTIONS.contains(mousePos))
					highlightHelp = true;

				else
					highlightHelp = false;

				// Exit game button
				if (EXIT_BUTTON.contains(mousePos))
					highlightExit = true;
				else
					highlightExit = false;
			}

			// Instructions screen
			else if (!difficultySelect && !levelSelect
					&& helpScreen > 0)
			{
				// Changes the mouse if it is hovering over any of the
				// buttons
				// on the instructions screen
				// Next screen button
				if (helpScreen >= 1 && helpScreen <= 4)
					if (NEXT_BUTTON.contains(mousePos))
					{
						highlightNext = true;
						setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
					else
					{
						highlightNext = false;
					}
				// Previous screen button
				if (helpScreen >= 2 && helpScreen <= 5)
				{
					if (PREV_BUTTON.contains(mousePos))
					{
						highlightPrev = true;
						setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
					else
						highlightPrev = false;
				}
			}

			// Level selection screen
			else if (levelSelect && helpScreen == 0
					&& !difficultySelect)
			{
				// Loops through all buttons in the button directory
				for (int buttonCheck = 0; buttonCheck < LevelButton
						.getButtonDirectory().size(); buttonCheck++)
				{
					// If cursor is inside Rectangle of any button, its own
					// highlighted is true
					if (LevelButton.getButtonDirectory().get(buttonCheck)
							.contains(mousePos))
						LevelButton.getButtonDirectory().get(buttonCheck)
								.setHighlighted(true);
					else
						LevelButton.getButtonDirectory().get(buttonCheck)
								.setHighlighted(false);
				}
			}

			// Difficulty select screen
			if (difficultySelect && helpScreen == 0
					&& !levelSelect)
			{
				// Beginner button's check for any mouse inside its
				// Rectangle
				if (BEGINNER.contains(mousePos))
				{
					highlightBeginner = true;
					setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					highlightBeginner = false;
					setCursor(Cursor.getDefaultCursor());
				}

				// Normal button's check for any mouse inside its Rectangle
				if (NORMAL.contains(mousePos))
				{
					highlightMedium = true;
					setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					highlightMedium = false;
					setCursor(Cursor.getDefaultCursor());
				}

				// Hard button's check for any mouse inside its Rectangle
				if (HARD.contains(mousePos))
				{
					highlightHard = true;
					setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					highlightHard = false;
					setCursor(Cursor.getDefaultCursor());
				}
			}

			// Highlights "return to main menu" button as necessary
			if (BACK_BUTTON.contains(mousePos)
					&& (levelSelect || helpScreen > 0 || difficultySelect))
			{
				highlightBack = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			else
			{
				highlightBack = false;
				setCursor(Cursor.getDefaultCursor());
			}
			repaint();
		}
	}
}
