import javax.swing.JFrame;

public class ServerRun{
    public static void main(String[] args){
    	Server tonystark = new Server();
     	tonystark.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tonystark.startRunning();
    
    }
}
