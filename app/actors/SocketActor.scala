package actors

import akka.actor._
import actors.SocketActor.SendData
import play.api.libs.json.JsValue

object SocketActor {
	def props(clientActorRef: ActorRef, master: ActorRef) = Props(new SocketActor(clientActorRef, master))

	/**
	 * sends json data to client
	 * @param data data to send
	 */
	case class SendData(data: JsValue)
}

class SocketActor(clientActorRef: ActorRef, master: ActorRef) extends Actor {
	master ! SocketMaster.AddConnectedActor(self)

	def receive: Receive = {
		case data: JsValue => master ! SocketMaster.Broadcast(data)
		case SendData(data: JsValue) => clientActorRef ! data
	}

}