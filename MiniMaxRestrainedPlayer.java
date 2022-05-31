/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author José María Serrano
 * @version 1.5 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxRestrainedPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar
 * el algoritmo MiniMax Restringido
 *
 */

/**
 * @author Sergio Trillo Rodriguez y Alvaro Ordoñez Romero
 */
public class MiniMaxRestrainedPlayer extends Player {

    /**
     * Para nuestro código en concreto nos centraremos en buscar la mejor columna
     */
    int mejorColumna;
    
    /**
     * Atributo profundidad recomendado por el profesor
     */
    int PROFUNDIDAD=4;
    
    public class InfoMiniMax {
        /**
         * @brief Estado actual del tablero
         */
        private final int[][] tablero;
        
        /**
         * @brief Jugador que le toca jugar este estado
         */
        private int jugador;

        /**
         * @brief Constructor parametrizado.
         * @param tabActual Dato que alberga el Estado
         */
        public InfoMiniMax(int[][] tabActual) {
            this.tablero = new int[tabActual.length][tabActual[0].length];
            for (int i = 0; i < tabActual.length; i++) {
                System.arraycopy(tabActual[i], 0, this.tablero[i], 0, tabActual[0].length);
            }
            this.jugador = 0; 
        }
    }
   /**
     * @brief Asignacion de la puntuacion de los posibles estados
     * @param est
     * @param jugador
     * @param numFichasConsecutivas
     * @return 
     */
    
    private int contadorFichas(InfoMiniMax est, int jugador, int numFichasConsecutivas) {
        int contFichas = 0;
        for (int i = 0; i < est.tablero.length; i++) {
            for (int j = 0; j < est.tablero[0].length; j++) {
                contFichas += CuentaCadenas(est.tablero, jugador, i, j, numFichasConsecutivas);
            }
        }
        return contFichas;
    }
    
    /**
     * @brief Contador de fichas consecutivas
     * @param tablero
     * @param jugador
     * @param fil
     * @param col
     * @param numFichasConsecutivas
     * @return el numero de cadenas
     */
    private int CuentaCadenas(int[][] tablero, int jugador, int fil, int col, int numFichasConsecutivas) {

        int numCadenas = 0;
        int FILAS = tablero.length;
        int COLUMNAS = tablero[0].length;
        
        //VERTICAL
        int contadorConsecutivas = 0;
        for (int i = fil; i >= 0; i--) {
            if (tablero[i][col] == jugador) {
                contadorConsecutivas++;
            } else {
                contadorConsecutivas = 0;
            }
            if (contadorConsecutivas == numFichasConsecutivas) {
                numCadenas++;
            }
        }

        //HORIZONTAL
        contadorConsecutivas = 0;
        for (int i = 0; i < COLUMNAS; i++) {
            if (tablero[fil][i] == jugador) {
                contadorConsecutivas++;
            } else {
                contadorConsecutivas = 0;
            }
            if (contadorConsecutivas == numFichasConsecutivas) {
                numCadenas++;
            }
        }
        contadorConsecutivas = 0;

        //DIAGONAL DER
        int filDef = 0, colDef = 0;
        int j = col;

        for (int i = fil; i < FILAS && j >= 0; i++) {
            filDef = fil;
            colDef = j;
            j--;
        }

        j = colDef;
        for (int i = filDef; i >= 0 && j < COLUMNAS; i--) {
            if (tablero[i][j++] == jugador) {
                contadorConsecutivas++;
            } else {
                contadorConsecutivas = 0;
            }
            if (contadorConsecutivas == numFichasConsecutivas) {
                numCadenas++;
            }
        }

        //DIAGONAL IZQ
        filDef = 0;
        colDef = 0;
        j = col;
        for (int i = fil; i < FILAS && j < COLUMNAS; i++) {
            filDef = fil;
            colDef = j;
            j++;
        }
        contadorConsecutivas = 0;
        j = colDef;
        for (int i = filDef; i >= 0 && j >= 0; i--) {
            if (tablero[i][j--] == jugador) {
                contadorConsecutivas++;
            } else {
                contadorConsecutivas = 0;
            }
            if (contadorConsecutivas == numFichasConsecutivas) {
                numCadenas++;
            }
        }
        return numCadenas;
    }
    
