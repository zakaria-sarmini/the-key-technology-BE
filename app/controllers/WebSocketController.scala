package controllers

import actors.{SocketActor, SocketMasterActor}
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import javax.inject.{Inject, Singleton}
import akka.actor.{ActorRef, ActorSystem, Props}

/**
 * application's socket entry
 */
@Singleton
class WebSocketController @Inject() (cc: ControllerComponents)(implicit actorSystem: ActorSystem)
  extends AbstractController(cc)
{
	private val socketMaster: ActorRef = actorSystem.actorOf(Props[SocketMasterActor], "Master")

	/**
	 * connects clients to the socket
	 * @return
	 */
	def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { requestHeader =>
		ActorFlow.actorRef { actorRef =>
			SocketActor.props(actorRef, socketMaster)
		}
	}

	/**
	 * returns the main socket handler
	 * @return
	 */
	def getSocketMaster: ActorRef = {
		socketMaster
	}
}