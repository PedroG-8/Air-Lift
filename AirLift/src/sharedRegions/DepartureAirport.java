package sharedRegions;

import infrastructures.*;
import entities.*;
import genclass.GenericIO;
import main.SimulPar;

public class DepartureAirport {

	/**
     *  Reference to passenger threads.
     */

	private final Passenger [] passenger;
	
	/**
	 *   Reference to the general repository.
     */

    private final GeneralRepos repos;
    
    private MemFIFO<Integer> inQueue;
    
    private boolean passBoarding = false;
    private boolean headOfQueue = false;
    private boolean readyToTakeOff = false;
    private boolean goToFlight = false;
    private boolean readyForBoarding = false;
	
	public DepartureAirport(GeneralRepos repos)
	{
	      	
//	  nReqCut = 0;
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  try
      { 
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
		((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
		repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState ());
		
	}
	
	
	public synchronized boolean waitInQueue()
	{
			
	  int passengerId;                                      // passenger id

      passengerId = ((Passenger) Thread.currentThread()).getPassengerId ();
      
      passenger[passengerId] = (Passenger) Thread.currentThread();
      
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
    	  GenericIO.writelnString("Passenger " + passengerId + " added to queue");
    	  inQueue.write(passengerId);                    // the passenger enters the Queue
      }
      catch (MemException e)
      { 
    	  GenericIO.writelnString ("Insertion of passenger id in queue failed: " + e.getMessage ());
          System.exit (1);
      }
      
      passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
      repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
      
      notifyAll ();


      while (!headOfQueue)
      { 
        try
        { 
        	wait ();
        }
        catch (InterruptedException e) {}
      }
      
      headOfQueue = false;
      GenericIO.writelnString("Passenger " + passengerId + " is in flight!");
      passenger[passengerId].setPassengerState(PassengerStates.INFLIGHT);
      repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
      
      return (true);                                     
	}
	
	public synchronized void waitForNextPassenger() {
		// hospedeira entra aqui primeiro
		// acorda quando algum chega
	}
	
	public synchronized void checkDocuments() {
		
		GenericIO.writelnString("checkDocuments()");
		System.out.println(inQueue.isEmpty());
		
		while(!inQueue.isEmpty()) {
						
			int passengerId = -1;
			try {
				passengerId = inQueue.read();
				
			} catch (MemException e) {
				e.printStackTrace();
			}
			
			GenericIO.writelnString("Checking passenger " + passengerId + " documents");
			headOfQueue = true;
			
			
//			passenger[passengerId] = (Passenger) Thread.currentThread();
			                                     
			((Hostess) Thread.currentThread()).setHostessState(HostessStates.WAITFORPASSENGER);
			repos.setHostessState(((Hostess) Thread.currentThread()).getHostessState());
			
//			passenger[passengerId].setPassengerState(PassengerStates.INFLIGHT);
//			repos.setPassengerState (passengerId, passenger[passengerId].getPassengerState ());
			goToFlight = true;
		}
	}
	
	
	
}
