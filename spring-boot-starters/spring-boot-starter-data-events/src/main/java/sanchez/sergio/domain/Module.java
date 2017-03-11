package sanchez.sergio.domain;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import sanchez.sergio.event.EventService;

/**
 *
 * @author sergio
 */
@Component
public abstract class Module<T extends Aggregate> implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Module.applicationContext = applicationContext;
    }

    public abstract Service<? extends Aggregate, ?> getDefaultService();

    public abstract EventService<?, ?> getDefaultEventService();
}
