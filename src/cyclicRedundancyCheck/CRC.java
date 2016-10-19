package cyclicRedundancyCheck;

import java.util.Arrays;

/**
 * Classe que calcula e verifica CRC de acordo com um gerador
 * Exercicio Pratico da disciplina "Redes de Sensores sem Fio"
 * Data: 05/04/2010
 * @author João Daher Neto
 */
public class CRC {
    private short[] data;
    private short[] generator;
    private short[] crc;

    /**
     * Class constructor that prepares a data or message to have its CRC calculated or verified
     * @param data: The data or message
     * @param generator: The binary knwon by the transmiter and the receiver, wich is applied to the CRC calculation and verification
     */
    public CRC(short[] data, short[] generator) {
        this.data = data;
        this.generator = generator;
    }
    
    /**
     * Calculates the CRC from the data given when the class is instantiated
     * @return The WHOLE message (data + CRC)
     */
    public void calculateCRC(){
        int cutAmt = generator.length;
        
        short[] dividend = this.prepareData();
        System.out.println("Dados modificados para: " + Arrays.toString(dividend));
                
        short[] num = this.subArray(dividend, 0, cutAmt-1);
        short[] leftData = this.subArray(dividend, cutAmt, dividend.length-1);
                
        short quocient;
        while(leftData != null){
            //System.out.println("Ainda falta dividir..." + Arrays.toString(leftData));
            quocient = this.div(num, generator);
            //System.out.println("Dividindo...: " +Arrays.toString(num)+ " % " +Arrays.toString(generator)+ " = " +quocient);
            
            if(quocient == 1){
                short[] rest = Binary.xorBin(num, generator, false);
                //System.out.println("\t...e resto = " + Arrays.toString(rest));
                num = this.addNextBit(rest, leftData);
                //System.out.println("A seguir, irei dividir o numero " + Arrays.toString(num));
                leftData = this.subArray(leftData, 1, leftData.length-1);
            }
            else{
                //System.out.println("\t...e resto indeferente!");
                num = this.addNextBit(num, leftData);
                //System.out.println("A seguir, irei dividir o numero " + Arrays.toString(num));
                leftData = this.subArray(leftData, 1, leftData.length-1);
            }
        }
        
        //System.out.println("Sem mais divisoes!! Sobrou: " + Arrays.toString(num));
        this.crc = Binary.extendBinary(num, generator.length-1, false);
        //System.out.println("Append data: " + Arrays.toString(crc));
    }

    /**
     * Verifies if the given message stored in data atributte (formed by transmitter's data + CRC) matches the CRC using the generator
     * @return True, if the CRC is right
     */
    public boolean verifyCRC(){
        int cutAmt = generator.length;
        
        short[] dividend = data; //here is the diference!
                
        short[] num = this.subArray(dividend, 0, cutAmt-1);
        short[] leftData = this.subArray(dividend, cutAmt, dividend.length-1);
        
        short quocient;
        short[] rest = null;
        
        while(leftData != null){
            //System.out.println("\nAinda falta dividir..." + Arrays.toString(leftData));
            quocient = this.div(num, generator);
            //System.out.println("\nDividindo...: " +Arrays.toString(num)+ " % " +Arrays.toString(generator)+ " = " +quocient);
            
            if(quocient == 1){
                rest = Binary.xorBin(num, generator, false);
                //System.out.println("\t...e resto = " + Arrays.toString(rest));
                num = this.addNextBit(rest, leftData);
                //System.out.println("A seguir, irei dividir o numero " + Arrays.toString(num));
                leftData = this.subArray(leftData, 1, leftData.length-1);
            }
            else{
                //System.out.println("\t...e resto indeferente!");
                num = this.addNextBit(num, leftData);
                //System.out.println("A seguir, irei dividir o numero " + Arrays.toString(num));
                leftData = this.subArray(leftData, 1, leftData.length-1);
            }
        }
        
        System.out.println("No final das contas, o resto eh: " + Arrays.toString(rest));
        
        return (rest[rest.length-1]==0)?true:false;
    }
    
