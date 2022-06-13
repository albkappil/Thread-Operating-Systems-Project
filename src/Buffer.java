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
	id: 4th program ,01000000: 64
	bits added to id: 
		4 = quitIDB  = 68
		1 = inDataInt = 67
	ids from Transfer device:
		 	write = 18  
		    read = 19	
		    
			
**/
public class Buffer{
	
		private	byte quitIDT = 20; // 4
		private	static byte quitIDB = 68; // 4
		private	static byte writeT = 18; // 2
		
	public static void main(String[] args){
		try{  
			
			Scanner fromBus = new Scanner(System.in);
			Buffer b = new Buffer();
			
			byte[] buf = new byte[128];
			byte[] tempID = new byte[2];
			
			byte idDataInt = 67; // id of data and interrupt
			short readT = 19; 
			
			//String mode  from transfer;
			short mode;
			int i = 0;
			int index = 0;
			
			
			mode = fromBus.nextShort();
			while(!(mode == quitIDB)){
				if(mode == readT){
					System.out.println(b.shortData(idDataInt, buf[i]));
					i++;
					if ( i == index)
						index = 0;
						
				}
				else if(mode == writeT){
					
					tempID = b.getByte(fromBus.nextShort());
					buf[index] = tempID[1]; 
					// buf[index] = fromBus.nextByte(); // debug use only with no catch
					index++;  
				}
				mode = fromBus.nextShort();	
			}

		}
		 catch(IOException ex){
			System.out.println("Wrong ID");
			
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
	
		public byte[] getByte(short value)throws IOException  {
			ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
			buffer.putShort(value);
			byte[] in = buffer.array();
			if (in[0] == quitIDT ||  in[0]  == quitIDB){
				System.exit(0);
				in[0] = -1;
				return in;
			}
			else if ((in[0] == writeT) )
				return in;
			else{
				 throw new IOException();
				
			}
		}
	
	
	
}	