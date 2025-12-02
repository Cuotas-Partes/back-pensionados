package com.unicauca.pensionados.back_pensionados;

//import java.util.Scanner;
//import org.javamoney.moneta.Money;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import javax.money.Monetary;
//import javax.money.MonetaryAmount;

@SpringBootApplication
public class BackPensionadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackPensionadosApplication.class, args);
		/*Scanner scanner = new Scanner(System.in);
		System.out.println("Hola mundo");

		System.out.println("Ingrese el valor 1");
		MonetaryAmount cantidad1 = Money.of(scanner.nextDouble(),Monetary.getCurrency("USD"));
		System.out.println("Ingrese el valor 2");
		MonetaryAmount cantidad2 = Money.of(scanner.nextDouble(),Monetary.getCurrency("USD"));
		
		MonetaryAmount resultado;
		resultado = cantidad1.add(cantidad2);
		System.out.println(resultado);*/
	}

	//@Bean
	//public WebMvcConfigurer corsConfigurer() {
		//return new WebMvcConfigurer() {
			//@Override
			//public void addCorsMappings(CorsRegistry registry) {
				//registry.addMapping("/**")
						//.allowedOrigins("http://localhost:5173") // origen del frontend
						//.allowedOrigins("https://pensionadosunicauca.herokuapp.com") // frontend en producción
						//.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						//.allowedHeaders("*")
						//.allowCredentials(true);
			//}
		//};
	//}

}
