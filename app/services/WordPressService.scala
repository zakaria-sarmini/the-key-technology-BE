package services

import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSClient
import settings.Config

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class WordPressService @Inject()(ws: WSClient, config: Config) {
	/**
	 * gets the posts from wordpress api
	 * @return a future which resolves to posts
	 */
	def getPosts: Future[List[JsObject]] = {
		ws.url(config.WordPressPostsURL)
		  // get posts with no extra content
		  .addQueryStringParameters("context" -> "embed")
		  .get()
		  .map { response =>
			  Json.parse(response.body.trim).as[List[JsObject]]
		  }
	}
}
