import org.jenkinsci.plugins.workflow.libs.Library
@Library('wolox-ci')
import com.wolox.parser.ConfigParser;
import com.wolox.*;

def call(String yamlName) {
    def yaml = readYaml file: yamlName;

    def buildNumber = Integer.parseInt(env.BUILD_ID);
    
    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env);

    def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase();
    def registry = "ronstarship/basicreact"
    def VERSION = "0.0.1"
    def registryCredential = 'dockerhub'

    // adds the last step of the build.
    def closure = buildSteps(projectConfig);
 
    // each service is a closure that when called it executes its logic and then calls a closure, the next step.
    projectConfig.services.each {

        closure = "${it.service.getVar()}"(projectConfig, it.version, closure);

    }

    // we execute the top level closure so that the cascade starts.
    try {
        closure([:]);

    // build the image specified in the configuration
    //def dockerImage = docker.build("-t $registry+:$BUILD_NUMBER --build-arg ARTIFACT_NAME=demo-$VERSION-SNAPSHOT.jar --build-arg APP_PORT=8080 "," --file ${projectConfig.dockerfile} .");
    def dockerImage = docker.build registry + ":${BUILD_NUMBER}"+ "--build-arg ARTIFACT_NAME=demo-1.0.0-SNAPSHOT.jar --build-arg APP_PORT=8080 "
    

    docker.withRegistry( '', registryCredential ) {
    dockerImage.push("$BUILD_NUMBER")
    }

    } finally{
        deleteDockerImages(projectConfig);
    }
}
