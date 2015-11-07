import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * A LevelButton represents a level in the level selection screen and its
 * corresponding data
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class LevelButton extends Rectangle
{
	private int level, drawX, drawY;
	private boolean unlocked;
	private Image[] images = new Image[3];
	private static ArrayList<LevelButton> buttonDirectory = new ArrayList<>();
	private boolean highlighted;

	/**
	 * Creates a new LevelButton
	 * @param level the level it represents
	 * @param unlocked whether or not it is a locked level (from text file)
	 */
	public LevelButton(int level, boolean unlocked)
	{
		// Calculates the position that the rectangle should be, and feeds it
		// into Rectangle
		super(
				(int) ((370 + ((level - 1) / 5) * 95)
				* GameMain.IMAGE_SCALE_FACTOR), (int) ((150
				+ ((level - 1) % 5) * 125) * GameMain.IMAGE_SCALE_FACTOR),
				(int) (80 * GameMain.IMAGE_SCALE_FACTOR),
				(int) (50 * GameMain.IMAGE_SCALE_FACTOR));
		
		// Sets its own X and Y for use later
		drawX = 370 + ((level - 1) / 5) * 95;
		drawY = 150 + ((level - 1) % 5) * 125;
		
		// Imports data
		this.level = level;
		this.unlocked = unlocked;
		
		// Imports images
		images[0] = new ImageIcon("export" + File.separator + "Menu Images"
				+ File.separator
				+ "Level Selection Buttons" + File.separator + "lvl"
				+ level + ".png")
				.getImage();
		images[1] = new ImageIcon("export" + File.separator + "Menu Images"
				+ File.separator
				+ "Level Selection Buttons" + File.separator + "lvl"
				+ level + "Two.png")
				.getImage();
		images[2] = new ImageIcon("export" + File.separator + "Menu Images"
				+ File.separator
				+ "Level Selection Buttons" + File.separator + "lock.png")
				.getImage();
		
		// Sets this to default
		highlighted = false;
	}

	/**
	 * @return the buttonDirectory
	 */
	public static ArrayList<LevelButton> getButtonDirectory()
	{
		return buttonDirectory;
	}

	/**
	 * @return the highlighted
	 */
	public boolean isHighlighted()
	{
		return highlighted;
	}

	/**
	 * @param highlighted the highlighted to set
	 */
	public void setHighlighted(boolean highlighted)
	{
		this.highlighted = highlighted;
	}

	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return the unlocked
	 */
	public boolean isUnlocked()
	{
		return unlocked;
	}

	/**
	 * @return the images
	 */
	public Image[] getImages()
	{
		return images;
	}

	/**
	 * @return the x
	 */
	public int getDrawX()
	{
		return drawX;
	}

	/**
	 * @return the y
	 */
	public int getDrawY()
	{
		return drawY;
	}
}
