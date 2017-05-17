import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import java.io.File;

public class Client2{
//---------------------------------------------------
	private String RollNo;
	private String ServerIP;
	private Socket connection;
	 private DataInputStream input;
	  private DataOutputStream output; 
	  static File[] oldListRoot = File.listRoots();
		static boolean Run = false;
	final int portNo;

	  public Client2(String rno , String host ,int p){
		RollNo = rno;
		ServerIP = host;
		portNo = p; 

	}




public void startRunning()
    {
        try
        {
        	connectToServer();
            	setupStreams();
            	waitForNotifying();
        }catch(EOFException eofExc){
        
        	System.out.println("\n Client terminated connection");
        }catch(IOException ioExp)
        	{
        		ioExp.printStackTrace();
       		}finally
        		{
        		closeThis();
        		}
     }

//connect toServer
    private void connectToServer() throws IOException
    {
    	try{
	System.out.println("Connecting------");
        connection = new Socket(InetAddress.getByName(ServerIP) , portNo);
        System.out.println("Connected to :"+ connection.getInetAddress().getHostName());
    }catch(Exception e3){
	  e3.printStackTrace();

	}
}
    
    //method setupStreams to send and receive messages
    private void setupStreams() throws IOException{
	try{
    	output = new DataOutputStream(connection.getOutputStream());
        output.flush();
        input = new DataInputStream(connection.getInputStream());
       }catch(Exception e3){
	  e3.printStackTrace();

	}
    
    }
    
    
//----------------------------------------------------
    private void waitForNotifying() {
		System.out.print("Running");
		sendMessage(RollNo);
//----
	         Run = true;
	            while (Run) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e){
	                    e.printStackTrace();
	                }
	                if (File.listRoots().length > oldListRoot.length){
	                    System.out.println("new drive detected");
	                    oldListRoot = File.listRoots();
	                    System.out.println("drive"+oldListRoot[oldListRoot.length-1]+" detected");
	                    sendMessage(RollNo+" Tries to connect the drive");
				

	                } else if (File.listRoots().length < oldListRoot.length) {
						System.out.println(oldListRoot[oldListRoot.length-1]+" drive removed");
						sendMessage(RollNo+" Removed the drive");

	                    oldListRoot = File.listRoots();
				

	                }

	            }
	       
	 
	}

    
    
//-----------------------------------------------------







    //close This Method to close Stream and Socket i.e closes connection
    private void closeThis(){
    	System.out.print("\n Closing Connectionnn..........Closed");
        
        try{
        		output.close();
            	input.close();
            	connection.close();
        }catch(Exception ioExp){
        	ioExp.printStackTrace();
        }
    }
                    
    private void sendMessage(String msg){
        try{
        	output.writeUTF(msg);
            output.flush();
            System.out.print("\nCLIENT - "+msg);
            
        }catch(IOException ioExp){
        	ioExp.printStackTrace();
        }           
     }
                    
     
     
    

//-----------------------------------
}
    
