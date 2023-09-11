import com.wolox.*;
import com.wolox.parser.*;

def call(String yamlName) {
    def yaml = readYaml file: config.yaml;

    def buildNumber = Integer.parseInt(env.BUILD_ID);
    println(yaml);

    // load project's configuration
    def ConfigParser = new ConfigParser()
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env);
    println(env);

    def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase();

    // build the image specified in the configuration
    def customImage = docker.build(imageName, "--file ${projectConfig.dockerfile} .");

    // adds the last step of the build.
    def closure = buildSteps(projectConfig, customImage);

    // each service is a closure that when called it executes its logic and then calls a closure, the next step.
    projectConfig.services.each {

        closure = "${it.service.getVar()}"(projectConfig, it.version, closure);
    }

    // we execute the top level closure so that the cascade starts.
    try {
        closure([:]);
    } finally{
        deleteDockerImages(projectConfig);
    }
}