package es.urjc.gomez;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReaderFile{
	
	public static int existUserPass(String name, String pass){

		int exist = 0;
		BufferedReader br = null;
		
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String user[] = line.split(":");
				if(user[0].equals("user")){
					String data [] = user[1].split("/");
					if(data[0].equals(name) && data[1].equals(pass)){
						exist = 1;
						break;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return exist;
	}
	
	public static int existUser(String name){
		
		int exist = 0;
		BufferedReader br = null;
		
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String user[] = line.split(":");
				if(user[0].equals("user")){
					String data [] = user[1].split("/");
					if(data[0].equals(name)){
						exist = 1;
						break;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return exist;
	}
	
	public static int existGroup(String group){
		int exist = 0;
		BufferedReader br = null;
		
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String lineSplit[] = line.split(":");
				if(lineSplit[0].equals("group")){
					String data[] = lineSplit[1].split("/");
					if(data[0].equals(group)){
						exist = 1;
						break;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return exist;
	}
	
	public static int belongGroup(String group, String name){
		int belong = 0;
		BufferedReader br = null;
		
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String lineSplit[] = line.split(":");
				if(lineSplit[0].equals("group")){
					String data[] = lineSplit[1].split("/");
					if(data[0].equals(group)){
						String members[] = data[1].split("%");
						for(int i = 0; i < members.length; i++){
							if(members[i].equals(name))
								return 1;
						}
					}
					
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return belong;
	}
	
	public static String[] getMembersGroup(String group){
		BufferedReader br = null;
		String members[] = null;
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String lineSplit[] = line.split(":");
				if(lineSplit[0].equals("group")){
					String data[] = lineSplit[1].split("/");
					if(data[0].equals(group)){
						members = data[1].split("%");
						return members;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return members;
	}
	
	public static List<String> getContactsbyName(String name){
		BufferedReader br = null;
		List<String> members = new ArrayList<String>();
		try {

			String line;
			br = new BufferedReader(new FileReader("DataBase"));

			while ((line = br.readLine()) != null) {
				String lineSplit[] = line.split(":");
				if(lineSplit[0].equals("group")){
					String membersGroup[] = lineSplit[1].split("/");
					String membersName[] = membersGroup[1].split("%");
					for(int i = 0; i < membersName.length; i++){
						if(membersName[i].equals(name)){
							for(i = 0; i < membersName.length; i++){
								if(!membersName[i].equals(name) && !members.contains(membersName[i])){
									members.add(membersName[i]);
								}
							}
						break;
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		return members;
	}
	
}