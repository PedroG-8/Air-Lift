package sharedRegions;

import infrastructures.MemException;
import infrastructures.MemFIFO;
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
    
	
	public DepartureAirport(GeneralRepos repos)
	{
//	  nReqCut = 0;
	  passenger = new Passenger [SimulPar.K];
	  for (int i = 0; i < SimulPar.K; i++)
		  passenger[i] = null;
	  
	  this.repos = repos;
	}
	
	public synchronized boolean waitInQueue()
	{
	  int passengerId;                                      // passenger id

      passengerId = ((Passenger) Thread.currentThread()).getPassengerId ();
      
      passenger[passengerId] = (Passenger) Thread.currentThread();
      
      passenger[passengerId].setPassengerState(PassengerStates.INQUEUE);
      
      // repos.setCustomerState (customerId, cust[customerId].getCustomerState ());

//      if (sitCustomer.full ())                             // the customer checks how full is the barber shop
//         return (false);                                   // if it is packed full, he leaves to come back later


      while (((Passenger) Thread.currentThread ()).getPassengerState () != PassengerStates.INFLIGHT)
      { /* the customer waits for the service to be executed */
        try
        { wait ();
        }
        catch (InterruptedException e) {}
      }

      return (true);                                       // the customer leaves the barber shop after being serviced
	}
	
	
}
