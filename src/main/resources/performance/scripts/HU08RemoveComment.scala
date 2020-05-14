package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU08RemoveComment extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.jpg""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("HOME")
			.get("/")
			.headers(headers_0))
		.pause(14)
	}
	object Login {
		val login = exec(http("LOGIN")
			.get("/login")
			.headers(headers_0)
			.resources(http("LOGIN")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(3)
		.exec(http("LOGGED")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	
	object ListProducts{
		val listProducts = exec(http("LIST PRODUCTS")
			.get("/products/all")
			.headers(headers_0))
		.pause(14)
	}
	
	object ShowProduct1 {
		val showProduct1 = exec(http("SHOW PRODUCT1")
			.get("/products/1")
			.headers(headers_0))
		.pause(11)
	}
	
	object RemoveComment {
		val removeComment = exec(http("REMOVE COMMENT")
			.get("/products/1/delete-product-coment/1")
			.headers(headers_0))
		.pause(33)
	
	}
	

	val scn_owner_1 = scenario("HU06PostCommentWithouRate").exec(Home.home,Login.login,ListProducts.listProducts,ShowProduct1.showProduct1,RemoveComment.removeComment)
	
		

	setUp(scn_owner_1.inject(atOnceUsers(1))).protocols(httpProtocol)
}