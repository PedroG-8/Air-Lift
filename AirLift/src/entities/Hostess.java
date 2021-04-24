package entities;

import genclass.GenericIO;
import sharedRegions.DepartureAirport;

/**
 *   Hostess thread.
 *
 *   It simulates the hostess life cycle.
 */

public class Hostess extends Thread {

  /**
   *  Hostess state.
   */

   private int hostessState;

  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport departAirport;

  /**
   *   Instantiation of a hostess thread.
   *
   *     @param name thread name
   *     @param departAirport reference to the Departure Airport
   */

   public Hostess (String name, DepartureAirport departAirport)
   {
      super (name);
      hostessState = HostessStates.WAITFORFLIGHT;
      this.departAirport = departAirport;
   }

  /**
   *   Set hostess state.
   *
   *     @param state new hostess state
   */
   
   public void setHostessState (int state)
   {
      hostessState = state;
   }

  /**
   *   Get hostess state.
   *
   *     @return hostess state
   */
   
   public int getHostessState ()
   {
      return hostessState;
   }

  /**
   *   Life cycle of the hostess.
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
		   }
		   departAirport.informPlaneReadyToTakeOff();
		   departAirport.waitForNextFlight();
		   if (departAirport.allPassengLeft()) {
			   break;
		   }
	   }
	   GenericIO.writelnString("Hostess should terminate");
   }
}
