package main;

import genclass.GenericIO;
import infrastructures.*;

public class Test {

	public static void main(String[] args) {
		
		System.out.println("Test the GenericIO module");
		GenericIO.writelnString("GenericIO working\n");
		
		
		System.out.println("Test the FIFO creation");
		
		String [] array = new String[2];
		array[0] = "Fifo";
		array[1] = "Working";
		try {
			MemFIFO<String> fifo = new MemFIFO<String>(array);
			fifo.write(array[0]);
			fifo.write(array[1]);
			GenericIO.writelnString(fifo.read() + " " + fifo.read());
		} catch (MemException e) {
			GenericIO.writelnString("Error creating the fifo");
			e.printStackTrace();
		}

	}

}
