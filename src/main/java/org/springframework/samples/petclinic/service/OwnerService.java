/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.OwnerCrudRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class OwnerService {

    private OwnerRepository ownerRepository;
    private OwnerCrudRepository ownerCrudRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    public OwnerService(final OwnerRepository ownerRepository, final OwnerCrudRepository ownerCrudRepository) {
	this.ownerRepository = ownerRepository;
	this.ownerCrudRepository = ownerCrudRepository;
    }

    @Transactional(readOnly = true)
    public Owner findOwnerById(final int id) throws DataAccessException {
	return this.ownerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
	return this.ownerRepository.findByLastName(lastName);
    }

    @Transactional(rollbackFor = DuplicatedPetNameException.class)
    public void saveOwner(final Owner owner) throws DuplicatedUsernameException {
	Integer countUsersWithSameUsername = this.ownerCrudRepository
		.countOwnersWithSameUserName(owner.getUser().getUsername());
	// CASO DE CREAR

	if (owner.getId() == null && countUsersWithSameUsername > 0) {
	    throw new DuplicatedUsernameException();
	    // CASO EDITAR
	} else if (owner.getId() != null) {
	    Owner old = this.ownerCrudRepository.findById(owner.getId()).get();
	    if (!owner.getUser().getUsername().equals(old.getUser().getUsername()) && countUsersWithSameUsername > 0) {
		throw new DuplicatedUsernameException();
	    } else {
		this.ownerRepository.save(owner);
		// creating user
		this.userService.saveUser(owner.getUser());
		// creating authorities
		this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	    }
	} else {
	    // creating owner
	    this.ownerRepository.save(owner);
	    // creating user
	    this.userService.saveUser(owner.getUser());
	    // creating authorities
	    this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}

    }

    @Transactional
    public Owner findOwnerByUsername(final String username) {
	return this.ownerCrudRepository.findOwnerByUsername(username);
    }

    public ByteArrayInputStream generatePDF(final Prescription p) throws MalformedURLException, IOException {
	Document document = new Document();
	ByteArrayOutputStream out = new ByteArrayOutputStream();

	try {
	    File file = new File(this.getClass()
		    .getResource("/static/resources/images/" + p.getPet().getType().getName() + ".png").getFile());
	    Image image1 = Image.getInstance(file.getAbsolutePath());
	    image1.setAlignment(Element.ALIGN_CENTER);
	    image1.scaleAbsolute(document.getPageSize().getHeight() * 0.5f, document.getPageSize().getWidth() * 0.2f);
	    FontFactory.register("https://fonts.googleapis.com/css?family=Liu+Jian+Mao+Cao&display=swap", "titleFont");
	    FontFactory.register("https://fonts.googleapis.com/css?family=Quicksand&display=swap", "textfont");

	    Font textFont = FontFactory.getFont("textfont", 13);

	    Font titleFont = FontFactory.getFont("titleFont", 16);

	    PdfPTable table = new PdfPTable(1);
	    table.setWidthPercentage(95);
	    table.setSpacingBefore(15);
	    table.setSpacingAfter(15);

	    PdfPCell hcell;
	    hcell = new PdfPCell(new Phrase("Vet", titleFont));
	    hcell.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase(p.getVet().getFirstName() + " " + p.getVet().getLastName(), textFont));
	    hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase("Owner of the pet", titleFont));
	    hcell.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase(
		    p.getPet().getOwner().getFirstName() + " " + p.getPet().getOwner().getLastName(), textFont));
	    hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase("Pet", titleFont));
	    hcell.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase(p.getPet().getName(), textFont));
	    hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase("Pet Type", titleFont));
	    hcell.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    hcell = new PdfPCell(new Phrase(p.getPet().getType().getName(), textFont));
	    hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(hcell);

	    PdfPTable table2 = new PdfPTable(1);
	    table2.setWidthPercentage(95);
	    table2.setSpacingBefore(15);
	    table2.setSpacingAfter(15);
	    PdfPCell hcell2;

	    hcell2 = new PdfPCell(new Phrase("Title", titleFont));
	    hcell2.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase(p.getName(), textFont));
	    hcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase("Description", titleFont));
	    hcell2.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase(p.getDescription(), textFont));
	    hcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase("Start Date", titleFont));
	    hcell2.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase(p.getDateInicio().toString(), textFont));
	    hcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase("End Date", titleFont));
	    hcell2.setBackgroundColor(new BaseColor(104, 221, 122));
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    hcell2 = new PdfPCell(new Phrase(p.getDateFinal().toString(), textFont));
	    hcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    hcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table2.addCell(hcell2);

	    PdfWriter.getInstance(document, out);
	    document.open();
	    document.setMargins(0, 0, 30, 30);
	    document.addTitle("Prescripción de la mascota:" + p.getPet().getName());
	    document.add(image1);
	    document.add(table);
	    document.add(table2);
	    document.close();

	} catch (DocumentException ex) {

	}

	return new ByteArrayInputStream(out.toByteArray());
    }

}
