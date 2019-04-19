package time_sys;

import ocsf.client.AbstractClient;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;


public class TSClient extends AbstractClient{

	static Scanner scan = new Scanner(System.in);
	static boolean ack = true;//Used to wait for server response.
	static boolean found = false;
	static Date date;
	
	public TSClient(String host, int port){
		super(host, port);
		
		
	}
	static TSClient cl = new TSClient("localhost", 5555);
	
	
	public static void main(String args[]){
		
		
		
		while(true){
			
			ack = false;
			System.out.println("Please enter a command:");
			String text = scan.nextLine();
			text = text.toLowerCase();
			if (text.equals("connect")){//Connects client to server
				try{
					cl.openConnection();
				}catch(Exception E){
					System.out.println("Error connecting to server");
					ack = true;
				}
			}
			
			else if (text.equals("disconnect")){//Disconnects client from server
				try{
					cl.closeConnection();
				}catch(Exception e){
					System.out.println("Error closing connection");
					ack = true;
				}
			}
			else if (text.equals("clock in")){//Clocks an employee in
				String msg = ClockIn();
				System.out.println(msg);
				if(cl.isConnected()){
					try{
						cl.sendToServer(msg);
					}catch(Exception e){
						//save to batch
						ack = true;
					}
				}
				else{
					//save to batch
					ack = true;
				}
				scan.nextLine();
			}
			else if (text.equals("clock out")){//CLocks an employee out
				String msg = ClockOut();
				System.out.println(msg);
				if(cl.isConnected()){
					try{
						cl.sendToServer(msg);
					}catch(Exception e){
						//save to batch
						ack = true;
					}
				}
				else{
					//save to batch
					ack = true;
				}
				scan.nextLine();
			}
			else if (text.equals("modify punches")){
				//TBI
				ModifyPunches();
			}
			
			else if (text.equals("add employee")){//Adds an employee ID to the server
				System.out.println("Enter employee ID to add:");
				int e_id = scan.nextInt();
				String msg = "AddEmp " + e_id;
				try{
					cl.sendToServer(msg);
				}catch(Exception e){
					System.out.println("Error adding employee");
					ack = true;
				}
				scan.nextLine();
				
			}
			else if (text.equals("get punch")){//Returns most recent punch for an ID
				System.out.println("Enter employee ID:");
				int e_id = scan.nextInt();
				String msg = "getPunch " + e_id;
				try{
					cl.sendToServer(msg);
				}catch(Exception e){
					System.out.println("Error sending request");
					ack = true;
				}
				scan.nextLine();
			}
			else if (text.equals("get all punches")){//Returns all punches for an ID
				System.out.println("Enter employee ID:");
				int e_id = scan.nextInt();
				String msg = "getAllPunches " + e_id;
				try{
					cl.sendToServer(msg);
				}catch(Exception e){
					System.out.println("Error sending request");
					ack = true;
				}
				scan.nextLine();
			}
			else ack = true;
			try{
				Thread.sleep(200);
			}catch(Exception e){
				
			}
			while(!(ack)){};
		}
		
		
		
	}
	
	protected static String ClockIn(){
		System.out.println("Enter employee ID: ");
		String userID = scan.next();
		
		System.out.println("Enter clock-in type ('R' for Regular or 'C' for Call-back)");
		String userWorkType = scan.next();
		//userWorkType = userWorkType.toUpperCase();
		/*while( !(userWorkType.equals("R")) || !(userWorkType.equals("C")) )
		{
			System.out.println("Please enter a valid clock-in type:");
			userWorkType = scan.next();
		}*/
		//System.out.println(userWorkType);
		
		date = new Date();
		String time = Long.toString(date.getTime());
		
		String msg ="ClockI " + userID + " " + userWorkType + " " + time;
		
		return msg;
	}
	
	protected static String ClockOut(){
		System.out.println("Enter employee ID: ");
		String userID = scan.next();
		
		date = new Date();
		String time = Long.toString(date.getTime());
		
		String msg ="ClockO " + userID + " "  + time;
		
		return msg;
	}
	
	protected static void ModifyPunches(){//Currently can only add a punch
		String expectedPattern = "h:mma MMM d";
		String clockin;
		String clockout;
		//System.out.println("Employee" + id + "found.");
		System.out.println("Would you like to add new time data (add) or edit existing data (edit)?");
		//while(scan.hasNext()){
		String response = scan.nextLine();
		if(response.equals("add")){
			
			System.out.println("Please enter the employee's ID number");
			int id = scan.nextInt();
			
			System.out.println("Enter clock-in type");
			String userWorkType = scan.next();
			
			//Prompt the user for the info and give a sample format
			System.out.println("Please enter the new time data in the following example format:");
			System.out.println("clock-in: 8:00am Jan 4; clock-out: 4:00pm Jan 5");
			scan.nextLine();
			//get the input and format it to just data
			response = scan.nextLine();
			clockin = response.substring(0, 22);
			clockout = response.substring(24);
			clockin = clockin.substring(10);
			clockout = clockout.substring(11);
			SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
			try{
				Date in = formatter.parse(clockin);
				Date out = formatter.parse(clockout);
				String CI = "ClockI " + id + " " + userWorkType + " " + Long.toString(in.getTime()); 
				String CO = "ClockO " + id + " "  + Long.toString(out.getTime()); 
				cl.sendToServer(CI);
				cl.sendToServer(CO);
				
				//System.out.println(in);
				//System.out.println(out);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(response.equals("edit")){
			System.out.println("edited");
		}
		//}
	}
	
	
	protected void handleMessageFromServer(Object msg){
		
		if (msg instanceof String){
			if(msg.equals("empFound")){
				found = true;
				ack = true;
			}
			else if(msg.equals("empNotFound")){
				found = false;
				ack = true;
			}
			else if (!(msg.equals("ack"))) {
				ack = true;
				System.out.println(msg);
			}
			else ack = true;
		}
		ack = true;
	}
	
	protected void connectionEstablished(){
		System.out.println("Connected to server.");
		ack = true;
	}
	
	protected void connectionClosed(){
		System.out.println("Connection to server terminated.");
		ack = true;
	}

}
