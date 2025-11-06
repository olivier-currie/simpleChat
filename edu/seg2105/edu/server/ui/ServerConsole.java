package edu.seg2105.edu.server.ui;

import java.util.Scanner;
import edu.seg2105.client.common.*;
import edu.seg2105.edu.server.backend.EchoServer;

/**
 * This class is analoguous to ClientConsole but on the server side.
 * It constructs the UI for the server.
 * It implements ChatIF and activates the display() method
 */
public class ServerConsole implements ChatIF {

    //Class variables**************

    /**
     * Instance of the server
     */
    EchoServer server;

    /**
    * The default port to listen on.
    */
    final public static int DEFAULT_PORT = 5555;

    /**
     * Instance of the scanner
     */
    Scanner fromConsole;

    //Constructor********************

    /**
     * Constructor
     * 
     * @param port The port number
     */
    public ServerConsole(int port) {
        try {
            server = new EchoServer(port, this);
            server.listen();
        } catch (Exception e) {
            System.out.println("Error: Can't setup server!"
                + " Terminating server.");
        System.exit(1);
        }

        fromConsole = new Scanner(System.in);
    }

    //Instance methods****************

    /**
     * This method listens or messages from the console
     */
    public void accept() 
    {
    try
    {

      String message;
        // implement
      while (true) 
      {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
    }

    /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }

  public static void main(String[] args) {

    int p = 0; //Port to listen on

    try
    {
      p = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      p = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole chat = new ServerConsole(p);
    chat.accept();
  }
}
