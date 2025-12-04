package com.unicauca.pensionados.back_pensionados.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**") // permite todos los endpoints
                //.allowedOrigins("http://localhost:5173") // origen del frontend
                //.allowedOrigins("https://pensionadosunicauca.herokuapp.com") // frontend en producción
              //  .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
            //    .allowedHeaders("*") // permite todos los headers
          //      .allowCredentials(true); // permite enviar cookies o tokens

        //Regla específica que BLOQUEA /api/logs/**
        //registry.addMapping("/api/logs/**")
          //      .allowedOrigins()
        //        .allowedMethods()
      //          .allowedHeaders();
    //}
                //.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                //.allowedHeaders("*") // permite todos los headers
                //.allowCredentials(true); // permite enviar cookies o tokens

}
