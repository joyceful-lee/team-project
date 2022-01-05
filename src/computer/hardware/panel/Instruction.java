package computer.hardware.panel;

import computer.hardware.cpu.*;
import computer.TimeToRock;
import computer.hardware.memory.*;

public class Instruction {
	Memory mem = computer.TimeToRock.mem;
	Registers reg = computer.TimeToRock.reg;

	/*takes instruction string and separates it into substrings for the following values
	 * array[0] = opcode
	 * array[1] = gpr#
	 * array[2] = index reg#
	 * array[3] = address
	 * array[4] = indirect bit, optional
	 * */
	public void disassembler(String str) {
		String[] dis = new String[5];
		String sub = str.substring(0,6);
		String opCode = opCheck(sub);
		dis[0] = opCode;
		int r = Integer.parseInt(str.substring(6,8),2);
		String currR = String.valueOf(r);
		dis[1] = currR;
		int ix = Integer.parseInt(str.substring(8,10),2);
		String currIX = String.valueOf(ix);
		dis[2] = currIX;
		//4 comes before 3 because we want the indirect bit last
		int i = Integer.parseInt(str.substring(10,11),2);
		String currI = String.valueOf(i);
		dis[4] = currI;
		int addr = Integer.parseInt(str.substring(11,16),2);
		String currAddr = String.valueOf(addr);
		dis[3] = currAddr;
		
		instrDirection(dis);
	}
	//calculates the EA
	public int eaCalc(int i, int addr, int ix) {
		String temp;
		String indirect;
		int EA = 0;
		if(i == 0) {
			if(ix == 0) {
				EA = addr;
			}
			else if(ix != 0) {
				EA = addr + ix;
			}
		}
		if(i == 1) {
			if(ix == 0) {
				temp = mem.readMemoryWordString(addr);
				EA = Integer.parseInt(temp,2);
				
			}
			else if(ix != 0) {
				temp = mem.readMemoryWordString(addr+ix);
				EA = Integer.parseInt(temp,2);
			}
		}
			return EA;
	}
	/*
 	* uses instruction string to separate and complete the instruction
 	*/
	public void instrDirection(String[] array) {
	//sets the array holding the values into separate strings
	String currOpcode = array[0]; //kept as string
	int currR = Integer.parseInt(array[1]);
	int currIX = Integer.parseInt(array[2]);
	int currAddr = Integer.parseInt(array[3]);
	int currI = Integer.parseInt(array[4]);
	int EA = eaCalc(currI, currAddr, currIX);
	
	switch(currOpcode) {
		case "HLT":
			//HALT stops the machine 
			stop();
			break;
		case "LDR":
			//LDR - load register from memory c(ea) -> r
			//memory from location EA, go to register currR;
			if(currR==0) {
				reg.GPR0.setValue(mem.readMemoryWordString(EA));
			}
			else if(currR==1) {
				reg.GPR1.setValue(mem.readMemoryWordString(EA));
			}
			else if(currR==2) {
				reg.GPR2.setValue(mem.readMemoryWordString(EA));
			}
			else if(currR==3) {
				reg.GPR3.setValue(mem.readMemoryWordString(EA));
			}
			break;
		case "STR":
			//STR - store register to memory c(r) -> mem(ea)
			//value from currR go into mem(EA)
			if(currR==0) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.GPR0.getValueString());
				}
			}
			else if(currR==1) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.GPR1.getValueString());
				}
			}
			else if(currR==2) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.GPR2.getValueString());
				}
			}
			else if(currR==3) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.GPR3.getValueString());
				}
			}
			break;
		case "LDA":
			//LDA - load register with address EA -> r
			//currR = EA
			if(currR==0) {
				reg.GPR0.setValue(to16(EA));
			}
			else if(currR==1) {
				reg.GPR1.setValue(to16(EA));
			}
			else if(currR==2) {
				reg.GPR2.setValue(to16(EA));
			}
			else if(currR==3) {
				reg.GPR3.setValue(to16(EA));
			}
			break;
		case "LDX":
			//LDX - load index register from memory c(ea)-> Xx
			//load currIX with data in mem of EA
			if(currIX==1) {
				reg.X1.setValue(mem.readMemoryWordString(EA));
			}
			else if(currIX==2) {
				reg.X2.setValue(mem.readMemoryWordString(EA));
			}
			else if(currIX==3) {
				reg.X3.setValue(mem.readMemoryWordString(EA));
			}
			break;
		case "STX":
			//STX - store index register to memory c(Xx) -> mem(ea);
			if(currIX==1) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.X1.getValueString());
				}
			}
			else if(currIX==2) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.X2.getValueString());
				}
			}
			else if(currIX==3) {
				if(mem.checkAvailability(EA)){
					mem.writeMemory(EA, reg.X3.getValueString());
				}
			}
			break;
		case "JZ":
			//jump if zero
			break;
		case "JNE":
			//jump if not equal
			break;
		case "JCC":
			//jump if condition code
			break;
		case "JMA":
			//unconditional jump to address
			break;
		case "JSR":
			//jump&save return addr
			break;
		case "RFS":
			//
			break;
		case "SOB":
			//subtract 1 & branch
			break;
		case "JGE":
			//jump >=
			break;
		case "AMR":
			//add mem to reg
			break;
		case "SMR":
			//subtract mem from reg
			break;
		case "AIR":
			//add immediate to reg
			break;
		case "SIR":
			//subtract immediate from reg
			break;
		case "MLT":
			//multiply reg by reg
			break;
		case "DVD":
			//divide reg by reg
			break;
		case "TRR":
			//test equality of reg and reg
			break;
		case "AND":
			//logical and of reg and reg
			break;
		case "ORR":
			//logical or of reg and reg
			break;
		case "NOT":
			//logical not of reg to reg
			break;
		case "SRC":
			//shift reg by count
			break;
		case "RCC":
			//rotate reg by count
			break;
		case "IN":
			//input char to reg from device
			break;
		case "OUT":
			//output char to device from reg
			break;
		/*case "TRAP":
			//in part 3
			break;
		case "CHK": in part 4 with floating ops
			//check device status to reg
			break;*/
		default:
			//machine fault;
			break;}
	}
	/*
	 * opCheck takes Binary opcode and translates it into symbols in order to use the codeDirection function
	 */
	public String opCheck(String code) {
		String written = null;
		switch(code) {
		case "000000":
			written = "HLT";
			break;
		case "000001":
			written = "LDR";
			break;
		case "000010":
			written = "STR";
			break;
		case "000011":
			written = "LDA";
			break;
		case "100001":
			written = "LDX";
			break;
		case "100010":
			written = "STX";
			break;
		case "001010":
			written = "JZ";
			break;
		case "001011":
			written = "JNE";
			break;
		case "001100":
			written = "JCC";
			break;
		case "001101":
			written = "JMA";
			break;
		case "001110":
			written = "JSR";
			break;
		case "001111":
			written = "RFS";
			break;
		case "010000":
			written = "SOB";
			break;
		case "01001":
			written = "JGE";
			break;
		case "000100":
			written = "AMR";
			break;
		case "000101":
			written = "SMR";
			break;
		case "000110":
			written = "AIR";
			break;
		case "000111":
			written = "SIR";
			break;
		case "010100":
			written = "MLT";
			break;
		case "010101":
			written = "DVD";
			break;
		case "010110":
			written = "TRR";
			break;
		case "010111":
			written = "AND";
			break;
		case "011000":
			written = "ORR";
			break;
		case "011001":
			written = "NOT";
			break;
		case "011111":
			written = "SRC";
			break;
		case "100000":
			written = "RCC";
			break;
		case "111101":
			written = "IN";
			break;
		case "111110":
			written = "OUT";
			break;
		/*case "011110": in part 3
			written = "TRAP";
			break;
		case "111111": in part 4 with floating ops
			written = "CHK";
			break;
			*/
		default:
			//machine fault;
			break;
		}
		return written;
	}
	
	//this function is called when ssButton is pressed to increment a step
	public String ssButton(int count) {
		String value = null;
		String v= null;
		String gpr = reg.IR.getValueString().substring(6,8);
		int r = Integer.parseInt(gpr,2);
		String addr = reg.IR.getValueString().substring(11,16);
		int a = Integer.parseInt(addr,2);
		int num = 0;
		switch(count) {
		case 1: //PC-> MAR
			reg.MAR.setValue(reg.PC.getValue());
			value = reg.MAR.getValueString();
			break;
		case 2: //MAR-> Read & Mem[MAR]->MBR
			String word = mem.readMemoryWordString(Integer.parseInt(reg.MAR.getValueString(),2));
			int val = Integer.parseInt(word, 2);
			reg.MBR.setValue(to16(val));
			value = reg.MBR.getValueString();
			break;
		case 3: //MBR->IR
			reg.IR.setValue(reg.MBR.getValue());
			value = reg.IR.getValueString();
			//increment PC
			v = reg.PC.getValueString();
			num = Integer.parseInt(v, 2)+1;
			reg.PC.setValue(to12(num));
			break;
		case 4: //IR->IAR
			//else count++;
			String str = reg.IR.getValueString().substring(8,10);
			int x = Integer.parseInt(str,2);
			if(x!=0) {
				if(r==0) {
					reg.GPR0.setValue(to16(x+a));
				}
				else if(r==1) {
					reg.GPR1.setValue(to16(x+a));
				}
				else if(r==2) {
					reg.GPR2.setValue(to16(x+a));
				}
				else if(r==3) {
					reg.GPR3.setValue(to16(x+a));
				}
			}
			else {
				if(r==0) {
					reg.GPR0.setValue(to16(a));
				}
				else if(r==1) {
					reg.GPR1.setValue(to16(a));
				}
				else if(r==2) {
					reg.GPR2.setValue(to16(a));
				}
				else if(r==3) {
					reg.GPR3.setValue(to16(a));
				}
			}
			break;
		case 5: //IAR-> MAR
			if(r==0) {
				int temp = Integer.parseInt(reg.GPR0.getValueString(),2);
				reg.MAR.setValue(to12(temp));
				value = reg.MAR.getValueString();
			}
			else if(r==1) {
				int temp = Integer.parseInt(reg.GPR1.getValueString(),2);
				reg.MAR.setValue(to12(temp));
				value = reg.MAR.getValueString();
			}
			else if(r==2) {
				int temp = Integer.parseInt(reg.GPR2.getValueString(),2);
				reg.MAR.setValue(to12(temp));
				value = reg.MAR.getValueString();
			}
			else if(r==3) {
				int temp = Integer.parseInt(reg.GPR3.getValueString(),2);
				reg.MAR.setValue(to12(temp));
				value = reg.MAR.getValueString();
			}
			break;
		case 6: //read MAR work and put into MBR
			int location = Integer.parseInt(reg.MAR.getValueString(),2);
			word = mem.readMemoryWordString(location);
			val = Integer.parseInt(word, 2);
			value = reg.MBR.getValueString();
			break;
		case 7: //execute based on opcode
			disassembler(reg.IR.getValueString());
			break;
		case 8: //IRR->
			value=null;
			break;
		}
		return value;
	}
	public static void main(String[] args) {

    }
	
	//when HLT
	public void stop() {
	String reset16 = "0000000000000000";
    	String reset12 = "000000000000";
	reg.GPR0.setValue(reset16);
        reg.GPR1.setValue(reset16);
        reg.GPR2.setValue(reset16);
        reg.GPR3.setValue(reset16);
        reg.PC.setValue(reset12);
        reg.IR.setValue(reset16);
       	reg.MAR.setValue(reset12);
        reg.MBR.setValue(reset16);
        reg.MFR.setValue("");
	}
	
	//changes smaller bit binary to full 12 or 16 bit
	public String to16(int num) {
		
		char[] arr = Integer.toBinaryString(num).toCharArray();
		char[] temp = new char[16];
		int length = arr.length;
		for(int i = 0; i< 12; i++) {
			if((arr.length-(i+1))<0 ) {
				temp[15-i] = '0';
			}
			else {
			temp[15-i] = arr[arr.length-(i+1)];
			}
		}
		String bin = new String(temp);
		return bin;
		
	}
	public String to12(int num) {
		
		char[] arr = Integer.toBinaryString(num).toCharArray();
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
		return bin;
		
	}


}
