import java.awt.Image;

/**
 * Entity is the basis for most other objects such as Player, Guard, Laser,
 * Door, etc.
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class Entity
{
	private int row;
	private int col;
	private int dir;
	private GamePanel game;
	private int drawX;
	private int drawY;
	private Image[] images = new Image[4];

	/**
	 * Creates an Entity object
	 * @param world the GameWorld it should reference to
	 * @param rowIn Entity's row
	 * @param colIn Entity's column
	 * @param dirIn Entity's direction of facing
	 */
	public Entity(GamePanel world, int rowIn, int colIn, int dirIn)
	{
		// Simply sets the fields with the variables
		row = rowIn;
		col = colIn;
		dir = dirIn;
		game = world;
	}

	/**
	 * Checks if a certain tile can be moved-onto
	 * @param row row to check
	 * @param col column to check
	 * @return a boolean of whether or not it can be moved onto
	 */
	public boolean gridCheck(int row, int col, boolean isGuard)
	{
		// Checks the given row and column of the input
		// Uses a try/catch block to avoid any array errors and just returns
		// "false" if the requested tile is outside the bounds... Entities can't
		// exist there anyway
		try
		{
			if (game.getWorld()[row][col] == 'b'
					|| (game.getWorld()[row][col] >= 'e' && game.getWorld()[row][col] <= 'h')
					|| (game.getWorld()[row][col] >= 'p' && game.getWorld()[row][col] <= 's'))
				return false;
			if (isGuard)
				if (game.getWorld()[row][col] == 'i' || game.getWorld()[row][col] == 'z')
					return false;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		return true;
	}

	// Getters and setters
	/**
	 * @return the drawX
	 */
	public int getDrawX()
	{
		return drawX;
	}

	/**
	 * @param drawX the drawX to set
	 */
	public void setDrawX(int drawX)
	{
		this.drawX = drawX;
	}

	/**
	 * @return the drawY
	 */
	public int getDrawY()
	{
		return drawY;
	}

	/**
	 * @param drawY the drawY to set
	 */
	public void setDrawY(int drawY)
	{
		this.drawY = drawY;
	}

	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getCol()
	{
		return col;
	}

	/**
	 * @param col the column to set
	 */
	public void setCol(int col)
	{
		this.col = col;
	}

	/**
	 * @return the direction
	 */
	public int getDir()
	{
		return dir;
	}

	/**
	 * @param dir the direction to set
	 */
	public void setDir(int dir)
	{
		this.dir = dir;
	}

	/**
	 * @return the images
	 */
	public Image[] getImages()
	{
		return images;
	}
}
