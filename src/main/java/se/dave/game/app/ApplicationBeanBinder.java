package se.dave.game.app;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import se.dave.game.dao.GameDAO;
import se.dave.game.dao.InMemGameDAOImpl;
import se.dave.game.service.GameService;
import se.dave.game.service.GameServiceImpl;

import javax.inject.Singleton;

/**
 * Custom binder for the application
 */
public class ApplicationBeanBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(InMemGameDAOImpl.class).to(GameDAO.class).in(Singleton.class);
        bind(GameServiceImpl.class).to(GameService.class).in(Singleton.class);
    }
}
