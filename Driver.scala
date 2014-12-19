package parkinglot

import scala.actors.Actor
import scala.actors.Actor._
import scala.util.Random


class Driver(val id: Int, val parkinglot: ParkingLot) extends Actor{
	private val random = new Random()
	var ticket: Ticket = null;	

	def askForAParkingSpace() = {		
		Thread.sleep(random.nextInt(1000))
		parkinglot ! this				
		Thread.sleep(random.nextInt(1000))
	}

	def goToParkingSpace(parkingspace: ParkingSpace) = synchronized {
		if (parkingspace.driver == null){
			synchronized {
				println("[D] Driver ["+ this.id + "] is parking at the ParkingSpace [" + parkingspace.name + "]!")
				parkingspace.driver = this
				Thread.sleep(random.nextInt(1000))
				parkingspace ! this
				notifyArrived(parkingspace)
			}
			Thread.sleep(random.nextInt(25000))
			payTickect()
			parkinglot ! parkingspace
			leaveTheParkingLot()
		}else{
			println("[D] Driver ["+ this.id + "] tried to park at the ParkingSpace [" + parkingspace.name + "], but was occupied. Asking for another ParkingSpace !")				
			askForAParkingSpace() 
		}		

	}

	def notifyArrived(parkingspace: ParkingSpace) ={
		parkinglot ! parkingspace
	}

	def leaveTheParkingLot(){
		Thread.sleep(random.nextInt(1000))
		println("[D] Driver ["+ this.id + "] leaved the ParkingLot !")
		Thread.sleep(random.nextInt(1000))		
	}

	def askForATicket(){
		println("[D] Driver ["+ this.id + "] arrived to the ParkingLot !")
		Thread.sleep(random.nextInt(1000))
		parkinglot ! this		
		Thread.sleep(random.nextInt(1000))
	}

	def payTickect(){
		Thread.sleep(random.nextInt(1000))
		ticket.pay()
		println("[D] Driver ["+ this.id + "] payed the ticket !")		
		Thread.sleep(random.nextInt(1000))				
	}

	def act() = {
		askForATicket()		
    	loop {
      		react {
      			case ticket: Ticket => this.ticket = ticket ; askForAParkingSpace() 
        		case parkingspace: ParkingSpace => goToParkingSpace(parkingspace)
        		case null => leaveTheParkingLot()
      }
    }
  }

}