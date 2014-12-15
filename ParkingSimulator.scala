package parkinglot

import scala.actors.Actor._
import scala.util.Random

object ParkingSimulator { 

	def main(args: Array[String]){

		val drivers = List[Driver]()	
		val random = new Random()
		val parkinglot = new ParkingLot(args(0).toInt)
		var i = 0

		println("[!] Starting parkinglot simulation")
		parkinglot.start()		
		println("[!] Generating drivers for the simulation")

		while(true){
			i = i + 1
			val driver = new Driver(i,parkinglot)
			driver.start()			
			driver :: drivers
			Thread.sleep(10000)
		}

		System.exit(0)
	}

}