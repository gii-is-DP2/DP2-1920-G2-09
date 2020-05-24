package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU12CancelOrder extends Simulation {

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
	
	object AdminView {
		val adminView = exec(http("ADMIN VIEW")
			.get("/admin")
			.headers(headers_0))
		.pause(7)
	}
	
	object CancelOrderFromList {
		val  cancelOrderFromList = exec(http("ORDERS LIST")
			.get("/orders/list")
			.headers(headers_0))
		.pause(10)
		.exec(http("CANCEL ORDER")
			.get("/orders/delete/2")
			.headers(headers_0))
		.pause(8)
	}
	
	object CancelOrderFromDetails {
		val cancelOrderFromDetails = exec(http("ORDERS LIST")
			.get("/orders/list")
			.headers(headers_0))
		.pause(10)
		.exec(http("SHOW ORDER DETAILS")
			.get("/orders/3")
			.headers(headers_0))
		.pause(4)
		.exec(http("CANCEL ORDER")
			.get("/orders/delete/3")
			.headers(headers_0))
		.pause(18)
	}

	

	val scn_owner_1 = scenario("HU12CancelOrderFromList").exec(Home.home,Login.login,AdminView.adminView,CancelOrderFromList.cancelOrderFromList)
	val scn_owner_2 = scenario("HU12CancelOrderFromDetails").exec(Home.home,Login.login,AdminView.adminView,CancelOrderFromDetails.cancelOrderFromDetails)
		

	setUp(scn_owner_1.inject(rampUsers(1000) during (100 seconds)),scn_owner_2.inject(rampUsers(1000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}