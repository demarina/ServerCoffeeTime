package es.urjc.gomez;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Laborer implements Runnable{

	private Socket socket;
	private DataOutputStream odata;
	private DataInputStream idata;
	private SchedulerData scheduler;
	
	public Laborer(Socket socket, SchedulerData scheduler) throws Exception{
		try{
			this.socket = socket;
			this.odata = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			this.idata = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.scheduler = scheduler;
		}catch (Exception e){
			System.err.println("Error to get stream of the client");
			e.printStackTrace();
			throw e;
		}
	}
	
	public int getType(){
		int type = -1;
		try {
			type = idata.readInt();
		} catch (IOException e) {
			System.err.println("Error to read type!");
			e.printStackTrace();
		}
		
		return type;
	}
	
	public void doLogin(){
		int length;
		try {
			length = idata.readInt();
			byte[] buf = new byte[length];
			idata.read(buf);
			String user = new String (buf, "UTF-8");
			String data[] = user.split("/");
			int exist = ReaderFile.existUserPass(data[0], data[1]);
			odata.writeInt(exist);
	        odata.flush();
	        if(exist == 1){
	        	System.err.println("Login OK!!");
	        }else{
	        	System.err.println("Login FAIL!!");
	        }
		} catch (IOException e) {
			System.err.println("Error to login!");
			e.printStackTrace();
		}
	}
	
	public void receiveMessageSimple(){
		int length;
		try{
			length = idata.readInt();
			byte[] buf = new byte[length];
			idata.read(buf);
			String destin = new String (buf, "UTF-8");
			int exist = ReaderFile.existUser(destin);
			if(exist == 1){
				System.err.println("Existe destinatario!");
				length = idata.readInt();
				buf = new byte[length];
				idata.read(buf);
				String name = new String (buf, "UTF-8");
				if(name.equals(destin)){
					System.err.println("Name and destin same!");
					odata.writeInt(3);
			        odata.flush();
			        return;
				}
				length = idata.readInt();
				buf = new byte[length];
				idata.read(buf);
				String message = new String (buf, "UTF-8");
				message = message + "%" + name;
				scheduler.addMessage(destin, message);
				odata.writeInt(1);
		        odata.flush();
			}else{
				odata.writeInt(2);
		        odata.flush();
				System.err.println("NOOO existe destinatario!");
			}
		} catch (IOException e) {
			try {
				odata.writeInt(0);
				odata.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.err.println("Error to read Message!");
			e.printStackTrace();
		}
	}
	
	public void sendNewMessages(){
		try {
			int length = idata.readInt();
			byte[] buf = new byte[length];
			idata.read(buf);
			String name = new String (buf, "UTF-8");
			List<String> msgs = scheduler.getNewMessages(name);
			if(msgs == null){
				odata.writeInt(0);
				odata.flush();
				return;
			}
			int size = msgs.size();
			odata.writeInt(size);
			for(String item : msgs){
				size = item.length();
				odata.writeInt(size);
				odata.write(item.getBytes("UTF-8"));
			}
			odata.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void receiveMessageGroup(){
		try {
			int length = idata.readInt();
			byte[] buf = new byte[length];
			idata.read(buf);
			String group = new String (buf, "UTF-8");
			int existGroup = ReaderFile.existGroup(group);
			if(existGroup == 0){
				System.err.println("Group not Exist!!");
				odata.writeInt(2);
				odata.flush();
				return;
			}
			length = idata.readInt();
			buf = new byte[length];
			idata.read(buf);
			String name = new String (buf, "UTF-8");
			int belongGroup = ReaderFile.belongGroup(group, name);
			if(belongGroup == 0){
				System.err.println("Not belong to group!!");
				odata.writeInt(3);
				odata.flush();
				return;
			}
			length = idata.readInt();
			buf = new byte[length];
			idata.read(buf);
			String message = new String (buf, "UTF-8");
			message = message + "%Grupo '" + group +"' (" + name +")";
			String members[] = ReaderFile.getMembersGroup(group);
			if(members != null){
				for(int i = 0; i < members.length; i++){
					if(!members[i].equals(name)){
						scheduler.addMessage(members[i], message);
					}
				}
			}
			odata.writeInt(1);
			odata.flush();
		} catch (IOException e) {
			try {
				odata.writeInt(0);
				odata.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void sendContacts(){
		int length;
		try {
			length = idata.readInt();
			byte[] buf = new byte[length];
			idata.read(buf);
			String name = new String (buf, "UTF-8");
			List<String> contacts = ReaderFile.getContactsbyName(name);
			odata.writeInt(contacts.size());
			for(String single : contacts){
				odata.writeInt(single.length());
				odata.write(single.getBytes("UTF-8"));
			}
			odata.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		try {
			int type = getType();
			switch(type){
				case -1:
					return;
				case 0:
					doLogin();
					break;
				case 1:
					receiveMessageSimple();
					break;
				case 2:
					sendNewMessages();
					break;
				case 3:
					receiveMessageGroup();
					break;
				case 4:
					sendContacts();
					break;
			}
		}finally{
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
