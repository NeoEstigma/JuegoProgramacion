package model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase de acceso al fichero de frases del juego.
 * Lee el fichero de texto ubicado en resources/documents/texto.txt
 * y permite obtener una frase aleatoria para mostrar en el terminal
 * durante la producción pasiva de Data Points.
 *
 * @author Mateo, Laura
 */

public class FrasesDao {
	
	/** Lista de frases cargadas desde el fichero de texto. */
    private List<String> frases;
    /** Generador de números aleatorios para seleccionar frases. */
    private Random random;

    /**
     * Constructor de FrasesDao.
     * Inicializa la lista de frases y el generador aleatorio,
     * y carga el contenido del fichero de texto.
     */
    
    public FrasesDao() {
        frases = new ArrayList<>();
        random = new Random();
        cargarFrases();
    }
    
    /**
     * Lee el fichero texto.txt línea a línea y almacena cada línea
     * no vacía en la lista de frases.
     * Si el fichero no existe o la ruta es incorrecta, muestra un mensaje
     * de error por consola y la lista queda vacía.
     */

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
    
    /**
     * Devuelve una frase aleatoria de la lista cargada.
     * Si la lista está vacía devuelve una cadena vacía.
     *
     * @return frase aleatoria del fichero de texto, o cadena vacía si no hay frases.
     */
        
    public String getFraseAleatoria() {
        if (frases.isEmpty()) return "";
        return frases.get(random.nextInt(frases.size()));
    }
}
