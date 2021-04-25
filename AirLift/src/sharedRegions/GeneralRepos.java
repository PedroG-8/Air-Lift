package sharedRegions;

import java.util.Objects;

import entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulPar;

public class GeneralRepos {

	  /**
	   *  Name of the logging file.
	   */

	   private final String logFileName;

	  /**
	   *  State of the passengers.
	   */

	   private final int [] passengerStates;

	  /**
	   *  State of the hostess.
	   */

	   private final int [] hostessState;

	   /**
		*  State of the pilot.
		*/

	   private final int [] pilotState;

	   /**
		*  Number of passengers in queue
		*/

	   private int InQ = 0;

	   /**
		*  Number of passengers in flight
		*/

	   private int InF = 0;

	   /**
		*  Number of passengers that have arrived
		*/

	   private int PTAL = 0;

	   /**
		*  Flight number.
		*/

	   private int flightNum = 0;

	   /**
		*  Array with information to pass to the repository
		*/

	   private int [] flightInfo = new int[5];

	   /**
	    *   Instantiation of a general repository object.
	    *
	    *   @param logFileName name of the logging file
	    *
	    */

	   public GeneralRepos (String logFileName)
	   {
	      if ((logFileName == null) || Objects.equals (logFileName, ""))
	         this.logFileName = "logger";
	         else this.logFileName = logFileName;
	      passengerStates = new int [SimulPar.K];
	      for (int i = 0; i < SimulPar.K; i++)
	    	  passengerStates[i] = PassengerStates.GOINGTOAIRPORT;

	      hostessState = new int [SimulPar.H];
	      hostessState[0] = HostessStates.WAITFORFLIGHT;

	      pilotState = new int [SimulPar.P];
	      pilotState[0] = PilotStates.ATTRANSFERGATE;
	      reportInitialStatus ();
	   }

	   /**
	    *   Set passenger state.
	    *
	    *     @param id passenger id
	    *     @param state passenger state
	    */

	    public synchronized void setPassengerState (int id, int state)
	    {
	       passengerStates[id] = state;
	       reportStatus ();
	    }

	   /**
	    *   Set hostess state.
	    *
	    *     @param state hostess state
	    */

	    public synchronized void setHostessState (int state)
	    {
	    	hostessState[0] = state;
	    	reportStatus ();
	    }

	    /**
	     *   Set pilot state.
	     *
	     *     @param state pilot state
	     */

	    public synchronized void setPilotState (int state)
	    {
	    	pilotState[0] = state;
	    	reportStatus ();
	    }

	   /**
	    *   Set passengers, hostess and pilot state.
	    *
	    *     @param passengerId passenger id
	    *     @param passengerState passenger state
	    *     @param hostState hostess state
	    *     @param pilotState pilot state
	    */

	    public synchronized void setPassengerHostessState (int passengerId, int passengerState, int hostState, int pilotState)
	    {
	       this.passengerStates[passengerId] = passengerState;
	       this.hostessState[0] = hostState;
	       this.pilotState[0] = pilotState;
	       reportStatus ();
	    }

	   /**
	    *  Write the header to the logging file.
	    *
	    *  The pilot is at the the transfer gate
	    *  The hostess is waiting for the next flight
	    *  The passenger is going to the airport
	    *  Internal operation.
	    */

	    private void reportInitialStatus ()
	    {
	       TextFile log = new TextFile ();                      // instantiation of a text file handler

	       if (!log.openForWriting (".", logFileName))
	          { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
	            System.exit (1);
	          }
	       log.writelnString ("                Airlift - Description of the internal state");
	       String str = " PT   HT ";
	       for(int i = 0; i < 21; i++) {
	    	   if(i < 10) {
	    		   str += "   P0" + i;
	    	   } else {
	    		   str += "   P" + i;
	    	   }
	       }
	       log.writelnString(str + "  InQ  InF  PTAL");
	       if (!log.close ())
	          { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
	            System.exit (1);
	          }
	       reportStatus ();
        }

	   /**
	    *  Write a state line at the end of the logging file.
	    *
	    *  The current state of the pilot, hostess and passengers is organized in a line to be printed.
	    *  Internal operation.
	    */

