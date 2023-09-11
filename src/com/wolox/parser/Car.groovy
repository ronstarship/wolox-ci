package com.wolox.parser;

class Car {
  int numberOfDoors
  String brand
  String model
  def script
  
	Car(script) {
		this.script = script
	}
	
  	void printOutCar() {
		echo "using script.echo"
  		script.echo "numberOfDoors:${numberOfDoors}"    	
		script.echo "brand:${brand}"    	
		script.echo "model:${model}"
  	}  
  
}