package modelo;

import java.util.Arrays;
import java.util.Scanner;

public class TicTacToe {

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_RESET = "\u001B[0m";

	// Jugador real sera la ficha X, internamente jugara como 1

	private final int JUGADORHUMANO = 1;
	// Jugador real sera la ficha O, internamente jugara como 2
	private final int JUGADORROBOT = 2;

	// VACIO REPRESENTA UN LUGAR DEL TABLERO QUE NO HAY FICHA
	private final int VACIO = 0;

	private int[][] tablero;
	private int turno;

	public TicTacToe() {

		this.tablero = new int[3][3];
		this.turno = 1;
	}

	public void pintar() {

		pintar(this.tablero);

	}
	
	public void pintar(int[][] tab) {

		System.out.printf(" %s | %s | %s |%n", mostrarPosicion(tab[0][0], 1), mostrarPosicion(tab[0][1], 2),
				mostrarPosicion(tab[0][2], 3));
		System.out.printf(" %s | %s | %s |%n", mostrarPosicion(tab[1][0], 4), mostrarPosicion(tab[1][1], 5),
				mostrarPosicion(tab[1][2], 6));
		System.out.printf(" %s | %s | %s |%n", mostrarPosicion(tab[2][0], 7), mostrarPosicion(tab[2][1], 8),
				mostrarPosicion(tab[2][2], 9));

	}

	private String mostrarPosicion(int i, int posicion) {

		if (i == JUGADORHUMANO) {
			return ANSI_RED + " X " + ANSI_RESET;
		} else if (i == JUGADORROBOT) {
			return ANSI_BLUE + " O " + ANSI_RESET;
		}

		return " " + posicion + " ";
	}

	public void jugar() {
		Scanner sc = new Scanner(System.in);

		boolean tiro = false;
		int posicionATirar = 0;

		do {
			pintar();

			if (turno == JUGADORHUMANO) {
				System.out.println("Donde quieres tirar? (X) 1-9");
				posicionATirar = sc.nextInt();

				if (sePuedeTirar(posicionATirar)) {
					ponerPieza(posicionATirar);
					tiro = true;
				}
			} else {
				System.out.println("Turno de la Maquina O. ");

				// Si le pasaramos el tablero, lo modificaria a el tambien y no quere
				int[][] copy = Arrays.stream(this.tablero).map(int[]::clone).toArray(int[][]::new);

				long start = System.currentTimeMillis();

				int posicion = minimax(copy, turno).posicion;

				// System.out.println("Tiempo transcurrido en ver todas las posibilidades-> \n"
				// + ((System.currentTimeMillis() - start) / 1000.0) + " segundos");

				ponerPieza(posicion);
				tiro = true;
			}

			if (tiro) {

				turno = (turno == JUGADORHUMANO) ? JUGADORROBOT : JUGADORHUMANO;
				tiro = false;

			}

		} while (!hayEmpate() && !hayGanador());

		if (hayEmpate()) {
			System.out.println("Hay empate");
		} else {
			System.out.println(
					(quienHaGanado(JUGADORHUMANO) == 1) ? "Ha ganado la X(humano)" : "Ha ganado el O(MAQUINA)");

		}
		sc.close();
	}

	private int quienHaGanado(int jugador, int[][] tab) {

		int devolver = 0;

		if (tab[0][0] == jugador && jugador == tab[0][1] && jugador == tab[0][2]) {
			devolver = 1;

		} else if (tab[0][0] == jugador && jugador == tab[1][0] && jugador == tab[2][0]) {
			devolver = 1;
		} else if (tab[2][2] == jugador && jugador == tab[2][1] && jugador == tab[2][0]) {
			devolver = 1;
		} else if (tab[1][0] == jugador && jugador == tab[1][1] && jugador == tab[1][2]) {
			devolver = 1;
		} else if (tab[0][2] == jugador && jugador == tab[1][2] && jugador == tab[2][2]) {
			devolver = 1;
		} else if (tab[0][1] == jugador && jugador == tab[1][1] && jugador == tab[2][2]) {
			devolver = 1;
		} else if (tab[0][2] == jugador && jugador == tab[1][1] && jugador == tab[1][2]) {
			devolver = 1;
		}

		return devolver;

	}

	private int quienHaGanado(int jugador) {

		return quienHaGanado(jugador, this.tablero);

	}

	private boolean hayGanador() {

		return hayGanador(this.tablero);
	}

	private boolean hayGanador(int[][] tab) {

		boolean devolver = false;

		// 0 0 | 0 1 | 0 2
		// 1 0 | 1 1 | 1 2
		// 2 0 | 2 1 | 2 2

		if (tab[0][0] != VACIO && tab[0][0] == tab[0][1] && tab[0][1] == tab[0][2]) {
			devolver = true;
		} else if (tab[0][0] != VACIO && tab[0][0] == tab[1][0] && tab[1][0] == tab[2][0]) {
			devolver = true;
		} else if (tab[2][2] != VACIO && tab[2][2] == tab[2][1] && tab[2][1] == tab[2][0]) {
			devolver = true;
		} else if (tab[1][0] != VACIO && tab[1][0] == tab[1][1] && tab[1][1] == tab[1][2]) {
			devolver = true;
		} else if (tab[0][2] != VACIO && tab[0][2] == tab[1][2] && tab[1][2] == tab[2][2]) {
			devolver = true;
		} else if (tab[0][0] != VACIO && tab[0][0] == tab[1][1] && tab[1][1] == tab[2][2]) {
			devolver = true;
		} else if (tab[0][2] != VACIO && tab[0][2] == tab[1][1] && tab[1][1] == tab[2][0]) {
			devolver = true;
		} else if (tab[0][1] != VACIO && tab[0][1] == tab[1][1] && tab[1][1] == tab[2][1]) {
			devolver = true;
		}

		return devolver;
	}

