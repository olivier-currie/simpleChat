package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;
import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  /**
   * The ui
   */
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if (msg instanceof String) {
      String message = (String) msg;
      if (message.startsWith("#login")) {
        String[] separatedString = message.split(" ");
        if (client.getInfo("login id") == null) {
          client.setInfo("login id", separatedString[1]);
        } else {
          try {
            client.sendToClient("Error: Duplicate connexion.");
            client.close();
          } catch (Exception e) {}
        }
      } else {
        this.sendToAllClients(client.getInfo("login id") + ": "+ message);
      }
  }
    System.out.println("Message received: " + msg + " from " + client);
  } 
  /**
   * This method handles messages from the console
   * 
   * @param message The message received
   */
  public void handleMessageFromServerUI(String message) {
    //if the message is a command
    if (message.startsWith("#")) {
      switch(message) {
        case "#quit":
          try {
            close();
          } catch (Exception e) {
            System.out.println("Server Error: Couldn't close properly");
          }
          System.exit(0);
          break;
        case "#stop":
          stopListening();
          break;
        case "#close":
          try {
            close();
          } catch (Exception e) {
            System.out.println("Server Error: Couldn't close properly");
          }
          break;
        case "#start":
          if (!isListening()) {
            try {
              listen();
            } catch (Exception e) {
              System.out.println("Server Error: Couldn't start the server.");
            }
          } else {
            System.out.println("Error: Already listening.");
          }
          break;
        case "#getport":
          serverUI.display(Integer.toString(getPort()));
          break;
        default:
          String[] separatedString = message.split(" ");
          if (message.startsWith("#setport")) {
            try {
              if (!isListening()) {
                setPort(Integer.parseInt(separatedString[1]));
              } else {
                System.out.println("Error: need to be disconnected to execute #sethost");
              }
            } catch (Exception e) {
              System.out.println("Invalid command.");
            }
          } else {
            //do nothing
          }
          break;
      }
      // if it isn't a command
    } else {
      serverUI.display(message);
      this.sendToAllClients("SERVER MSG> " + message);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass. Called when a client connects to the server
   * 
   * @param client The connection of the client
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("New Client connected: " + client.toString());
  }

  /**
   * This method overrides the one in the superclass. Called when a client disconnects to the server
   * 
   * @param client The client's connection
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client disconnected");
    super.clientDisconnected(client);
		
	}

  /**
   * This method overrides the one in the superclass. Called when an exception is caught from the client's connection
   * 
   * @param client The connection of the client
   * @param exception The exception caught
   */
  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("Error from client: " + exception);
  }
  
}
//End of EchoServer class
