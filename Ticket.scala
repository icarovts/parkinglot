package parkinglot

import scala.actors.Actor
import scala.actors.Actor._
import scala.util.Random

class Ticket extends Actor {

  var isPayed = false

  def act() = {
    loop {
      react {
        case payed: Boolean => this.isPayed = payed
        case _ => null
      }
    }
  }
}