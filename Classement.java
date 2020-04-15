
public class Classement {
	
	private int idUser;
	private String user;
	private int score;
	
	/*
	 * Constructeur par d�faut
	 */
	public Classement(){
		setIdUser(-1);
		setUser(new String());
		setScore(-1);
	}
	
	/*
	 * Constructeur champ � champ
	 */
	public Classement(int idUser, String message, int score){
		setIdUser(idUser);
		setUser(message);
		setScore(score);
	}

	/*
	 * Accesseur
	 */
	public int getIdUser() {
		return idUser;
	}
	public String getUser() {
		return user;
	}
	public int getScore() {
		return score;
	}
	
	/*
	 * Mutateur
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	/*
     * Affichage par d�faut pour la classe Classement
     * 
     * @return 		Affichage par d�faut
     */
	public String toString(){
		return "ID_MESSAGE n�" + getIdUser() + " (Score : " + getScore() + ") --> " + getUser() + "\n";
	}
}
