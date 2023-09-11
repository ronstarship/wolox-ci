import com.wolox.parser.*;

def call(String yamlName) {

	def yaml = readYaml file: yamlName;
	print(yaml.environment)
}