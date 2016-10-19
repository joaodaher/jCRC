package cyclicRedundancyCheck;

import java.util.Arrays;

/**
 * Classe auxiliadora criada na disciplina de "Arquitetura de Computadores"
 * Data: 12/06/2009
 * @author João Daher Neto
 */
public class Binary {

    public static double binToDec(short[] bin, boolean signalBit){
        double dec = 0;
        if(signalBit && bin[0] == 1){ //negative
            short[] oneNeg = new short[bin.length];
            for(int i=0; i<oneNeg.length; i++){
                oneNeg[i] = 1;
            }

            bin = sumBin(bin, oneNeg); //subtract 1 (binary)
            bin = invertBin(bin);

            int cont=0;
            for(int i=bin.length-1; i>=0; i--){
                dec += bin[i]*Math.pow(2, cont);
                cont++;
            }
            dec=dec*(-1);
        }
        else{ //positive
            int cont=0;
            for(int i=bin.length-1; i>=0; i--){
                dec += bin[i]*Math.pow(2, cont);
                cont++;
            }
        }
        return dec;
    }

    public static String binToHex(short[] bin, boolean signalBit){
        if(bin.length%4!=0){
            System.out.println("HEX: O binario tem " +bin.length+ " bits e será aumentado para " +(bin.length+(bin.length%4))+ " bits.");
            bin = Binary.extendBinary(bin, bin.length+(bin.length%4), signalBit);
        }
        
        String quart, hex = "";
        for(int i=0; i<bin.length; i+=4){
            quart = bin[i] + "" + bin[i+1] + "" + bin[i+2] + "" + bin[i+3];
            
            if(quart.equalsIgnoreCase("0000")){
                hex += "0";
            }
            else if(quart.equalsIgnoreCase("0001")){
                hex += "1";
            }
            else if(quart.equalsIgnoreCase("0010")){
                hex += "2";
            }
            else if(quart.equalsIgnoreCase("0011")){
                hex += "3";
            }
            else if(quart.equalsIgnoreCase("0100")){
                hex += "4";
            }
            else if(quart.equalsIgnoreCase("0101")){
                hex += "5";
            }
            else if(quart.equalsIgnoreCase("0110")){
                hex += "6";
            }
            else if(quart.equalsIgnoreCase("0111")){
                hex += "7";
            }
            else if(quart.equalsIgnoreCase("1000")){
                hex += "8";
            }
            else if(quart.equalsIgnoreCase("1001")){
                hex += "9";
            }
            else if(quart.equalsIgnoreCase("1010")){
                hex += "A";
            }
            else if(quart.equalsIgnoreCase("1011")){
                hex += "B";
            }
            else if(quart.equalsIgnoreCase("1100")){
                hex += "C";
            }
            else if(quart.equalsIgnoreCase("1101")){
                hex += "D";
            }
            else if(quart.equalsIgnoreCase("1110")){
                hex += "E";
            }
            else if(quart.equalsIgnoreCase("1111")){
                hex += "F";
            }
        }
        return hex;
    }
    
    public static short[] decToBin(int dec, int digits) throws ArrayIndexOutOfBoundsException{
        short resto;
        short[] bin;
        String [] binStr;
        String aux = "";

        boolean negative = dec<0;

        if(negative){
            dec = dec*(-1);
        }

        do{
            resto = (short)(dec % 2);
            aux = resto + " " + aux;
            dec = dec/2;
        }while(dec>0);

        binStr = aux.split(" ");

        bin = new short[digits];
        int blank = digits-binStr.length; //digits left to get the required size
        for(int i=0; i<binStr.length; i++){
            bin[blank+i] = (short) Integer.parseInt(binStr[i]);
        }

        if(!negative){
            for(int i=0; i<blank; i++){
                bin[i]=0;
            }
        }
        else{
            bin = invertBin(bin);
            short[] one = new short[bin.length];
            for(int i=0; i<one.length-1; i++){
                one[i]=0;
            }
            one[one.length-1] = 1;

            bin = sumBin(bin, one);

            for(int i=1; i<blank; i++){
                bin[i]=1;
            }
        }
        return bin;
    }

    private static short[] invertBin(short[] bin){
        short[] inverse = new short[bin.length];

        for(int i=0; i<bin.length; i++){ //inverts the binary number
            if(bin[i]==0){
                inverse[i]=1;
            }
            else{
                inverse[i]=0;
            }
        }
        return inverse;
    }

