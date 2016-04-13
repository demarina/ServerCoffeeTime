package es.urjc.gomez;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket servsocket;
	private SchedulerData scheduler;
	
	public Server(String port_str) throws IOException{
			this.servsocket = new ServerSocket(Integer.parseInt(port_str));
			this.scheduler = new SchedulerData();
	}

	public void start(){
		System.err.println("Server bind correctly, WORKING!.");
		for(;;){
			try {
				Socket client = this.servsocket.accept();
				new Thread(new Laborer(client, scheduler)).start();
			} catch (Exception e) {
				System.err.println("Error to process data!");
			}
		}
	}

	public static void main (String args[]){
		Server server = null;
		try{
			server = new Server("2000");
			server.start();
		}catch (IOException e){
			System.err.println("Error to bind server, SERVER CAN'T START.");
			e.printStackTrace();
		}
	}
}