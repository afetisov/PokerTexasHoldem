package client.service;


import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import commons.admin.AdminServer;
import commons.log.Logger;
import commons.log.LoggingLevel;


public class ServiceManager {
	
	private ServiceManager() {}
	
	public static Remote getRemoteService(String name) throws RemoteException {

		String host = System.getProperty("host", "localhost");

		int port = 1099;
		
		try {
			
			port = Integer.parseInt(System.getProperty("port", "1099"));
		
		}catch(NumberFormatException e) {}

		try {
			
			return LocateRegistry.getRegistry(host,port).lookup("rmi://" + name);
	
		}catch(NotBoundException exc) {
			Logger.write(LoggingLevel.ERROR, "Failed to locate RMI registry rmi://" + name, exc);
		}
		
		return null;
	}
}
