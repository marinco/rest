/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rassus.dz.first;

/**
 *
 * @author Jakov
 */
public interface ServerInterface {
    
        // Server startup. Starts all services offered by the server.
	public void startup();

	// Server loops when in running mode. The server must be active
	// to accept client requests.
	public void loop();

	// Server shutdown. Shuts down all services started during
	// startup.
	public void shutdown();

}
