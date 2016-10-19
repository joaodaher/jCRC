package cyclicRedundancyCheck;

/**
 * Classe que calcula e verifica o CRC-CCITT
 * Exercicio Pratico da disciplina "Redes de Sensores sem Fio"
 * Data: 05/04/2010
 * @author João Daher Neto
 */
public class CRC_CCITT extends CRC{

    /**
     * Construtor that instantiates the super-class cyclicRedundancyCheck.CRC
     * by using the CCITT generator, wich is the following:
     * [1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1] = x16 + x12 + x5 + 1
     * @param data: The data or message
     */
    public CRC_CCITT(short[] data) {
        super(data, new short[] {1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1});
    }
    
}
