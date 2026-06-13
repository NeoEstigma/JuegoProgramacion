package model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrasesDao {

    private List<String> frases;
    private Random random;

    public FrasesDao() {
        frases = new ArrayList<>();
        random = new Random();
        cargarFrases();
    }

    private void cargarFrases() {
    	    try {
    	        Path ruta = Paths.get(getClass().getResource("/documents/texto.txt").toURI());
    	        List<String> lineas = Files.readAllLines(ruta);
    	        for (String linea : lineas) {
    	            if (!linea.trim().isEmpty()) {
    	                frases.add(linea.trim());
    	            }
    	        }
    	    } catch (IOException e) {
    	        System.out.println("Error al leer el fichero: " + e.getMessage());
    	    } catch (URISyntaxException e) {
    	        System.out.println("Ruta incorrecta: " + e.getMessage());
    	    }
    	}
        
    public String getFraseAleatoria() {
        if (frases.isEmpty()) return "";
        return frases.get(random.nextInt(frases.size()));
    }
}
