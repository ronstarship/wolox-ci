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

    // build the image specified in the configuration
    def dockerImage = docker.build(imageName, "${registry} --file ${projectConfig.dockerfile} .");

    def registryCredential = 'dockerhub'

    docker.withRegistry( '', registryCredential ) {
    dockerImage.push('latest')
    }
    // adds the last step of the build.
    def closure = buildSteps(projectConfig);
 
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
