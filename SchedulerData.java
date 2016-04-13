package es.urjc.gomez;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulerData {

	private ConcurrentHashMap<String, List<String>> StoreMessageUser = new ConcurrentHashMap<String, List<String>>();
	
	public synchronized void addMessage(String destin, String message){
		List<String> msgs = StoreMessageUser.get(destin);
		if(msgs == null){
			msgs = new ArrayList<String>();
			msgs.add(message);
			StoreMessageUser.put(destin, msgs);
		}else{
			msgs.add(message);
			StoreMessageUser.put(destin, msgs);
		}
	}
	
	public synchronized List<String> getNewMessages(String name){
		List<String> msgs = StoreMessageUser.get(name);
		if(msgs == null){
			return null;
		}
		List<String> msgsNew = new ArrayList<String>();
		StoreMessageUser.put(name, msgsNew);
		
		return msgs;
	}

}
