package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU06RateProduct extends Simulation {

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
			.formParam("username", "prueba")
			.formParam("password", "prueba")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	
	object ListProducts{
		val listProducts = exec(http("LISTPRODUCTS")
			.get("/products/all")
			.headers(headers_0))
		.pause(14)
	}
	
	object RateProduct1 {
	 val rateProduct1 = exec(http("SHOWPRODUCT 1")
			.get("/products/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(2)
		.exec(http("RATE PRODUCT 1")
			.post("/products/1/add-product-coment/")
			.headers(headers_3)
			.formParam("title", "")
			.formParam("description", "")
			.formParam("rating", "3")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}
	
	object RateProduct2 {
		val rateProduct2 = exec(http("SHOWPRODUCT 2")
			.get("/products/2")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(28)
		.exec(http("RATE PRODUCT 2")
			.post("/products/2/add-product-coment/")
			.headers(headers_3)
			.formParam("title", "")
			.formParam("description", "")
			.formParam("rating", "5")
			.formParam("_csrf", "${stoken}"))
		.pause(81)
	}
	
	val scn_owner_1 = scenario("HU06RateProduct1").exec(Home.home,Login.login,ListProducts.listProducts,RateProduct1.rateProduct1)
	val scn_owner_2 = scenario("HU06RateProduct2").exec(Home.home,Login.login,ListProducts.listProducts,RateProduct2.rateProduct2)	
		

	setUp(scn_owner_1.inject(rampUsers(3000) during (100 seconds)),scn_owner_2.inject(rampUsers(3000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}