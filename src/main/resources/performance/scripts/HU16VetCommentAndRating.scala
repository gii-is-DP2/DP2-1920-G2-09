package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU16VetCommentAndRating extends Simulation {

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
			.formParam("username", "vet1")
			.formParam("password", "v3t")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	
	object ListAllProducts {
		val listAllProducts = exec(http("PRODUCTS LIST")
			.get("/products/all")
			.headers(headers_0))
		.pause(15)
	}
	
	
	
	object VetPostCommentWithoutRating {
		val vetPostCommentWithoutRating = exec(http("SHOW PRODUCT")
			.get("/products/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(23)
		.exec(http("POST COMMENT WITHOUT RATING")
			.post("/products/1/add-product-coment/")
			.headers(headers_3)
			.formParam("title", "TITULO DEL COMENTARIO")
			.formParam("description", "DESCRIPCI�N DEL COMENTARIO")
			.formParam("_csrf", "${stoken}"))
		.pause(37)
	}
	
	object VetPostCommentWithRating {
		val vetPostCommentWithRating = exec(http("SHOW PRODUCT")
			.get("/products/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(23)
		.exec(http("POST COMMENT WITH RATING")
			.post("/products/1/add-product-coment/")
			.headers(headers_3)
			.formParam("title", "COMENTARIO CON RATING")
			.formParam("description", "COMENTARIO CON RATING, DESCRIPCI�N Y TITULO")
			.formParam("rating", "5")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
	}

	val scn_owner_1 = scenario("HU16VetPostCommentWithoutRating").exec(Home.home,Login.login,ListAllProducts.listAllProducts,VetPostCommentWithoutRating.vetPostCommentWithoutRating)
	val scn_owner_2 = scenario("HU16VetPostCommentWithRating").exec(Home.home,Login.login,ListAllProducts.listAllProducts,VetPostCommentWithRating.vetPostCommentWithRating)
		

	setUp(scn_owner_1.inject(rampUsers(400) during (10 seconds)),scn_owner_2.inject(rampUsers(400) during (10 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}