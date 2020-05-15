package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU10AddPrescription extends Simulation {

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
	
	object FindAllOwners {
		val findAllOwners = exec(http("FIND OWNERS")
			.get("/owners/find")
			.headers(headers_0))
		.pause(3)
		.exec(http("FIND ALL OWNERS")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(8)
	}
	
	object ShowOwner {
		val showOwner = exec(http("HU10AddPrescription_6")
			.get("/owners/1")
			.headers(headers_0))
		.pause(6)
	}
	
	object  AddPrescriptionOk {
		val addPrescriptionOk = exec(http("ADD PRESCRIPTION FORM ")
			.get("/owners/1/pets/1/prescriptions/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(40)
		.exec(http("ADD PRESCRIPTION OK")
			.post("/owners/1/pets/1/prescriptions/new")
			.headers(headers_3)
			.formParam("dateInicio", "2020/08/07")
			.formParam("dateFinal", "2020/09/11")
			.formParam("name", "Javier Romero")
			.formParam("description", "Gel de ba�o para perros Gel de ba�o para perros")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}
	
	object AddPrescriptionFail {
		val addPrescriptionFail = exec(http("ADD PRESCRIPTION FORM ")
			.get("/owners/1/pets/1/prescriptions/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(1)
		.exec(http("ADD PRESCRIPTION FAIL")
			.post("/owners/1/pets/1/prescriptions/new")
			.headers(headers_3)
			.formParam("dateInicio", "")
			.formParam("dateFinal", "")
			.formParam("name", "")
			.formParam("description", "")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	val scn_owner_1 = scenario("HU10AddPrescriptionsOk").exec(Home.home,Login.login,FindAllOwners.findAllOwners,ShowOwner.showOwner,AddPrescriptionOk.addPrescriptionOk)
	val scn_owner_2 = scenario("HU10AddPrescriptionsFail").exec(Home.home,Login.login,FindAllOwners.findAllOwners,ShowOwner.showOwner,AddPrescriptionFail.addPrescriptionFail)
		

	setUp(scn_owner_1.inject(atOnceUsers(1)),scn_owner_2.inject(atOnceUsers(1))).protocols(httpProtocol)
}