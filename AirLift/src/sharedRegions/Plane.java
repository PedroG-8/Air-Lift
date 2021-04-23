package sharedRegions;

import entities.*;
import genclass.GenericIO;
import infrastructures.*;
import main.*;

public class Plane {

	/**
     *  Number of passengers in queue.
     */

//	private int nQueue;
	   
	   
	/**
     *  Reference to passenger threads.
     */

	private final Passenger [] passenger;
	
	/**
	 *   Reference to the general repository.
     */

    private final GeneralRepos repos;
    
    private MemFIFO<Integer> onPlane;
    
    // True if the plane reached its destination
    private boolean atDestination = false;
    private boolean allPassengersLeave = false;
    private int flightNum = 0;
    private int flightPasseng = 0;
//    private boolean tookOff = false;
    
    public Plane(GeneralRepos repos)
	{
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  try
      { 
		  onPlane = new MemFIFO<> (new Integer [SimulPar.K]);
      } catch (MemException e)
      { 	
    	  GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
      	  System.exit (1);
      }
	  
	  this.repos = repos;
	}
    
    
    public synchronized void boardThePlane() {
    	
    	int passengerId;
    	allPassengersLeave = false;
		passengerId = ((Passenger) Thread.currentThread()).getPassengerId (); 
	    passenger[passengerId] = (Passenger) Thread.currentThread();
    	
		try {
			onPlane.write(passengerId);
			flightPasseng++;
			repos.addToF();
		} catch(Exception e) {}
		
		GenericIO.writelnString("Passenger " + passengerId + " is now on the plane");
		
		
		notifyAll();
		
		while (!atDestination) {
	    	try {
	    		wait();
	    	} catch(InterruptedException e) {}
	    }
	}
    
    
    public synchronized void flyToDestinationPoint() {
    	GenericIO.writelnString("Plane took off");
    	
    	((Pilot) Thread.currentThread()).setPilotState(PilotStates.FLYINGFORWARD);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		repos.updateFlightInfo(flightPasseng);
//    	tookOff = true;
    }
    
    
    public synchronized void anounceArrival() {
    	GenericIO.writelnString("Plane reached its destination");
    	flightNum++;
    	repos.print("\nFlight " + flightNum + ": arrived.");
    	
    	((Pilot) Thread.currentThread()).setPilotState(PilotStates.DEBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
    	
		atDestination = true;
		notifyAll();
		
		while(!allPassengersLeave) {
			try {
				wait();
			} catch(Exception e) {}
		}
		
		GenericIO.writelnString("All passengers left!");
    }
    
    
    
    
    public synchronized void leaveThePlane() {
    	
    	int passengerId = -1;
    	
//		passengerId = ((Passenger) Thread.currentThread()).getPassengerId (); 
//    	passenger[passengerId] = (Passenger) Thread.currentThread();
    	
		try {
			passengerId = onPlane.read();
			
			repos.removeFromF();
			repos.addToTotal();
			if (onPlane.isEmpty()) {
				allPassengersLeave = true;
				flightPasseng = 0;
				atDestination = false;
			}
		} catch(Exception e) {}
		
		GenericIO.writelnString("Passenger " + passengerId + " is deboarding");
		
		passenger[passengerId].setPassengerState(PassengerStates.ATDESTINATION);
		repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
		
		
		// Morre
		
		notifyAll();		
	}
}
