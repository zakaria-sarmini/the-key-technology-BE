package models

import play.api.libs.json.JsValue

import scala.collection.mutable

class WordPressPost {
	/**
	 * calculates wordpress's post word count
	 * @param post word press post
	 * @return word count map
	 */
	def calculateWordCount(post: JsValue): mutable.Map[String, Int] = {
		var postWordCount: mutable.Map[String, Int] = mutable.Map.empty[String, Int].withDefaultValue(0)

		var postWords: Array[String] = {
			// retrieve post content
			(post \ "excerpt" \ "rendered")
			  .get
			  .toString
			  // remove html tags, html entities and special characters
			  .replaceAll(
				  "<[^>]*>|(&.+;)|[!@#$%^&*„“;\")(\\[\\]]|(\\\\r\\\\n)+|\\\\r+|\\\\n+|\\\\t+",
				  ""
			  )
			  // string to array
			  .split("[ ,!.]+")
			  // remove numerics
			  .filter((e) => !e.forall(Character.isDigit))
		}

		postWords.foreach((word: String) => {
			postWordCount(word.toLowerCase()) += 1
		})

		postWordCount
	}
}
