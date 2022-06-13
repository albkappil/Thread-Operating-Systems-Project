import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**	
	Project 1 
	CS  
	@author Albey Kappil 
	@date 	
	id: 2nd program ,00010000: 16
	bits added to id: 
		4 = quit = 20
		2 = write = 18
		3 = read = 19 
		1 = full = 17
	ids from CS, IO,and Buffer:	
	idCS = 8;
	quitIDcs = 12;
	idIO = 32;
	bufDI = 67;
**/
public class TransferDevice{
	public static byte idIOFull = 35;
	
	public static void main(String[] args){
		try {
			/* String idBits = "00010011"; // the program id  = 2 so 2nd value after 3bit is set to one.
			byte id = 2;  */

			// program ids 
			byte idCS = 8;
			byte bufDI = 67;
			byte idIO = 32;
			// for IO
			short accepted = 16; // idT
			short full = 17;
			byte[] dataIO = new byte[2]; // IO input
			byte[] temp = new byte[2];
			// for buffer
			short write = 18;
			short fullData = 19;
			byte printID = 19;
			//for CS
			short quitIDcs = 12;
			byte[] print = new byte[2];
			
			// from bus
			short quitIDT = 20;
			
			Scanner fromBus = new Scanner(System.in);

			TransferDevice td = new TransferDevice();
	/* 		System.out.println(16);
		//	System.out.println();
			short a = fromBus.nextShort();
			System.out.println(a); */
			
			byte[] idcheck = new byte[2]; 
			idcheck = td.getBytes(idCS,fromBus.nextShort());// cs id - 8
			
			short numBytes = fromBus.nextShort(); // number of bytes 
			
			int currSize = 1; 
			int bufferSize = 128;
			int loop;
			int leftover;
			//while(System.in.available() == 0){	
			while(true){
				dataIO =  td.getBytes(idIO, fromBus.nextShort()); // get dataIO 
				if(dataIO[1] == idIOFull)
					currSize--;
				if(dataIO[1] != idIOFull){
					if(numBytes % bufferSize != 0 && currSize-1 % bufferSize == 0 && currSize != 1 ){ // divisable by 125
						System.out.println(full); // pause IO
						loop = bufferSize;	
						while(loop > 0){
							System.out.println(fullData);			// sends mode to buffer
							print =  td.getBytes(bufDI,fromBus.nextShort()); // data from buffer
							System.out.println(td.shortdataIO(printID,print[1])); // send dataIO to cs to print to screen
							loop--;					
						}
					}
				}
				
					System.out.println(accepted);
					System.out.println(write); 		// send mode to write 
					System.out.println(td.shortdataIO((byte)write,dataIO[1])); // send dataIO to buffer
				
				if (currSize == numBytes || dataIO[1] == idIOFull){
					if( currSize <= bufferSize)
						leftover = currSize;
					else
						leftover = currSize - ( (currSize / bufferSize) * bufferSize) ;
					while(leftover > 0){
						System.out.println(fullData);			// sends read mode to buffer
						print =  td.getBytes(bufDI,fromBus.nextShort()); // data from buffer
						System.out.println(td.shortdataIO(printID,print[1])); // send dataIO to cs to print to screen
						leftover--;					
					}
					System.out.println(quitIDcs);
					//System.out.println("waiting to quit ...");
					while( fromBus.nextShort() != quitIDT )// quit from CS by bus
						Thread.sleep(Long.parseLong("2000"));
					System.exit(0);
				}
	
				currSize++;	
			}

			
			
		}	
		catch(IOException ex)
		{
			System.out.println("Wrong ID");
		}
		catch(InterruptedException e){
			System.out.println("ERROR");
		} 
	
	}
	
	
	
	
	public short shortdataIO( byte id, byte dataIO){
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(id);
		bb.put(dataIO);
		short shortVal = bb.getShort(0);
		return shortVal;		
		
	}
	
	public byte[] getBytes(byte checkid,short value) throws IOException {
		//quit code of programs
			byte quitIDIO = 36;
			byte quitIDCS = 12;
			byte quitIDB = 68;
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());
		buffer.putShort(value);
		byte[] in = buffer.array();
		if ( in[0] == quitIDCS || in[0] == quitIDIO || in[0] == quitIDB ){
			System.exit(0);
			return null;
		}
		else if ((in[0] == checkid) )
			return in;
		else if ((in[0] == idIOFull)){
			in[1] = idIOFull;
			return in;
		}
		else{
			in[0] = -1;
			throw new IOException();
		}
		
	}
	
	
	
	
	
	
	
	
}	


