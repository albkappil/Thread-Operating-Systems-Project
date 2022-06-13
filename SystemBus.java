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
	  
**/
public class SystemBus{
	public static void main(String[] args){
		try {
			/* String idBits = "10000110";// the program id  = 5 so 5th value after 3bit is set to one.
			byte sBusid = 52; */
			String file = args[0];
			Scanner fromComSys = new Scanner(System.in);

			//modes
			short quitT = 20;
			short quitCS = 12;
			short quitIO = 36;
			short quitB =68;
			
			// for IO
			short accepted = 16; // idT
			short full = 17;
			byte[] dataIO = new byte[2]; // IO input
			byte idIOFull = 35;
			// for buffer
			short write = 18;
			short fullData = 19;
			byte printID = 19;
			
			//IO pipes
			String[] cmdArrayIO = new String[3];
			cmdArrayIO[0] = "java";
			cmdArrayIO[1] = "InOut.java";
			cmdArrayIO[2] = file;
			Process pIO = Runtime.getRuntime().exec(cmdArrayIO,null);
			InputStream inStreamIO = pIO.getInputStream();
			OutputStream outStreamIO = pIO.getOutputStream();
			Scanner fromIO = new Scanner(inStreamIO);
			PrintStream toIO = new PrintStream(outStreamIO);
			
			//Buffer pipes
			String[] cmdArrayB = new String[2];
			cmdArrayB[0] = "java";
			cmdArrayB[1] = "Buffer.java";
			Process pBuffer = Runtime.getRuntime().exec(cmdArrayB,null);
			InputStream inSBuffer = pBuffer.getInputStream();
			OutputStream outSBuffer = pBuffer.getOutputStream();
			Scanner fromBuffer = new Scanner(inSBuffer);
			PrintStream toBuffer = new PrintStream(outSBuffer);
			
			//Transfer pipes
			String[] cmdArrayT = new String[2];
			cmdArrayT[0] = "java";
			cmdArrayT[1] = "TransferDevice.java";
			Process pTransfer = Runtime.getRuntime().exec(cmdArrayT,null);
			InputStream inSTransfer = pTransfer.getInputStream();
			OutputStream outSTransfer = pTransfer.getOutputStream();
			Scanner fromTransfer = new Scanner(inSTransfer); 
			PrintStream toTransfer = new PrintStream(outSTransfer);
			
			
		
			
			
			toTransfer.println(fromComSys.nextShort());
			short numchars = fromComSys.nextShort();
			toTransfer.println(numchars);
			toTransfer.flush();
			
			short share = 0; 
			short datatobuf; 
			int bufferSize = 128;
			int loops;
		
			short endIO = 0;
			for(int i = 1; System.in.available() == 0 ; i++ ) {
			//	if(fromIO.hasNext()){
				if(fromIO.hasNextShort(idIOFull)){										
					endIO = fromIO.nextShort();  
					i--;	
				} 
				
				share = fromIO.nextShort(); 		//get data from IO to Transfer
				toTransfer.println(share);
				toTransfer.flush();
				
				if (endIO != idIOFull){									
					if(numchars % bufferSize != 0 && i-1 % bufferSize == 0 && i != 1  ){
						toIO.println(fromTransfer.nextShort());
						toIO.flush();
						loops = bufferSize;
						while(loops > 0){

							toBuffer.println(fromTransfer.nextShort()); // mode read
							toBuffer.flush();
							
							if(fromBuffer.hasNext())
							toTransfer.println(fromBuffer.next());
							toTransfer.flush();
							
							System.out.println(fromTransfer.nextShort()); // sends cs to print
							loops--;
						}
					}
				}

					share = fromTransfer.nextShort();
					toIO.println(share);
					toIO.flush();
					toBuffer.println(fromTransfer.nextShort()); // mode write
					datatobuf = fromTransfer.nextShort();
				//	System.out.println(datatobuf);  // from io to buf
					toBuffer.println(datatobuf); // data
					toBuffer.flush();	
				
				
				if (i == numchars || endIO == 35 ){
					if( i <= bufferSize)
						loops = i;
					else
						loops = i - ( (i / bufferSize) * bufferSize) ;
					while(loops > 0){

						share = fromTransfer.nextShort();
					//	System.out.println(share);
						toBuffer.println(share); // mode read
						toBuffer.flush();
						
						if(fromBuffer.hasNext())
						share = fromBuffer.nextShort();
					//	System.out.println(share);
						toTransfer.println(share);
						toTransfer.flush();
						
						System.out.println(fromTransfer.nextShort()); // sends cs to print
						loops--;
					}
					System.out.println(fromTransfer.nextShort());
					if(fromComSys.hasNext()){	
						if( quitCS == fromComSys.nextShort()){ // send quit signals
							 toIO.println(quitIO);
							 toTransfer.println(quitT);
							 toBuffer.println(quitB);
							 System.exit(0);	
						}							
					}
					
				}
					
			}
		

		}
		catch(IOException ex)
		{
			System.out.println("Unable to run ");
			ex.printStackTrace();
		}
		/* catch(InterruptedException e){
			System.out.println("ERROR");
		} */
	
	}
		
}
	
	
	
	
	
	