package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                		getClass().getResourceAsStream("/documents/texto.txt")))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    frases.add(linea.trim());
                }
            }

        } catch (Exception e) {
            System.out.println("Error al cargar frases: " + e.getMessage());
        }
    }

    public String getFraseAleatoria() {
        if (frases.isEmpty()) return "";
        return frases.get(random.nextInt(frases.size()));
    }
}