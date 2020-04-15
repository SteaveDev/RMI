import java.util.Date;

public class Message {
	
	protected Date date;
	protected int idPost;
	protected int idUser;
	protected String message;
	protected String user;
	protected int score;
	protected int position;
    
	/*
	 * Constructeur
	 */
	public Message(Date date, int idMessage, User user, String message){
    	setDate(date);
    	setIdPost(idMessage);
    	setIdUser(user.getId());
    	setMessage(message);
    	setUser(user.getNom());
    	setScore(20);
    	setPosition(0);
    }
    
	/*
	 * Accesseur
	 */
	public Date getDate() {
		return date;
	}
	public int getIdPost() {
		return idPost;
	}
	public int getIdUser() {
		return idUser;
	}
	public String getMessage() {
		return message;
	}
	public String getUser() {
		return user;
	}
	public int getScore() {
		return score;
	}
	public int getPosition() {
		return position;
	}
	
	/*
	 * Mutateur
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	public void setIdPost(int idPost){
		this.idPost = idPost;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	/*
     * Affichage par défaut pour la classe Message
     * 
     * @return 		Affichage par défaut
     */
    public String toString(){
    	return " MESSAGE (Score : " + getScore() + ") : " + getDate() + " | " + getIdPost() + " | " + getIdUser() + " | " + getMessage() + " | " + getUser() + " ||";
    }
    
}
