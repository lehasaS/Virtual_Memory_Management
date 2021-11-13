/**
 * @author Lehasa Seoe (SXXLEH001)
 * 27 May 2021
 * This class is used to perform memory translation from Virtual Memory to Physical Memory.
 */
import java.io.*;
import java.util.ArrayList;

public class OS1Assignment {
    private static final ArrayList<String> virtualAddress = new ArrayList<>();
    private static final ArrayList<String> physicalAddress = new ArrayList<>();
    
    /**
     * This method reads in the file and converts the virtual addresses into 64-bit addresses and stores them in a ArrayList
     * @param fileName
     * @throws this method will throw an exception when the file to be read is not found
     */
    public static void readFile(String fileName){
        try(RandomAccessFile data = new RandomAccessFile(fileName, "r")){
           byte[] bytes = new byte[8];
           String temp="";
            for(long i=0, len=data.length()/8; i<len; i++) {
                data.readFully(bytes);
                for(int j=bytes.length-1; j>=0; j--){
                    String str = Integer.toBinaryString(bytes[j]);
                    if(str.length()<8){
                        int numberOfZeros = 8-str.length();
                        for(int p=0; p<numberOfZeros; p++){
                            str = "0"+str;
                        }
                    }
                    if(str.length()>8){
                        str = str.substring(str.length()-8);
                    }

                    temp+=str;
                }

                virtualAddress.add(temp.substring(temp.length()-12));
                temp="";
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

     /**
     * This method is used to perform the address translation through string manipulation and stores the
     physical addresses in a ArrayList in the format 0x0000...
     */
    public static void getPhysicalAddress(){
        String[] pageTable={"2","4","1","7","3","5","6"};
        String offset, index, physical, mapValue="";
        for(String address: virtualAddress){
            offset = address.substring(address.length()-7);
            StringBuilder stringBuilder = new StringBuilder(address);
            index = stringBuilder.delete(address.length()-7,address.length()).toString();
            if (index.equals("")){
                index="0";
            }
            int intIndex = Integer.parseInt(index, 2);

            // This is to check whether we get a map value with index within the bounds of the size of the page table array.
            try {
                mapValue=pageTable[intIndex];
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                System.out.println(intIndex+" Is greater than the Page Table Size");
            }
            String map = Integer.toBinaryString(Integer.parseInt(mapValue));
            physical = map + offset;
            int decimal = Integer.parseInt(physical,2);
            String hexNum = Integer.toString(decimal,16);
            if(hexNum.length()<8){
                int numberOfZeros = 8-hexNum.length();
                for(int p=0; p<numberOfZeros; p++){
                    hexNum = "0"+hexNum;
                }
            }
            hexNum = "0x"+hexNum;
            physicalAddress.add(hexNum);
            physical="";
            offset="";
            index="";
        }
    }
    
     /**
     * This method runs the program and calls appropriate methods to do specific tasks
     * @param args It is an array of strings, it takes the input we type.
     */
    public static void main(String[] args){
        readFile(args[0]);
        getPhysicalAddress();
        try{
            FileWriter writer = new FileWriter("output-OS1.txt", false);
            for (String address : physicalAddress) {
                writer.write(address + "\r\n");
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
