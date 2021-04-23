package entities;

 import genclass.GenericIO;
import sharedRegions.DepartureAirport;

/**
 *   Barber thread.
 *
 *   It simulates the barber life cycle.
 *   Static solution.
 */

public class Hostess extends Thread
{

  /**
   *  Barber state.
   */

   private int hostessState;

  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport departAirport;

  /**
   *   Instantiation of a barber thread.
   *
   *     @param name thread name
   *     @param barberId barber id
   *     @param bShop reference to the barber shop
   */

   public Hostess (String name, DepartureAirport departAirport)
   {
      super (name);
      hostessState = HostessStates.WAITFORFLIGHT;
      this.departAirport = departAirport;
   }

  

  /**
   *   Set barber state.
   *
   *     @param state new barber state
   */

   public void setHostessState (int state)
   {
      hostessState = state;
   }

  /**
   *   Get barber state.
   *
   *     @return barber state
   */

   public int getHostessState ()
   {
      return hostessState;
   }

  /**
   *   Life cycle of the barber.
   */
   
   
   @Override
   public void run() {
	   
	   
	   GenericIO.writelnString("Hostess created");
	   
	   while(true) {
		   departAirport.prepareForPassBoarding();
		   while (true) {
			   int id = departAirport.waitForNextPassenger();
			   if (id == -1) break;
			   departAirport.checkDocuments(id);
			   if (departAirport.allPassengLeft()) {
				   return;
			   }
		   }
		   GenericIO.writelnString("Saiu do ciclo");
		   departAirport.informPlaneReadyToTakeOff();
		   departAirport.waitForNextFlight();
	   }
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

//   private void checkDocuments()
//   {
//      try
//      { sleep ((long) (1 + 100 * Math.random ()));
//      }
//      catch (InterruptedException e) {}
//   }
}
