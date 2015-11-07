import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * A door is a variable-permitted-passage tile, that, in certain states, will
 * allow the player or guards to pass through, and in other states, prohibit
 * such movement
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class Door extends Entity
{
	private boolean closed, locked;
	private GamePanel game;
	private int drawX;
	private int drawY;
	private Image[] doorImages = new Image[2];
	private static ArrayList<Door> doorDirectory = new ArrayList<Door>();

	/**
	 * Creates a new Door object
	 * @param world a GameWorld reference
	 * @param row the row of the door
	 * @param col the column of the door
	 * @param dir the direction the door is facing
	 * @param closed the state of the door (opened or closed)
	 */
	public Door(GamePanel world, int row, int col, char fileLetter, boolean closed)
	{
		// Imports data
		super(world, row, col, 0);
		this.closed = closed;
		game = world;

		// Imports doors' images (0 is closed, 1 is open/ajar)
		doorImages[0] = new ImageIcon("export" + File.separator
				+ fileLetter + "Day.png").getImage();
		doorImages[1] = new ImageIcon("export" + File.separator
				+ (char)(fileLetter + 4) + "Day.png").getImage();

		// Sets the drawing coordinates for each door specifically depending on
		// its position on the grid
		this.setDrawX((int) (col * GameMain.IMAGE_SCALE_FACTOR * game
				.getIconSize()));
		this.setDrawY((int) (row * GameMain.IMAGE_SCALE_FACTOR * game
				.getIconSize()));
	}

	/**
	 * Used to open/close doors
	 */
	public void toggleState()
	{
		if (closed)
			// There are four directions, so it just adds 4 to the character
			// code and edits the world array as needed
			game.getWorld()[getRow()][getCol()] = (char) (game.getWorld()[getRow()][getCol()] + 4);
		else
			// Same principle, but subtracts 4
			game.getWorld()[getRow()][getCol()] = (char) (game.getWorld()[getRow()][getCol()] - 4);
		
		closed = !closed;
		game.repaint();
	}
	
	/**
	 * Used to "lock" a door (ie. it can only be opened by its accompanying lever)
	 * @param True if door is to be locked, false otherwise
	 */
	public void setLockState(boolean lock)
	{
		this.locked = lock;
	}

	// Getters and setters
	/**
	 * @return the doorImages array
	 */
	public Image[] getDoorImages()
	{
		return doorImages;
	}

	/**
	 * @return the doorDirectory
	 */
	public static ArrayList<Door> getDoorDirectory()
	{
		return doorDirectory;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed()
	{
		return closed;
	}

	/**
	 * @return the drawX
	 */
	public int getDrawX()
	{
		return drawX;
	}

	/**
	 * @return the drawY
	 */
	public int getDrawY()
	{
		return drawY;
	}

	/**
	 * @param drawX the drawX to set
	 */
	public void setDrawX(int drawX)
	{
		this.drawX = drawX;
	}

	/**
	 * @param drawY the drawY to set
	 */
	public void setDrawY(int drawY)
	{
		this.drawY = drawY;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * @param doorDirectory the doorDirectory to set
	 */
	public static void setDoorDirectory(ArrayList<Door> doorDirectory)
	{
		Door.doorDirectory = doorDirectory;
	}
}