    /**
     * Returns the CRC array *USE the method 'calculateCRC()' beforeward*
     * @return The CRC binary value
     */
    public short[] getCRC(){
        return this.crc;
    }
    
    public short[] getMessage(){
        return this.joinArray(data, crc);
    }
    
    /**
     * Prepares the data to enter the multiple XOR division
     * by left shifting the data an amount determined bit the most significative bit from the generator array
     * Data: 010101
     * Generator: 1011 (x3 + x1 + x0)
     * New data: Data << 3 : 010101(000)
     * @return 010101000
     */
    private short[] prepareData(){
        short[] num = new short[data.length+generator.length-1];
        int i;
        for(i=0; i<data.length; i++){
            num[i] = data[i];
        }
        for(int j=i; j<num.length; j++){
            num[j] = 0;
        }
        return num;
    }
    
    /**
     * Adds the first bit from 'leftData' to the end of 'rest' array
     * @param rest: [0,1,1,0]
     * @param leftData: [1,0,1,0]
     * @return [0,1,1,0,1]
     */
    private short[] addNextBit(short[] rest, short[] leftData){
        if(leftData == null) return null;
        else{
            short[] newBin = new short[rest.length+1];
            for(int i=0; i<rest.length; i++){
                newBin[i] = rest[i];
            }
            newBin[newBin.length-1] = leftData[0];
            return newBin;
        }
    }
    
    /**
     * Divides 2 bit arrays by checking the most significative bit of each array
     * @param num: [0,1,1] *Lenght must be smaller or equal div's lenght*
     * @param div: [1,0,1] *Most significative bit must be '1'*
     * @return 0
     */
    public short div(short[] num, short[] div){
        div = this.removeLeftZeros(div); //o bit mais significativo eh 1 por definicao, mas por garantia...
        
        num = Binary.extendBinary(num, div.length, false);
        
        return (num[0]==0)?(short)0:(short)1;
    }
    
    /**
     * Makes bit '1' be the most significative one by removing the '0' bits from the left. 
     * @param bin: [0,0,0,1,0,1,1,0]
     * @return [1,0,1,1,0]
     */
    private short[] removeLeftZeros(short[] bin){
        int zero = 0;
        for(int i=0; i<bin.length; i++){
            if(bin[i] == 0) zero++;
            else break;
        }
        
        short[] array = new short[bin.length-zero];
        for(int i=0; i<array.length; i++){
            array[i] = bin[i+zero];
        }
        
        return array;
    }

    /**
     * Creates a subarray from a given array by removing unwanted bits
     * @param array: [10,20,30,40,50,60]
     * @param startIdx: 2
     * @param endIdx: 4
     * @return [30,40,50]
     */
    private short[] subArray(short[] array, int startIdx, int endIdx){
        //System.out.println("#Cortando o vetor " +Arrays.toString(array) + " entre " +startIdx+ " e " +endIdx+ " #");
        //System.out.println("#O novo vetor tera o tamanho: " +(endIdx-startIdx+1)+ " #");
        if(startIdx <= endIdx){
            if(endIdx >= array.length) endIdx=array.length-1;
            if(startIdx <0) startIdx = 0;
            
            short[] newArray = new short[endIdx-startIdx+1];
            for(int i=startIdx; i<=endIdx; i++){
                newArray[i-startIdx] = array[i];
            }
            return newArray;
        }
        else{
            System.out.println("Corte maluco!!"
                             + "\tData: " + Arrays.toString(array)
                             + "\tStartIdx: " + startIdx
                             + "\tEndIdx: " + endIdx);
            return null;
        }
        
    }

    /**
     * Concatenates 2 given arrays
     * @param a1: [1,2,3,4]
     * @param a2: [5,6,7,8]
     * @return [1,2,3,4,5,6,7,8]
     */
    private short[] joinArray(short[] a1, short[] a2){
        short[] array = new short[a1.length+a2.length];
        for(int i=0; i<a1.length; i++){
            array[i] = a1[i];
        }
        for(int i=0; i<a2.length; i++){
            array[i+a1.length] = a2[i];
        }
        return array;
    }
}
