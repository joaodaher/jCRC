
package Arquivos;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManipulation {
    private BufferedReader reader;
    private PrintWriter writer;
    private final String FileName;


    public FileManipulation(String FileName) {
        this.FileName = FileName;
    }

    public void Write(Object message){
        try{
            writer = new PrintWriter(new FileWriter(FileName)); //abrir o arquivo
            writer.print(message); //escrever a mensagem
            writer.close(); //salvar o arquivo
        }
        catch(IOException e){
            System.out.println("Erro ao escrever no arquivo.");
        }
    }

    public String Read(){
        try{
            reader = new BufferedReader(new FileReader(FileName)); //abrir o arquivo

            String message = "";
            while(reader.ready()){
                message += reader.readLine() + "\n";
            }

            reader.close(); //fechar o arquivo

            return message;
        }
        catch(FileNotFoundException e){
            System.out.println("Arquivo não localizado.");
            return null;
        }
        catch(IOException e){
            System.out.println("Erro ao ler o arquivo.");
            return null;
        }
    }


}
