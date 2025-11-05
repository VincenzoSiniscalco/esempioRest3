package com.azienda.esempioRest3.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azienda.esempioRest3.model.Localita;

public interface LocalitaRepository extends JpaRepository<Localita, Integer>{
	

	List<Localita> findByNome(String nome);

	List<Localita> findByTemperaturaLessThanEqual(Float temperatura);

	List<Localita> findByTemperaturaGreaterThanEqual(Float temperatura);
	
	Localita findByNomeAndTemperatura(String nome, Float temperatura);
}
