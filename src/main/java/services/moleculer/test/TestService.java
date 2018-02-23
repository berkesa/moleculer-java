package services.moleculer.test;

import org.springframework.stereotype.Component;

import services.moleculer.cacher.Cache;
import services.moleculer.eventbus.Listener;
import services.moleculer.eventbus.Subscribe;
import services.moleculer.service.Action;
import services.moleculer.service.Dependencies;
import services.moleculer.service.Middlewares;
import services.moleculer.service.Name;
import services.moleculer.service.Service;
import services.moleculer.service.Version;

@Component
@Name("math")
@Version("2")
@Middlewares({"filter1"})
@Dependencies({"service1", "service2"})
public class TestService extends Service {

	// --- SERVICE INSTANCE CREATED ---
	
	@Override
	public void created() throws Exception {
		System.out.println("CREATED");
	}

	// --- SERVICE STARTED AND PUBLISHED ---
	
	@Override
	public void started() throws Exception {
		System.out.println("STARTED");
	}
	
	// --- PUBLISHED ACTIONS ---
	
	@Name("add")
	@Cache(keys = { "a", "b" }, ttl = 30)
	public Action add = ctx -> {
			
		return ctx.params.get("a", 0) + ctx.params.get("b", 0);
		
	};

	@Name("math.test")
	@Middlewares({"filter2"})
	public Action test = ctx -> {
		return ctx.params.get("a", 0) + ctx.params.get("b", 0);
	};
	
	// --- EVENT LISTENERS ---
	
	@Subscribe("math.*")
	public Listener evt = payload -> {
		System.out.println("RECEIVED: " + payload);
	};
	
	// --- STOP SERVICE INSTANCE ---
	
	@Override
	public void stopped() {
		System.out.println("STOPPED");
	}
	
}