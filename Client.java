import java.rmi.*;

public class Client{

	public static void main(String [] args) throws Exception{
		
        Data d = (Data) Naming.lookup("rmi://localhost/33333");
        System.out.println("Client connecté.");
        
        d.start();
        
        while (true) {
            d.majScoreTempsReel();
            try {
            	
                Thread.sleep(1000);
                d.majScoreMessage();
                System.out.println(d.meilleurs());
                //System.out.println(d.afficheMessage());
                //System.out.println(d.afficheCommentaire());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
}
