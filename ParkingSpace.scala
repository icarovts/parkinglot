package parkinglot

import scala.actors.Actor
import scala.actors.Actor._
import scala.util.Random

class ParkingSpace(val name: String) extends Actor {
	var driver: Driver = null;

  def isEmpty: Boolean = this.driver == null

  def act() = {
    loop {
      react {
        case driver: Driver => this.driver = driver//; println("[PS] parkingSpace [" + this.name+ "] was occupied by the driver [" + this.driver.id + "] ")
        case _ => this.driver = null
      }
    }
  }
}