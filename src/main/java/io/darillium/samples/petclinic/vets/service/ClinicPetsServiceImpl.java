/*
 * Copyright 2002-2017 the original author or authors.
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
package io.darillium.samples.petclinic.vets.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.darillium.samples.petclinic.vets.model.Specialty;
import io.darillium.samples.petclinic.vets.model.Vet;
import io.darillium.samples.petclinic.vets.repository.SpecialtyRepository;
import io.darillium.samples.petclinic.vets.repository.VetRepository;

/**
 * Mostly used as a facade for Petclinic Vets controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 * @author Maxim Avezbakiev
 */
@Service

public class ClinicPetsServiceImpl implements ClinicVetsService {

    private VetRepository vetRepository;
    private SpecialtyRepository specialtyRepository;

    @Autowired
     public ClinicPetsServiceImpl(
    		 VetRepository vetRepository,
    		 SpecialtyRepository specialtyRepository) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository; 
    }

	@Override
	@Transactional(readOnly = true)
	public Vet findVetById(int id) throws DataAccessException {
		Vet vet = null;
		try {
			vet = vetRepository.findById(id);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return vet;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Vet> findAllVets() throws DataAccessException {
		return vetRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
    @Cacheable(value = "vets")
	public Collection<Vet> findVets() throws DataAccessException {
		return vetRepository.findAll();
	}

	@Override
	@Transactional
	public void saveVet(Vet vet) throws DataAccessException {
		vetRepository.save(vet);
	}

	@Override
	@Transactional
	public void deleteVet(Vet vet) throws DataAccessException {
		vetRepository.delete(vet);
	}

	@Override
	@Transactional(readOnly = true)
	public Specialty findSpecialtyById(int specialtyId) {
		Specialty specialty = null;
		try {
			specialty = specialtyRepository.findById(specialtyId);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return specialty;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Specialty> findAllSpecialties() throws DataAccessException {
		return specialtyRepository.findAll();
	}

	@Override
	@Transactional
	public void saveSpecialty(Specialty specialty) throws DataAccessException {
		specialtyRepository.save(specialty);
	}

	@Override
	@Transactional
	public void deleteSpecialty(Specialty specialty) throws DataAccessException {
		specialtyRepository.delete(specialty);
	}

}
