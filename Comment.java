import java.util.Date;

public class Comment extends Message{

	private int pidCommentaire;
	private int pidMessage;

	/*
	 * Constructeur
	 */
	public Comment(Date date, int idMessage, User user, String message, int pidCommentaire, int pidMessages) {
		super(date, idMessage, user, message);
		setPidCommentaire(pidCommentaire);
		setPidMessage(pidMessages);
	}	
	
	/*
	 * Accesseur
	 */
	public int getPidCommentaire() {
		return pidCommentaire;
	}
	public int getPidMessage() {
		return pidMessage;
	}

	/*
	 * Mutateur
	 */
	public void setPidCommentaire(int pidCommentaire) {
		this.pidCommentaire = pidCommentaire;
	}
	public void setPidMessage(int pidMessage) {
		this.pidMessage = pidMessage;
	}
	
	/*
     * Affichage par défaut pour la classe Message
     * 
     * @return		Affichage par défaut (affiche pidM ou pidC en fonction de la valeur de celle-ci)
     */
	public String toString(){
		
		return " COMMENTAIRE (Score : " + getScore() + ") : " + getDate() +
				" | " + getIdPost() + " | " + getIdUser() + " | " + getMessage() + " | " + getUser() + " |" + 
				( getPidCommentaire()==-1 ? ("| pidM = " + getPidMessage()) : (" pidC = " + getPidCommentaire() + " | ") );
	}
	
}
