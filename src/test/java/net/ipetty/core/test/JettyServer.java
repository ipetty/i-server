package net.ipetty.core.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 启动内嵌Jetty服务器并运行本Web应用
 * 
 * @author luocanfeng
 * @date 2014年3月22日
 */
public class JettyServer {

	private static final int PORT = 8080;
	private static final String CONTEXT_PATH = "/";
	private static final String DEFAULT_WEBAPP_PATH = "webapp";

	private static Server server;

	public static void start() throws Exception {
		server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(PORT);
		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });

		WebAppContext webapp = new WebAppContext(DEFAULT_WEBAPP_PATH, CONTEXT_PATH);
		server.setHandler(webapp);

		server.start();
		// server.join();
	}

	public static void stop() throws Exception {
		if (server != null) {
			server.stop();
			server = null;
		}
	}

	public static void main(String[] args) throws Exception {
		start();
	}

}
