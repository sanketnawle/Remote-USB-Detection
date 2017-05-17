import java.util.*;
public class ClientRun2{
	
	    public static void main(String[] args){
	    	Client2 Clt;
	    	String rno1;
	    	String sip;
		int port;
	    	Scanner src = new Scanner(System.in);
	    	System.out.println("Enter Roll no");
	    	rno1 = src.nextLine();
	    	System.out.println("Enter IP OF Server");
	    	sip = src.nextLine();
		System.out.println("Enter Port No.");
	    	port = src.nextInt();
			    
	        Clt = new Client2(rno1,sip,port);
	        
	        Clt.startRunning();
	    }
	}


