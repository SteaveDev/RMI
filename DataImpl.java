import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class DataImpl extends UnicastRemoteObject implements Data {

	private ArrayList<Message> les_messages;
	private ArrayList<Comment> les_commentaires;
	private ArrayList<User> les_utilisateurs;
	private Classement[] podium;
	
	/*
	 * Constructeur par défaut
	 */
	public DataImpl() throws RemoteException {
		les_messages = new ArrayList<>();
		les_commentaires = new ArrayList<>();
		les_utilisateurs = new ArrayList<>();
		
		podium = new Classement[3];
		for(int i=0; i<3; i++)
			podium[i] = new Classement();
	}

	/*
	 * Accesseur
	 */
	public ArrayList<Message> getLes_messages() {
		return les_messages;
	}
	public ArrayList<Comment> getLes_commentaires() {
		return les_commentaires;
	}
	public ArrayList<User> getLes_utilisateurs() {
		return les_utilisateurs;
	}
	public Classement getPodium(int position){
		return podium[position];
	}
	
	/*
	 * Mutateur
	 */
	public void setLes_messages(ArrayList<Message> les_messages) {
		this.les_messages = les_messages;
	}
	public void setLes_commentaires(ArrayList<Comment> les_commentaires) {
		this.les_commentaires = les_commentaires;
	}
	public void setLes_utilisateurs(ArrayList<User> les_utilisateurs) {
		this.les_utilisateurs = les_utilisateurs;
	}
	public void setPodium(Classement c, int nEff){
		this.podium[nEff] = c;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#start()
	 *
	 * Fonction adaptée pour la lecture du fichier "reseau social.txt"
	 * En récupurant les différentes informations
	 * 
	 * On demande à la fonction de lire chaque ligne jusqu'à la fin
	 * Initialisation des variables pour les différents identifiants, pid, les messages, les utilisateur
	 * 
	 * "tempo" sert à récupérer temporairement la chaine de caractères entre chaque '|'
	 * "nbBaton" servira à compter le nombre de '|' que le programme aura parcouru sur chaque ligne
	 * 
	 * Si, le caractère est différent de '|' alors on continue de créer la chaine 
	 * Sinon, avec "tempo" on peut la stocker sur une des variables correspondantes (user, message, id, etc..) 
	 * 
	 * J'utilise aussi la conversion des chaines de caratères en entier, avec Integer.ParseInt(chaine)
	 * 
	 * 
	 * 
	 */
	public void start() throws RemoteException {
		try {
			BufferedReader in = new BufferedReader(new FileReader("reseau social.txt"));
			String line;
			
			while ((line = in.readLine()) != null){
				
				int idMsg = -1, idUser = -1, pidC = -1, pidM = -1, nbBaton = 0;
				String message = new String(), nom_user = new String(), tempo = new String();
				
				for(int i=0; i<line.length(); i++){
					if(line.charAt(i)!='|'){
						tempo = tempo + line.charAt(i);
						if(i+1==line.length()){
							pidM = Integer.parseInt(tempo);
							tempo = new String();
							break;						
						}
					}
					else{
						nbBaton++;
						if(nbBaton==1) idMsg = Integer.parseInt(tempo);
						else if(nbBaton==2) idUser = Integer.parseInt(tempo);
						else if(nbBaton==3) message = tempo;
						else if(nbBaton==4) nom_user = tempo;
						else if(nbBaton==5)
							if(tempo.length()!=0 && i==line.length()-1)
								pidC = Integer.parseInt(tempo);
						tempo = new String();
					}
				}
				
				if(!existe(idUser))
					les_utilisateurs.add(new User(idUser, nom_user));
			
				if(pidC==-1 && pidM==-1){
					addMessage(new Message(new Date(), idMsg, new User(idUser, nom_user), message));
				}
				else if(pidC==-1 && pidM!=-1 || pidC!=-1 && pidM==-1){
					addComment(new Comment(new Date(), idMsg, new User(idUser, nom_user), message, pidC, pidM));
				}	
			}

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#majScoreTempsReel()
	 * 
	 * Fonction pour mettre à jour les scores des Messages et Commentaires
	 * 
	 * Premiere boucle :
	 * On recupere chaque message dans l'ArrayList
	 * On fait une comparaison entre la date de création et la date actuelle
	 * On décrémente de 1 toute les 30 secondes
	 * Si le score descends en dessous de 0 alors on arrete de décrémenter
	 * 
	 * Seconde boucle : 
	 * Identique à la premire boucle mais pour le fait pour l'ArrayList appartenant à Commentaire 
	 * 
	 * 
	 */
	public void majScoreTempsReel() throws RemoteException{
        Date date = new Date();
        
        for (int i = 0; i < getLes_messages().size(); i++) {
            Message m = getLes_messages().get(i);
            long diff_secondes = (date.getTime() - m.getDate().getTime())/ (2*1000); //(30*1000) // pour 30secondes
            m.setScore( (20-(int)diff_secondes)>0 ? (20-(int)diff_secondes) : 0 );
            les_messages.set(i, m);
        }
        
        for (int i = 0; i < getLes_commentaires().size(); i++) {
            Comment c = getLes_commentaires().get(i);
            long diff_secondes = (date.getTime() - c.getDate().getTime())/ (2*1000); //(30*1000) // pour 30secondes
            c.setScore( (20-(int)diff_secondes)>0 ? (20-(int)diff_secondes) : 0 );
            les_commentaires.set(i, c);
        }
    }
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#majScoreMessage()
	 * 
	 * Boucle(i) : On recupere les scores de chaque messages
	 * 
	 * Boucle(j) : On recupere les scores de chaque commentaires appartenenant au message(i)
	 * On fait la somme de tous les scores, pour les ajouter au message(i) avec la fonction setScore
	 * Et on remets à jour l'ArrayList Message pour que la mise à jour soit bien effective
	 * 
	 */
	public void majScoreMessage() throws RemoteException{
		for (int i = 0; i < les_messages.size(); i++) {			
            Message m = les_messages.get(i);
            int score_temp = m.getScore();
            
        	for(int j=0; j < les_commentaires.size(); j++){
            	Comment c_temp = les_commentaires.get(j);
            	if(m.getIdPost() == c_temp.getPidMessage()){
            		score_temp+=c_temp.getScore();                		
            	}
            }
            m.setScore(score_temp);
            les_messages.set(i, m);
        }
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#meilleurs()
	 * 
	 * nEff[] = consiste à sauvegarder l'indice de l'ArrayList des trois meilleurs score de la liste
	 * 
	 * Premier podium:
	 * On recupere le plus gros score dans l'ArrayList Message
	 * On la sauvegarde, ce sera notre (Rang 1)
	 * On recupere aussi son idPost, pour l'utiliser plus tard
	 * 
	 * Second podium: 
	 * On va recuperer la valeur inférieur la plus proche du (rang 1)
	 * On recupere aussi son idPost, pour l'utiliser plus tard
	 * 
	 * Troisieme podium:
	 * On va recuperer la valeur inférieur la plus proche du (rang 2)
	 * On recupere aussi son idPost, pour l'utiliser plus tard
	 * 
	 * On recupere toute les informations, on les rassemble dans le tableau "podium" qui est definit dès le départ
	 * Puis on retourne l'affichage du classement.
	 * 
	 */
	public String meilleurs() throws RemoteException{
		String s = new String();
		majScoreMessage();
		int[] nEff={0,0,0};
		
		int val_podium_1=0;
        int id_post_msg1=0;
        for(int i=0; i<les_messages.size(); i++)
            if(les_messages.get(i).getScore()>val_podium_1){
                val_podium_1 = les_messages.get(i).getScore();
                id_post_msg1 = les_messages.get(i).getIdPost();
                nEff[0] = i;
            }
        
        int val_podium_2=val_podium_1;
        int id_post_msg2=0;
        for(int i=0; i<les_messages.size(); i++)
            if(les_messages.get(i).getScore()<= val_podium_2)
                if(id_post_msg1 != les_messages.get(i).getIdPost()){
                    val_podium_2 = les_messages.get(i).getScore();
                    id_post_msg2 = les_messages.get(i).getIdPost();
                    nEff[1] = i;
                }
        
        int val_podium_3=val_podium_2;
        for(int i=0; i<les_messages.size(); i++)
            if(les_messages.get(i).getScore()<= val_podium_3)
                if(id_post_msg2 != les_messages.get(i).getIdPost()){
                    val_podium_3 = les_messages.get(i).getScore();
                    nEff[2] = i;
                }

		for(int i=0; i<3; i++){
			Message temp = les_messages.get(nEff[i]);
			Classement c = new Classement(temp.getIdPost(), temp.getUser(), temp.getScore());
			podium[i] = c;
		}
		
		for(int i=0; i<3; i++)
			s = s + podium[i] + "\n";
		
		return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see DataImpl#start()
	 * 
	 * @param message	Message utilisé pour la verification d'une clé primaire
	 * 
	 * Vérifie si le message à ajouter dans l'ArrayList est bien unique, dans le cas contraire, il y aura un message d'erreur
	 * Fonction modifié pour plus de sécurité (UNIQUE KEY sur iDPost)
	 * 
	 */
	public void addMessage(Message message) {
		boolean unique = true;
		int i=0;
		while(i<les_messages.size() && unique){
        	if(message.getIdPost() == les_messages.get(i).getIdPost())
        		unique=!unique;
        	i++;
		}
		if(unique) les_messages.add(message);
		else System.out.println("Existing ID : Unable to add message -> n°" + message.getIdPost());
    }
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#afficheMessage()
	 * 
	 * Permets d'afficher toute la liste de Message chez le client
	 * 
	 * @return			Affichage ArrayList<Message> les_messages
	 * 
	 */
	public String afficheMessage(){
		String s = new String();
		for(int i=0; i<les_messages.size(); i++){
			s = s + les_messages.get(i).toString() + "\n";
		}	
		return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see DataImpl#start()
	 * 
	 * @param commentaire	Commentaire utilisé pour la verification d'une clé primaire
	 * 
	 * Vérifie si le commentaire à ajouter dans l'ArrayList est bien unique, dans le cas contraire, il y aura un message d'erreur
	 * Fonction modifié pour plus de sécurité (UNIQUE KEY sur iDPost)
	 * 
	 */
	public void addComment(Comment commentaire) {
		boolean unique = true;
		int i=0;
		while(i<les_commentaires.size() && unique){
        	if(commentaire.getIdPost() == les_commentaires.get(i).getIdPost())
        		unique=!unique;
        	i++;
		}
		if(unique) les_commentaires.add(commentaire);
		else System.out.println("Existing ID : Unable to add comment -> n°" + commentaire.getIdPost());
    }

	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#afficheCommentaire()
	 * 
	 * Permets d'afficher toute la liste de Commentaire chez le client
	 * 
	 * @return			Affichage ArrayList<Comment> les_commentaires
	 */
	public String afficheCommentaire(){
		String s = new String();
		for(int i=0; i<les_commentaires.size(); i++){
			s = s + les_commentaires.get(i).toString() + "\n";
		}
		return s;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see DataImpl#start()
	 * 
	 * @param id		Identifiant d'un utilisateur
	 * 
	 * L'identifiant permettra de savoir si l'identifiant existe ou pas dans l'ArrayList (PRIMARY KEY)
	 *
	 * @return			true -> id existant / false -> id qui n'existe pas dans la liste
	 */
	public boolean existe(int id){
		boolean existe = false;
		int i=0;
		while(i<les_utilisateurs.size() && !existe){
        	if(id == les_utilisateurs.get(i).getId())
        		existe=!existe;
        	i++;
		}
		return existe;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Data#afficheUtilisateur()
	 * 
	 * Permets d'afficher toute la liste d'utilisateur chez le client
	 * 
	 * @return			Affichage ArrayList<User> les_utilisateurs
	 */
	public String afficheUtilisateur(){
		String s = new String();
		for(int i=0; i<les_utilisateurs.size(); i++)
			s = s + les_utilisateurs.get(i).toString() + "\n";
		return s;
	}
}
