# Simulated Computer - Architecture Project

## Structure

### Registers

**Register Interface**(Register.java),

then those register class implement the Register Interface:

2. Condition Code(4bits) set when arithmetic/logical operations are executed(CC.java)
3. Divider Register to store the divider of the DVD instruction(DR.java)
4. General Program Register(GPR.java)
5. Internal Address Register(IAR.java)
6. Instruction Register(IR.java)
6. Internal Result Register which is used to temporarily store calculation results(IRR.java)
8. Memory Address Register(MAR.java)
9. Memory Buffer Register(MBR.java)
10. Reserved Register MFR, Only one instance ofthis class exist in computer(MFR.java)
10. Multiplicand Register MLR(MLR.java)
11. Program Counter Register(12 bits) contains the address of the nextinstruction to be executed(PC.java)
12. Register which stores the product in instruction "MLT"(PR.java)
13. Quotient Register(QR.java)
14. Remainder Register(RR.java)
15. Shift and Rotate Register(16bits, be used in shift and rotate data) implemented in SRR.java
14. Index Register(X.java)
15. Temporary Register to store operands forcalculation in ALU(Y.java)
18. Z Register to store the result calculated in ALU(Z.java)

### Addressing

Addressing function implemented in the Addressing.java, provide the way to calculate the effective address.

Core Functions:

```
public class Addressing {
	...
    public static void getEffectiveAddress(String x, String i, String address){
    	...
    	// direct addressing
    	if (I=="0"){
            // no indexing
            if X=="00", EA = Address
            // indexing
            if X==1..3, EA = c(X) + A
    	}
    	// indirect addressing
    	else if (I=="1"){
            // no indexing
            if X=="00", EA = c(Address)
            // indexing
            if X==1..2, EA = c(c(X)+address)
    	}
    }

    public static void sendStepInformation(){
    	// Send Information to GUI
	}
}
```

### ALU

Arithmetic logic unit is implemented in the ALU.java, provide addition, minus, compareTwo, logical OR, logical AND and logical NOT.

### Cache

The class Cache implemented in Cache.java represents A **Fully Associated Cache** in the Computer.

```
//fully associated cache
public class Cache {
    ...
    // According to address ,return data
    public String getdata(String address){
        ...
    }
    
    //if miss, then get data from memory
    public String getIfMiss(String address){
        ...
    }
	
	// fetch word from cache or memory by address
    public  void cacheToMBR(String address){
        ...
    }

    //write back data to Cache and Memory
    public void writeBack(String address,String data){
        ...
    }
}
```

### CacheLine

The class CacheLine in CacheLine.java is used to present one line in the Cache.

### Controler

Ued to control the process in the system which is implemented in controler.java

### CPU

It includes all registers, ALU, Memory, cycle and Cache implemented in CPU.java

### Decoder

Implemented in Decoder.java, and aim to decode the instruction sent by I/O.

Partition the binary command to four part: opcode(LDR, STR, LDA, LDX, STX…), X(Index Register), R(General Purpose Register), I(direct or indirect addresing).

### Halt

Multi-thread to block to main thread(in ordert to parition the process of one instuction to several parts and show the procedure on the GUI).

### IOmemory

I/O has separated i/o memory space implement in IOmemory.java

### ISA

ISA implementd in ISA.java is used to get the make decision which instuction is executed. And other instuctions extend it. There are the Instruction set:

1. Arithmetic instructions include add, substract, multiply and divide(ArithmeticInstructions.java)
2. I/O Instructions include IN and OUT implemented in IOInstructions.java
3. Load and store instruction implemented in LoadAndStore.java
4. Logical instruction implemented in LogicalInstruction.java
5. Micellaneous Instructions(Miscellaneous Instruction.java)


4. Shift and Rotate Instructions implemented in ShiftAndRotate.java


5. Transfer Instruction implemented in TransferInstruction.java

### Memory

There is a Memory class using HashMap<address, data> to represents memory implemented in Memory.java

```
package core;

import java.util.HashMap;

public class Memory {
    //use HashMap to represent memory,key represents address,value represents data
    private HashMap<String ,String> memorySpace=new HashMap<String, String>();
    ...
    //get the content from  memory
    public String getContent(String address){
    	...
    }

    //store value to the address
    public void setContent(String address,String value){
    	...
    }

    //add one to the address to help build the HashMap
    public String addressAddone(String s1){
        ...
    }
    
    //clear all memory unit
    public void clear() {
    	...
    }
}
```

