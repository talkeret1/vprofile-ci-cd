package com.visualpathit.account.utils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;

import net.spy.memcached.MemcachedClient;

@Service
public class MemcachedUtils {

	private static Components object;

	@Autowired
	public void setComponents(Components object) {
		MemcachedUtils.object = object;
	}

	public static String memcachedSetData(User user, String key) {

		int expireTime = 900;

		MemcachedClient client = null;

		try {
			client = memcachedConnection();

			if (client == null) {
				return "Cache unavailable";
			}

			Future<?> future = client.set(key, expireTime, user);

			future.get();

			return "Data is From DB and Data Inserted In Cache !!";

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return "Cache set interrupted";

		} catch (Exception e) {
			return "Cache set failed: " + e.getMessage();

		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	public static User memcachedGetData(String key) {

		MemcachedClient client = null;

		try {
			client = memcachedConnection();

			if (client == null) {
				return null;
			}

			return (User) client.get(key);

		} catch (Exception e) {
			return null;

		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	public static MemcachedClient memcachedConnection() {

		if (object == null) {
			return null;
		}

		String activeHost = object.getActiveHost();
		String activePort = object.getActivePort();

		try {
			if (isNotEmpty(activeHost, activePort)) {

				MemcachedClient client = new MemcachedClient(
						new InetSocketAddress(activeHost, Integer.parseInt(activePort)));

				if (isClientValid(client)) {
					return client;
				}

				client.shutdown();
			}

		} catch (Exception ignored) {
			// fallback below
		}

		return standByMemcachedConn();
	}

	public static MemcachedClient standByMemcachedConn() {

		if (object == null) {
			return null;
		}

		String host = object.getStandByHost();
		String port = object.getStandByPort();

		if (!isNotEmpty(host, port)) {
			return null;
		}

		try {
			MemcachedClient client = new MemcachedClient(new InetSocketAddress(host, Integer.parseInt(port)));

			if (isClientValid(client)) {
				return client;
			}

			client.shutdown();

		} catch (Exception ignored) {
		}

		return null;
	}

	private static boolean isClientValid(MemcachedClient client) {

		if (client == null) {
			return false;
		}

		try {
			String key = "pid";

			for (SocketAddress addr : client.getStats().keySet()) {
				String port = client.getStats().get(addr).get(key);

				if (port != null && !port.isEmpty()) {
					return true;
				}
			}

		} catch (Exception ignored) {
		}

		return false;
	}

	private static boolean isNotEmpty(String host, String port) {
		return host != null && !host.isEmpty()
				&& port != null && !port.isEmpty();
	}
}