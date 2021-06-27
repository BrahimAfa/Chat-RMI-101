package com.ensas.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface IChatServer extends Remote {
		
	void Send(String userName, String chatMessage)throws RemoteException;

	void registerClient(String[] details)throws RemoteException;
	
	void quite(String userName)throws RemoteException;
	
}


