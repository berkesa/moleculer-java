/**
 * THIS SOFTWARE IS LICENSED UNDER MIT LICENSE.<br>
 * <br>
 * Copyright 2017 Andras Berkes [andras.berkes@programmer.net]<br>
 * Based on Moleculer Framework for NodeJS [https://moleculer.services].
 * <br><br>
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
package services.moleculer.error;

/**
 * 'Service not available' error message.
 */
public class ServiceNotAvailable extends MoleculerRetryableException {

	// --- SERIAL VERSION UID ---

	private static final long serialVersionUID = 5711144461029539583L;

	// --- PROPERTIES ---

	protected final String action;

	protected final String nodeID;

	// --- CONSTRUCTOR ---

	public ServiceNotAvailable(String action, String nodeID) {
		super(nodeID == null ? "Action \"" + action + "\" is not available."
				: "Service \"" + action + "\" is not available on \"" + nodeID + "\" node.", null, 404, null,
				"action", action, "nodeID", nodeID);
		this.action = action;
		this.nodeID = nodeID;
	}

	// --- PROPERTY GETTERS ---

	public String getAction() {
		return action;
	}

	public String getNodeID() {
		return nodeID;
	}

}