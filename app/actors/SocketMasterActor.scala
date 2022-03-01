package actors

import actors.SocketMaster._
import akka.actor.{Actor, ActorRef}
import play.api.libs.json.JsValue

class SocketMasterActor extends Actor {
	private var connectedActors = List.empty[ActorRef]

	def receive: Receive = {
		case AddConnectedActor(connectedActor) => connectedActors ::= connectedActor
		case Broadcast(data: JsValue) => for(ca <- connectedActors) ca ! SocketActor.SendData(data)
	}
}

object SocketMaster {
	/**
	 * registers connect client to the socket master
	 * @param actor client actor retrieved when connecting to socket
	 */
	case class AddConnectedActor(actor: ActorRef)

	/**
	 * broadcasts data to all connected clients
	 * @param data data to broadcast
	 */
	case class Broadcast(data: JsValue)
}
