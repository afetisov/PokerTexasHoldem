package client.admin.remote;

import java.rmi.RemoteException;

import client.service.ServiceManager;
import commons.admin.AdminServer;

public class AdminRemoteService {
	
	private static final String adminServer = "AdminServer";
	
	public static AdminServer getAdminServer() {
		try {
			return (AdminServer) ServiceManager.getRemoteService(adminServer);
		}catch(RemoteException exc) {
			exc.printStackTrace();
		}
		return null;
	}

}
