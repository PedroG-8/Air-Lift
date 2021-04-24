package main;

import entities.*;
import genclass.FileOp;
import genclass.GenericIO;
import sharedRegions.*;

/**
 * @author Pedro Gonçalves 88859
 * @author Rui Oliveira 89216
 *
 */
public class AirLift {

	/**
     *    Main method.
     *
     *    @param args runtime arguments
     */
	
	public static void main (String [] args)
	   {
	      Passenger [] passenger = new Passenger [SimulPar.K];	// array of passenger threads
	      Pilot pilot;    										// array of customer threads
	      Hostess hostess;  									// array of hostess threads
	      DepartureAirport departAirport;                      	// reference to the departure airport
	      Plane plane;											// reference to the plane
	      DestinationAirport destAirport;						// reference to the destination airport
	      GeneralRepos repos;                                  	// reference to the general repository
	      String fileName;                                    	// logging file name
	      char opt;                                            	// selected option
	      boolean success;                                     	// end of operation flag

	     /* problem initialization */

	      GenericIO.writelnString ("\n" + "      Air Lift\n");
	      do
	      { 
	    	  GenericIO.writeString ("Logging file name? ");
	    	  fileName = GenericIO.readlnString ();
	    	  if (FileOp.exists (".", fileName))
	           { do
	             { GenericIO.writeString ("There is already a file with this name. Delete it (y - yes; n - no)? ");
	               opt = GenericIO.readlnChar ();
	             } while ((opt != 'y') && (opt != 'n'));
	             if (opt == 'y')
	                success = true;
	                else success = false;
	           }
	           else success = true;
	      } while (!success);
	      
	      repos = new GeneralRepos (fileName);
	      departAirport = new DepartureAirport(repos);
	      plane = new Plane(repos);
	      destAirport = new DestinationAirport(repos);
	      
	      pilot = new Pilot("PT", departAirport, plane, destAirport);
	      hostess = new Hostess("HT", departAirport);
	      
	      for (int i = 0; i < SimulPar.K; i++)
	        passenger[i] = new Passenger ("P" + i, i, departAirport, plane);
	      
	     /* start of the simulation */
	      
	      pilot.start();
	      hostess.start();
	      for (int i = 0; i < SimulPar.K; i++)
	    	  passenger[i].start ();

	     /* waiting for the end of the simulation */
	      
	      try {
	    	  pilot.join();
	      } catch(InterruptedException e) {}
	      GenericIO.writelnString ("The pilot has terminated.");
	           
	      try {
	    	  hostess.join();
	      } catch(InterruptedException e) {}
	      GenericIO.writelnString ("The hostess has terminated.");
	      
	      for (int i = 0; i < SimulPar.K; i++)
	      { 
	    	  try
	    	  { 
	    		  passenger[i].join ();
	    	  }
	    	  catch (InterruptedException e) {}
	    	  GenericIO.writelnString ("The passenger " + i + " has terminated.");
	      }
    }
}
