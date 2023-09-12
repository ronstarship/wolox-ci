import org.jenkinsci.plugins.workflow.libs.Library
@Library('wolox-ci')
import com.wolox.*;
import com.wolox.steps.Step;

def call(ProjectConfiguration projectConfig, def dockerImage) {
    return { variables ->
        List<Step> stepsA = projectConfig.steps.steps
        stepsA.each { step ->
                    stage(step.name) {
                        step.commands.each { command ->
                            print("command---"+command)
                            sh command
                        }
                    }
        }
    }
}
