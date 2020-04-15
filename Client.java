import java.rmi.*;

public class Client{

	public static void main(String [] args) throws Exception{
		
        Data d = (Data) Naming.lookup("rmi://localhost/33333");
        System.out.println("Client connecté.");
        
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println(d.meilleurs());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
}
