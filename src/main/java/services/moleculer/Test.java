package services.moleculer;

import io.datatree.Tree;
import services.moleculer.cachers.MemoryCacher;

public class Test {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		ServiceBroker broker = new ServiceBroker(null, new MemoryCacher(), null, null);

		TestService service = new TestService();
		
		Service svc = broker.createService(service);

		broker.start();

		// ---------

		Tree t = new Tree().put("a", 5);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 3; i++) {
			Object result = broker.call("v2.test.add", t, null, "x");
			System.out.println("RESULT: " + result);
		}
		System.out.println(System.currentTimeMillis() - start);

		// ------------------

		broker.on("user.create", (payload) -> {
			System.out.println("RECEIVED in 'user.create': " + payload);
		});
		broker.on("user.created", (payload) -> {
			System.out.println("RECEIVED in 'user.created': " + payload);
		});
		broker.on("user.*", (payload) -> {
			System.out.println("RECEIVED in 'user.*': " + payload);
		});
		broker.on("post.*", (payload) -> {
			System.out.println("RECEIVED in 'post.*': " + payload);
		});
		broker.on("*", (payload) -> {
			System.out.println("RECEIVED in '*': " + payload);
		});
		broker.on("**", (payload) -> {
			System.out.println("RECEIVED in '**': " + payload);
		});
	}

	@Version("v2")
	public static class TestService extends Service {
		
			// --- CREATED ---

			@Override
			public void created() {

				// Created
				logger.debug("Service created!");

			}

			// --- ACTIONS ---

			public Action list = (ctx) -> {
				return this.processData(ctx.params.get("a", -1));
			};

			@Cache(keys = {"name", "user"})
			public Action add = (ctx) -> {
				return ctx.call("v1.test.list", ctx.params, null);
				// return 2;
			};

			// --- EVENT LISTENERS ---

			// Context, Tree, or Object????
			public Listener test = (input) -> {

			};

			// --- METHODS ---

			int processData(int a) {
				this.logger.info("Process data invoked: " + a);
				return a * 2;
			}

	}
	
}
