import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * Because Lasers are simply Entities, this handles all timing code for lasers
 * @author Vince Ou and Barbara Guo
 * @version January 2015
 */
public class LaserTimer implements ActionListener
{
	private Entity laser;
	private boolean extended = true;
	private GamePanel game;
	private static ArrayList<Timer> timerDirectory = new ArrayList<Timer>();

	/**
	 * Creates a new LaserTimer object
	 * @param laser
	 * @param world
	 */
	public LaserTimer(Entity laser, GamePanel world)
	{
		this.laser = laser;
		this.game = world;
	}

	/**
	 * Timer code for the lasers to run in a separate thread in the background
	 */
	public void actionPerformed(ActionEvent e)
	{
		// Disables movement when paused
		if (game.isPaused())
			return;
		
		// Retracts if extended and extends if retracted
		if (extended)
		{
			game.extendRetractLaser(laser, true);
		}
		else
		{
			game.extendRetractLaser(laser, false);
		}
		// Inverts the state
		extended = !extended;
	}

	public static void stopLasers()
	{
		for (int timerIndex = 0; timerIndex < timerDirectory.size(); timerIndex++)
		{
			timerDirectory.get(timerIndex).stop();
		}
	}

	/**
	 * @return the laserDirectory
	 */
	public static ArrayList<Timer> getLaserDirectory()
	{
		return timerDirectory;
	}
}