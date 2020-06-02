package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU14EditWalk extends Simulation {

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
	
	object WalksList {
		val walksList = exec(http("WALKS LIST")
			.get("/walks/all")
			.headers(headers_0))
		.pause(11)
	}
	
	object ShowWalk {
		val showWalk = exec(http("SHOW WALK")
			.get("/walks/1")
			.headers(headers_0))
		.pause(9)
	}
	
	object EditWalkOK{
		val editWalkOK = exec(http("EDIT WALK FORM")
			.get("/walks/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("EDIT WALK OK")
			.post("/walks/1/edit")
			.headers(headers_3)
			.formParam("name", "Primer Paseo")
			.formParam("description", "Esto es un paseo1")
			.formParam("map", "https://ev.us.es/webapps/osv-kaltura-BB5ac734ed505df/images/videoContent50.png")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}
	
	object EditWalkFail {
		val editWalkFail = exec(http("EDIT WALK FORM")
			.get("/walks/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(6)
		.exec(http("EDIT WALK OK FAIL")
			.post("/walks/1/edit")
			.headers(headers_3)
			.formParam("name", "")
			.formParam("description", "")
			.formParam("map", "")
			.formParam("_csrf", "${stoken}"))
		.pause(5)
		
	}

	val scn_owner_1 = scenario("HU14EditWalkOK").exec(Home.home,Login.login,WalksList.walksList,ShowWalk.showWalk,EditWalkOK.editWalkOK)
	val scn_owner_2 = scenario("HU14EditWalkFail").exec(Home.home,Login.login,WalksList.walksList,ShowWalk.showWalk,EditWalkFail.editWalkFail)
		

	setUp(scn_owner_1.inject(rampUsers(1500) during (100 seconds)),scn_owner_2.inject(rampUsers(1500) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}