	private boolean hayEmpate(int[][] tab) {

		return !(existenMovimientos(tab)) && !hayGanador();
	}

	private boolean existenMovimientos(int[][] tab) {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tab[i][j] == VACIO)
					return true;

			}
		}

		return false;
	}

	private boolean hayEmpate() {

		return hayEmpate(tablero);
	}

	private void ponerPieza(int posicionATirar) {

		tiradaManual(posicionATirar, this.tablero);

	}

	private void tiradaManual(int posicionATirar, int[][] tab) {

		tiradaManual(posicionATirar, tab, turno);
	}

	// La maquina quiere la menor puntuacion y el humano la mayor
	private Posicion minimax(int[][] tablero, int turno) {

		// En caso de que el turno sea igual a el humano se le asigna el robot, en caso
		// contrario el humano

		int otroJugador = (turno == JUGADORHUMANO) ? JUGADORROBOT : JUGADORHUMANO;

		// DEPENDIENDO DE LO QUE PONGAMOS ACTUARA DIFERENTE PUEDIENDO SER MAS FACIL O
		// MAS DIFICIL
		// Si es (other_player == max_player) "piensa" mas que si hacemos (other_player
		// ==
		// turno)

		// Hay ganador, retornaremos un numero, que dependra de el otro jugador en el
		// momento de que haya victoria y de la cantidad de Vacios que hay, para elegir
		// la opcion con menos tiradas
		if (hayGanador(tablero)) {
			return new Posicion(-2, (otroJugador == JUGADORHUMANO) ? 1 * (cantidadDeVacios(tablero) + 1)
					: -1 * (cantidadDeVacios(tablero) + 1));

		} else if (hayEmpate(tablero)) {
			return new Posicion(-2, 0);
		}

		// Recursividad
		Posicion posicion;
		
		// Si esta jugando el humano
		if (turno == JUGADORHUMANO) {
			//El humano se le asigna la menor puntuacion, ja que buscara la mayor
			posicion = new Posicion(-2, Integer.MIN_VALUE);
			
		} else {
			posicion = new Posicion(-2, Integer.MAX_VALUE);
		}

		//Bucle donde vera todas las posibilidades de las jugadas
		for (int posiblePosicion = 1; posiblePosicion <= 9; posiblePosicion++) {

			// Crear nuevo tablero con el posible movimiento

			if (sePuedeTirar(posiblePosicion, tablero)) {

				//Devuelve el nuevo tablero con la jugada pintada
				int[][] nuevoTablero = tiradaManual(posiblePosicion,
						Arrays.stream(tablero).map(int[]::clone).toArray(int[][]::new), turno);
				
				//pintar(nuevoTablero);
				
				Posicion sim_score = minimax(nuevoTablero, otroJugador);

				//Asignamos la posible posicion
				sim_score.posicion = posiblePosicion;

				//Si estamos simulando el turno del humano, comprobamos si es mayor, si no si es menor
				
				if (turno == JUGADORHUMANO) { // X es max player
					if (sim_score.puntuacion > posicion.puntuacion) {
						posicion = sim_score;
					}

				} else {
					if (sim_score.puntuacion < posicion.puntuacion) {
						posicion = sim_score;
					}
				}
			}

		}

		return posicion;

	}

	private int cantidadDeVacios(int[][] t) {

		int contadorDeVacios = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (t[i][j] == VACIO) {
					contadorDeVacios++;
				}
			}
		}
		return contadorDeVacios;
	}

	private int[][] tiradaManual(int posicionATirar, int[][] tableroCopia, int turno) {

		boolean continuar = true;

		for (int c = 0; c < 3 && continuar; c++) {

			for (int f = 0; f < 3 && continuar; f++) {
				posicionATirar--;
				if (posicionATirar == 0) {
					tableroCopia[c][f] = turno;

					continuar = false;
				}

			}

		}
		return tableroCopia;

	}

	private boolean sePuedeTirar(int posicionATirar, int[][] tab) {

		if (posicionATirar <= 9 && posicionATirar >= 1) {

			for (int c = 0; c < 3; c++) {

				for (int f = 0; f < 3; f++) {
					posicionATirar--;
					if (posicionATirar == 0) {
						return tab[c][f] == VACIO;
					}

				}

			}
		}
		return false;
	}

	private boolean sePuedeTirar(int posicionATirar) {

		return sePuedeTirar(posicionATirar, this.tablero);
	}

}
