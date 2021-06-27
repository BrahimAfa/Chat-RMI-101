package com.ensas.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ChatServer extends UnicastRemoteObject implements IChatServer {
	String line = "---------------------------------------------\n";
	private Vector<Client> clients;
	private static final long serialVersionUID = 1L;
	
	//Constructor
	public ChatServer() throws RemoteException {
		super();
		clients = new Vector<Client>(10, 1);
	}

	public static void main(String[] args) throws RemoteException {
		java.rmi.registry.LocateRegistry.createRegistry(1099);
		System.out.println("RMI Server ready");
		try{
			IChatServer hello = new ChatServer();
			Naming.rebind("rmi://localhost/EnsasNetwork", hello);
			System.out.println("Server Started");
		}
		catch(Exception e){
			System.out.println("Server had problems starting");
		}	
	}

	@Override
	public void Send(String name, String nextPost) throws RemoteException {
		String message =  name + " : " + nextPost + "\n";
		sendToAll(message);
	}


	@Override
	public void registerClient(String[] details) throws RemoteException {
		registerChatter(details);
	}

	private void registerChatter(String[] details){		
		try{
			IChatClient nextClient = (IChatClient)Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			clients.addElement(new Client(details[0], nextClient));
			updateUserList();		
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}

	private void updateUserList() {
		String[] currentUsers = getUserList();	
		for(Client c : clients){
			try {
				c.getClient().updateUserList(currentUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

	private String[] getUserList(){
		// generate an array of current users
		String[] allUsers = new String[clients.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = clients.elementAt(i).getName();
		}
		return allUsers;
	}

	public void sendToAll(String newMessage){	
		for(Client c : clients){
			try {
				c.getClient().messageFromServer(newMessage);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void quite(String userName) throws RemoteException{
		
		for(Client c : clients){
			if(c.getName().equals(userName)){
				clients.remove(c);
				break;
			}
		}		
		if(!clients.isEmpty()){
			updateUserList();
		}			
	}
	
}



