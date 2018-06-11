package se.dave.game.app;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationResourceConfig extends ResourceConfig {
    public ApplicationResourceConfig() {
        packages("se.dave.game.dao", "se.dave.game.service", "se.dave.game.web");
        register(new ApplicationBeanBinder());
    }
}
