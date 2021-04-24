package entities;

import sharedRegions.*;

/**
 *   Pilot thread.
 *
 *   It simulates the barber life cycle.
 */

public class Pilot extends Thread
{

  /**
   *  Pilot state.
   */

   private int pilotState;

  /**
   *    Reference to the departure airport.
   */

   private final DepartureAirport departAirport;
   
   /**
    * 	Reference to the plane.
    */
   
   private final Plane plane;
   
   /**
    * 	Reference to the destination airport.
    */
   
   private final DestinationAirport destAirport;

   /**
    * 	Instantiation of the hostess thread. 	
    * 
    * 	@param name thread name
    * 	@param departAirport reference to the Departure Airport
    * 	@param plane reference to the plane
    * 	@param destAirport reference to the Destination Airport
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
   *   Set pilot state.
   *
   *     @param state new pilot state
   */

   public void setPilotState (int state)
   {
      pilotState = state;
   }

  /**
   *   Get pilot state.
   *
   *     @return pilot state
   */

   public int getPilotState ()
   {
      return pilotState;
   }

  /**
   *   Life cycle of the pilot.
   */
   
   @Override
   public void run() {
	   
	   while(true) {
		   departAirport.parkAtTransferGate();
		   if (departAirport.allPassengLeft()) {
			   break;
		   }
		   preparingForBoarding();
		   departAirport.informPlaneReadyForBoarding();
		   departAirport.waitForAllInBoard();
		   plane.flyToDestinationPoint();
		   flyingToDestination();
		   plane.anounceArrival();
		   destAirport.flyToDeparturePoint();
		   
		   flyingBack();
	   }
   }


  /**
   *  Flying to destination.
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
   
   /**
    *  Flying back to departure.
    *
    *  Internal operation.
    */
   
   private void flyingBack()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
   
   /**
    *  Preparing for boarding.
    *
    *  Internal operation.
    */
   
   private void preparingForBoarding()
   {
      try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
}
