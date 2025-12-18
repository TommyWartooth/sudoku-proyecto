
# Sudoku

Este proyecto consiste en el desarrollo de un juego de Sudoku en Java con interfaz gráfica usando JavaFX, enfocado en la aplicación práctica de estructuras de datos y algoritmos de búsqueda.
El sistema modela el tablero y la lógica del juego utilizando listas enlazadas, nodos, pilas y árboles, evitando el uso directo de matrices para reforzar el aprendizaje de estructuras dinámicas.

# Sudoku JavaFX  
Juego de Sudoku con estructuras de datos y algoritmos de búsqueda

## Tecnologías Usadas

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaFX-GUI-007396?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-Project-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
  <img src="https://img.shields.io/badge/Estructuras%20de%20Datos-Nodos%20y%20Árboles-4CAF50?style=for-the-badge" />
</p>

---

## Descripción

Este proyecto consiste en el desarrollo de un **juego de Sudoku en Java** con interfaz gráfica usando **JavaFX**, enfocado en la **aplicación práctica de estructuras de datos y algoritmos de búsqueda**.
El sistema modela el tablero y la lógica del juego utilizando **listas enlazadas, nodos, pilas y árboles**, evitando el uso directo de matrices para reforzar el aprendizaje de estructuras dinámicas.

---

##  Funcionalidades Principales

-  Tablero de Sudoku 9x9 generado dinámicamente  
-  Interfaz gráfica interactiva con JavaFX  
-  Celdas fijas (pistas) y celdas editables  
-  Sistema de **deshacer movimientos (Undo)** usando pila  
-  **Ayuda inteligente**:
  - Muestra candidatos válidos para una celda
  - Representación de candidatos mediante **árbol binario**
-  Control de tiempo de la partida  
-  Selección de dificultad (fácil, medio, difícil, experto)  
-  Validación automática de movimientos  
-  Resolución del Sudoku mediante **backtracking**

---

## Estructuras de Datos Utilizadas

- **Lista Enlazada**
  - Representación del tablero (81 nodos `CellNode`)
- **Pila (Stack)**
  - Registro de movimientos para la funcionalidad *Undo*
- **Árbol Binario**
  - Representación y recorrido de candidatos posibles
- **Nodos**
  - Cada celda del tablero es un nodo independiente

---

##  Algoritmos Implementados

- Validación de restricciones del Sudoku  
  - Fila  
  - Columna  
  - Bloque 3x3  

- **Backtracking**
  - Exploración del espacio de soluciones
  - Poda de ramas inválidas
  - Resolución automática del tablero

- Recorridos de árboles
  - Preorden para mostrar candidatos

---

## Arquitectura del Proyecto

```txt
src/
 └── com.example
     ├── controllers      # Controladores JavaFX
     ├── model            # Lógica del juego y estructuras
     │   ├── CellNode
     │   ├── SudokuBoardLL
     │   ├── Move
     │   ├── PartidaSudoku
     │   ├── SudokuSolver
     │   └── SudokuGenerator
     ├── utils            # Helpers y algoritmos
     │   ├── SudokuHelper
     │   └── OrdenamientoSudoku
     └── Main.java        # Punto de entrada
```

Logica revisada por:
<p align="center">
<img src="https://github.com/user-attachments/assets/09f85783-682d-4c1c-8afc-04b4577da552" width="150"/>
<img src="https://github.com/user-attachments/assets/f0d88037-bace-4211-8ead-0218b32a10ba" width="250"/>
<img src="https://github.com/user-attachments/assets/5a933530-b018-43c2-9a67-fb21b0a939a2" width="150"/>
</p>

👥 Autores
<table align="center"> <tr> <td align="center"> <a href="https://github.com/bigMackProject"> <img src="https://avatars.githubusercontent.com/u/209021209?v=4" width="120px;" /> <br /> <sub><b>Paulo Escaray</b></sub> </a> </td> <td align="center"> <a href="https://github.com/TommyWartooth"> <img src="https://avatars.githubusercontent.com/u/169411148?v=4" width="120px;" /> <br /> <sub><b>Emilia Crespo</b></sub> </a> </td> <td align="center"> <a href="https://github.com/annetryoshka"> <img src="https://avatars.githubusercontent.com/u/164533936?v=4" width="120px;" /> <br /> <sub><b>Adriana Pando</b></sub> </a> </td> </tr> </table>

