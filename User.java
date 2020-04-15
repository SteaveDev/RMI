
public class User {

	private int id;
	private String nom;

	/*
	 * Constructeur
	 */
	public User(int id, String nom){
		setId(id);
		setNom(nom);
	}
	
	/*
	 * Accesseur
	 */
	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	
	/*
	 * Mutateur
	 */
	public void setId(int id) {
		this.id = id;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
    /*
     * Affiche ID + Nom
     */
	public String toString(){
		return "User : ID n°" + getId() + " : " + getNom();
	}
	
}
