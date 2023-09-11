import com.wolox.parser.*;

def call(String yamlName) {

	def yaml = readYaml file: yamlName;
	print(env.BUILD_ID);
	def buildNumber = Integer.parseInt(env.BUILD_ID);
	ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env);
	
}