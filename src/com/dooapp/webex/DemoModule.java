package com.dooapp.webex;

import com.dooapp.webex.com.dooapp.webex.model.Project;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 16:14
 */
public class DemoModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    protected Project[] createProjects() {
        return new Project[] {
                new Project("Project 1"),
                new Project("Project 2")
        };
    }

}
