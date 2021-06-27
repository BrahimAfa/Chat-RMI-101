package com.ensas.server;


/**
 * A class used by the server program to keep
 * details of connected clients ordered
 * @author Daragh Walshe 	B00064428
 * RMI Assignment 2		 	April 2015
 *
 */
public class Client {

	public String name;
	public IChatClient client;
	
	//constructor
	public Client(String name, IChatClient client){
		this.name = name;
		this.client = client;
	}

	
	//getters and setters
	public String getName(){
		return name;
	}
	public IChatClient getClient(){
		return client;
	}
	
	
}
