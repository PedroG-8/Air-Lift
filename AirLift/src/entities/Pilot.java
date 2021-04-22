package entities;

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
//   private final DestinationAirport departAirport;
  /**
   *   Instantiation of a barber thread.
   *
   *     @param name thread name
   *     @param barberId barber id
   *     @param bShop reference to the barber shop
   */

   public Pilot (String name, DepartureAirport departAirport, Plane plane)
   {
      super (name);
      pilotState = PilotStates.ATTRANSFERGATE;
      this.departAirport = departAirport;
      this.plane = plane;
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
	   
	   waitAbit();
	   departAirport.informPlaneReadyForBoarding();
	   
	   plane.flyToDestinationPoint();
	   waitAbit();
	   plane.anounceArrival();
	   return;
//	   prepareForPassBoarding();
	   // LOOP {
	   // Passa ao estado WAITFORPASSENGER
	   // dorme ate haver um passageiro na queue
//	   checkDocuments();
	   // Passa ao estado CHECKPASSENGER
	   // checka os documentos 
	   // waitForNextPassenger();
	   
	   // if(!passageirosNaFila ou max) break
	   // }
//	   informPlaneReadyToTakeOff();
	   // Passa ao estado READYTOFLY
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

   private void waitAbit()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
}
