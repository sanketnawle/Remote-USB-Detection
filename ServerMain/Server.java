package com.servermain;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.servermain.R;

public class Server extends ActionBarActivity {
    private TextView tvClientMsg,tvServerIP,tvServerPort;
    private final int SERVER_PORT = 8080; //Define the server port
    Boolean mrun = false;
    ServerSocket socServer;
    Socket socClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
        tvServerPort.setText(Integer.toString(SERVER_PORT));

        getDeviceIpAddress();
        //New thread  listen to incoming connections
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    socServer = new ServerSocket(SERVER_PORT);



                    while (true){


                        tvClientMsg.setText("Connecting------");
                        socClient = socServer.accept();
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();


                        serverAsyncTask.execute(new Socket[]{socClient});



                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public void getDeviceIpAddress() {
        try {
            //Loop through all the network interface devices
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                //Loop through all the ip addresses of the network interface devices
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    //Filter out loopback address and other irrelevant ip addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        //Print the device ip address in to the text view
                        tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
   //HANDLS COMMUNICATION WITH CLIENT
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        //Background task which serve for the client
        @Override
        protected String doInBackground(Socket... params) {
            String result = null;

            Socket mySocket = params[0];
            mrun = true;
            try {
                tvClientMsg.setText("Connected to : "+socClient.getInetAddress());

                while(mrun) {

                    InputStream is = mySocket.getInputStream();

                    DataInputStream input = new DataInputStream(is);



                    result = input.readUTF();


                    tvClientMsg.append("\n"+result);

                }
                    mySocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e4){
                e4.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {


            tvClientMsg.setText(s);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

            socServer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}