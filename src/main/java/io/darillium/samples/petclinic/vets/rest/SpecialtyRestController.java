/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.darillium.samples.petclinic.vets.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.darillium.samples.petclinic.vets.model.Specialty;
import io.darillium.samples.petclinic.vets.service.ClinicVetsService;

/**
 * @author Vitaliy Fedoriv
 *
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/specialties")
public class SpecialtyRestController {
	
	@Autowired
	private ClinicVetsService clinicVetsService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Specialty>> getAllSpecialtys(){
		Collection<Specialty> specialties = new ArrayList<Specialty>();
		specialties.addAll(this.clinicVetsService.findAllSpecialties());
		if (specialties.isEmpty()){
			return new ResponseEntity<Collection<Specialty>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Specialty>>(specialties, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> getSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicVetsService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Specialty>(specialty, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> addSpecialty(@RequestBody @Valid Specialty specialty, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (specialty == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicVetsService.saveSpecialty(specialty);
		headers.setLocation(ucBuilder.path("/api/specialtys/{id}").buildAndExpand(specialty.getId()).toUri());
		return new ResponseEntity<Specialty>(specialty, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> updateSpecialty(@PathVariable("specialtyId") int specialtyId, @RequestBody @Valid Specialty specialty, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (specialty == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
		}
		Specialty currentSpecialty = this.clinicVetsService.findSpecialtyById(specialtyId);
		if(currentSpecialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		currentSpecialty.setName(specialty.getName());
		this.clinicVetsService.saveSpecialty(currentSpecialty);
		return new ResponseEntity<Specialty>(currentSpecialty, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public ResponseEntity<Void> deleteSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicVetsService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicVetsService.deleteSpecialty(specialty);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
