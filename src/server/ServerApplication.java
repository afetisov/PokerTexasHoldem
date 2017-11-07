package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DataBindingException;

import commons.game.GameClient;
import commons.game.GameServer;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.Session;
import commons.model.TableInfo;
import commons.model.TablesFilter;

import server.admin.AdminServerImpl;
import server.dao.DatabaseService;
import server.game.GameController;
import server.game.GameServerEnvironment;
import server.game.GameServerImpl;
import server.game.TablesMaintenanceTask;
import server.persistence.HibernateUtil;

public class ServerApplication {

	private static Registry registry;
	
	private static final String AdminManagerUrl = "rmi://AdminServer";
	
	private static final String GameServerUrl = "rmi://GameServer";
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public static void main(String[] args) {

		System.out.print("Starting server.");
		
		sleep(1000);
		
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		
		if(System.getSecurityManager() == null){
			
			System.setSecurityManager(new SecurityManager());
		
		}
		
		try {
		
			registry = LocateRegistry.createRegistry(1099);
		
			System.out.print(".");
			sleep(1000);
			
		}catch(RemoteException e) {

			Logger.write(LoggingLevel.FATAL, "Failed to create RMI registry for default port 1099.", e);

			showMessage();
			
			return;
		}
		
		try {

			registry.rebind(AdminManagerUrl, new AdminServerImpl());
			
			System.out.print(".");
			sleep(1000);
			
		}catch(RemoteException e) {

			Logger.write(LoggingLevel.FATAL, "Failed to bind admin manager remote object.", e);
			
			registry = null;

			showMessage();
			
			return;
		}

		try {

			registry.rebind(GameServerUrl, new GameServerImpl());

			System.out.print(".");
			sleep(1000);

		}catch(RemoteException e) {

			Logger.write(LoggingLevel.FATAL, "Failed to bind game server remote object.", e);
			
			registry = null;

			showMessage();
			
			return;
		}

		try {
			
			HibernateUtil.start();

			System.out.print(".");
			sleep(1000);

		}catch(Exception e) {

			Logger.write(LoggingLevel.FATAL, "Failed to start hibernate.", e);
			
			serverShutdown();

			showMessage();
			
			System.exit(0);
		}

		try {
			
			startGames();
		
			System.out.print(".");
			sleep(1000);

		}catch(Exception e) {

			Logger.write(LoggingLevel.FATAL, "Failed to start game controllers.", e);

			serverShutdown();

			showMessage();
			
			System.exit(0);
			
		}
		
		scheduler.scheduleWithFixedDelay(new TablesMaintenanceTask(), 0, 5, TimeUnit.SECONDS);
		
		Runtime.getRuntime().addShutdownHook( new Thread() {

			@Override
			public void run() {
				serverShutdown();
			} 
		});
		
		System.out.println();
		System.out.println("Server started");
		System.out.println("Enter 'quit' to stop server");
		
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			
			String command = null;
			
			try {
				command = bufReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(command != null && command.equals("quit")) break;
			
			System.out.println("Incorrect command. Please enter 'quit' to exit the program.");
				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		
		System.out.println("Stopping server...");
		sleep(1000);

		System.exit(0);
	}
	
	private static void startGames() throws Exception {
		
		GameServerEnvironment.games.clear();
		
		DatabaseService.getTablesDAO().resetTablesInfo();

		List<TableInfo> tables = DatabaseService.getTablesDAO().getActiveTables();
		
		for(TableInfo table : tables) {
			
			GameController gc = new GameController(table);
			
			GameServerEnvironment.games.put(table.getId(), gc);
			
			DatabaseService.getTablesDAO().updateTableGameID(table.getId(), gc.getId());
		
			gc.start();
		}
	}
	
	private static void stopGames() throws Exception {
		for(GameController game : GameServerEnvironment.games.values()) {
			game.stopGame(true);
		}
	}
	
	private static void serverShutdown() {
		try {
			if (registry != null) {
				
				registry.unbind(AdminManagerUrl);
				
				registry.unbind(GameServerUrl);
				
				registry = null;
			}

			scheduler.shutdownNow();
			
			stopGames();
			
			HibernateUtil.shutdown();
			
			System.out.println("Server stopped");
			
		}catch(Exception e) {
			Logger.write(LoggingLevel.ERROR, "Failed to stop server.", e);
		}		
	}
	
	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {}
	}
	
	private static void readLine() {
		try {
			System.in.read();
		}catch(Exception e) {}
	}
	
	private static void showMessage() {
		
		System.out.println();
		System.out.println("Server failed to start. Please see server's log file for details.");
		System.out.println("Press <Enter> to exit.");
		
		readLine();		
	}
}
