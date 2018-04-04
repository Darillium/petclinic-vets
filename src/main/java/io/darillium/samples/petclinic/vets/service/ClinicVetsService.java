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

import org.springframework.dao.DataAccessException;
import io.darillium.samples.petclinic.vets.model.Specialty;
import io.darillium.samples.petclinic.vets.model.Vet;


/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 * @author Maxim Avezbakiev
 */
public interface ClinicVetsService {
	
	Vet findVetById(int id) throws DataAccessException;
	Collection<Vet> findVets() throws DataAccessException;
	Collection<Vet> findAllVets() throws DataAccessException;
	void saveVet(Vet vet) throws DataAccessException;
	void deleteVet(Vet vet) throws DataAccessException;
	
	Specialty findSpecialtyById(int specialtyId);
	Collection<Specialty> findAllSpecialties() throws DataAccessException;
	void saveSpecialty(Specialty specialty) throws DataAccessException;
	void deleteSpecialty(Specialty specialty) throws DataAccessException;

}
