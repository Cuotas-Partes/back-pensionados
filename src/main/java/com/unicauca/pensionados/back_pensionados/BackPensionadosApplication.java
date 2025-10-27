package com.unicauca.pensionados.back_pensionados;

//import java.util.Scanner;
//import org.javamoney.moneta.Money;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

}