    public static short[] sumBin(short[] a, short[] b){
        short carry = 0;
        int aux;
        short[] result;
        result = new short[b.length];

        for(int i=a.length-1; i>=0; i--){
            aux = a[i] + b[i] + carry;
            if(aux > 1){
                carry = 1;
            }
            else{
                carry = 0;
            }

            if(aux == 0 || aux == 2){
                result[i] = 0;
            }
            else{
                result[i] = 1;
            }
        }

        return result;

    }

    public static short[] subBin(short[] a, short[] b){
        short[] newB = invertBin(b);
        short[] one = decToBin(1, b.length);
        newB = sumBin(newB, one);

        return sumBin(a, newB);
    }

    public static short[] extendBinary(short[] bin, int digits, boolean signalBit){
        short[] newBin = new short[digits];
        int blanks = digits-bin.length;
        //System.out.println("Diferença de digitos: " +blanks);

        if(bin.length < digits){ //fill extra digits
            if(signalBit && binToDec(bin, true) < 0){ //for negative numbers, fill with '1'
                for(int i=0; i<blanks; i++){
                    newBin[i] = 1;
                }
            }
            else{ //for positive ones, fill with '0'
                for(int i=0; i<blanks; i++){
                    newBin[i] = 0;
                }
            }

            for(int i=blanks; i<digits; i++){
                newBin[i] = bin[i-blanks];
            }

        }
        else { //truncate extra digits
            newBin = Arrays.copyOfRange(bin, blanks*(-1), bin.length);
        }

        //System.out.println("<<Binario: " +Arrays.toString(bin) + "(" +bin.length+ " digitos) " 
        //                 + "modificado para " +digits+ " digitos: " +Arrays.toString(newBin)+ " >>");
        return newBin;
    }

