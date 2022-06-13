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
public class ComputerSystem{
	public static byte idTData = 19;
	public static byte quitIDT = 20;
	//byte 
	public static void main(String[] args){
		try
		{	
			ComputerSystem cs = new ComputerSystem();
			byte[] print = new byte[2];
			byte[] dataID = new byte[2];
			
			
			short csID = 8; 
			short quitIDcs = 12;

			
			short numBytes = Short.parseShort(args[1]);

			String file = args[0];
			String[] cmdArray = new String[3];
			cmdArray[0] = "java";
			cmdArray[1] = "SystemBus.java";
			cmdArray[2] = file;
			Process process = Runtime.getRuntime().exec(cmdArray,null);
			InputStream inStream = process.getInputStream();
			OutputStream outStream = process.getOutputStream();
			PrintStream toBus = new PrintStream(outStream);
			Scanner fromBus = new Scanner(inStream);

			
			toBus.println(csID); // send id to Transfer device
			toBus.println(numBytes); // numberBytes to Transfer Device
			toBus.flush();
			
			
			short idsignal = 0;
			byte newline = 10;
			char letter;
			//System.out.println("waiting on data ...");
			/* 
			for(short i = 0;  inStream.available() == 0; i++){		
				print = cs.getBytes(fromBus.nextShort());		
				/* if(print[0] == quitIDcs){
					System.out.println("quit mode");
					if (print[1] == newline)
						System.out.print("\n");
					else{
					letter = (char) print[1];
					System.out.print(letter);
					toBus.println(quitIDcs);
					toBus.flush();
					System.exit(0);
					}
				} */
			//	if(print[0] == idTData ){
					if (print[1]  == newline)
						System.out.print("\n");
					else{
						letter = (char) print[1];
						System.out.print(letter);
					}
			//	}
			
					
				
			//} 
			short inData;
			
			for(short i = 1; (inData = fromBus.nextShort()) != quitIDcs ; i++){		
				print = cs.getBytes(inData);
				if (print[1]  == newline)
						System.out.print("\n");
				else{
					letter = (char) print[1];
					System.out.print(letter);
				}
			}
				
			toBus.println(quitIDcs);
			toBus.flush();
		
		}
	
		catch(IOException ex)
		{
			System.out.println("Processes are closed");
		}

		
	
	}
	public short shortData( byte id, byte data){
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(id);
		bb.put(data);
		short shortVal = bb.getShort(0);
		return shortVal;
		
	}
	
	public static byte[] getBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(value);
		byte[] in = buffer.array();
		return in;
				
		
	}
	
	
}