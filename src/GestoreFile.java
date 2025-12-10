import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestoreFile {
    private String nome;

    public GestoreFile(String nome) {
        this.nome = nome;
    }

    public String leggi() {
        StringBuilder contenuto = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(nome))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenuto.append(linea).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Errore lettura file: " + e.getMessage());
        }
        return contenuto.toString();
    }

    public void scrivi(String testo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nome, true))) {
            writer.write(testo);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Errore scrittura file: " + e.getMessage());
        }
    }
}
