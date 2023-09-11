import com.wolox.parser.*;
import com.wolox.*;

def call(String yamlName) {

	def yaml = readYaml file: yamlName;
	print(env.BUILD_ID);
	def buildNumber = Integer.parseInt(env.BUILD_ID);
	ProjectConfiguration projectConfig = Car.parsec(yaml, env);
	
}