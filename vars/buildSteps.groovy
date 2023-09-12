import org.jenkinsci.plugins.workflow.libs.Library
@Library('wolox-ci')
import com.wolox.*;
import com.wolox.steps.Step;

def call(ProjectConfiguration projectConfig, def dockerImage) {
    return { variables ->
        List<Step> stepsA = projectConfig.steps.steps
        print("stepsA---"+stepsA)
        def links = variables.collect { k, v -> "--link ${v.id}:${k}" }.join(" ")
        print("links---"+links)
        dockerImage.inside(links) {
            stepsA.each { step ->
                stage(step.name) {
                    step.commands.each { command ->
                        print(command)
                        sh command
                    }
                }
            }
        }
    }
}
