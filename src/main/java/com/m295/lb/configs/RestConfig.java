package com.m295.lb.configs;

import com.m295.lb.filters.AuthenticationFilter;
import com.m295.lb.models.Book;
import com.m295.lb.models.Lending;
import com.m295.lb.services.BookController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationPath("/resources")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(
                List.of(PersistenceJPAConfig.class, AuthenticationFilter.class,
                        Book.class, Lending.class, BookController.class));

    }
}

