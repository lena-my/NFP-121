package allumettes;

/** Exception qui indique que la configuration d'une partie est incorrecte.
 * @author  Xavier Crégut
 * @version 1.4
 */
public class ConfigurationException extends RuntimeException {

	/** Initialiser une ConfigurationException avec le message précisé.
	  * @param message le message explicatif
	  */
	public ConfigurationException(String message) {
		super(message);
	}

}
