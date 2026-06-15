# Terminal Clicker

## Autoría
Diego Isorna
Laura Fernández Martínez
Mateo Gómez Giráldez

---

## Descripción breve
**Terminal Clicker** es un juego incremental (clicker) ambientado en la estética de una terminal de hacking según el cliché clásico. El jugador acumula **Data Points (DP)** haciendo clic, compra mejoras para automatizar la producción y avanza a través de 5 niveles de acceso hasta alcanzar el **ROOT ABSOLUTO**.

---

## Instrucciones de ejecución

### Requisitos
- Java 21 o superior
- MongoDB en ejecución local en `localhost:27017`
- Gradle (incluido como wrapper en el proyecto)

### Pasos
```bash
# 1. Clona el repositorio
git clone https://github.com/NeoEstigma/JuegoProgramacion.git
cd JuegoProgramacion

# 2. Asegúrate de tener MongoDB iniciado
mongod

# 3. Ejecuta el juego
./gradlew :lib:run
```

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| JavaFX | 21.0.2 | Interfaz gráfica |
| MongoDB Driver Sync | 5.2.1 | Persistencia de datos |
| Gradle | 9.1 | Gestión del proyecto |
| Scene Builder | — | Diseño de vistas FXML |

---

## Estructura del proyecto

```
JuegoProgramacion/
└── lib/
    └── src/main/
        ├── java/
        │   ├── application/
        │   │   └── Main.java               ← Punto de entrada JavaFX
        │   ├── controller/
        │   │   ├── GameController.java     ← Lógica de pantalla de juego
        │   │   ├── MenuController.java     ← Lógica del menú principal
        │   │   └── RankingController.java  ← Lógica del ranking
        │   └── model/
        │       ├── Partida.java            ← Entidad principal del juego
        │       ├── PartidaDao.java         ← Acceso a datos de partida
        │       ├── Mejoras.java            ← Datos de mejoras compradas
        │       ├── Ranking.java            ← Entidad de ranking
        │       ├── RankingDao.java         ← Acceso a datos de ranking
        │       ├── Conexion.java           ← Singleton de conexión MongoDB
        │       └── FrasesDao.java          ← Carga de frases del terminal
        └── resources/
            ├── View/
            │   ├── Menu.fxml               ← Vista del menú
            │   ├── Game.fxml               ← Vista del juego
            │   ├── Ranking.fxml            ← Vista del ranking
            │   └── style.css              ← Estilos globales
            ├── documents/
            │   └── texto.txt              ← Frases del terminal
            └── imagenes/
                └── fondoNumeros.gif       ← Imagen de fondo
```

---

## Funcionalidades implementadas

| Código | Funcionalidad | Descripción |
|---|---|---|
| F01 | Nueva partida | Permite introducir un nombre e iniciar una nueva partida |
| F02 | Continuar partida | Carga la partida guardada y recalcula los precios de mejoras |
| F03 | Hackear (clic) | Genera DP manualmente con cada pulsación |
| F04 | Producción pasiva | Las mejoras Junior, Senior, Café y RGBS generan DP automáticamente por segundo |
| F05 | Comprar mejoras | 6 mejoras disponibles con precios escalables |
| F06 | Avanzar nivel | Gasta DP para subir de rango; 5 niveles hasta el ROOT ABSOLUTO |
| F07 | Guardar partida | Guarda el estado actual en MongoDB |
| F08 | Guardar y salir | Guarda y vuelve al menú principal |
| F09 | Fin de juego | Al completar el nivel 5, guarda en ranking, elimina la partida y vuelve al menú |
| F10 | Ranking | Muestra todas las partidas registradas ordenadas por DP |
| F11 | Eliminar ranking | Borra todos los registros del ranking |
| F12 | Frases del terminal | Muestra frases temáticas cada 10 segundos si hay producción pasiva |

---

## Sistema de persistencia elegido

**MongoDB local** (`localhost:27017`) — base de datos NoSQL orientada a documentos.

Se utiliza una única base de datos llamada `terminal_clicker` con dos colecciones:

- `partidas` — almacena la partida activa del jugador (una por vez)
- `ranking` — almacena los resultados de partidas completadas o guardadas

---

## Justificación de la persistencia

MongoDB fue elegido por su flexibilidad para almacenar documentos con estructura anidada. La entidad `Partida` contiene un subdocumento `mejoras` con 6 campos numéricos, lo que encaja de forma natural en el modelo de documentos de MongoDB sin necesidad de tablas relacionadas.

