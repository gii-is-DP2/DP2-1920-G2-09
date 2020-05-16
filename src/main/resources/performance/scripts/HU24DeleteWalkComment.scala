package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU24DeleteWalkComment extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.jpg""", """.*.css""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
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
	
	object ListWalks{
		val listWalks = exec(http("LIST WALKS")
			.get("/walks/all")
			.headers(headers_0))
		.pause(35)
	}

	object DeleteWalkComment{
		val deleteWalkComment = exec(http("SHOW WALK")
			.get("/walks/1")
			.headers(headers_0))
		.pause(11)
		.exec(http("DELETE WALK COMMENT")
			.get("/walks/1/walkComents/4/delete")
			.headers(headers_0))
		.pause(30)//58
	}
	
	object DeleteOtherWalkComment{
		val deleteOtherWalkComment= exec(http("SHOW WALK")
			.get("/walks/2")
			.headers(headers_0))
		.pause(10)
		.exec(http("DELETE OTHER WALK COMMENT")
			.get("/walks/2/walkComents/2/delete")
			.headers(headers_0))
		.pause(16)
	}
		

	val scn_owner_1 = scenario("HU24DeleteWalkComment").exec(Home.home,Login.login,ListWalks.listWalks,DeleteWalkComment.deleteWalkComment)
	val scn_owner_2 = scenario("HU24DeleteOtherWalkComment").exec(Home.home,Login.login,ListWalks.listWalks,DeleteOtherWalkComment.deleteOtherWalkComment)	
		

	setUp(scn_owner_1.inject(atOnceUsers(1)),scn_owner_2.inject(atOnceUsers(1))).protocols(httpProtocol)
}