package io.jrocket.infra.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ApplicationBootstrap {

    private static Logger logger = LoggerFactory.getLogger(ApplicationBootstrap.class);

    @Inject
    DataGenerator dataGenerator;

    @PostConstruct
    public void bootstrap() {
        dataGenerator.populateData();
    }

}
