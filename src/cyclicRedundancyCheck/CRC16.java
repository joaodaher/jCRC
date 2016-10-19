package cyclicRedundancyCheck;

/**
 * Classe que calcula e verifica o CRC-16
 * Exercicio Pratico da disciplina "Redes de Sensores sem Fio"
 * Data: 05/04/2010
 * @author João Daher Neto
 */
public class CRC16 extends CRC{
    /**
     * Construtor that instantiates the super-class cyclicRedundancyCheck.CRC
     * by using the CC-16 generator, wich is the following:
     * [1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1] = x16 + x15 + x2 + 1
     * @param data: The data or message
     */
    public CRC16(short[] data) {
        super(data, new short[]{1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1});
    }
    
}