    public static boolean compareBin(short[] a, short[] b){
        if(a.length == b.length){
            for(int i=0; i<a.length; i++){
                if(a[i]!=b[i]){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean lessThan(short[] a, short[] b){
        short[] c = subBin(a, b);

        if(c[0] == 1){ //negative result
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean greaterThan(short[] a, short[] b){
        short[] c = subBin(a, b);

        if(c[0] == 1){ //negative result
            return false;
        }
        else if(compareBin(a, b)){ //equal numbers
            return false;
        }
        else{
            return true;
        }
    }

    //XOR bit-to-bit between 2 binary numbers
    public static short[] xorBin(short[] a, short[] b, boolean signalBit){
        if(a.length != b.length){
            if(a.length > b.length){
                b = extendBinary(b, a.length, signalBit);
            }
            else{
                a = extendBinary(a, b.length, signalBit);
            }
        }

        short[] aXORb = new short[a.length];
        boolean tagA;
        boolean tagB;
        for(int i=a.length-1; i>=0; i--){
            aXORb[i] = xor(a[i], b[i]);
        }
        return aXORb;
    }

    //multiple XOR among every bit of a binary number
    public static short multXOR(short[] binary){
        short xor = 0;
        if(binary.length > 1){
            for(int i=0; i<binary.length; i++){
                xor = xor(xor, binary[i]);
            }
        }
        return xor;
    }

    public static short xor (short a, short b){
        if(a!=0 && a!=1) a=0;
        if(b!=0 && b!=1) b=0;
        
        if(a != b){
            return 1;
        }
        else{
            return 0;
        }
    }

    public static short[] orBin(short[] a, short[] b, boolean signalBit){
        if(a.length != b.length){
            if(a.length > b.length){
                b = extendBinary(b, a.length, signalBit);
            }
            else{
                a = extendBinary(a, b.length, signalBit);
            }
        }

        short[] aORb = new short[a.length];
        boolean tagA;
        boolean tagB;
        for(int i=a.length-1; i>=0; i--){
            //convert a[i]
            if(a[i] == 1){
                tagA = true;
            }
            else{
                tagA = false;
            }
            //convert b[i]
            if(b[i] == 1){
                tagB = true;
            }
            else{
                tagB = false;
            }
            //convert XOR operation
            if(tagA || tagB == true){
                aORb[i] = 1;
            }
            else{
                aORb[i] = 0;
            }
        }
        return aORb;
    }

    public static short[] andBin(short[] a, short[] b, boolean signalBit){
        if(a.length != b.length){
            if(a.length > b.length){
                b = extendBinary(b, a.length, signalBit);
            }
            else{
                a = extendBinary(a, b.length, signalBit);
            }
        }

        short[] aANDb = new short[a.length];
        boolean tagA;
        boolean tagB;
        for(int i=a.length-1; i>=0; i--){
            //convert a[i]
            if(a[i] == 1){
                tagA = true;
            }
            else{
                tagA = false;
            }
            //convert b[i]
            if(b[i] == 1){
                tagB = true;
            }
            else{
                tagB = false;
            }
            //convert XOR operation
            if(tagA & tagB == true){
                aANDb[i] = 1;
            }
            else{
                aANDb[i] = 0;
            }
        }
        return aANDb;
    }

    public static short[] notBin(short[] a){
        short[] NOTa = new short[a.length];
        
        for(int i=a.length-1; i>=0; i--){
            //NOT operation
            if(a[i] == 0){
                NOTa[i] = 1;
            }
            else{
                NOTa[i] = 0;
            }
        }
        return NOTa;
    }

    public static short[] norBin(short[] a, short[] b, boolean signalBit){
        return invertBin(orBin(a, b, signalBit));
    }
    
    public static short[] nandBin(short[] a, short[] b, boolean signalBit){
        return invertBin(andBin(a, b, signalBit));
    }

    public static short[] randomBinary(int lenght){
        short[] rand = new short[lenght];
        for(int i=0; i<lenght; i++){
            if(Math.random() <= 0.5){
                rand[i] = 0;
            }
            else{
                rand[i] = 1;
            }
        }
        return rand;
    }

    //the pattern "01010101"
    public static short[] stringToBin(String txt){
        System.out.println("Binario tem " +(txt.length())+ " digitos");
        short[] bin = new short[txt.length()];
        for(int i=0; i<txt.length(); i++){
            bin[i] = Short.parseShort(String.valueOf(txt.charAt(i)));
        }
        return bin;
    }
    
    //PATTERNS
    //"0000"
    //"0 0 0 0"
    //"0,0,0,0"
    //"[0,0,0,0]"
    public static String formatBinary(short[] bin, String pattern){
        if(pattern.equalsIgnoreCase("0000")){
            String binary = "";
            for(int i=0; i<bin.length; i++){
                binary += bin[i];
            }
            return binary;
        }
        else if(pattern.equalsIgnoreCase("0 0 0 0")){
            String binary = "";
            for(int i=0; i<bin.length; i++){
                binary += bin[i] + " ";
            }
            return binary;
        }
        else if(pattern.equalsIgnoreCase("0,0,0,0")){
            String binary = Arrays.toString(bin);
            return binary.substring(1, binary.length()-1);
        }
        else if(pattern.equalsIgnoreCase("[0,0,0,0]")){
            return Arrays.toString(bin);
        }
        else{
            return null;
        }
    }

    //PATTERNS
    //"1.234"
    public static String formatDecimal(double dec, String pattern){
        String decimal = "";
        if(pattern.equalsIgnoreCase("1.234")){
            int hundredCount = 3;
            String num = String.valueOf(dec);
            
            if(num.charAt(num.length()-3) == 'E'){
                decimal = "1,";
                for(int i=2; i<num.length()-3; i++){
                    if(hundredCount == 0){
                        decimal += ".";
                        hundredCount = 3;
                    }
                    decimal += num.charAt(i);
                    hundredCount--;
                }
                decimal += " x10^" + num.charAt(num.length()-2) + "" + num.charAt(num.length()-1);
                
            }
            else{
                for(int i=num.length()-1; i>=0; i--){
                    if(hundredCount == 0){
                        decimal = "." + decimal;
                        hundredCount = 3;
                    }
                    decimal = num.charAt(i) + "" + decimal;
                    hundredCount--;
                }
            }
        }
        return decimal;
    }
    
    public static boolean isBinary(String txt){
        for(int i=0; i<txt.length(); i++){
            if(txt.charAt(i) != '0' && txt.charAt(i) != '1') return false;
        }
        return true;
    }
    
    public static String binToPolynom(short[] bin){
        String p = "";        
        int exp = -1;
        
        if(bin.length==1 && bin[0]==1){
            p = "1";
        }
        else{
            for(int i=bin.length-1; i>=0; i--){
                exp++;
                if(bin[i] == 1){
                    if(exp == 0){
                        p = "1" +p;
                    }
                    else if(exp == 1){
                        if(p.equalsIgnoreCase("")){
                            p = "x" +p;
                        }
                        else{
                            p = "x + " +p;
                        }
                    }
                    else if(p.equalsIgnoreCase("")){
                        p = "x<sup>" +exp+ "</sup>" +p;
                    }
                    else{
                        p = "x<sup>" +exp+ "</sup> + " +p;
                    }
                }
            }
        }
        p = "<html>" + p + "</html>";
        return p;
    }
}
