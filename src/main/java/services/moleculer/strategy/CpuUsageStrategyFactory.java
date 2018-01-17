/**
 * MOLECULER MICROSERVICES FRAMEWORK<br>
 * <br>
 * This project is based on the idea of Moleculer Microservices
 * Framework for NodeJS (https://moleculer.services). Special thanks to
 * the Moleculer's project owner (https://github.com/icebob) for the
 * consultations.<br>
 * <br>
 * THIS SOFTWARE IS LICENSED UNDER MIT LICENSE.<br>
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
package services.moleculer.strategy;

import io.datatree.Tree;
import services.moleculer.ServiceBroker;
import services.moleculer.service.Name;
import services.moleculer.transporter.Transporter;
import services.moleculer.util.CommonUtils;

/**
 * Factory of lowest CPU usage strategy.
 * 
 * @see RoundRobinStrategyFactory
 * @see SecureRandomStrategyFactory
 * @see XORShiftRandomStrategyFactory
 * @see NanoSecRandomStrategyFactory
 */
@Name("Lowest CPU Usage Strategy Factory")
public class CpuUsageStrategyFactory extends ArrayBasedStrategyFactory {

	// --- PROPERTIES ---

	protected int maxTries = 3;
	protected int lowCpuUsage = 10;

	// --- COMPONENTS ---

	protected Transporter transporter;

	// --- CONSTRUCTORS ---

	public CpuUsageStrategyFactory() {
		super(false);
	}

	public CpuUsageStrategyFactory(boolean preferLocal) {
		super(preferLocal);
	}

	public CpuUsageStrategyFactory(boolean preferLocal, int maxTries, int lowCpuUsage) {
		super(preferLocal);
		this.maxTries = maxTries;
		this.lowCpuUsage = lowCpuUsage;
	}

	// --- START INVOCATION STRATEGY ---

	/**
	 * Initializes strategy instance.
	 * 
	 * @param broker
	 *            parent ServiceBroker
	 * @param config
	 *            optional configuration of the current component
	 */
	@Override
	public void start(ServiceBroker broker, Tree config) throws Exception {
		
		// Process config
		maxTries = config.get("maxTries", maxTries);
		lowCpuUsage = config.get("lowCpuUsage", lowCpuUsage);
		
		// Get components
		transporter = broker.components().transporter();
		if (transporter == null) {
			logger.warn(CommonUtils.nameOf(this, true)
					+ " can't work without transporter. Strategy switched to Round-Robin mode.");
		}
	}

	// --- FACTORY METHOD ---

	@Override
	public <T extends Endpoint> Strategy<T> create() {
		if (transporter == null) {
			return new RoundRobinStrategy<T>(preferLocal);
		}
		return new CpuUsageStrategy<T>(preferLocal, maxTries, lowCpuUsage, transporter);
	}

	// --- GETTERS / SETTERS ---
	
	public int getMaxTries() {
		return maxTries;
	}

	public void setMaxTries(int maxTries) {
		this.maxTries = maxTries;
	}

	public int getLowCpuUsage() {
		return lowCpuUsage;
	}

	public void setLowCpuUsage(int lowCpuUsage) {
		this.lowCpuUsage = lowCpuUsage;
	}
	
}