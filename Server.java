import java.rmi.*;

public class Server{
	
	public static void main(String [] args) throws Exception {
		DataImpl d = new DataImpl();
        Naming.rebind("33333", d);
        System.out.println("Serveur connecté.");

        d.start();
        
        while (true) {
            d.majScoreTempsReel();
            try {
                Thread.sleep(1000);
                d.majScoreMessage();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

	}
	
}
