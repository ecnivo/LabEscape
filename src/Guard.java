import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * Guards are enemies that the player has to avoid. They randomly run around the
 * world, and if one touches the player, it will harm the player, possibly
 * ending the level
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class Guard extends Entity implements ActionListener
{
	private static ArrayList<Guard> guardDirectory = new ArrayList<Guard>();
	private GamePanel game;
	private short tickTimer;
	private static ArrayList<Timer> guardTimers = new ArrayList<Timer>();

	/**
	 * Creates a new Guard object
	 * @param world a GameWorld to reference to
	 * @param row the starting row of the guard
	 * @param col the starting column of the guard
	 */
	public Guard(GamePanel world, int row, int col)
	{
		// Creates a new Entity to store some of the guard's data
		super(world, row, col, 0);
		game = world;

		// Imports images for drawing the guard
		this.getImages()[0] = new ImageIcon("export" + File.separator
				+ "guard0Day.png").getImage();
		this.getImages()[1] = new ImageIcon("export" + File.separator
				+ "guard1Day.png").getImage();
		this.getImages()[2] = new ImageIcon("export" + File.separator
				+ "guard2Day.png").getImage();
		this.getImages()[3] = new ImageIcon("export" + File.separator
				+ "guard3Day.png").getImage();
		tickTimer = 0;
	}

	/**
	 * Listens for events from the guard timer that is created alongside the
	 * guard
	 */
	public void actionPerformed(ActionEvent event)
	{
		// Pause game... disables any movement
		if (game.isPaused())
			return;
		
		// Different commands (eg. checking for available tile, changing
		// row/column as needed) for each direction of facing
		if (this.getDir() == GamePanel.UP)
		{
			// Checks if the tile ahead of the guard can be moved onto
			if (this.getRow() > 0
					&& guardCheckGrid(this.getRow() - 1, this.getCol()))
			{
				/*
				 * tickTimer counts to ten, and every tenth iteration of the
				 * calling of this method, the guard will make a "major"
				 * movement (eg. change the row/col) and every one calling of
				 * this method, the guard will animate one tile
				 */
				if (tickTimer == 0)
				{
					// Changes row (or column) as necessary
					setRow(getRow() - 1);

					// Allows teleportation of guards
					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
					// Turns guard to face the player
					this.setDir(game.facePlayer(this));
				}

				// Changes the drawX and drawY (animation variables) each time
				// to properly render their locations
				this.setDrawX((int) (this.getCol() * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
				this.setDrawY((int) ((this.getRow() - tickTimer / 10.0)
						* game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
			}

			// If the guard is, in fact, facing a wall, then it'll turn randomly
			else
				this.setDir((int) (Math.random() * 4));
		}

		// For each different direction... Right
		else if (this.getDir() == GamePanel.RIGHT)
		{
			if (this.getCol() < game.getWorld()[0].length - 1
					&& guardCheckGrid(this.getRow(), this.getCol() + 1))
			{
				if (tickTimer == 0)
				{
					setCol(getCol() + 1);

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
					this.setDir(game.facePlayer(this));
				}
				this.setDrawX((int) ((this.getCol() + tickTimer / 10.0)
						* game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
				this.setDrawY((int) (this.getRow() * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
			}
			else
				this.setDir((int) (Math.random() * 4));

		}

		// Guard facing down
		else if (this.getDir() == GamePanel.DOWN)
		{
			if (this.getRow() < game.getWorld().length - 1
					&& guardCheckGrid(this.getRow() + 1, this.getCol()))
			{
				if (tickTimer == 0)
				{
					setRow(getRow() + 1);

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
					this.setDir(game.facePlayer(this));
				}

				this.setDrawX((int) (this.getCol() * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
				this.setDrawY((int) ((this.getRow() + tickTimer / 10.0)
						* game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
			}
			else
				this.setDir((int) (Math.random() * 4));
		}

		// Guard facing left
		else if (this.getDir() == GamePanel.LEFT)
		{
			if (this.getCol() > 0
					&& guardCheckGrid(this.getRow(), this.getCol() - 1))
			{
				if (tickTimer == 0)
				{
					setCol(getCol() - 1);

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
					this.setDir(game.facePlayer(this));
				}
				this.setDrawX((int) ((this.getCol() - tickTimer / 10.0)
						* game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
				this.setDrawY((int) (this.getRow() * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR));
			}
			else
				this.setDir((int) (Math.random() * 4));
		}

		// Manages the ten-call cycle of the tickTimer
		tickTimer++;
		if (tickTimer == 10)
			tickTimer = 0;

		// Paints around the guard
		game.paintImmediately(
				(int) ((this.getCol() - 2) * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR),
				(int) ((this.getRow() - 2) * game.getIconSize() * GameMain.IMAGE_SCALE_FACTOR),
				(int) ((game.getIconSize() + 2) * GameMain.IMAGE_SCALE_FACTOR),
				(int) ((game.getIconSize() + 2) * GameMain.IMAGE_SCALE_FACTOR));
		game.repaint();
		// Collision with the player
		game.checkForGuardCollision();
	}

	/**
	 * Checks the grid for any collisions with other guards, or any walls, etc.
	 * @param row the row of the tile to check for any conflicts
	 * @param col the column of the tile to check for conflicts
	 * @return a boolean of whether or not a guard can move onto the tile
	 */
	private boolean guardCheckGrid(int row, int col)
	{
		// Assume it's true to start
		boolean allowed = true;
		// Check for any conflicts with guards
		for (int guardIndex = 0; guardIndex < guardDirectory.size(); guardIndex++)
		{
			if (guardDirectory.get(guardIndex).getCol() == col
					&& guardDirectory.get(guardIndex).getRow() == row)
				allowed = false;
		}
		// Check for any conflicts with walls
		if (allowed && gridCheck(row, col, true))
			return true;
		return false;
	}

	/**
	 * Used to stop all guards' movements in a certain level upon loading
	 * another level
	 */
	public static void stopGuardTimers()
	{
		for (int timerIndex = 0; timerIndex < guardTimers.size(); timerIndex++)
		{
			guardTimers.get(timerIndex).stop();
		}
	}

	/**
	 * @return the guardDirectory
	 */
	public static ArrayList<Guard> getGuardDirectory()
	{
		return guardDirectory;
	}

	/**
	 * @param guardDirectory the guardDirectory to set
	 */
	public static void setGuardDirectory(ArrayList<Guard> guardDirectory)
	{
		Guard.guardDirectory = guardDirectory;
	}

	/**
	 * @return the guardTimers
	 */
	public static ArrayList<Timer> getGuardTimers()
	{
		return guardTimers;
	}
}
