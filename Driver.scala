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

	def goToParkingSpace(parkingspace: ParkingSpace) = {
		println("[D] Driver ["+ this.id + "] is parking at the ParkingSpace [" + parkingspace.name + "]!")
		parkingspace.driver = this
		Thread.sleep(random.nextInt(1000))
		parkingspace ! this
		notifyArrived(parkingspace)
		Thread.sleep(random.nextInt(3000))
		payTickect()
		parkinglot ! parkingspace
		leaveTheParkingLot()
	}

	def notifyArrived(parkingspace: ParkingSpace) ={
		parkinglot ! parkingspace
	}

	def leaveTheParkingLot(){
		Thread.sleep(random.nextInt(1000))
		println("[D] Driver ["+ this.id + "] leaved to the ParkingLot !")
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
		ticket.pay()w
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