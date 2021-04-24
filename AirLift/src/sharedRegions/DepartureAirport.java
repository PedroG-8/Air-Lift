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
    
    /**
     *   Passengers in queue.
     */
    
    private MemFIFO<Integer> inQueue;
    
    /**
     *   Passengers in the airport.
     */
    
    private MemFIFO<Integer> inAirport;
   
    /**
     *   Array that checks if each passenger has shown their documents.
     */
    
    private boolean [] documentsShowed = new boolean[SimulPar.K];
    
    /**
     *   Total passengers that are on the plane.
     */
    
    private int passengersOnPlane = 0;
    
    /**
     *   Number of passengers that still haven't departured.
     */
    
    private int remainingPassengers = SimulPar.K;
    
    /**
     *   Id of the passenger that is being checked for his documents.
     */
    
    private int actualId = -1;
    
    /**
     *   Number of the current flight.
     */
    
    private int flightNum = 0;
 
    /**
     *   true if the plane is ready to take off.
     */
    
    private boolean readyToTakeOff = false;
    
    /**
     *   true if the plane is ready for boarding.
     */
    
    private boolean readyForBoarding = false;
    
    /**
     *   true if it's the last flight.
     */
    
    private boolean lastFlight = false;
    
    /**
     *   true if hostess can inform the pilot that the plane is ready to take off.
     */
    
    private boolean informPilot = false;
	
    /**
     *  Departure airport instantiation.
     *
     *    @param repos reference to the general repository
     */
    
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
		} catch (MemException e) { 	
			GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
      	  	inQueue = null;
      	  	System.exit (1);
		}
		this.repos = repos;
	}
	
	/**
	 *  Operation park at transfer gate.
	 *
	 *  It is called by the pilot when the plane is parked at the transfer gate.
	 */
	
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
	
	/**
	 *  Operation inform plane ready for boarding.
	 *
	 *  It is called by the pilot when the plane is ready for boarding.
	 */
	
	public synchronized void informPlaneReadyForBoarding() {
		
		GenericIO.writelnString("Plane ready for flight!");
		readyForBoarding = true;
		flightNum++;
		if (SimulPar.K - repos.getTotal() <= 5) {
			lastFlight = true;
		}
		
		repos.print("\nFlight " + flightNum + ": boarding started.");
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.READYFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
	}
	
	/**
	 *  Operation wait for all in board.
	 *
	 *  It is called by the pilot when he is waiting for the passengers to board the plane.
	 */
	
	public synchronized void waitForAllInBoard() {
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.WAITINGFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		
		notifyAll();
		
		System.out.println("Entra no waitForAllInBoard");
		while (!informPilot)
		{
        	try
	        { 
        		GenericIO.writelnString("Pilot is sleeping!");
        		wait ();
	        }
	        catch (InterruptedException e) {}
		}
		informPilot = false;
	}
	
	/**
	 *  Operation prepare for passing boarding.
	 *
	 *  It is called by the hostess to start the boarding process.
	 */
	
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
	
	/**
	 *  Operation wait in queue.
	 *
	 *  It is called by each passenger when they go to wait in the queue.
	 */
	
	public synchronized void waitInQueue()
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
    	  inQueue.write(passengerId);                    	// the passenger enters the Queue
    	  nQueue++;
    	  repos.addToQ();									// update the repository
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
	}
	
	/**
	 *  Operation wait for next passenger.
	 *
	 *  It is called by the hostess when she's ready to check the next passenger.
	 *  
	 *  @return passenger id or -1 if the plane is ready to take off.
	 */
	
	public synchronized int waitForNextPassenger() {
		
		readyForBoarding = false;
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		
		if (passengersOnPlane == SimulPar.MAX) {
			readyToTakeOff = true;
			return -1;
		}
		
		if (remainingPassengers == 0 && lastFlight) {
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
	
	/**
	 *  Operation show documents.
	 *
	 *  It is called by the passengers when they show the documents to the hostess.
	 */
	
	public synchronized void showDocuments() {
		
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
	}
	
	/**
	 *  Operation check documents.
	 *
	 *  It is called by the hostess when she checks the passenger's documents, 
	 *  and also makes sure it's the correct passenger.
	 */
	
	public synchronized void checkDocuments(int passengerID) {
		
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

		System.out.println("Remaining passengers: " + remainingPassengers);
		System.out.println("Last FLight: " + lastFlight);
		GenericIO.writelnString("Checking passenger " + passengerID + " documents!");
		
		repos.addToF();
		passenger[passengerID].setPassengerState(PassengerStates.INFLIGHT);
		repos.setPassengerState (passengerID, passenger[passengerID].getPassengerState ());
		
		passengersOnPlane++;
		
		if (remainingPassengers == 0 && lastFlight) {
			readyToTakeOff = true;
		}
	}
	
	/**
	 *  Operation inform plane ready to take off.
	 *
	 *  It is called by the hostess when the plane has a valid number of passengers
	 *  and is ready to take off.
	 */
	
	public synchronized void informPlaneReadyToTakeOff() {
	
		while (!readyToTakeOff)
		{ 
			try
			{ 
				wait ();
			}
			catch (InterruptedException e) {}
		}
		readyToTakeOff = false;
		GenericIO.writelnString("Plane ready to take off with " + passengersOnPlane + " passengers!!");
		
		repos.print("\nFlight " + flightNum + ": departed with " + passengersOnPlane + " passengers.");

		notifyAll();
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.READYTOFLY);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
	
		informPilot = true;
	}
	
	/**
	 *  Operation wait for next flight.
	 *
	 *  It is called by the hostess when the plane has taken off and
	 *  she is now waiting for the next flight.
	 */
	
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
	
	/**
	 *  Operation all passengers left.
	 *
	 *  It is called by the hostess and the pilot to check if there are still passengers to board.
	 *  
	 *  @return true if all passengers have left the departing airport.
	 */
	
	public synchronized boolean allPassengLeft() {
		GenericIO.writelnString("Missing " + remainingPassengers + " passengers!!");
		
		if (remainingPassengers == 0) {
			notifyAll();
			readyForBoarding = true;
			return true;         
		}
		return false;
	}

}