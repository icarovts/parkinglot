package parkinglot

import scala.actors.Actor
import scala.actors.Actor._
import scala.util.Random

case object LeaveParkingSpace

class ParkingLot(numberSpaces: Int) extends Actor {

	val random = new Random()

	val parkingSpaces = for(i <- (1 to numberSpaces).toList) yield new ParkingSpace("V" + i)

	parkingSpaces.map(ps => ps.start())


	def receiveDriver(driver: Driver){
		if(emptyParkingSpaces.isEmpty){
			println("[PL] No free ParkingSpace was found for the driver [" + driver.id + "]. His ticket was canceled !")
			driver ! null
		}
		else {
			Thread.sleep(random.nextInt(1000))
			driver ! emptyParkingSpaces.head
		}
	}

	def generateTicket(driver: Driver){
		println("[PL] Genereting ticket for the driver [" + driver.id + "]")
		Thread.sleep(random.nextInt(1000))
		val ticket = new Ticket();
		ticket.start()
		Thread.sleep(random.nextInt(1000))
		driver ! ticket
	}

	def confirmParking(parkingspace: ParkingSpace){
		println("[PL] Driver [" + parkingspace.driver.id + "] confirmed that it's parked at ParkingSpace [" + parkingspace.name + "]")		
	}

	def freeParkingSpace(parkingspace: ParkingSpace){		
		println("[PL] Driver [" + parkingspace.driver.id + "] leaved the ParkingSpace [" + parkingspace.name + "]")			
		parkingspace.driver = null
	}

	def emptyParkingSpaces: List[ParkingSpace] = parkingSpaces.filter(ps => ps.isEmpty)
	def occupiedParkingSpaces: List[ParkingSpace] = parkingSpaces.filterNot(ps => ps.isEmpty)
	def parkedDrivers: List[Driver] = occupiedParkingSpaces.map(ps => ps.driver)

	def act() = {

		loop {
			react {
				case driver: Driver if (driver.ticket == null) => generateTicket(driver)
				case driver: Driver if (driver.ticket != null) => receiveDriver(driver)
				case parkingspace: ParkingSpace if(!parkingspace.driver.ticket.isPayed) => confirmParking(parkingspace)
				case parkingspace: ParkingSpace if(parkingspace.driver.ticket.isPayed) => freeParkingSpace(parkingspace)
			}
		}

	}

}