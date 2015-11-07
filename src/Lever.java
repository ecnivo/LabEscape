import java.util.ArrayList;

/**
 * Levers are remote toggles for doors
 * @author Vince Ou and Barbara Guo
 */
public class Lever extends Entity
{
	private Door relatedDoor;
	private static ArrayList<Lever> leverDirectory = new ArrayList<Lever>();

	/**
	 * Creates a new Lever object
	 * @param panel Mandatory reference to GamePanel
	 * @param row row of the lever
	 * @param col column of the lever
	 * @param door the Door object it remotely controls
	 */
	public Lever(GamePanel panel, int row, int col, Door door)
	{
		// Assigns and stores variables
		super(panel, row, col, 0);
		relatedDoor = door;
	}
	
	/**
	 * Changes the state of its linked door
	 */
	public void toggleDoor()
	{
		relatedDoor.toggleState();
	}

	/**
	 * @return the leverDirectory
	 */
	public static ArrayList<Lever> getLeverDirectory()
	{
		return leverDirectory;
	}

	/**
	 * @param leverDirectory the leverDirectory to set
	 */
	public static void setLeverDirectory(ArrayList<Lever> leverDirectory)
	{
		Lever.leverDirectory = leverDirectory;
	}
}