	    private void reportStatus ()
	    {
	       TextFile log = new TextFile ();                      // instantiation of a text file handler

	       String lineStatus = "";                              // state line to be printed

	       if (!log.openForAppending (".", logFileName))
	          { GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
	            System.exit (1);
	          }

	       switch (pilotState[0])
	         { case PilotStates.ATTRANSFERGATE:  lineStatus += "ATRG ";
	                                              break;
	           case PilotStates.READYFORBOARDING: lineStatus += "RDFB ";
	                                              break;
	           case PilotStates.WAITINGFORBOARDING:      lineStatus += "WTFB ";
	                                              break;
	           case PilotStates.FLYINGFORWARD:    lineStatus += "FLFW ";
	                                              break;
	           case PilotStates.DEBOARDING:      lineStatus += "DRPP ";
               									  break;
	           case PilotStates.FLYINGBACK:    lineStatus += "FLBK ";
               								      break;
	         }

	       switch (hostessState[0])
	         { case HostessStates.WAITFORFLIGHT:  lineStatus += " WTFL ";
	                                              break;
	           case HostessStates.WAITFORPASSENGER: lineStatus += " WTPS ";
	                                              break;
	           case HostessStates.CHECKPASSENGER:      lineStatus += " CKPS ";
	                                              break;
	           case HostessStates.READYTOFLY:    lineStatus += " RDTF ";
	                                              break;
	         }

	       for (int i = 0; i < SimulPar.K; i++)
	         switch (passengerStates[i])
	         { case PassengerStates.GOINGTOAIRPORT:   lineStatus += " GTAP ";
	                                         break;
	           case PassengerStates.INQUEUE: 	  lineStatus += " INQE ";
	                                         break;

	           case PassengerStates.INFLIGHT: 	lineStatus += " INFL ";
	           								break;
	           case PassengerStates.ATDESTINATION: 	lineStatus += " ATDS ";
					break;
	         }

	       lineStatus += "  " + InQ + "    " + InF + "    " + PTAL;

	       log.writelnString (lineStatus);
	       if (!log.close ())
	       { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           		System.exit (1);
	       }
	    }


	    /**
	     * Write any text to the logg file
	     *
	     * @param toPrint text to be printed on the logg file
	     */

	    public synchronized void print(String toPrint) {

	    	TextFile log = new TextFile ();                      // instantiation of a text file handler

	    	if (!log.openForAppending (".", logFileName))
	    	{
	    		GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
	            System.exit (1);
	    	}

	    	log.writelnString (toPrint);
	    	if (!log.close ())
	    	{
	    		GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
	            System.exit (1);
	    	}
	    }

	    /**
	     * Write last status of the flight
	     * Air lift sum up
	     */

	    public synchronized void printFinal() {
	    	TextFile log = new TextFile ();                      // instantiation of a text file handler

	    	if (!log.openForAppending (".", logFileName))
	    	{
	    		GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
	            System.exit (1);
	    	}

	    	log.writelnString ("\nAirlift sum up:");

	    	int i = 1;
	    	for (int numP : flightInfo) {
	    		if(numP != 0) {
		    		log.writelnString("Flight " + i + " transported " + numP + " passengers");
					i++;
	    		}
			}

	    	if (!log.close ())
	    	{
	    		GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
	            System.exit (1);
	    	}
	    }

	    /**
	     * Increases the number of passengers in the queue
	     */

	    public void addToQ() {
	    	this.InQ += 1;
	    }

	    /**
	     * Decreases the number of passengers in the queue
	     */

	    public void removeFromQ() {
	    	this.InQ -= 1;
	    }

	    /**
	     * Increases the number of passengers in flight
	     */

	    public void addToF() {
	    	this.InF += 1;
	    }

	    /**
	     * Decreases the number of passengers in flight
	     */

	    public void removeFromF() {
	    	this.InF -= 1;
	    }

	    /**
	     * Increases the number of passengers that have arrived
	     */

	    public void addToTotal() {
	    	this.PTAL += 1;
	    }

	    /**
	     * @return total of passengers that have arrived
	     */

	    public int getTotal() {
	    	return PTAL;
	    }

	    /**
	     * Updates the flight information
	     *
	     * @param passengNum number of passengers
	     */

	    public void updateFlightInfo(int passengNum) {
	    	System.out.println(flightNum);
	    	flightInfo[this.flightNum] = passengNum;
	    	this.flightNum++;
	    }
}
