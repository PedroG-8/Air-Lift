package entities;

 import genclass.GenericIO;
import sharedRegions.*;

/**
 *   Barber thread.
 *
 *   It simulates the barber life cycle.
 *   Static solution.
 */

public class Pilot extends Thread
{

  /**
   *  Barber state.
   */

   private int pilotState;

  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport departAirport;
   private final Plane plane;
   private final DestinationAirport destAirport;
//   private final DestinationAirport departAirport;
  /**
   *   Instantiation of a barber thread.
   *
   *     @param name thread name
   *     @param barberId barber id
   *     @param bShop reference to the barber shop
   */

   public Pilot (String name, DepartureAirport departAirport, Plane plane, DestinationAirport destAirport)
   {
      super (name);
      pilotState = PilotStates.ATTRANSFERGATE;
      this.departAirport = departAirport;
      this.plane = plane;
      this.destAirport = destAirport;
      System.out.println("Pilot created");
   }

  

  /**
   *   Set barber state.
   *
   *     @param state new barber state
   */

   public void setPilotState (int state)
   {
      pilotState = state;
   }

  /**
   *   Get barber state.
   *
   *     @return barber state
   */

   public int getPilotState ()
   {
      return pilotState;
   }

  /**
   *   Life cycle of the barber.
   */
   
   
   
   
   @Override
   public void run() {
	   
	   while(true){
		   departAirport.parkAtTransferGate();
		   if (departAirport.allPassengLeft()) {
			   GenericIO.writelnString("FINITO");
			   break;
		   }
		   preparingForBoarding();
		   departAirport.informPlaneReadyForBoarding();
		   departAirport.waitForAllInBoard();
//		   if (departAirport.allPassengLeft()) {
//			   return;
//		   }
		   plane.flyToDestinationPoint();
		   flyingToDestination();
		   plane.anounceArrival();
		   destAirport.flyToDeparturePoint();
		   
		   waitAbit();
	   }
   }

//   @Override
//   public void run ()
//   {
//      int customerId;                                      // customer id
//      boolean endOp;                                       // flag signaling end of operations
//
//      while (true)
//      { endOp = bShop.goToSleep ();                        // the barber sleeps while waiting for a customer to service
//        if (endOp) break;                                  // check for end of operations
//        customerId = bShop.callACustomer ();               // the barber has waken up and calls next customer
//        cutHair ();                                        // the barber cuts the customer hair
//        bShop.receivePayment (customerId);                 // the barber finishes his service and receives payment for it
//      }
//   }

  /**
   *  Cutting the customer hair.
   *
   *  Internal operation.
   */

   private void flyingToDestination()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
   
   private void waitAbit()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
   
   private void preparingForBoarding()
   {
      try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
}
