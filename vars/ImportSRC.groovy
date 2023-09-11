import com.wolox.parser.*;

def call(String yamlName) {
    def myCar = new Car(script)
	myCar.numberOfDoors = 5
	myCar.brand = "123"
	myCar.model = "abc"
	myCar.printOutCar()
	def yaml = readYaml file: yamlName;
}