---

## Diseño de datos

### Base de datos: `terminal_clicker`

#### Colección `partidas`
Almacena la partida activa. Solo existe un documento a la vez.

```json
{
  "jugador": "neo",
  "dataPoints": 15200,
  "dpPorClick": 6,
  "dpPorSegundo": 10,
  "nivel": 2,
  "terminado": false,
  "progresoMaximo": 220000,
  "mejoras": {
    "numRaspberry": 5,
    "numPC": 1,
    "numJunior": 1,
    "numSenior": 0,
    "numMaqCafe": 0,
    "numRGBS": 0
  },
  "tiempoPartida": 342,
  "fechaInicio": { "$date": "2026-06-13T18:00:00Z" }
}
```

#### Colección `ranking`
Almacena resultados de partidas. Puede tener múltiples documentos.

```json
{
  "jugador": "neo",
  "dataPoints": 980000,
  "nivel": 5,
  "mejoras": {
    "numRaspberry": 20,
    "numPC": 10,
    "numJunior": 8,
    "numSenior": 4,
    "numMaqCafe": 2,
    "numRGBS": 1
  },
  "tiempoPartida": 1840,
  "fechaInicio": { "$date": "2026-06-13T17:00:00Z" }
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `jugador` | String | Nombre del jugador |
| `dataPoints` | Long | DP acumulados |
| `dpPorClick` | Long | DP por clic en el momento del guardado |
| `dpPorSegundo` | Long | Producción pasiva en el momento del guardado |
| `nivel` | Int | Nivel de acceso alcanzado (1–5) |
| `terminado` | Boolean | Si la partida ha finalizado |
| `progresoMaximo` | Long | DP necesarios para avanzar al siguiente nivel |
| `mejoras` | Documento | Cantidad de cada mejora comprada |
| `tiempoPartida` | Long | Segundos jugados en total |
| `fechaInicio` | Date | Fecha y hora de inicio de la partida |

---

## Capturas de pantalla

> *(Añadir capturas de las vistas: Menú, Juego y Ranking)*

---

## Tabla de pruebas

| Código | Prueba | Acción realizada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|
| P01 | Inicio de aplicación | Ejecutar el proyecto | Se abre el menú principal | Correcto |
| P02 | Inicio de partida | Pulsar el botón de nueva partida | Se carga la pantalla de juego | Correcto |
| P03 | Continuar partida | presionar el botón de continuar | Se carga la pantalla de juego | Correcto |
| P04 | Finalización | Completar una partida | Se muestra el resultado final | Correcto |
| P05 | Guardado de datos | Presionar el botón de guardar | Se guarda la información correspondiente | Correcto |
| P06 | Carga de datos | Abrir ranking, o continuar partida | Se muestran los datos guardados | Correcto |
| P07 | Continuar partida inexistente | Presionar el botón de continuar sin que exista una partida | El botón aparece bloqueado | Correcto |

---

## Problemas encontrados

- **Permisión de continuación de partida ganada:** al ganar una partida el botón continuar no se desactibaba, lo cual permitía volver a una partida en la que no tienes posibilidad de acción. Se solucionó eliminando la partida ganada con `eliminarUnica()` y cambiando la vista al menú con `volverAlMenu()`.
- **`initialize()` no se relanzaba al navegar:** al cambiar de vista con `setRoot()`, el controlador no se reiniciaba. Se solucionó usando `stage.setScene(new Scene(...))` en lugar de `setRoot()`.
- **Condición de victoria duplicada:** el nivel máximo se detectaba dos veces en `avanzarProgreso()`, impidiendo que el jugador jugara el nivel 5. Se eliminó la comprobación redundante.
- **Al finalizar partida cada acción muestra null en terminal** al ganar la partida los botones se desactivan, pero en lugar de simplemente dejar de funcionar devolvían un null. La solución fue cambiar null por un mensaje personalizado.

---

## Mejoras futuras

- Añadir más niveles y mejoras con efectos distintos
- Añadir multiplicadores de clic como mejoras independientes
- Mostrar animaciones al subir de nivel
- Aumentar repertorio de frases para la terminal
- Hacer que las frases vayan acorde con las acciones del jugador
- Añadir eventos aleatorios como huelgas de trabajadores
