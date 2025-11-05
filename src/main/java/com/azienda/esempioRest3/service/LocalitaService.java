package com.azienda.esempioRest3.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azienda.esempioRest3.exception.EmptyListException;
import com.azienda.esempioRest3.exception.IsStringException;
import com.azienda.esempioRest3.exception.LocalitaAlreadyExistsException;
import com.azienda.esempioRest3.exception.LocalitaNotExistsException;
import com.azienda.esempioRest3.exception.NotFloatException;
import com.azienda.esempioRest3.exception.NotIntegerException;
import com.azienda.esempioRest3.model.Localita;
import com.azienda.esempioRest3.repository.LocalitaRepository;

@Service
@Transactional
public class LocalitaService{
	
	@Autowired
	private LocalitaRepository localitaRepository;
	
	public void riempiDb() {
		try {
			insert("Roma", 15.6f);						
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Milano",18.5f);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Torino", 23.7f);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Bologna", 13.5f);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Manfredonia", 25.0f);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Napoli",17.7f);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insert("Taranto", 22.2f);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Localita> read() {
		return localitaRepository.findAll();
	}
	public Localita readById(Integer id) {
		return localitaRepository.findById(id).orElse(null);
	}
	public List<Localita> readByNome(String nome){
		return localitaRepository.findByNome(nome);
	}
	
	public void insert(String nome,Float temperatura) throws IsStringException, NotFloatException, LocalitaAlreadyExistsException{
		Localita loc = null;
		isString(nome);
		isFloat(temperatura);
	
		loc= localitaRepository.findByNomeAndTemperatura(nome,temperatura);
		if(loc!=null) {
			throw new LocalitaAlreadyExistsException("La località è già presente!", null);
		}
		loc=new Localita(nome,temperatura);
		localitaRepository.save(loc);
	}


	public List<Localita> findByTemperaturaUnder(Float temperatura) {
		List<Localita> locFredde=new ArrayList<Localita>();
		for(Localita loc: localitaRepository.findByTemperaturaLessThanEqual(temperatura)) {
			if(loc.getTemperatura()<=temperatura) {
				locFredde.add(loc);
			}
		}
		return locFredde;
	}
	
	public List<Localita> findByTemperaturaOver(Float temperatura) {
		List<Localita> locCalde= new ArrayList<Localita>();
		for(Localita loc:localitaRepository.findByTemperaturaGreaterThanEqual(temperatura)) {
			if(loc.getTemperatura()>=temperatura) {
				locCalde.add(loc);
			}
		}
		return locCalde;
	}
	
	public Localita update(String nomeNew,Float temperaturaNew,Integer id) throws IsStringException, LocalitaAlreadyExistsException, LocalitaNotExistsException, NotFloatException, NotIntegerException {

		isInt(id);
		isString(nomeNew);
		isFloat(temperaturaNew);

		Localita loc = localitaRepository.findById(id).orElse(null);
		if(loc==null) {
			throw new LocalitaNotExistsException("La località che stai cercando di aggiornare non esiste!", null);
		}
		Localita loc2 = localitaRepository.findByNomeAndTemperatura(nomeNew, temperaturaNew);
	    if (loc2 != null && !loc2.getId().equals(id)) {
	        throw new LocalitaAlreadyExistsException("Una località con questi valori esiste già nel database!", null);
	    }
		
		loc.setNome(nomeNew);
		loc.setTemperatura(temperaturaNew);

		return loc;
	}
	public Localita updateParziale(String nomeNew,Float temperaturaNew,Integer id) throws IsStringException, NotIntegerException, NotFloatException, LocalitaAlreadyExistsException, LocalitaNotExistsException{
		
		isInt(id);

		Localita loc= localitaRepository.findById(id).orElse(null); 
		if(loc==null) {
			throw new LocalitaNotExistsException("La località non esiste!", null);
		}	
		Localita loc2=localitaRepository.findByNomeAndTemperatura(nomeNew,temperaturaNew);
		if(loc2!=null) {
			throw new LocalitaAlreadyExistsException("La località che stai provando ad inserire al posto della vecchia già esiste nel database!", null);
		}
		if(nomeNew!=null) {
			loc.setNome(nomeNew);
		}
		if(temperaturaNew!=null) {
			loc.setTemperatura(temperaturaNew);
		}
		
		return loc;
	}
	public void removeById(String nome,Float temperatura) throws IsStringException, NotFloatException, LocalitaNotExistsException{
		isString(nome);
		isFloat(temperatura);
		Localita loc=localitaRepository.findByNomeAndTemperatura(nome,temperatura);
		if(loc==null) {
			throw new LocalitaNotExistsException("La località non esiste!", null);
		}	
		Integer idRemove=loc.getId();													
		localitaRepository.deleteById(idRemove);
	}
	
	public void removeUnderTemp(Float temperatura) throws NotFloatException, EmptyListException{
		
		isFloat(temperatura);
		List<Localita> toRemove= findByTemperaturaUnder(temperatura);
		 
		if(toRemove.size()==0) {
			throw new EmptyListException("Non son state trovate località da eliminare!", null);
		}	

		localitaRepository.deleteAll(toRemove);
	}
	
	public void removeOverTemp(Float temperatura) throws NotFloatException, EmptyListException{
		isFloat(temperatura);
		List<Localita> toRemove= findByTemperaturaOver(temperatura);
		 
		if(toRemove.size()==0) {
			throw new EmptyListException("Non son state trovate località da eliminare!", null);
		}	

		localitaRepository.deleteAll(toRemove);
	}
	
	private boolean isFloat(Float f) throws NotFloatException {
		if(f==null) {
			throw new NotFloatException("Il campo non è nel formato numerico corretto (float).",null);
		}
		return true;
	}
	private boolean isString(String s) throws IsStringException {
		if(s.isEmpty()||s.isBlank()) {
			throw new IsStringException("Non hai inserito una stringa valida!",null);
		}return true;
	}
	private boolean isInt(Integer i) throws NotIntegerException {
		if(i==null) {
			throw new NotIntegerException("Il campo non contiene un numero.",null);
		}
		return true;
	}

	
}
