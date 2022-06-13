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
public class InOut{
		byte idIO = 32;
		static byte idIOFull = 35;
		byte idTransfer = 16;
		static byte quitIDIO = 36;
		byte quitIDT = 20;
		byte tFull = 17;
		byte tAccepted = 16;
	public static void main(String[] args){
		
		try {
			 // 5, 4
			short fullT = 17;
			short acceptedT = 16;

			String file = args[0];
			String idBits = "00100011";// the program id  = 3 so 3rd value after 3bit is set to one. first 3bits are first digit in btye in binary from 7-1
			 
			byte IOID = 32;
			
			byte[] wire = new byte[2];
			short out;
			short in;
			
			Scanner fromBus = new Scanner(System.in);
			InOut IO = new InOut();
			BufferedReader inputFile = new BufferedReader(new FileReader(file));
			
			int numinput = 0;
			byte isSent; 
			String line;
			String nextL;
			byte[] buffer = new byte[8]; //rand
		//	String line = inputFile.readLine();
			while ((line = inputFile.readLine()) != null) {
				char c = line.charAt(0);
				if(c == 't'){
					buffer = line.substring(1).getBytes();
				//	nextL = inputFile.readLine();
					int j=0;
					for(int i = 1; i < line.length() ; i++ ){
						buffer[j] = (byte) line.charAt(i);
						out = IO.shortData(IOID,buffer[j]);
						System.out.println(out); // to transfer device
						while(((in = fromBus.nextShort()) != acceptedT)){ // from transfer device if value is accpeted
							if(in == quitIDIO )
								System.exit(0);
							else if ( in == fullT){
								System.out.println("waiting 1 sec");
								Thread.sleep(Long.parseLong("100"));
							}
							else
								System.out.println("wrong ID");
						}
						j++;
						numinput++;
						if( j >= buffer.length)
							j=0;
					}
					
				}
				else if( c =='n'){
						byte newline = 10;
						if((nextL = inputFile.readLine()) == null){
							out = IO.shortData(idIOFull,newline);
							System.out.println(idIOFull);
						}
						else
							out = IO.shortData(IOID,newline);  //newline = 100
						System.out.println(out); // to transfer device
						while(((in = fromBus.nextShort()) != acceptedT)){
							if(in == quitIDIO )
								System.exit(0);
							else if ( in == fullT){
								System.out.println("waiting 1 sec");
								Thread.sleep(Long.parseLong("100"));
							}
							else
								System.out.println("wrong ID");
						}
						numinput++;
				}
				else if ( c == 'd')
					Thread.sleep(Long.parseLong(line.substring(2)));
				//	Thread.sleep(Long.parseLong("10000"));
				else
					throw new InterruptedException("ERROR");				
			}
//		System.out.println(idIOFull);
//			System.out.println(numinput);
		}
		catch(IOException ex)
		{
			System.out.println("Unable to run ");
		}
		catch(InterruptedException e){
			System.out.println("ERROR");
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
	
	
	
}	