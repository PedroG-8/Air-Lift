package main;

import entities.*;
import genclass.FileOp;
import genclass.GenericIO;
import sharedRegions.*;

public class Test {

	public static void main (String [] args)
	   {
	      Passenger [] passenger = new Passenger [SimulPar.K];          // array of passenger threads
	      Pilot pilot;    								// array of customer threads
	      Hostess hostess;  
	      DepartureAirport departAirport;                                    // reference to the barber shop
	      Plane plane;
	      DestinationAirport destAirport;
	      GeneralRepos repos;                                  // reference to the general repository
	      String fileName;                                     // logging file name
	      char opt;                                            // selected option
	      boolean success;                                     // end of operation flag

	     /* problem initialization */

	      GenericIO.writelnString ("\n" + "      Air Lift\n");
	      do
	      { 
	    	  GenericIO.writeString ("Logging file name? ");
//	        	fileName = GenericIO.readlnString ();
	    	  fileName = "logg";
	        if (FileOp.exists (".", fileName))
	           { do
	             { GenericIO.writeString ("There is already a file with this name. Delete it (y - yes; n - no)? ");
//	               opt = GenericIO.readlnChar ();
	             	opt = 'y';
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
	      
	      
	      
//	      for (int i = 0; i < SimulPar.N; i++)
//	        customer[i] = new Customer ("Cust_" + (i+1), i, bShop, nIter);

	     /* start of the simulation */
	      
	      
	      pilot.start();
	      hostess.start();
	      for (int i = 0; i < SimulPar.K; i++)
	    	  passenger[i].start ();
	      

	     /* waiting for the end of the simulation */
	      
//	      GenericIO.writelnString ();
	      
	      try {
	    	  pilot.join();
	      } catch(InterruptedException e) {}
	      GenericIO.writelnString ("The pilot has terminated.");
	           
	      try {
	    	  hostess.join();
	      } catch(InterruptedException e) {}
	      GenericIO.writelnString ("The hostess has terminated.");
	      
	      
	      for (int i = 0; i < SimulPar.K; i++)
	      { try
	        { passenger[i].join ();
	        }
	        catch (InterruptedException e) {}
	        GenericIO.writelnString ("The passenger " + i + " has terminated.");
	      }
//	      GenericIO.writelnString ();
    }
}