    /**
     * @brief Funcion para los posibles movimientos
     * @param est de tipo InfoMiniMax
     * @param jugador de tipo int
     * @return aux
     */
    private List<InfoMiniMax> posiblesTiradas(InfoMiniMax est, int jugador) {
        List<InfoMiniMax> aux = new ArrayList<>();
        for (int j = 0; j < est.tablero[0].length; j++) {
            InfoMiniMax hijo = null;
            boolean espacioLibre = false;
            for (int i = est.tablero.length - 1; i >= 0 && !espacioLibre; i--) {
                if (est.tablero[i][j] == 0) {
                    espacioLibre = true;
                    hijo = new InfoMiniMax(est.tablero);
                    for (int k = 0; k < hijo.tablero.length; k++) {
                        System.arraycopy(est.tablero[k], 0, hijo.tablero[k], 0, hijo.tablero[0].length);
                        hijo.tablero[i][j] = jugador;
                    }
                }
            }
            aux.add(hijo);
        }
        return aux;
    }
    
    /**
     * @brief Método MiniMax que nos ayudará a mostrar las posibles que tiene la IA 
     * @param est
     * @param prof
     * @param jugador 
     */
     private void MiniMaxArbol(InfoMiniMax est, int prof, boolean ganado, boolean empatado) {
         
         if(!ganado && prof < PROFUNDIDAD && !empatado){
            for (int j = 0; j <est.tablero[0].length; j++) { //Bucle para obtener las 7 posibles tiradas   
                boolean sePuedeMeter = false;
                for (int i = est.tablero.length - 1; i >= 0 && !sePuedeMeter; i--){
                    for (int l = 0; l < est.tablero.length; l++) {
                        for (int k = 0; k < est.tablero[0].length; k++) {
                            if(est.tablero[l][k]==0)
                                sePuedeMeter=true;
                        }
                    }
                    int[][] nuevoTab = new int[est.tablero.length][est.tablero[0].length];
                    //Trabajamos con una copia del tablero
                    if(sePuedeMeter){
                        for (int a = 0; a < est.tablero.length; a++) {
                            System.arraycopy(est.tablero[a], 0, nuevoTab[a], 0, est.tablero[0].length);
                        }
                        //Metemos la ficha para la posible tirada1
                        nuevoTab[i][j] = est.jugador;
                    }
                    InfoMiniMax nuevoEstado = new InfoMiniMax(nuevoTab);
                    nuevoEstado.jugador = (-1) * (est.jugador); //Actualizacion de jugador
                    int nuevaProfun = prof + 1;
                    if(contadorFichas(nuevoEstado, nuevoEstado.jugador, 4)!=0){
                        ganado = true;
                    }
                    
                    for (int w = 0; w < nuevoEstado.tablero.length; w++) {
                        for (int k = 0; k < nuevoEstado.tablero[0].length; k++) {
                            if(est.tablero[w][k]==0)
                                empatado=true;
                        }
                    }
                    ShowArbol(nuevoEstado, nuevaProfun);
                    MiniMaxArbol(nuevoEstado, nuevaProfun, ganado, empatado);
                    
                    
                }
            }
         }

        
    }

     
     /**
     * @brief Mostramos el arbol minimax
     * @param est
     * @param prof
     */
    private void ShowArbol(InfoMiniMax est, int prof){
        System.out.println("ARBOL MINIMAX-PRONFUNDIDAD: " + prof);
        System.out.println("");
        System.out.println("JUGADOR: " + est.jugador);
        ShowTablero(est);
        //Da fallo al mostrarlo x fichero asi que lo mostramos por pantalla
//        String sFichero = "arbol.txt";
//        File fichero = new File(sFichero);
//        if(fichero.exists()){
//            BufferedWriter bw = null;
//            try {
//                bw = new BufferedWriter(new FileWriter(sFichero));
//                bw.write("ARBOL MINIMAX-PRONFUNDIDAD: " + prof);
//                bw.write("");
//                bw.write("JUGADOR: " + est.jugador);
//                for (int i = 0; i < est.tablero.length; i++) {
//                    for (int j = 0; j < est.tablero[0].length; j++) {
//                        if (est.tablero[i][j] != -1) {
//                            bw.write("+");
//                        }
//                        bw.write(est.tablero[i][j]);
//                        bw.write(" ");
//                    }
//                    bw.write("");
//                }   bw.write("");
//            } catch (IOException ex) {
//                Logger.getLogger(MiniMaxPlayer.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                try {
//                    bw.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(MiniMaxPlayer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }
    
     
     /**
     * @brief Mostramos las posibles combinaciones/tiradas que puede hacer el minimax
     * @param est
     */
    private void ShowTablero(InfoMiniMax est) {
        for (int i = 0; i < est.tablero.length; i++) {
            for (int j = 0; j < est.tablero[0].length; j++) {
                if (est.tablero[i][j] != -1) {
                    System.out.print("+");
                }
                System.out.print(est.tablero[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    /**
     * @brief Funcion MiniMaxMax
     * @param est de tipo InfoMiniMax
     * @param jugador de tipo int
     * @param profundidad de tipo int
     * @return mejorBeneficio
     */
    private int MiniMaxMAX(InfoMiniMax est, int jugador, int profundidad){
        //Comprobamos si hay espacio libre en el tablero
        boolean sePuedeMeter = false;
        for (int l = 0; l < est.tablero.length; l++) {
            for (int k = 0; k < est.tablero[0].length; k++) {
                if(est.tablero[l][k]==0)
                    sePuedeMeter=true;
            }
        }
        if(contadorFichas(est, jugador, 4)!=0 || profundidad == PROFUNDIDAD+1 || !sePuedeMeter)
            //Puntuacion para defender la jugada de 2 fichas o la de 3
             return (contadorFichas(est, jugador, 4) + contadorFichas(est, jugador, 2) + contadorFichas(est, jugador, 3)) - (contadorFichas(est, jugador *(-1), 4) + contadorFichas(est, jugador * (-1), 2) + contadorFichas(est, jugador * (-1), 3));
        else{
            List<InfoMiniMax> minHijos = posiblesTiradas(est, jugador); //Obtencion de posibles tiradas (hijos) para luego escoger la mejor
            int mejorBeneficio = 0;
            int posibleColumna = 0;
            for(InfoMiniMax i : minHijos){
                if(i != null){
                    int nuevaProfundidad = profundidad + 1;
                    int actualizaJugador = jugador * (-1);
                    int puntMinHijo = MiniMaxMIN(i,actualizaJugador, nuevaProfundidad);
                    if(puntMinHijo > mejorBeneficio){
                        mejorBeneficio = puntMinHijo;
                        mejorColumna = posibleColumna;
                    }
                }  
                posibleColumna++;
            }
            return mejorBeneficio;
        }
    }
    
    /**
     * @brief Funcion MinimaxMin
     * @param est de tipo InfoMiniMax
     * @param jugador de tipo int
     * @param profundidad de tipo int
     * @return mejorBeneficio
     */
    private int MiniMaxMIN(InfoMiniMax est, int jugador, int profundidad){
        //Comprobamos si hay espacio libre en el tablero
        boolean sePuedeMeter = false;
        for (int l = 0; l < est.tablero.length; l++) {
            for (int k = 0; k < est.tablero[0].length; k++) {
                if(est.tablero[l][k]==0)
                    sePuedeMeter=true;
            }
        }
        if(contadorFichas(est, jugador, 4)!=0 || profundidad == PROFUNDIDAD+1 || !sePuedeMeter)
            //Puntuacion para defender la jugada de 2 fichas o la de 3
            return (contadorFichas(est, jugador, 4) + contadorFichas(est, jugador, 2) + contadorFichas(est, jugador, 3)) - (contadorFichas(est, jugador *(-1), 4) + contadorFichas(est, jugador * (-1), 2) + contadorFichas(est, jugador * (-1), 3));
        else{
            List<InfoMiniMax> maxHijos = posiblesTiradas(est, jugador); //Obtencion de posibles tiradas para luego escoger la mejor
            int mejorBeneficio = 100000;
            int posibleColumna = 0;
            for(InfoMiniMax i : maxHijos){
                if  (i != null) {
                    int nuevaProfundidad = profundidad + 1;
                    int actualizaJugador = jugador * (-1);
                    int puntMaxHijo = MiniMaxMAX(i, actualizaJugador, nuevaProfundidad);
                    if(puntMaxHijo < mejorBeneficio){
                        mejorBeneficio = puntMaxHijo;
                        mejorColumna = posibleColumna;
                    }
                }  
                posibleColumna++;
            }
            return mejorBeneficio;
        }
    }
    /**
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int turnoJugada(Grid tablero, int conecta) {
        // Versión MiniMax
        
        // ...
        // Calcular la mejor columna posible donde hacer nuestra jugada
        // Pintar Ficha (sustituir 'columna' por el valor adecuado)
        //int columna = getRandomColumn(tablero);
        InfoMiniMax estadoInicial = new InfoMiniMax(tablero.toArray());
        estadoInicial.jugador = Conecta4.PLAYER2;
        MiniMaxArbol(estadoInicial, 0, false, false);
        int aux = MiniMaxMAX(estadoInicial, estadoInicial.jugador, 0);
        return tablero.checkWin(tablero.setButton(mejorColumna, Conecta4.PLAYER2), mejorColumna, conecta);

    } // turnoJugada


} // MiniMaxPlayer
