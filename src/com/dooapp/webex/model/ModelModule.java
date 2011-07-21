package com.dooapp.webex.model;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 15:56
 */
public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ProjectManager.class).in(Singleton.class);
        bind(TimeEntryManager.class).in(Singleton.class);
    }

}
