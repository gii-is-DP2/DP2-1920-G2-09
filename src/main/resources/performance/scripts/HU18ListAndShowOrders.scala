package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU18ListAndShowOrders extends Simulation {

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
		.pause(5)
		.exec(http("LOGGED")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}
	
	object AdminView {
		val adminView = exec(http("ADMIN VIEW")
			.get("/admin")
			.headers(headers_0))
		.pause(5)
	}
	
	object OrdersList {
		val ordersList = exec(http("ORDERS LIST")
			.get("/orders/list")
			.headers(headers_0))
		.pause(9)
	}
	
	object ShowOrder {
		val showOrder = exec(http("SHOW ORDER")
			.get("/orders/1")
			.headers(headers_0))
		.pause(3)
	}
	
	val scn_owner_1 = scenario("HU18ListOrder").exec(Home.home,Login.login,AdminView.adminView,OrdersList.ordersList)
	val scn_owner_2 = scenario("HU18ShowOrder").exec(Home.home,Login.login,AdminView.adminView,OrdersList.ordersList,ShowOrder.showOrder)
	
	setUp(scn_owner_1.inject(atOnceUsers(1)),scn_owner_2.inject(atOnceUsers(1))).protocols(httpProtocol)
}