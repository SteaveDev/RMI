import java.rmi.*;

public interface Data extends Remote{

	public void start() throws RemoteException;
	public String afficheMessage() throws RemoteException;
	public String afficheCommentaire() throws RemoteException;
	public String afficheUtilisateur() throws RemoteException;
	public String meilleurs() throws RemoteException;
	public void majScoreTempsReel() throws RemoteException;
	public void majScoreMessage() throws RemoteException;
	
	
}