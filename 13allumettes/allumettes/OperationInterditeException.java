package allumettes;

public class OperationInterditeException extends RuntimeException {
	/** Initialiser OperationInterditeException à partir du coup joué
	 * Par exemple, on peut avoir un joueur qui tente de modifier le jeu
     * sans y être autorisé.     
	 */

	private static boolean triche = false;

	public OperationInterditeException(String message) {  
		super("OperationInterditeException: Une triche a été détectée" + message);
		// On enregistre que l'on a tenté de tricher
		// pour que l'arbitre puisse le signaler.
		triche = true;
	}
	public static boolean quelqunATriche() {		
		return triche;
	}
}
