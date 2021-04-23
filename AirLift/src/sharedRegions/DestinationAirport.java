package sharedRegions;

import infrastructures.*;
import entities.*;
import genclass.GenericIO;
import main.SimulPar;

public class DestinationAirport {

	/**
     *  Reference to passenger threads.
     */

	private final Passenger [] passenger;
	
	/**
	 *   Reference to the general repository.
     */
	
	private final GeneralRepos repos;
    
    private MemFIFO<Integer> onDestination;
	
	public DestinationAirport(GeneralRepos repos)
	{
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  try
      { 
		  onDestination = new MemFIFO<> (new Integer [SimulPar.K]);
      } catch (MemException e)
      { 	
    	  GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
      	  System.exit (1);
      }
	  
	  this.repos = repos;
	}
	
	
	public synchronized void flyToDeparturePoint() {
    	GenericIO.writelnString("Plane took off");
    	
    	((Pilot) Thread.currentThread()).setPilotState(PilotStates.FLYINGBACK);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());

		GenericIO.writelnString("End of first flight!");
    }
}
