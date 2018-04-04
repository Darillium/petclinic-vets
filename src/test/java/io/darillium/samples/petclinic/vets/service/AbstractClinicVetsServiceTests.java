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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import io.darillium.samples.petclinic.vets.model.Specialty;
import io.darillium.samples.petclinic.vets.model.Vet;
import io.darillium.samples.petclinic.vets.service.ClinicVetsService;
import io.darillium.samples.petclinic.vets.util.EntityUtils;

/**
 * <p> Base class for {@link ClinicVetsService} integration tests. </p> <p> Subclasses should specify Spring context
 * configuration using {@link ContextConfiguration @ContextConfiguration} annotation </p> <p>
 * AbstractclinicServiceTests and its subclasses benefit from the following services provided by the Spring
 * TestContext Framework: </p> <ul> <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li> <li><strong>Dependency Injection</strong> of test fixture instances, meaning that
 * we don't need to perform application context lookups. See the use of {@link Autowired @Autowired} on the <code>{@link
 * AbstractClinicVetsServiceTests#clinicVetsService clinicVetsService}</code> instance variable, which uses autowiring <em>by
 * type</em>. <li><strong>Transaction management</strong>, meaning each test method is executed in its own transaction,
 * which is automatically rolled back by default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script. <li> An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean lookup if necessary. </li> </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 * @author Maxim Avezbakiev
 */
public abstract class AbstractClinicVetsServiceTests {

    @Autowired
    protected ClinicVetsService clinicVetsService;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindVets() {
        Collection<Vet> vets = this.clinicVetsService.findVets();

        Vet vet = EntityUtils.getById(vets, Vet.class, 3);
        assertThat(vet.getLastName()).isEqualTo("Douglas");
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
        assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
    }

    @Test
    public void shouldFindVetDyId(){
    	Vet vet = this.clinicVetsService.findVetById(1);
    	assertThat(vet.getFirstName()).isEqualTo("James");
    	assertThat(vet.getLastName()).isEqualTo("Carter");
    }
    
    @Test
    @Transactional
    public void shouldInsertVet() {
        Collection<Vet> vets = this.clinicVetsService.findAllVets();
        int found = vets.size();

        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Dow");
                
        this.clinicVetsService.saveVet(vet);
        assertThat(vet.getId().longValue()).isNotEqualTo(0);

        vets = this.clinicVetsService.findAllVets();
        assertThat(vets.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdateVet(){
    	Vet vet = this.clinicVetsService.findVetById(1);
    	String oldLastName = vet.getLastName();
        String newLastName = oldLastName + "X";
        vet.setLastName(newLastName);
        this.clinicVetsService.saveVet(vet);
        vet = this.clinicVetsService.findVetById(1);
        assertThat(vet.getLastName()).isEqualTo(newLastName);
    }
    
    @Test
    @Transactional
    public void shouldDeleteVet(){
    	Vet vet = this.clinicVetsService.findVetById(1);
        this.clinicVetsService.deleteVet(vet);
        try {
        	vet = this.clinicVetsService.findVetById(1);
		} catch (Exception e) {
			vet = null;
		}
        assertThat(vet).isNull();
    }
    
    @Test
    public void shouldFindSpecialtyById(){
    	Specialty specialty = this.clinicVetsService.findSpecialtyById(1);
    	assertThat(specialty.getName()).isEqualTo("radiology");
    }
    
    @Test
    public void shouldFindAllSpecialtys(){
        Collection<Specialty> specialties = this.clinicVetsService.findAllSpecialties();
        Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
        assertThat(specialty1.getName()).isEqualTo("radiology");
        Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
        assertThat(specialty3.getName()).isEqualTo("dentistry");
    }
    
    @Test
    @Transactional
    public void shouldInsertSpecialty() {
        Collection<Specialty> specialties = this.clinicVetsService.findAllSpecialties();
        int found = specialties.size();

        Specialty specialty = new Specialty();
        specialty.setName("dermatologist");
        
        this.clinicVetsService.saveSpecialty(specialty);
        assertThat(specialty.getId().longValue()).isNotEqualTo(0);

        specialties = this.clinicVetsService.findAllSpecialties();
        assertThat(specialties.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdateSpecialty(){
    	Specialty specialty = this.clinicVetsService.findSpecialtyById(1);
    	String oldLastName = specialty.getName();
        String newLastName = oldLastName + "X";
        specialty.setName(newLastName);
        this.clinicVetsService.saveSpecialty(specialty);
        specialty = this.clinicVetsService.findSpecialtyById(1);
        assertThat(specialty.getName()).isEqualTo(newLastName);
    }
    
    @Test
    @Transactional
    public void shouldDeleteSpecialty(){
    	Specialty specialty = this.clinicVetsService.findSpecialtyById(1);
        this.clinicVetsService.deleteSpecialty(specialty);
        try {
        	specialty = this.clinicVetsService.findSpecialtyById(1);
		} catch (Exception e) {
			specialty = null;
		}
        assertThat(specialty).isNull();
    }
       


}
