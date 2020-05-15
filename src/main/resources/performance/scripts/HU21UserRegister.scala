package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU21UserRegister extends Simulation {

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
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")
	
	object Home {
		val home = exec(http("HOME")
			.get("/")
			.headers(headers_0))
		.pause(14)
	}
	
	object UserRegisterOk {
		val userRegisterOK = 
		exec(http("REGISTER FORM")
			.get("/users/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(28)
		.exec(http("REGISTRATION OK")
			.post("/users/new")
			.headers(headers_2)
			.formParam("firstName", "usuario1")
			.formParam("lastName", "usuario1")
			.formParam("address", "usuario1")
			.formParam("city", "usuario1")
			.formParam("telephone", "911911911")
			.formParam("user.username", "usuario1")
			.formParam("user.password", "usuario1")
			.formParam("user.email", "usuario@gmail.com")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}
	
	object LoginAfterRegister {
		val loginAfterRegister = exec(http("LOGIN")
			.get("/login")
			.headers(headers_0)
			.resources(http("LOGIN")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(5)
		.exec(http("LOGGED")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "usuario1")
			.formParam("password", "usuario1")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	} 
	
	object SavePaymentDetails {
		val savePaymentDetails = exec(http("PAYMENT DETAILS FORM")
			.get("/owners/payment-details")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(9)
		.exec(http("SAVE PAYMENT DETAIL")
			.post("/owners/payment-details")
			.headers(headers_2)
			.formParam("creditCardNumber", "1111222233334444")
			.formParam("cvv", "122")
			.formParam("expirationMonth", "3")
			.formParam("expirationYear", "2039")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}
	
	object UserRegisterFail {
		val userRegisterFail = exec(http("REGISTER FORM")
			.get("/users/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(2)
		.exec(http("REGISTER FAIL")
			.post("/users/new")
			.headers(headers_2)
			.formParam("firstName", "")
			.formParam("lastName", "")
			.formParam("address", "")
			.formParam("city", "")
			.formParam("telephone", "")
			.formParam("user.username", "")
			.formParam("user.password", "")
			.formParam("user.email", "")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}
	
	val scn_owner_1 = scenario("HU21UserRegisterAndPaymentDetails").exec(Home.home,UserRegisterOk.userRegisterOK,LoginAfterRegister.loginAfterRegister,SavePaymentDetails.savePaymentDetails)
	val scn_owner_2 = scenario("HU21UserRegisterFail").exec(Home.home,UserRegisterFail.userRegisterFail)
	
	setUp(scn_owner_1.inject(atOnceUsers(1)),scn_owner_2.inject(atOnceUsers(1))).protocols(httpProtocol)
	
}