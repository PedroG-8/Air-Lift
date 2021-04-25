package sharedRegions;

import entities.*;
import genclass.GenericIO;
import main.SimulPar;

/**
 * Definition of the Destination Airport
 *
 */

public class DestinationAirport {

	/**
     *  Reference to passenger threads.
     */

	private final Passenger [] passenger;
	
	/**
	 *   Reference to the general repository.
     */
	
	private final GeneralRepos repos;
	
	/**
     *  Flight number
     */
	
	private int flightNum = 0;
	
	/**
     *   Instantiation of the destination airport
     *
     *     @param repos general repository
     */
	
	public DestinationAirport(GeneralRepos repos)
	{
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  this.repos = repos;
	}
	
	/**
	 *  Operation fly to departure point
	 *
	 *  It is called by the pilot after deboarding.
	 *
	 */
	
	public synchronized void flyToDeparturePoint() 
	{
    	GenericIO.writelnString("Plane took off");
    	flightNum++;
    	repos.print("\nFlight " + flightNum + ": returning");
    	((Pilot) Thread.currentThread()).setPilotState(PilotStates.FLYINGBACK);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		GenericIO.writelnString("End of flight " + flightNum + "!");
    }
}
