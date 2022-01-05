package computer.hardware.panel;

import computer.hardware.cpu.*;
import computer.hardware.memory.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
 * Reads in Instructions.txt automatically when called via IPL
 * Sets text into memory.
 * 
 * @author lee.joyce21@gmail.com
 * @version v0.0.2
 * 
 * */
public class FileActions {
	List<String> data = new ArrayList<String>();
	Memory mem = computer.TimeToRock.mem;
	Registers reg = computer.TimeToRock.reg;
	public static void main(String[] args) throws Exception {
		
	}
	
	//the function to read the memory file
	//reads into as a multidimensional array with the addresses and the data
	public void readFile(String name) throws Exception{
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream(name);
		try(InputStreamReader sR = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader buffRead = new BufferedReader(sR)){
		String str;
		
		//reads file into string list
		while((str=buffRead.readLine())!=null) {
			data.add(str);
		}
		//changes list to data array, primArray for primary Array
		String[] primArray = new String[data.size()];
		primArray = data.toArray(primArray);
		
		//splits the values in primArray to get rid of the ""
		String[][] hexArray = new String[primArray.length][8];
		for(int i=0; i<primArray.length; i++) {
			String[] splitstr = primArray[i].split("");

			hexArray[i][0]=splitstr[0];
			hexArray[i][1]=splitstr[1];
			hexArray[i][2]=splitstr[2];
			hexArray[i][3]=splitstr[3];//splitstr[4] is missing because that is the space
			hexArray[i][4]=splitstr[5];
			hexArray[i][5]=splitstr[6];
			hexArray[i][6]=splitstr[7];
			hexArray[i][7]=splitstr[8];
			}
			hexToBin(hexArray);
		
		buffRead.close();
	}}
	//changes the hexadecimal numbers to binary, still in a multidimensional array
	public void hexToBin(String[][] array) {
		String[][] address = new String[array.length][2];
		
		for(int i=0;i<array.length;i++) {
			StringBuilder memoryAddress = new StringBuilder();
			for(int j=0;j<4;j++) {
				//first switch case is for the memory addresses (0-3)
				String value = array[i][j];
				//case statement for changing hex to bin by checking character by character
				switch(value) {
				case "0":
					memoryAddress.append("0000");break;
				case "1":
					memoryAddress.append("0001");break;
				case "2":
					memoryAddress.append("0010");break;
				case "3":
					memoryAddress.append("0011");break;
				case "4":
					memoryAddress.append("0100");break;
				case "5":
					memoryAddress.append("0101");break;
				case "6":
					memoryAddress.append("0110");break;
				case "7":
					memoryAddress.append("0111");break;
				case "8":
					memoryAddress.append("1000");break;
				case "9":
					memoryAddress.append("1001");break;
				case "A":
					memoryAddress.append("1010");break;
				case "B":
					memoryAddress.append("1011");break;
				case "C":
					memoryAddress.append("1100");break;
				case "D":
					memoryAddress.append("1101");break;
				case "E":
					memoryAddress.append("1110");break;
				case "F":
					memoryAddress.append("1111");break;
				}
		}
			address[i][0]= memoryAddress.toString();
			memoryAddress = new StringBuilder();
		
			//following switch statement for the data (4-7)
			for(int j=4;j<8;j++) {
				String value = array[i][j];
				//case statement for changing hex to bin
				switch(value) {
				case "0":
					memoryAddress.append("0000");break;
				case "1":
					memoryAddress.append("0001");break;
				case "2":
					memoryAddress.append("0010");break;
				case "3":
					memoryAddress.append("0011");break;
				case "4":
					memoryAddress.append("0100");break;
				case "5":
					memoryAddress.append("0101");break;
				case "6":
					memoryAddress.append("0110");break;
				case "7":
					memoryAddress.append("0111");break;
				case "8":
					memoryAddress.append("1000");break;
				case "9":
					memoryAddress.append("1001");break;
				case "A":
					memoryAddress.append("1010");break;
				case "B":
					memoryAddress.append("1011");break;
				case "C":
					memoryAddress.append("1100");break;
				case "D":
					memoryAddress.append("1101");break;
				case "E":
					memoryAddress.append("1110");break;
				case "F":
					memoryAddress.append("1111");break;
				}
		}
			address[i][1]= memoryAddress.toString();
			//memory at address[i][0], set value [i][1]
			mem.writeMemory(Integer.parseInt(address[i][0], 2), address[i][1]);
		}
		
		char[] arr = address[0][0].toCharArray();
		char[] temp = new char[12];
		int length = arr.length;
		for(int i = 0; i< 12; i++) {
			if((arr.length-(i+1))<0 ) {
				temp[11-i] = '0';
			}
			else {
			temp[11-i] = arr[arr.length-(i+1)];
			}
		}
		String bin = new String(temp);
		reg.PC.setValue(temp);
	}
	
	
}
