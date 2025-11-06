// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    // if the message is a command
    if (message.startsWith("#")) {
      switch(message) {
        case "#quit":
          quit();
          break;
        case "#logoff":
          try {
            closeConnection();
          } catch (IOException io) {}
          break;
        case "#login":
          if (!isConnected()) {
            try {
              openConnection();
            } catch (IOException io) {}
          } else {
            System.out.println("Error: Client already connected to the server.");
          }
          break;
        case "#gethost":
          clientUI.display(getHost());
          break;
        case "#getport":
          clientUI.display(Integer.toString(getPort()));
          break;
        default:
          String[] separatedString = message.split(" ");
          if (message.startsWith("#sethost")) {
            try {
              if (!isConnected()) {
                setHost(separatedString[1]);
              } else {
                System.out.println("Error: need to be disconnected to execute #sethost");
              }
            } catch (ArrayIndexOutOfBoundsException e) {
              System.out.println("Invalid command.");
            }
          } else if (message.startsWith("#setport")) {
            try {
              if (!isConnected()) {
                setPort(Integer.parseInt(separatedString[1]));
              } else {
                clientUI.display("Error: need to be disconnected to execute #setport");
              }
            } catch (Exception e) {
              System.out.println("Invalid command.");
            }
          } else {
            // do nothing
          }
          break;
      }
      // if it isn't a command
    } else {
      try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
    }
  }

  /**
   * This method announces that the connection to the server has ended
   */
  @Override
  protected void connectionClosed() {
    clientUI.display("The connection has ended.");
  }

  /**
   * This method is called when an exception occurs from the connection with the server
   * 
   * @param exception The exception raised
   */
  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("Error:" + exception + " Terminating client.");
    if (isConnected()) {
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
