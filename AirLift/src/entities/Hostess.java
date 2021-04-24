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
		   System.out.println("receomec");
		   departAirport.prepareForPassBoarding();
		   while (true) {
			   int id = departAirport.waitForNextPassenger();
			   if (id == -1) break;
			   departAirport.checkDocuments(id);
		   }
		   GenericIO.writelnString("Saiu do ciclo");
		   departAirport.informPlaneReadyToTakeOff();
		   departAirport.waitForNextFlight();
		   GenericIO.writelnString("Entra aqui");
		   if (departAirport.allPassengLeft()) {
			   GenericIO.writelnString("Entra aqui");
			   break;
		   }
		   System.out.println("acaba");
	   }
	   GenericIO.writelnString("Hostess should terminate");
   }
}
