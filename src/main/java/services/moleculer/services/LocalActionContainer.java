package services.moleculer.services;

import java.util.Objects;

import io.datatree.Tree;
import services.moleculer.Promise;
import services.moleculer.ServiceBroker;
import services.moleculer.context.CallingOptions;
import services.moleculer.utils.CommonUtils;

public final class LocalActionContainer implements ActionContainer {

	// --- PROPERTIES ---

	private final String name;
	private final String nodeID;
	private final boolean cached;
	private final String[] cacheKeys;

	// --- LOCAL ACTION ---

	private final Action action;

	// --- COMPONENTS ---

	private ServiceRegistry serviceRegistry;

	// --- CONSTRUCTOR ---

	public LocalActionContainer(ServiceBroker broker, Tree parameters, Action instance) {

		// Set name
		String n = parameters.get("name", "");
		if (n.isEmpty()) {
			n = CommonUtils.nameOf(instance);
		}
		name = n;

		// Set nodeID
		nodeID = broker.nodeID();
		Objects.nonNull(nodeID);

		// Set action
		action = instance;
		Objects.nonNull(action);

		// Set cache parameters
		cached = parameters.get("cached", false);
		cacheKeys = parameters.get("cacheKeys", "").split(",");
	}

	// --- INVOKE LOCAL SERVICE ---

	@Override
	public final Promise call(Tree params, CallingOptions opts) {
		return serviceRegistry.call(action, params, opts);
	}

	// --- PROPERTY GETTERS ---

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final String nodeID() {
		return nodeID;
	}

	@Override
	public final boolean local() {
		return true;
	}

	@Override
	public final boolean cached() {
		return cached;
	}

	@Override
	public final String[] cacheKeys() {
		return cacheKeys;
	}

}