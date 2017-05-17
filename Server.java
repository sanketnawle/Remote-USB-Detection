import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

	private JTextField userText;
    private JTextArea chatWindow;
    private DataOutputStream output; //to communicate with 	others computer( we  use Stream)
    //two types of stream we are making output and input
    private DataInputStream input;
    private ServerSocket server;
    private Socket connection;//to set connection from computer
   
   	//constructor
    public Server(){
    	super("VNS Messanger(Server)");
        userText = new JTextField();
        userText.setEditable(false);//if not connected to  anyone you cant type any message
        userText.addActionListener(
            new ActionListener(){
            //if user submitt the text(i.e after enter)
            public void actionPerformed(ActionEvent event){
            	sendMessage(event.getActionCommand());
                
                userText.setText("");//after entering the userText area becomes empty like in whats app we see.
                
            
            }
          }
        );
        
        add(userText,BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(500,250);
        setVisible(true);
    
    
    }
    
    
    //set up and run the server
    public void startRunning(){
        try{
            //to connect to particular port
        	server = new ServerSocket(6789,100);//(application ,no of people)
        		
            while(true){
                try{
                  	//connection and have conversation
                    
                    waitForConnection();//while connecting(wait for connection)
                    setupStreams();//set up connection
                    whileChatting();//whaen two omputers are connected
                }catch(EOFException eofException){
                		showMessage("\n Server ended the connection !"); // EOF means end of i.e if connection end
                }finally{
                	closeCon();
                }
            
            }
        }catch(IOException ioException){
        		ioException.printStackTrace();//if some error ocuur it trace and show.
        }
    
    }
    
    //wait for connection and connected than display connection information
    private  void waitForConnection() throws IOException{
        
    	showMessage("Waiting for someone to connect...");
        connection = server.accept();//connection is a socket.request accepted than connection created between server and client
        
        showMessage("Now Connected to"+ connection.getInetAddress().getHostName());
        
    
    }
    
    //get stream to send and receive data
    private void setupStreams() throws IOException{
    		output  = new DataOutputStream(connection.getOutputStream());
        	output.flush(); // bytes of information ko clear karne ke liye
        	
        	input = new DataInputStream(connection.getInputStream());// set path for another computter to chta with us
        	showMessage("\n Streams are setup! \n");
       		
    }
    
    //during chat
    private void whileChatting() throws IOException{
    	String message = "You are now Connected ab to khush ho jaa !";
        sendMessage(message);
        ableToType(true);//allows user to type
        do{
        	//conversation 
           
            	//we read the message send by other.
                //if otherone is sending msg(Object) that we cant convert to string than "catch".
                message = (String) input.readUTF();
                showMessage("\n" +message);
                
                	
           
            
            
        }while(!message.equals("CLIENT - BYE"));
        
      }
    
    //closes streams and socket
    private void closeCon(){
    
    	showMessage("\n Closing connections.....\n");
        ableToType(false);
        try{
        		//it closes connection
            	output.close();
            	input.close();
            	connection.close();
              	
        }catch(IOException ioException){
        		ioException.printStackTrace();
        }
    
    }
    
    //send msg to client
    private void sendMessage(String message){
        try{
        	output.writeUTF("SERVER - " + message);
            output.flush();//if some byte left than clean
            showMessage("\nSERVER -" + message);
        }catch(IOException ioException){
        	chatWindow.append("\n EROR : MSG Cant send");
        
        }
    }
    
    //updates chatWindow
    private void showMessage(final String text){
    	SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                 	chatWindow.append(text);
                
                }
            }
        );
    }
    
    //let user type in the chat box
    private void ableToType(final boolean tof){
    		SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                     	userText.setEditable(tof);
          					
                    }
                }
            );
    }
}
