package com.vlkan.hrrs.example.jaxrs;

import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

public class HelloApplication extends Application {

    public Set<Class<?>> getClasses() {
        return Collections.<Class<?>>singleton(HelloResource.class);
    }

}
