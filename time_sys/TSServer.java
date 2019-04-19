package time_sys;

import ocsf.server.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class TSServer extends AbstractServer{
	
	static HashMap<Integer, ArrayList<String>> emp_table = new HashMap<Integer, ArrayList<String>>();
	static Scanner scan;
	
	static TSServer sv = new TSServer(5555);//Creating the server.
	
	public static void main(String args[]){
		
		
		try{
			sv.listen();//Server starts listening for clients.
		}
		catch(Exception ex){
			System.out.println("Error listening.");
		}
	}
	
	public TSServer(int port){//Generic constructor
		super(port);
	}
	
	protected void serverStarted(){//Hook method		
		System.out.println("Server started.");
	}
	
	protected void serverStopped(){//Hook method
		System.out.println("Server stopped.");
	}
	
	protected void clientConnected(ConnectionToClient client){//Hook method
		System.out.println("A client has connected.");
	}
	
	protected void clientDisconnected(ConnectionToClient client){//Hook method
		System.out.println("A client has disconnected.");
	}
	
	protected void handleMessageFromClient(Object msg, ConnectionToClient client){//Handles all incoming messages from clients
		if (msg instanceof String) {
			System.out.println((String)msg);
			scan = new Scanner((String)msg);
			String temp = scan.next();
			if (temp.equals("ClockI") || temp.equals("ClockO")){
				int e_id = scan.nextInt();
				if (emp_table.containsKey(e_id)){
					emp_table.get(e_id).add((String)msg);
				}
				else{
					try{
						client.sendToClient("Employee not found");
					}catch(Exception e){
						System.out.println("ERROR");
					}
					
				}
			}
			else if(temp.equals("AddEmp")){
				int e_id = scan.nextInt();
				emp_table.put(e_id, new ArrayList<String>());
				System.out.println("Employee " + e_id + " added.");
				try{
					client.sendToClient("Employee added");
				}catch(Exception e){
					System.out.println("ERROR");
				}
				
			}
			else if(temp.equals("getPunch")){
				int e_id = scan.nextInt();
				String text;
				if (emp_table.containsKey(e_id)){
					try{
						text =emp_table.get(e_id).get(emp_table.get(e_id).size()-1);
					}catch(Exception E){
						text = "No punch found";
					}
					try{
						client.sendToClient(text);
					}catch(Exception E){
						System.out.println("Error sending punch");
					}
					
				}
				else{
					try{
						client.sendToClient("Employee not found");
					}catch(Exception e){
						System.out.println("ERROR");
					}
					
				}
			}
			else if(temp.equals("getAllPunches")){
				int e_id = scan.nextInt();
				String text;
				if (emp_table.containsKey(e_id)){
					for (int i = emp_table.get(e_id).size() - 1; i >= 0; i-- ){
						try{
							text = emp_table.get(e_id).get(i);
						}catch(Exception E){
							text = "No punch found";
						}
						try{
							client.sendToClient(text);
						}catch(Exception E){
							System.out.println("Error sending punch");
							break;
						}
					}
					
				}
				else{
					try{
						client.sendToClient("Employee not found");
					}catch(Exception e){
						System.out.println("ERROR");
					}
					
				}
				
			}
			else if(temp.equals("findEmp")){
				int e_id = scan.nextInt();
				if (emp_table.containsKey(e_id)){
					try{
						client.sendToClient("empFound");
					}catch(Exception e){
						System.out.println("Error sending findEmpAck");
					}
				}
				else{
					try{
						client.sendToClient("empNotFound");
					}catch(Exception e){
						System.out.println("Error sending findEmpNak");
					}
					
				}
			}
			try{
				client.sendToClient("ack");
			}catch(Exception e){
				System.out.println("Error ack");
			}
			
		}
		
	}

}
