ReadMe file

files:
	ComputerSystem.class - main class file compiled by program.
	ComputerSystem.java - computer system class main program executes Bus and print output
	InOut.java - the IO class that interprets the input file and sends it out to transfer device
	inputs.txt - the text file to interpret and used as byted data to store and print out.
	SystemBus.java - Bus program acting as wires between the other programs, sending 16bit data to each other.
	TransferDevice.java - mangages the amount of data transfer from IO to Buffer and send data to computer System
to print out.
	Buffer.java - stores 128 bytes of data at a time using read and write modes

compiling: 
	In command line excute" javac ComputerSystem.java".
	then execute" java ComputerSystem inputs.txt 'numberOfBytes' ".

