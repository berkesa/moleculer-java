package services.moleculer.cachers;

import java.util.List;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import com.lambdaworks.redis.event.Event;
import com.lambdaworks.redis.event.EventBus;
import com.lambdaworks.redis.resource.DefaultClientResources;

import io.datatree.Tree;
import io.datatree.dom.TreeWriter;
import io.datatree.dom.TreeWriterRegistry;
import io.datatree.dom.builtin.JsonBuiltin;
import rx.Observable;
import services.moleculer.ServiceBroker;
import services.moleculer.utils.RedisUtilities;

public class RedisCacher extends Cacher {

	// --- PROPERTIES ---

	protected String[] urls = new String[] { "localhost" };
	protected String password;
	protected boolean useSSL;
	protected boolean startTLS;

	// --- REDIS CLIENTS ---

	protected RedisAsyncCommands<String, String> client;
	protected RedisAdvancedClusterAsyncCommands<String, String> clusteredClient;

	// --- CACHED JSON CONVERTER ---

	protected final TreeWriter writer = TreeWriterRegistry.getWriter(null);

	// --- CONSTUCTORS ---

	public RedisCacher() {
		super(true);
	}

	public RedisCacher(String... urls) {
		this(false, false, urls);
	}

	public RedisCacher(boolean useSSL, boolean startTLS, String... urls) {
		super(true);
		this.urls = urls;
		this.useSSL = useSSL;
		this.startTLS = startTLS;
	}

	public RedisCacher(RedisAsyncCommands<String, String> client) {
		super(true);
		this.client = client;
	}

	public RedisCacher(RedisAdvancedClusterAsyncCommands<String, String> clusteredClient) {
		super(true);
		this.clusteredClient = clusteredClient;
	}

	// --- INIT CACHE INSTANCE ---

	/**
	 * Initializes cacher instance.
	 * 
	 * @param broker
	 */
	public final void init(ServiceBroker broker) throws Exception {
		super.init(broker);
		if (client == null && clusteredClient == null) {

			// Create Redis connection
			List<RedisURI> redisURIs = RedisUtilities.parseURLs(urls, password, useSSL, startTLS);
			DefaultClientResources clientResources = RedisUtilities.createClientResources(new EventBus() {

				@Override
				public final void publish(Event event) {
				}

				@Override
				public final Observable<Event> get() {
					return null;
				}

			});
			if (urls.length > 1) {

				// Clustered client
				clusteredClient = RedisClusterClient.create(clientResources, redisURIs).connect().async();

			} else {

				// Single connection
				client = RedisClient.create(clientResources, redisURIs.get(0)).connect().async();

			}
		}
	}

	// --- CLOSE CACHE INSTANCE ---

	@Override
	public void close() {
		if (client != null) {
			client.close();
			client = null;
		}
		if (clusteredClient != null) {
			clusteredClient.close();
			clusteredClient = null;
		}
	}

	// --- CACHE METHODS ---

	@Override
	public Object get(String key) {
		try {
			RedisFuture<String> future;
			if (client != null) {
				future = client.get(key);
			} else if (clusteredClient != null) {
				future = clusteredClient.get(key);
			} else {
				return null;
			}

			// TODO Do not block thread
			String packet = future.get();
			if (packet == null || "null".equals(packet)) {
				return null;
			}
			if (packet.isEmpty()) {
				return packet;
			}
			final int c = packet.charAt(0);

			// JSON value
			if (c == '{' || c == '[') {
				return new Tree(packet);
			}

			// Scalar value (String, Boolean, etc.)
			return new JsonBuiltin().parse(packet);

		} catch (Exception cause) {
			cause.printStackTrace();
		}
		return null;
	}

	@Override
	public void set(String key, Object value) {

		// Convert Object to JSON
		String json;
		if (value == null) {

			// Null value
			json = "null";

		} else if (value instanceof Tree) {

			// Hierarchial JSON value
			json = ((Tree) value).toString(null, false, true);

		} else {

			// Scalar value (String, Boolean, etc.)
			json = JsonBuiltin.serialize(value, null);

		}

		// Send to Redis
		if (client != null) {
			client.set(key, json);
			return;
		}
		if (clusteredClient != null) {
			clusteredClient.set(key, json);
		}
	}

	@Override
	public void del(String key) {

		// TODO wait for finished state?
		if (client != null) {
			client.del(key);
			return;
		}
		if (clusteredClient != null) {
			clusteredClient.del(key);
		}
	}

	@Override
	public void clean(String match) {
	}

	// --- GETTERS / SETTERS ---

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public boolean isStartTLS() {
		return startTLS;
	}

	public void setStartTLS(boolean startTLS) {
		this.startTLS = startTLS;
	}

	public RedisAsyncCommands<String, String> getClient() {
		return client;
	}

	public void setClient(RedisAsyncCommands<String, String> client) {
		this.client = client;
	}

	public RedisAdvancedClusterAsyncCommands<String, String> getClusteredClient() {
		return clusteredClient;
	}

	public void setClusteredClient(RedisAdvancedClusterAsyncCommands<String, String> clusteredClient) {
		this.clusteredClient = clusteredClient;
	}

}