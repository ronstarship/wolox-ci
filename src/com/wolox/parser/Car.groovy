package com.wolox.parser;

import com.wolox.ProjectConfiguration;
import com.wolox.docker.DockerConfiguration;
import com.wolox.services.*;
import com.wolox.steps.*;

class Car {

    private static String LATEST = 'latest';
    private static Integer DEFAULT_TIMEOUT = 600;   // 600 seconds

    static ProjectConfiguration parsec(def yaml, def env) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildNumber = env.BUILD_ID;

        // parse the environment variables and jenkins environment variables to be passed
        projectConfiguration.environment = parseEnvironment(yaml.environment, yaml.jenkinsEnvironment, env);

        // add Build Number environment variables
        projectConfiguration.environment.add("BUILD_ID=${env.BUILD_ID}");

        // add SCM environment variables
        projectConfiguration.environment.add("BRANCH_NAME=${env.BRANCH_NAME.replace('origin/','')}");
        projectConfiguration.environment.add("CHANGE_ID=${env.CHANGE_ID}");

        if (env.CHANGE_ID) {
            projectConfiguration.environment.add("CHANGE_BRANCH=${env.CHANGE_BRANCH}");
            projectConfiguration.environment.add("CHANGE_TARGET=${env.CHANGE_TARGET}");
        }

        // parse the execution steps
        projectConfiguration.steps = parseSteps(yaml.steps);

        // parse the necessary services
        projectConfiguration.services = parseServices(yaml.services);

        // load the dockefile
        projectConfiguration.dockerfile = parseDockerfile(yaml.config);

        // load the project name
        projectConfiguration.projectName = parseProjectName(yaml.config);

        projectConfiguration.env = env;

        projectConfiguration.dockerConfiguration = new DockerConfiguration(projectConfiguration: projectConfiguration);

        projectConfiguration.timeout = yaml.timeout ?: DEFAULT_TIMEOUT;

        return projectConfiguration;
    }
}