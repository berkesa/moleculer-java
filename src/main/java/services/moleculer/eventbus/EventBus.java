package services.moleculer.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.moleculer.ServiceBroker;
import services.moleculer.services.Name;
import services.moleculer.utils.MoleculerComponent;

@Name("Event Bus")
public abstract class EventBus implements MoleculerComponent {

	// --- LOGGER ---

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// --- CONSTUCTOR ---

	public EventBus() {
	}

	// --- INIT EVENT BUS ---

	@Override
	public void init(ServiceBroker broker) throws Exception {
	}

	// --- STOP EVENT BUS ---

	@Override
	public void close() {
	}

	// --- REGISTER LISTENER ----

	public abstract void on(String name, Listener listener, boolean once);

	// --- UNREGISTER LISTENER ---

	/**
	 * Unsubscribe from an event
	 * 
	 * @param name
	 * @param listener
	 */
	public abstract void off(String name, Listener listener);

	// --- EMIT EVENT TO LISTENERS ---

	public abstract void emit(String name, Object payload);

}