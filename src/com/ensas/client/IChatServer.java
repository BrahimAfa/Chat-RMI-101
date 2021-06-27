package com.ensas.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatServer extends Remote {
		
	void Send(String userName, String chatMessage)throws RemoteException;

	void registerClient(String[] details)throws RemoteException;
	
	void quite(String userName)throws RemoteException;
	
}


