import java.io.File;

import javax.swing.ImageIcon;

/**
 * Player is the player-controlled entity that moves around representing the
 * player's movements on the grid
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class Player extends Entity
{
	private boolean hasKey;
	private boolean hasArmour;
	private boolean hasMop;
	private int powerLevel, startPower;
	private GamePanel game;

	/**
	 * Creates a Player object
	 * @param world GameWorld reference
	 * @param row starting row of player
	 * @param col starting column of player
	 */
	public Player(GamePanel world, int row, int col)
	{
		// Player extends Entity, so it sets up variables in Entity
		super(world, row, col, 0);
		game = world;

		// Imports images for Player
		this.getImages()[0] = new ImageIcon("export" + File.separator
				+ "player0Day.png").getImage();
		this.getImages()[1] = new ImageIcon("export" + File.separator
				+ "player1Day.png").getImage();
		this.getImages()[2] = new ImageIcon("export" + File.separator
				+ "player2Day.png").getImage();
		this.getImages()[3] = new ImageIcon("export" + File.separator
				+ "player3Day.png").getImage();
	}

	/**
	 * Overrides Entity's move method to include support for powerLevel,
	 * collision check with thing such as spills, lasers, and level completion
	 * checks
	 */
	public void move(int dir)
	{
		if (game.isPaused())
			return;
		// Player cannot do anything without power
		if (powerLevel > 0)
		{
			// Checks for any spills in the area
			game.updateSpills();
			// Turns the entity
			setDir(dir);
			// Does a slightly different action for each direction...

			// For going up
			if (dir == GamePanel.UP)
			{
				// Checks if it is going to hit a wall
				if (this.getRow() >= 1
						&& gridCheck(this.getRow() - 1, this.getCol(), false))
				{
					// Animates the movement and changes row, powerLevel as
					// necessary
					game.animate(this);
					setRow(getRow() - 1);
					powerLevel--;

					// Teleportation support
					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
				}
			}

			// If facing right
			else if (dir == GamePanel.RIGHT)
			{
				if (this.getCol() < game.getWorld()[0].length - 1
						&& gridCheck(this.getRow(), this.getCol() + 1, false))
				{
					game.animate(this);
					setCol(getCol() + 1);
					powerLevel--;

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
				}
			}

			// Player facing down
			else if (dir == GamePanel.DOWN)
			{
				if (this.getRow() < game.getWorld().length - 1
						&& gridCheck(this.getRow() + 1, this.getCol(), false))
				{
					game.animate(this);
					setRow(getRow() + 1);
					powerLevel--;

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
				}
			}

			// If player is facing left
			else if (dir == GamePanel.LEFT)
			{
				if (this.getCol() >= 1
						&& gridCheck(this.getRow(), this.getCol() - 1, false))
				{
					game.animate(this);
					setCol(getCol() - 1);
					powerLevel--;

					if (game.getWorld()[this.getRow()][this.getCol()] == 'n')
						game.teleport(this);
				}
			}

			// Checks for any collisions
			// with guards
			game.checkForGuardCollision();
			char worldTile = game.getWorld()[getRow()][getCol()];
			// Exit check
			if (worldTile == 'z' && hasKey)
			{
				game.levelComplete();
			}

			// Laser check
			else if (worldTile == 'd' || worldTile == 'o')
			{
				// Armour support
				if (hasArmour)
					hasArmour = false;
				else
					game.levelFail();
			}

			// Chemical spill check (supports both mops and armour)
			else if (worldTile == 'i')
			{
				if (hasMop)
				{
					// "Cleans up" the spill using the mop
					hasMop = false;
					game.getWorld()[getRow()][getCol()] = 'a';
					game.wipeSpill(getRow(), getCol());
					game.repaint();
				}
				// Player can also be protected by armour from the spill
				else if (hasArmour)
					hasArmour = false;
				else
					game.levelFail();
			}

			// Items check and picks them up as needed
			else if (worldTile == 'j' && !hasMop)
			{
				hasMop = true;
				game.getWorld()[getRow()][getCol()] = 'a';
			}
			else if (worldTile == 'k' && !hasArmour)
			{
				hasArmour = true;
				game.getWorld()[getRow()][getCol()] = 'a';
			}
			else if (worldTile == 'l')
			{
				hasKey = true;
				game.getWorld()[getRow()][getCol()] = 'a';
			}
			else if (worldTile == 'x')
			{
				game.getWorld()[getRow()][getCol()] = 'a';
				powerLevel += game.getDifficulty() * 3 + 15;
			}
			game.repaint();
		}
	}

	/**
	 * When space is pressed by the player, all doors in the vicinity of the
	 * player will be toggled to either opened or closed states
	 */
	public void toggleDoorsInVicinity()
	{
		// Loops through a 5x5 around the Player
		for (int rowCheck = this.getRow() - 2; rowCheck < this.getRow() + 2; rowCheck++)
		{
			for (int colCheck = this.getCol() - 2; colCheck < this.getCol() + 2; colCheck++)
			{
				// Compares coordinates for all doors in the doorDirectory with
				// each coordinate on the grid
				for (int doorIndex = 0; doorIndex < Door.getDoorDirectory()
						.size(); doorIndex++)
				{
					if (Door.getDoorDirectory().get(doorIndex).getRow() == rowCheck
							&& Door.getDoorDirectory().get(doorIndex)
									.getCol() == colCheck
							&& !Door.getDoorDirectory().get(doorIndex)
									.isLocked())
					{
						// Opens/closes the door
						Door.getDoorDirectory().get(doorIndex).toggleState();
					}

				}
			}
		}
	}

	// Getters and setters

	/**
	 * Used to initialize the power level at the beginning of the game
	 * @param world GameWorld to find world's size
	 */
	public void setPower(char[][] world)
	{
		/*
		 * Try/catch block in case there is no player initialized on the world
		 * (this is the first bit of code involving a player object that is
		 * called, so if a player does not exist, this is the first chance to
		 * catch its disappearance)
		 */
		try
		{
			// Give user to the power
			powerLevel = (int) (world.length * world[0].length / 2 - game
					.getDifficulty() * 12);
		}
		catch (NullPointerException e)
		{
			GameMain.runtimeError(GameMain.NO_PLAYER);
		}

		// startPower used to calculate the battery bar
		startPower = powerLevel;
	}

	/**
	 * @return the powerLevel
	 */
	public int getPowerLevel()
	{
		return powerLevel;
	}

	/**
	 * @return the hasKey
	 */
	public boolean hasKey()
	{
		return hasKey;
	}

	/**
	 * @return the hasArmour
	 */
	public boolean hasArmour()
	{
		return hasArmour;
	}

	/**
	 * @return the hasMop
	 */
	public boolean hasMop()
	{
		return hasMop;
	}

	/**
	 * @param hasArmour the hasArmour to set
	 */
	public void setArmour(boolean hasArmour)
	{
		this.hasArmour = hasArmour;
	}

	/**
	 * @return the startPower
	 */
	public int getStartPower()
	{
		return startPower;
	}
}
