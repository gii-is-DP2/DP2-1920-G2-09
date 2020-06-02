package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU23AddWalkComment extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.jpg""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
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

	object ListWalks{
		val listWalks = exec(http("LIST WALKS")
			.get("/walks/all")
			.headers(headers_0))
		.pause(35)
	}
		
	object PostCommentWithoutRate{
		val postCommentWithoutRate = exec(http("SHOW WALK")
			.get("/walks/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
		.exec(http("COMMENT WITHOUT RATE")
			.post("/walks/1/add-walk-coment/")
			.headers(headers_2)
			.formParam("title", "comentario")
			.formParam("description", "esto es un comentario")
			.formParam("_csrf", "${stoken}"))
		.pause(30)
	}	
	
	object PostCommentWithRate {
		val postCommentWithRate = exec(http("SHOW WALK")
			.get("/walks/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
		.exec(http("COMMENT WITH RATE")
			.post("/walks/1/add-walk-coment/")
			.headers(headers_2)
			.formParam("title", "comentario2")
			.formParam("description", "esto es un segundo comentario")
			.formParam("rating", "3")
			.formParam("_csrf", "${stoken}"))
		.pause(21)
	}
		

	val scn_owner_1 = scenario("HU23AddCommentWithouRate").exec(Home.home,Login.login,ListWalks.listWalks,PostCommentWithoutRate.postCommentWithoutRate)
	val scn_owner_2 = scenario("HU23AddCommentWithRate").exec(Home.home,Login.login,ListWalks.listWalks,PostCommentWithRate.postCommentWithRate)	
		

	setUp(scn_owner_1.inject(rampUsers(1000) during (10 seconds)),scn_owner_2.inject(rampUsers(1000) during (10 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}