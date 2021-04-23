package sharedRegions;

import infrastructures.*;
import entities.*;
import genclass.GenericIO;
import main.SimulPar;

public class DepartureAirport {

	
	/**
     *  Number of passengers in queue.
     */

	private int nQueue;
	   
	   
	/**
     *  Reference to passenger threads.
     */

	private final Passenger [] passenger;
	
	/**
	 *   Reference to the general repository.
     */

    private final GeneralRepos repos;
    
    private MemFIFO<Integer> inQueue;
    private MemFIFO<Integer> inAirport;
   
    private boolean [] documentsShowed = new boolean[SimulPar.K];
    
    private int passengersOnPlane = 0;
    private int remainingPassengers = SimulPar.K;
    private int passengInAirport = SimulPar.K;
    private int actualId = -1;
    private int flightNum = 0;
 
    private boolean readyToTakeOff = false;
    private boolean readyForBoarding = false;
    private boolean lastFlight = false;
	
	public DepartureAirport(GeneralRepos repos)
	{
	      	
	  nQueue = 0;
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  try
      { 
		  inAirport = new MemFIFO<> (new Integer [SimulPar.K]);
		  inQueue = new MemFIFO<> (new Integer [SimulPar.K]);
      } catch (MemException e)
      { 	
    	  GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
      	  inQueue = null;
      	  System.exit (1);
      }
	  
	  this.repos = repos;
	}
	
	public synchronized void parkAtTransferGate() {
		
		GenericIO.writelnString("Park at transfer gate");
		if (flightNum != 0) {
			((Pilot) Thread.currentThread()).setPilotState(PilotStates.ATTRANSFERGATE);
			repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		}
		if (repos.getTotal() == SimulPar.K) {
			repos.printFinal();
		}
	}
	
	public synchronized void informPlaneReadyForBoarding() {
		
		GenericIO.writelnString("Plane ready for flight!");
		readyForBoarding = true;
		flightNum++;
		if (SimulPar.MAX - repos.getTotal() <= 5) {
			lastFlight = true;
		}
		
		repos.print("\nFlight " + flightNum + ": boarding started.");
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.READYFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		
	}
	
	public synchronized void waitForAllInBoard() {
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.WAITINGFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		
		notifyAll();
		
		while (!readyToTakeOff)
		{
        	try
	        { 
        		GenericIO.writelnString("Pilot is sleeping!");
        		wait ();
	        }
	        catch (InterruptedException e) {}
		}
		readyToTakeOff = false;
	}
	
	
	public synchronized void prepareForPassBoarding() {
		
		// Hostess is waiting for the pilot info
		while (!readyForBoarding)
		{
        	try
	        { 
        		GenericIO.writelnString("Waiting for boarding!");
        		wait ();
	        }
	        catch (InterruptedException e) {}
		}
		
		GenericIO.writelnString("Preparing for boarding!");
	}
	
	
	public synchronized boolean waitInQueue()
	{
			
	  int passengerId;                                      // passenger id

	  passengerId = ((Passenger) Thread.currentThread()).getPassengerId ();
      
      passenger[passengerId] = (Passenger) Thread.currentThread();
      
      try {
    	  inAirport.write(passengerId);
      } catch (MemException e1) {
    	  e1.printStackTrace();
      }
      try
      { 
    	  passengerId = inAirport.read();
    	  GenericIO.writelnString("Passenger " + passengerId + " added to queue");
    	  inQueue.write(passengerId);                    // the passenger enters the Queue
    	  nQueue++;
    	  repos.addToQ();
    	  documentsShowed[passengerId] = false;
      }
      catch (MemException e)
      { 
    	  GenericIO.writelnString ("Insertion of passenger id in queue failed: " + e.getMessage ());
          System.exit (1);
      }
      
      passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
      repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
      
      notifyAll ();
      while (actualId != passengerId)
      { 
        try
        { 
        	wait ();
        }
        catch (InterruptedException e) {}
      }
      
      GenericIO.writelnString("Passou o " + passengerId);
      
      return (true);                                     
	}
	
	public synchronized int waitForNextPassenger() {
		
		readyForBoarding = false;
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		
		if (passengersOnPlane == SimulPar.MAX) {
			readyToTakeOff = true;
			return -1;
		}
		
		while(nQueue == 0) {
			try {
				if (passengersOnPlane >= SimulPar.MIN) {
					
					readyToTakeOff = true;
					return -1;
				}
				wait();
			} catch(Exception e) {
				return -1;
			}
		}
		
		nQueue -= 1;
		repos.removeFromQ();
		int passengerId = -1;
		try {
			passengerId = inQueue.read();
			actualId = passengerId;
			
		} catch (MemException e) {
			e.printStackTrace();
		}
		
		notifyAll();
		
		GenericIO.writelnString("Waiting for passenger " + passengerId);
		
		
		
		return passengerId;
	}
	
	public synchronized boolean showDocuments() {
		
		int passengerId;
			
		passengerId = ((Passenger) Thread.currentThread()).getPassengerId (); 
	    passenger[passengerId] = (Passenger) Thread.currentThread();
			   
	    GenericIO.writelnString(passengerId + " - Showing documents!");
	    
	    documentsShowed[passengerId] = true;
	    notifyAll();
	    while (passenger[passengerId].getPassengerState() != PassengerStates.INFLIGHT) {
	    	try {
	    		wait();
	    	} catch(InterruptedException e) {}
	    }
	    
	    GenericIO.writelnString("Algum sai daqui? " + passengerId);
	    return true;
	}
	
	public synchronized boolean checkDocuments(int passengerID) {
		
		while (!documentsShowed[passengerID] || actualId != passengerID)
		{ 
			try
			{ 
				wait ();
			}
			catch (InterruptedException e) {}
		}
		
		notifyAll();
		
		repos.print("\nFlight " + flightNum + ": passenger " + passengerID + " checked.");
		
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.CHECKPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		remainingPassengers--;

		
		GenericIO.writelnString("Checking passenger " + passengerID + " documents!");
		passenger[passengerID].setPassengerState(PassengerStates.INFLIGHT);
		repos.setPassengerState (passengerID, passenger[passengerID].getPassengerState ());
		passengersOnPlane++;
		
		if (remainingPassengers == 0 && lastFlight) {
			readyToTakeOff = true;
		}
		
		return true;
	}
	
	public synchronized void informPlaneReadyToTakeOff() {
	
		
		while (!readyToTakeOff)
		{ 
			try
			{ 
				wait ();
			}
			catch (InterruptedException e) {}
		}

		GenericIO.writelnString("Plane ready to take off with " + passengersOnPlane + " passengers!!");
		
		repos.print("\nFlight " + flightNum + ": departed with " + passengersOnPlane + " passengers.");

		
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.READYTOFLY);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
	
	}
	
	public synchronized void waitForNextFlight() {
		
		
		passengersOnPlane = 0;
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORFLIGHT);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		
		while(!readyForBoarding) {
			try {
				wait();
			} catch(Exception e) {}
		}
	}
	
	public synchronized boolean allPassengLeft() {
		GenericIO.writelnString("Missing " + remainingPassengers + " passengers!!");
		if (remainingPassengers == 0) return true;         
		return false;
	}

}