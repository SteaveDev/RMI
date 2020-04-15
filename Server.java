import java.rmi.*;

public class Server{
	
	public static void main(String [] args) throws Exception {
		DataImpl d = new DataImpl();
        Naming.rebind("33333", d);
        System.out.println("Serveur connecté.");

	}
	
}
