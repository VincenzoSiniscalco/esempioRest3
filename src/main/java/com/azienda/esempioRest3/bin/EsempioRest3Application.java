package com.azienda.esempioRest3.bin;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.azienda.esempioRest3.service.LocalitaService;

@SpringBootApplication(scanBasePackages = {"com.azienda.esempioRest3.controller","com.azienda.esempioRest3.service"})
@EntityScan(basePackages = {"com.azienda.esempioRest3.model"})
@EnableJpaRepositories(basePackages = {"com.azienda.esempioRest3.repository"})
public class EsempioRest3Application {

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext context= SpringApplication.run(EsempioRest3Application.class, args);
			
			LocalitaService service= context.getBean(LocalitaService.class);
			
			service.riempiDb();
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}

}
