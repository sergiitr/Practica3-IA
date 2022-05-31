/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

/**
 *
 * @author José María Serrano
 * @version 1.4 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Curso 2016-17: Primera versión, Conecta-N Curso 2017-18: Se introducen
 * obstáculos aleatorios Curso 2018-19: Conecta-4 Curso 2021-22: Conecta4
 * clásico
 *
 * Última revisión: 22-03-2022
 *
 * Código original: * Lenin Palafox * http://www.leninpalafox.com
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Conecta4 extends JFrame implements ActionListener {

    // Número de turnos/movimientos
    private int movimiento = 0;
    // Turno (empieza jugador 1)
    private boolean turnoJ1 = true;
    // Jugador 2, CPU por defecto
    private boolean jugadorcpu = true;
    // Jugador 2, CPU aleatorio por defecto
    private int iaplayer;
    // Marca si el jugador pulsa sobre el tablero
    private boolean pulsado;

    // Jugadores
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = -1;
    public static final int VACIO = 0;

    // Algoritmos
    public static final int MINIMAX = 1;
    public static final int MINIMAXRES = 2;
    public static final int ALFABETA = 3;

    // Parámetros
    // Número de filas
    private static final int FILAS = 6;
    // Número de columnas
    private static final int COLUMNAS = 7;
    // Número de fichas que han de alinearse para ganar
    private static final int CONECTA = 4;

    // Tablero de juego
    private Grid juego;
    //jugadores
    private Player player2;

    //menus y elementos de la GUI
    private final JMenuBar barra = new JMenuBar();
    private final JMenu archivo = new JMenu("Archivo");
    private final JMenu opciones = new JMenu("Opciones");
    private final JMenuItem salir = new JMenuItem("Salir");
    private final JRadioButton p1h = new JRadioButton("Humano", true);
    private final JRadioButton p2h = new JRadioButton("Humano", false);
    private final JRadioButton p2c = new JRadioButton("CPU (Greedy)", true);
    private final JRadioButton p2c2 = new JRadioButton("CPU (MiniMax)", false);
    private final JRadioButton p2c3 = new JRadioButton("CPU (MiniMax Restringido)", false);
    private final JRadioButton p2c4 = new JRadioButton("CPU (MiniMax AlfaBeta)", false);
    // Leyendas y cabeceras
    private String cabecera = "Pr\u00e1cticas de IA (Curso 2021-22)";
    private final JLabel nombre = new JLabel(cabecera, JLabel.CENTER);
    private final String title = "Conecta4 - UJA-DI - Pr\u00e1cticas de IA (Curso 2021-22)";

    /**
     * Gestión de eventos y del transcurso de la partida
     *
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Eventos del menú Opciones
        if (ae.getSource() == p2h) {
            jugadorcpu = false; // Humano
            reset();
        } else if (ae.getSource() == p2c) {
            jugadorcpu = true; // CPU Greedy
            iaplayer = 0;
            reset();
        } else if (ae.getSource() == p2c2) {
            jugadorcpu = true; // CPU MiniMax;
            iaplayer = MINIMAX;
            reset();
        } else if (ae.getSource() == p2c3) {
            jugadorcpu = true; // CPU MiniMax Restringido;
            iaplayer = MINIMAXRES;
            reset();
        } else if (ae.getSource() == p2c4) {
            jugadorcpu = true; // CPU MiniMax AlfaBeta;
            iaplayer = ALFABETA;
            reset();
        } else if (ae.getSource() == salir) {
            dispose();
            System.exit(0);
        } else {
            // Control del juego por el usuario
            int x;
            // Siempre empieza el jugador 1
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if (ae.getSource() == juego.getJButton(i, j)) {
                        if (turnoJ1) {
                            x = juego.setButton(j, PLAYER1);
                        } else {
                            x = juego.setButton(j, PLAYER2);
                        }
                        // Comprobar si la jugada es válida
                        if (!(x < 0)) {
                            if (jugadorcpu) { //Si es modo un jugador o dos
                                pulsado = true;
                            }
                            turnoJ1 = !turnoJ1;
                            movimiento++;
                            // Comprobar si acabó el juego
                            finJuego(juego.checkWin(x, j, CONECTA));
                        } // En otro caso, la columna ya está completa
                    } // if
                } // for 2
            } // for 1

            // Pasa el turno al jugador 2 (cuando se juega contra la CPU)
            if (pulsado) {
                if (jugadorcpu) {
                    pulsado = false;
                    turnoJ1 = !turnoJ1;
                    movimiento++;
                    // El jugador CPU hace su jugada
                    // Comprobar si acabó el juego
                    finJuego(player2.turnoJugada(juego, CONECTA));
                }
            }

            // Actualizar cabecera
            String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
            if (turnoJ1) {
                cabecera2 += "Jugador 1";
            } else {
                cabecera2 += "Jugador 2";
            }
            nombre.setText(cabecera2);
        }

    } // actionPerformed         

    public void finJuego(int ganador) {
        // Actualizar tablero tras cada movimiento
        juego.print(movimiento);
        // Comprobamos si llegamos al final del juego
        // Empate!!!
        if (movimiento >= FILAS * COLUMNAS) {
            JOptionPane.showMessageDialog(this, "¡Empate!", "Conecta4", JOptionPane.INFORMATION_MESSAGE);
            reset();
        }
        // Mostrar mensaje ganador
        switch (ganador) {
            case PLAYER1:
                System.out.println("Ganador: Jugador 1, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 1\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, juego.getFicha1());
                reset();
                break;
            case PLAYER2:
                System.out.println("Ganador: Jugador 2, en " + movimiento + " movimientos.");
                JOptionPane.showMessageDialog(this, "Ganador, Jugador 2\nen " + movimiento + " movimientos!", "Conecta-4", JOptionPane.INFORMATION_MESSAGE, juego.getFicha2());
                reset();
                break;
        }

    } // finJuego

    /**
     * Reinicia una partida
     */
    private void reset() {
        // Volver el programa al estado inicial	
        juego.reset();
        turnoJ1 = true;
        movimiento = 0;
        pulsado = false;

        System.out.println();
        System.out.print("Nueva partida. Jugador 1: Humano vs Jugador 2: ");
        if (jugadorcpu) {
            switch (iaplayer) {
                case MINIMAX:
                    player2 = new MiniMaxPlayer();
                    System.out.println("CPU (MiniMax)");
                    cabecera = "Juego: Humano vs CPU MiniMax - ";
                    break;
                case MINIMAXRES:
                    player2 = new MiniMaxRestrainedPlayer();
                    System.out.println("CPU (MiniMax Restringido)");
                    cabecera = "Juego: Humano vs CPU MiniMax Restringido - ";
                    break;
                case ALFABETA:
                    player2 = new AlfaBetaPlayer();
                    System.out.println("CPU (MiniMax AlfaBeta)");
                    cabecera = "Juego: Humano vs CPU MiniMax AlfaBeta - ";
                    break;
                default:
                    player2 = new GreedyPlayer();
                    System.out.println("CPU (Greedy)");
                    cabecera = "Juego: Humano vs CPU Greedy - ";
            } // case
        } else {
            player2 = null;
            System.out.println("Humano");
            cabecera = "Juego: Humano vs Humano - ";
        }
        juego.print(movimiento);
        // Actualizar cabecera
        String cabecera2 = cabecera + "Pasos: " + movimiento + " - Turno: ";
        if (turnoJ1) {
            cabecera2 += "Jugador 1";
        } else {
            cabecera2 += "Jugador 2";
        }
        nombre.setText(cabecera2);

    } // reset

    /**
     * Configuración inicial
     *
     * Creación de la interfaz gráfica del juego
     */
    private void run() {
        // Estado inicial del tablero
        juego = new Grid(FILAS, COLUMNAS, "assets/player1.png", "assets/player2.png");
        int altoVentana = (FILAS + 1) * juego.getFicha1().getIconWidth();
        int anchoVentana = COLUMNAS * juego.getFicha1().getIconWidth();

        switch (iaplayer) {
            case MINIMAX:
                player2 = new MiniMaxPlayer();
                break;
            case MINIMAXRES:
                player2 = new MiniMaxRestrainedPlayer();
                break;
            case ALFABETA:
                player2 = new AlfaBetaPlayer();
                break;
            default:
                player2 = new GreedyPlayer();
        } // case

        //menu GUI
        salir.addActionListener(this);
        archivo.add(salir);
        // Player 1
        opciones.add(new JLabel("Jugador 1:"));
        ButtonGroup m1Jugador = new ButtonGroup();
        m1Jugador.add(p1h);
        // Player 2
        opciones.add(p1h);
        opciones.add(new JLabel("Jugador 2:"));
        p2h.addActionListener(this);
        p2c.addActionListener(this);
        p2c2.addActionListener(this);
        p2c3.addActionListener(this);
        p2c4.addActionListener(this);
        ButtonGroup m2Jugador = new ButtonGroup();
        m2Jugador.add(p2h);
        m2Jugador.add(p2c);
        m2Jugador.add(p2c2);
        m2Jugador.add(p2c3);
        m2Jugador.add(p2c4);
        opciones.add(p2h);
        opciones.add(p2c);
        opciones.add(p2c2);
        opciones.add(p2c3);
        opciones.add(p2c4);

        barra.add(archivo);
        barra.add(opciones);
        setJMenuBar(barra);

        //Panel Principal 
        JPanel principal = new JPanel();
        principal.setLayout(new GridLayout(FILAS, COLUMNAS));

        //Colocar Botones
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                juego.initialize(i, j, this, Color.BLACK);
                principal.add(juego.getJButton(i, j));
            }
        }
        nombre.setForeground(Color.BLUE);
        add(nombre, "North");
        add(principal, "Center");

        //Para cerrar la Ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });

        //tamaño frame
        setLocation(170, 25);
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        setSize(500,500);
        setTitle(title);
        setVisible(true);
        reset();
    } // run

    /**
     * Método principal
     *
     * Lectura de parámetros desde línea de comandos e inicio del programa
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Conecta4 - 4 en Raya");
        System.out.println("-----------------------------------------");
        System.out.println("Inteligencia Artificial - Curso 2021-22");

        Conecta4 juego = new Conecta4();
        juego.run();

    } // main

} // Conecta4
