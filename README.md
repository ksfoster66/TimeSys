The TimeSyS.

Initially created as part of a larger project, this forms the core of the project, the central time keeping system. The most important portion of this is meant to be ran server side with multiple clients connecting to deliver employee punch information.

Their are some feature lacking in this as they were split among the project memebers and all I am providing here is the code I have written, which luckily can be ran stand alone. As such this means aspects like a GUI frontend and the system to properly log in and modify employee punch data are absent and frankly unlikely to be added by me at this point.

The client and server should be compiled seperately. They both contain a main function as they should be ran seperately

Once the server is running there is nothing you really do other than check whatever message you give it or kill it with Ctrl+c.

On the client side the available commands are(case insensitive):
	Connect: Connects to the server. Only needs to be 
	
	Disconnect: Disconnects from the server
	
	Clock in: Queries for employee ID number and type of punch, then sends info to server
	
	Clock out: Queries for employee ID and sends info to server
	
	Modify Punches: The two options are to add a punch or to edit existing punches. Only the Add punch method currently functions
	
	Add Employee: Adds an employee to the system. Only adds an ID number
	
	Get Punches: Returns the most recent punch for an entered Employee ID
	
	Get All Punches: Returns all punches for the entered Employee ID.