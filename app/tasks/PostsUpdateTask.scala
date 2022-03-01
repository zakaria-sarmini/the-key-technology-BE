package tasks

import actors.SocketActor

import javax.inject.Inject
import akka.actor.{ActorSystem, Props}
import controllers.WebSocketController
import models.WordPressPost
import play.api.libs.json.{JsObject, Json}
import play.api.libs.streams.ActorFlow
import services.WordPressService
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PostsUpdateTask @Inject()
(webSocketController: WebSocketController, wpService: WordPressService, wpPost: WordPressPost)
(implicit actorSystem: ActorSystem)
{
	ActorFlow.actorRef { actorRef =>
		val props: Props = SocketActor.props(actorRef, webSocketController.getSocketMaster)
		val socket = actorSystem.actorOf(props)

		// starting scheduled task
		actorSystem.scheduler.scheduleAtFixedRate(initialDelay = 0.seconds, interval = 5.seconds) { () =>
			// getting posts from api
			val postsFuture: Future[List[JsObject]] = wpService.getPosts;

			postsFuture.map(posts => {
				var newPosts: List[JsObject] = List.empty[JsObject]

				// calculating word count
				// @TODO convert to List.map
				posts.foreach((post) => {
					val wordCount = wpPost.calculateWordCount(post)
					val newPost = post.as[JsObject] + ("wordCount" -> Json.toJson(wordCount))
					newPosts ::= newPost
				})

				// broadcasting through socket
				socket ! Json.toJson(newPosts)
			})
		}

		props
	}
}