package se.dave.game;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import se.dave.game.app.ApplicationResourceConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the rock paper scissor application
 */
public class RockPaperScissors {
    private static final Logger LOGGER = Logger.getLogger(RockPaperScissors.class.getName());

    public RockPaperScissors() throws Exception {
        Server server = new Server(8080);

        ResourceConfig resourceConfig = new ApplicationResourceConfig();
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder servletHolder = new ServletHolder(servletContainer);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(servletHolder, "/*");

        String resourceBase = this.getClass().getClassLoader().getResource("webapp").toExternalForm();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase(resourceBase);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, servletContextHandler });
        server.setHandler(handlers);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception thrown from server", ex);
        } finally {
            server.destroy();
        }
    }

    public static void main(String[] args) throws Exception {
        new RockPaperScissors();
    }
}
