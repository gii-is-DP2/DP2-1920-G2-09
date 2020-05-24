package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU25UpdateProduct extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.jpg""", """.*.css""", """.*.js"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")
		
	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")	

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"

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
	
	object ListProducts{
		val listProducts = exec(http("LIST PRODUCTS")
			.get("/products/all")
			.headers(headers_0))
		.pause(18)
	}
	
	object ShowProduct{
		val showProduct = exec(http("SHOW PRODUCT")
			.get("/products/2")
			.headers(headers_0))
		.pause(7)
	}
	
	object UpdateProductOk{
		val updateProductOk = exec(http("SHOW FORM")
			.get("/products/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("UPDATE PRODUCT OK")
			.post("/products/2/edit")
			.headers(headers_3)
			.formParam("name", "Gel de gato")
			.formParam("description", "Es un gel de gato muy bonito y bueno")
			.formParam("stock", "150")
			.formParam("urlImage", "/resources/images/dog.png")
			.formParam("unitPrice", "102.0")
			.formParam("available", "on")
			.formParam("category", "HYGIENE")
			.formParam("_csrf", "${stoken}"))
		.pause(31)
	}
	
	object UpdateProductFailed{
		val updateProductFailed = exec(http("SHOW FORM")
			.get("/products/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("UPDATE PRODUCT FAILED")
			.post("/products/2/edit")
			.headers(headers_3)
			.formParam("name", "Gel de gato")
			.formParam("description", "")
			.formParam("stock", "150")
			.formParam("urlImage", "/resources/images/dog.png")
			.formParam("unitPrice", "102.0")
			.formParam("available", "on")
			.formParam("category", "HYGIENE")
			.formParam("_csrf", "${stoken}"))
		.pause(4)
		
	}
	
	
	
	val scn_owner_1 = scenario("HU25UpdateProductOk").exec(Home.home,Login.login,ListProducts.listProducts,ShowProduct.showProduct,UpdateProductOk.updateProductOk)
	val scn_owner_2 = scenario("HU25UpdateProductFailed").exec(Home.home,Login.login,ListProducts.listProducts,ShowProduct.showProduct,UpdateProductFailed.updateProductFailed)	
		

	setUp(scn_owner_1.inject(rampUsers(300) during (100 seconds)),scn_owner_2.inject(rampUsers(300) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}