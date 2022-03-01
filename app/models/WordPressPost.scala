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
			  .asOpt[String]
			  .toString
			  // remove html tags
			  .replaceAll("<[^>]*>", "")
			  // string to array
			  .split("[ ,!.]+")
			  // remove numerics
			  .filter((e) => e.forall(Character.isLetter))
		}

		postWords.foreach((word: String) => {
			postWordCount(word) += 1
		})

		postWordCount
	}
}
