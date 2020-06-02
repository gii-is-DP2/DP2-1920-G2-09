package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU09AddProduct extends Simulation {

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
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	object AdminView{
		val adminView = exec(http("ADMIN VIEW")
			.get("/admin")
			.headers(headers_0))
		.pause(17)
	}

	object AddProductOK{
		val addProductOK = exec(http("SHOW PRODUCT")
			.get("/products/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(9)
		.exec(http("PRODUCT ADDED OK")
			.post("/products/new")
			.headers(headers_3)
			.formParam("name", "Gel de foca")
			.formParam("description", "gel quemagrasa para las focas")
			.formParam("stock", "20")
			.formParam("urlImage", "/resources/images/dog.png")
			.formParam("unitPrice", "1")
			.formParam("available", "on")
			.formParam("category", "ACCESORY")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object AddProductFailed{
		val addProductFailed = exec(http("SHOW PRODUCT")
			.get("/products/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(9)
		.exec(http("PRODUCT ADDED FAILED")
			.post("/products/new")
			.headers(headers_3)
			.formParam("name", "")
			.formParam("description", "")
			.formParam("stock", "0")
			.formParam("urlImage", "")
			.formParam("unitPrice", "0.0")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	val scn_owner_1 = scenario("HU09AddProductOK").exec(Home.home,Login.login,AdminView.adminView,AddProductOK.addProductOK)
	val scn_owner_2 = scenario("HU09AddProductFailed").exec(Home.home,Login.login,AdminView.adminView,AddProductFailed.addProductFailed)


	setUp(scn_owner_1.inject(rampUsers(2000) during (100 seconds)),scn_owner_2.inject(rampUsers(2000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )

}