package com.azienda.esempioRest3.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azienda.esempioRest3.model.Localita;
import com.azienda.esempioRest3.service.LocalitaService;

@RestController
@RequestMapping(path = "/rest",produces = {"application/json"})
@CrossOrigin(origins = {"*"})
public class LocalitaRestController {

	@Autowired
	private LocalitaService localitaService;
	
	public LocalitaRestController() {
		
	}

	@GetMapping("/elenco")
	public List<Localita> elenco(){
		return localitaService.read();
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<Localita> getById(@PathVariable("id") Integer id){
		try {
			if(id<0 || id==null) {
				return new ResponseEntity<Localita>(HttpStatus.BAD_REQUEST);
			}
			Localita l=localitaService.readById(id);
			HttpStatus httpStatus= l!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<Localita>(l, httpStatus);

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Localita>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getByNome/{nome}")
	public ResponseEntity<List<Localita>> getByNome(@PathVariable("nome") String nome){
		try{
			if(nome.isBlank()||nome.isEmpty()) {
				return new ResponseEntity<List<Localita>>(HttpStatus.BAD_REQUEST);
			}
			List<Localita> localitaList=localitaService.readByNome(nome);
			HttpStatus httpStatus= localitaList.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Localita>>(localitaList, httpStatus);

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Localita>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@GetMapping("/getUnder/{temperatura}")
	public ResponseEntity<List<Localita>> getByTempUnder(@PathVariable("temperatura") Float temperatura){
		try{
			if(temperatura.isNaN()||temperatura==null) {
				return new ResponseEntity<List<Localita>>(HttpStatus.BAD_REQUEST);
			}
			List<Localita> locFredde=localitaService.findByTemperaturaUnder(temperatura);
			HttpStatus httpStatus= locFredde.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK  ;
			return new ResponseEntity<List<Localita>>(locFredde, httpStatus);

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Localita>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getOver/{temperatura}")
	public ResponseEntity<List<Localita>> getByTempOver(@PathVariable("temperatura") Float temperatura){
		try{
			if(temperatura.isNaN()||temperatura==null) {
				return new ResponseEntity<List<Localita>>(HttpStatus.BAD_REQUEST);
			}
			
			List<Localita> locCalde=localitaService.findByTemperaturaOver(temperatura);
			HttpStatus httpStatus= locCalde.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK  ;
			return new ResponseEntity<List<Localita>>(locCalde, httpStatus);

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Localita>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(path="/inserisci",consumes = {"application/json"})
	public ResponseEntity<Localita> inserisci(@RequestBody Localita l){
		try {
			Localita loc=localitaService.readById(l.getId());
			HttpStatus httpStatus= null;
			if(loc!=null) {
				httpStatus= HttpStatus.BAD_REQUEST;
			}else {
				httpStatus= HttpStatus.CREATED;
				localitaService.insert(l.getNome(),l.getTemperatura());
			}
			return new ResponseEntity<Localita>(l, httpStatus);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Localita>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path="/aggiornaTotale",consumes = {"application/json"})
	public ResponseEntity<Localita> aggiornaTotale(@RequestBody Localita l){
		try {
			Localita loc=localitaService.readById(l.getId());
			HttpStatus httpStatus= null;
			if(loc!=null) {
				httpStatus= HttpStatus.OK;
				localitaService.update(l.getNome(),l.getTemperatura(),l.getId());
				return new ResponseEntity<Localita>(l, httpStatus);
			}else {
				httpStatus= HttpStatus.NOT_FOUND;
				return new ResponseEntity<Localita>(httpStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Localita>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PatchMapping(path="/aggiornaParziale",consumes = {"application/json"})
	public ResponseEntity<Localita> aggiornaParziale(@RequestBody Localita l){
		try {
			Localita loc=localitaService.readById(l.getId());

			HttpStatus httpStatus= null;
			if(loc!=null) {
				httpStatus= HttpStatus.OK;

				localitaService.updateParziale(l.getNome(),l.getTemperatura(),l.getId());
				return new ResponseEntity<Localita>(l, httpStatus);
			}else {
				httpStatus= HttpStatus.NOT_FOUND;
				return new ResponseEntity<Localita>(httpStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Localita>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/cancella/{id}")
	public ResponseEntity<Void> cancella(@PathVariable("id") Integer id) {
		try {
			if(id<0 || id==null) {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			Localita loc=localitaService.readById(id);
			if(loc==null) {
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			localitaService.removeById(loc.getNome(),loc.getTemperatura());
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@DeleteMapping("/cancellaUnder/{temperatura}")
	public ResponseEntity<Void> cancellaTempUnder(@PathVariable("temperatura") Float temperatura) {
		try {
			if(temperatura.isNaN()||temperatura==null) {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			
			localitaService.removeUnderTemp(temperatura);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@DeleteMapping("/cancellaOver/{temperatura}")
	public ResponseEntity<Void> cancellaTempOver(@PathVariable("temperatura") Float temperatura) {
		try {
			if(temperatura.isNaN()||temperatura==null) {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			
			localitaService.removeOverTemp(temperatura);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
}
