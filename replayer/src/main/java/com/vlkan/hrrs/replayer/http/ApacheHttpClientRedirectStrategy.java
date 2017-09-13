package com.vlkan.hrrs.replayer.http;

import org.apache.http.client.RedirectStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.LaxRedirectStrategy;

public enum ApacheHttpClientRedirectStrategy {

    NONE(null),

    DEFAULT(new DefaultRedirectStrategy()),

    LAX(new LaxRedirectStrategy());

    private final RedirectStrategy implementation;

    ApacheHttpClientRedirectStrategy(RedirectStrategy implementation) {
        this.implementation = implementation;
    }

    public RedirectStrategy getImplementation() {
        return implementation;
    }

}
