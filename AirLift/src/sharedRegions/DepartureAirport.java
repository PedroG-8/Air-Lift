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
    
//    private int [] passengersInAirport = new int[SimulPar.K];
    
    private int passengersOnPlane = 0;
    private int remainingPassengers = SimulPar.K;
    private int actualId = -1;
    
    private boolean passBoarding = false;
    private boolean headOfQueue = false;
   
    private boolean readyToTakeOff = false;
    private boolean readyForBoarding = false;
	
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
	
	public synchronized void informPlaneReadyForBoarding() {
		
		GenericIO.writelnString("Plane ready for flight!");
		readyForBoarding = true;
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.READYFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		
		((Pilot) Thread.currentThread()).setPilotState(PilotStates.WAITINGFORBOARDING);
		repos.setPilotState(((Pilot) Thread.currentThread()).getPilotState ());
		
		notifyAll();
		
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
		passBoarding = true;
//		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
//		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
	}
	
	
	public synchronized boolean waitInQueue()
	{
			
	  int passengerId;                                      // passenger id

	  passengerId = ((Passenger) Thread.currentThread()).getPassengerId ();
      
      passenger[passengerId] = (Passenger) Thread.currentThread();
      
      try {
    	  inAirport.write(passengerId);
      } catch (MemException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
      }
      
      // Hostess is waiting for the pilot info
      while (!passBoarding)
		{
	  	try
	        { 
	  		GenericIO.writelnString("Waiting to enter the queue: " + passengerId);
	  		wait ();
	        }
	        catch (InterruptedException e) {}
		}
      
      
      try
      { 
    	  passengerId = inAirport.read();
    	  GenericIO.writelnString("Passenger " + passengerId + " added to queue");
    	  inQueue.write(passengerId);                    // the passenger enters the Queue
    	  nQueue++;
    	  documentsShowed[passengerId] = false;
      }
      catch (MemException e)
      { 
    	  GenericIO.writelnString ("Insertion of passenger id in queue failed: " + e.getMessage ());
          System.exit (1);
      }
      
      passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
      repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
      
//      int firstP = -1;
//      try {
//    	  firstP = inQueue.readNotTake();
//      } catch(Exception e) {}
      
      notifyAll ();
      
      
//      GenericIO.writelnString("Primeiro " + firstP);
      while (actualId != passengerId)
      { 
        try
        { 
        	wait ();
        }
        catch (InterruptedException e) {}
      }
      
      GenericIO.writelnString("Passou o " + passengerId);
      
//      
      headOfQueue = false;
//      GenericIO.writelnString("Passenger " + passengerId + " is in flight!");
//      passenger[passengerId].setPassengerState(PassengerStates.INFLIGHT);
//      repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
      
      return (true);                                     
	}
	
	public synchronized int waitForNextPassenger() {
		
		
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		
		// acorda quando algum chega
		
		while(nQueue == 0) {
			try {
				if (passengersOnPlane >= SimulPar.MIN) {
					GenericIO.writelnString("Plane ready to take off with " + passengersOnPlane + " passengers!!");
					((Hostess) Thread.currentThread()).setHostessState(HostessStates.READYTOFLY);
					repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
					System.exit(0);
					readyToTakeOff = true;
					return -1;
				}
				wait();
			} catch(Exception e) {
				return -1;
			}
		}
		
		headOfQueue = true;
		nQueue -= 1;
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
		
		int passengerId2 = ((Passenger) Thread.currentThread()).getPassengerId ();
		GenericIO.writelnString("Woke up passenger " + passengerId2);
		int passengerId;
		
		
//		passengerId = ((Passenger) Thread.currentThread()).getPassengerId (); 
//	    passenger[passengerId] = (Passenger) Thread.currentThread();
//	    passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
//	    repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
	    
		Passenger passenger2 = ((Passenger) Thread.currentThread());
		passengerId = passenger2.getPassengerId();
		passenger[passengerId] = passenger2;
		passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
		
//	    GenericIO.writelnString("Stuck in the while " + passengerId);
//		while (actualId != passengerId)
//		{ 
//			try
//			{ 
//				wait ();
//			}
//			catch (InterruptedException e) {}
//		}
		
			    
	    
	    
	    
	    GenericIO.writelnString(passengerId + " - Showing documents!");
	    
//	    calledPassengerDocuments = passengerId;
	    documentsShowed[passengerId] = true;
	    notifyAll();
	    while (passenger[passengerId].getPassengerState() != PassengerStates.INFLIGHT) {
	    	try {
	    		wait();
	    	} catch(InterruptedException e) {}
	    }
	    return true;
	}
	
	public synchronized boolean checkDocuments(int passengerID) {
		
		
		GenericIO.writelnString(passengerID + " - checkDocumets()");
		
		while (!documentsShowed[passengerID] || actualId != passengerID)
		{ 
			try
			{ 
				wait ();
			}
			catch (InterruptedException e) {}
		}
		
		notifyAll();
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.CHECKPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		remainingPassengers--;
		
		GenericIO.writelnString("Checking passenger " + passengerID + " documents!");
		passenger[passengerID].setPassengerState(PassengerStates.INFLIGHT);
		repos.setPassengerState (passengerID, passenger[passengerID].getPassengerState ());
		
		
		passengersOnPlane++;
		
		return true;
	}
	
	
	
}