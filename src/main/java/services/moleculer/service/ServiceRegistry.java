/**
 * This software is licensed under MIT license.<br>
 * <br>
 * Copyright 2017 Andras Berkes [andras.berkes@programmer.net]<br>
 * <br>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:<br>
 * <br>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.<br>
 * <br>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package services.moleculer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.datatree.Tree;
import services.moleculer.ServiceBroker;
import services.moleculer.config.MoleculerComponent;

/**
 * Base superclass of all Service Registry implementations.
 * 
 * @see DefaultServiceRegistry
 */
@Name("Service Registry")
public abstract class ServiceRegistry implements MoleculerComponent {

	// --- LOGGER ---

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// --- INIT SERVICE REGISTRY ---

	/**
	 * Initializes ServiceRegistry instance.
	 * 
	 * @param broker
	 *            parent ServiceBroker
	 * @param config
	 *            optional configuration of the current component
	 */
	@Override
	public void start(ServiceBroker broker, Tree config) throws Exception {
	}

	// --- STOP SERVICE REGISTRY ---

	@Override
	public void stop() {
	}

	// --- RECEIVE REQUEST FROM REMOTE SERVICE ---

	public abstract void receiveRequest(Tree message);

	// --- RECEIVE RESPONSE FROM REMOTE SERVICE ---

	public abstract void receiveResponse(Tree message);

	// --- ADD LOCAL SERVICE ---

	public abstract void addService(Service service, Tree config) throws Exception;

	// --- ADD REMOTE SERVICE ---

	public abstract void addService(Tree config) throws Exception;

	// --- REMOVE ALL REMOTE SERVICES/ACTIONS OF A NODE ---

	public abstract void removeService(String nodeID);

	// --- GET LOCAL SERVICE ---

	public abstract Service getService(String name);

	// --- GET LOCAL OR REMOTE ACTION CONTAINER ---

	public abstract ActionContainer getAction(String name, String nodeID);

	// --- GENERATE SERVICE DESCRIPTOR ---

	public abstract Tree generateDescriptor();
	
}