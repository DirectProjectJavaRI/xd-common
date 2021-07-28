/* 
 * Copyright (c) 2010, NHIN Direct Project
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.  
 * 3. Neither the name of the the NHIN Direct Project (nhindirect.org)
 *    nor the names of its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhindirect.xd.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;

import org.nhindirect.xd.transform.pojo.SimplePerson;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit tests for the DirectDocument2 class.
 * 
 * @author beau
 */
@Slf4j
public class DirectDocument2Test
{
    /**
     * Test mimeType.
     * 
     * @throws Exception
     */
	@Test
    public void testMimeType() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setMimeType(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getMimeType());
    }

    /**
     * Test description.
     * 
     * @throws Exception
     */
	@Test
    public void testDescription() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setDescription(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();
        
        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);
        
        assertEquals(value, metadata.getDescription());
    }

    /**
     * Test creationTime.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
	public void testCreationTime() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        Date date = new Date(1, 2, 3, 4, 5, 6);
        metadata.setCreationTime(date);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(date.getYear(), metadata.getCreationTime().getYear());
        assertEquals(date.getMonth(), metadata.getCreationTime().getMonth());
        assertEquals(date.getDate(), metadata.getCreationTime().getDate());
        assertEquals(0, metadata.getCreationTime().getHours());
        assertEquals(0, metadata.getCreationTime().getMinutes());
        assertEquals(0, metadata.getCreationTime().getSeconds());
    }

    /**
     * Test languageCode.
     * 
     * @throws Exception
     */
    @Test
    public void testLanguageCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setLanguageCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getLanguageCode());
    }

    /**
     * Test serviceStartTime.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
	public void testServiceStartTime() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        Date date = new Date(1, 2, 3, 4, 5, 6);
        metadata.setServiceStartTime(date);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(date.getYear(), metadata.getServiceStartTime().getYear());
        assertEquals(date.getMonth(), metadata.getServiceStartTime().getMonth());
        assertEquals(date.getDate(), metadata.getServiceStartTime().getDate());
        assertEquals(date.getHours(), metadata.getServiceStartTime().getHours());
        assertEquals(date.getMinutes(), metadata.getServiceStartTime().getMinutes());
        assertEquals(0, metadata.getServiceStartTime().getSeconds());
    }

    /**
     * Test serviceStopTime.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
	public void testServiceStopTime() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        Date date = new Date(1, 2, 3, 4, 5, 6);
        metadata.setServiceStopTime(date);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(date.getYear(), metadata.getServiceStopTime().getYear());
        assertEquals(date.getMonth(), metadata.getServiceStopTime().getMonth());
        assertEquals(date.getDate(), metadata.getServiceStopTime().getDate());
        assertEquals(date.getHours(), metadata.getServiceStopTime().getHours());
        assertEquals(date.getMinutes(), metadata.getServiceStopTime().getMinutes());
        assertEquals(0, metadata.getServiceStopTime().getSeconds());
    }

    /**
     * Test sourcePatient.
     * 
     * @throws Exception
     */
/*    public void testSourcePatient() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        SimplePerson sourcePatient = new SimplePerson();
        sourcePatient.setFirstName("first");
        sourcePatient.setMiddleName("middle");
        sourcePatient.setLastName("last");
        sourcePatient.setLocalId("localId");
        sourcePatient.setLocalOrg("localOrg");
        sourcePatient.setBirthDateTime("19560527");
        sourcePatient.setGenderCode("M");
        sourcePatient.setStreetAddress1("street");
        sourcePatient.setCity("city");
        sourcePatient.setState("state");
        sourcePatient.setZipCode("zip");

        metadata.setSourcePatient(sourcePatient);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(sourcePatient.getFirstName(), metadata.getSourcePatient().getFirstName());
        assertEquals(sourcePatient.getMiddleName(), metadata.getSourcePatient().getMiddleName());
        assertEquals(sourcePatient.getLastName(), metadata.getSourcePatient().getLastName());
        assertEquals(sourcePatient.getLocalId(), metadata.getSourcePatient().getLocalId());
        assertEquals(sourcePatient.getLocalOrg(), metadata.getSourcePatient().getLocalOrg());
        assertEquals(sourcePatient.getBirthDateTime(), metadata.getSourcePatient().getBirthDateTime());
        assertEquals(sourcePatient.getGenderCode(), metadata.getSourcePatient().getGenderCode());
        assertEquals(sourcePatient.getStreetAddress1(), metadata.getSourcePatient().getStreetAddress1());
        assertEquals(sourcePatient.getCity(), metadata.getSourcePatient().getCity());
        assertEquals(sourcePatient.getState(), metadata.getSourcePatient().getState());
        assertEquals(sourcePatient.getZipCode(), metadata.getSourcePatient().getZipCode());
    }
*/
    /**
     * Test authorPerson.
     * 
     * @throws Exception
     */
    @Test
    public void testAuthorPerson() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setAuthorPerson(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getAuthorPerson());
    }

    /**
     * Test authorInstitution.
     * 
     * @throws Exception
     */
    @Test
    public void testAuthorInstitution() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        List<String> values = Arrays.asList("input1", "input2");
        metadata.setAuthorInstitution(values);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(values.get(0), metadata.getAuthorInstitution().get(0));
        assertEquals(values.get(1), metadata.getAuthorInstitution().get(1));
    }

    /**
     * Test authorRole.
     * 
     * @throws Exception
     */
    @Test
    public void testAuthorRole() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setAuthorRole(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getAuthorRole());
    }

    /**
     * Test authorSpecialty.
     * 
     * @throws Exception
     */
    @Test
    public void testAuthorSpecialty() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setAuthorSpecialty(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getAuthorSpecialty());
    }

    /**
     * Test classCode.
     * 
     * @throws Exception
     */
    @Test
    public void testClassCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setClassCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getClassCode());
        assertEquals(null, metadata.getClassCode_localized());
        
        metadata.setClassCode(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getClassCode());
        assertEquals(value, metadata.getClassCode_localized());
    }

    /**
     * Test classCode_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testClassCode_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setClassCode(UUID.randomUUID().toString());
        metadata.setClassCode_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getClassCode_localized());
    }

    /**
     * Test confidentialityCode.
     * 
     * @throws Exception
     */
    @Test
    public void testConfidentialityCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setConfidentialityCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getConfidentialityCode());
        assertEquals(null, metadata.getConfidentialityCode_localized());
        
        metadata.setConfidentialityCode(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getConfidentialityCode());
        assertEquals(value, metadata.getConfidentialityCode_localized());
    }

    /**
     * Test confidentialityCode_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testConfidentialityCode_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setConfidentialityCode(UUID.randomUUID().toString());
        metadata.setConfidentialityCode_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getConfidentialityCode_localized());
    }

    /**
     * Test formatCode.
     * 
     * @throws Exception
     */
    @Test
    public void testFormatCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setFormatCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getFormatCode());
        assertEquals(null, metadata.getFormatCode_localized());        

        metadata.setFormatCode(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getFormatCode());
        assertEquals(value, metadata.getFormatCode_localized());    
    }

    /**
     * Test formatCode_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testFormatCode_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setFormatCode(UUID.randomUUID().toString());
        metadata.setFormatCode_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getFormatCode_localized());
    }

    /**
     * Test healthcareFacilityTypeCode.
     * 
     * @throws Exception
     */
    @Test
    public void testHealthcareFacilityTypeCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setHealthcareFacilityTypeCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getHealthcareFacilityTypeCode());
        assertEquals(null, metadata.getHealthcareFacilityTypeCode_localized());
        
        metadata.setHealthcareFacilityTypeCode(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getHealthcareFacilityTypeCode());
        assertEquals(value, metadata.getHealthcareFacilityTypeCode_localized());
    }

    /**
     * Test healthcareFacilityTypeCode_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testHealthcareFacilityTypeCode_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setHealthcareFacilityTypeCode(UUID.randomUUID().toString());
        metadata.setHealthcareFacilityTypeCode_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getHealthcareFacilityTypeCode_localized());
    }

    /**
     * Test practiceSettingCode.
     * 
     * @throws Exception
     */
    @Test
    public void testPracticeSettingCode() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setPracticeSettingCode(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getPracticeSettingCode());
        assertEquals(null, metadata.getPracticeSettingCode_localized());
        
        metadata.setPracticeSettingCode(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getPracticeSettingCode());
        assertEquals(value, metadata.getPracticeSettingCode_localized());
    }

    /**
     * Test practiceSettingCode_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testPracticeSettingCode_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setPracticeSettingCode(UUID.randomUUID().toString());
        metadata.setPracticeSettingCode_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getPracticeSettingCode_localized());
    }

    /**
     * Test loinc.
     * 
     * @throws Exception
     */
    @Test
    public void testLoinc() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setLoinc(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getLoinc());
        assertEquals(null, metadata.getLoinc_localized());

        metadata.setLoinc(value, true);

        eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getLoinc());
        assertEquals(value, metadata.getLoinc_localized());
    }

    /**
     * Test loinc_localized.
     * 
     * @throws Exception
     */
    @Test
    public void testLoinc_localized() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setLoinc(UUID.randomUUID().toString());
        metadata.setLoinc_localized(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getLoinc_localized());
    }

    /**
     * Test patientId.
     * 
     * @throws Exception
     */
    @Test
    public void testPatientId() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setPatientId(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getPatientId());
    }

    /**
     * Test uniqueId.
     * 
     * @throws Exception
     */
    @Test
    public void testUniqueId() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setUniqueId(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getUniqueId());
    }

    /**
     * Test hash.
     * 
     * @throws Exception
     */
    @Test
    public void testHash() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setHash(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getHash());
    }
    
    @Test
    public void testURI() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        String value = "input";
        metadata.setURI(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getURI());
    }

    /**
     * Test size.
     * 
     * @throws Exception
     */
    @Test
    public void testSize() throws Exception
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        Long value = 11L;
        metadata.setSize(value);

        ExtrinsicObjectType eot = metadata.generateExtrinsicObjectType();

        metadata = new DirectDocument2.Metadata();
        metadata.setValues(eot);

        assertEquals(value, metadata.getSize());
    }
    
    /**
     * Generic setter test.
     */
    @Test
    public void testDirectDocument2Metadata()
    {
        DirectDocument2 document = new DirectDocument2();
        DirectDocument2.Metadata metadata = document.getMetadata();

        displayMetadata(metadata);

        metadata.setMimeType("1");
        metadata.setId("2");
        metadata.setDescription("3");
        metadata.setCreationTime(new Date());
        metadata.setLanguageCode("5");
        metadata.setServiceStartTime(new Date());
        metadata.setServiceStopTime(new Date());
        metadata.setSourcePatient(new SimplePerson("Bob", "Smith"));
        metadata.setAuthorPerson("10");
        metadata.setAuthorInstitution(Arrays.asList("11.1", "11.2"));
        metadata.setAuthorRole("12");
        metadata.setAuthorSpecialty("13");
        metadata.setClassCode("14");
        metadata.setClassCode_localized("15");
        metadata.setConfidentialityCode("16");
        metadata.setConfidentialityCode_localized("17");
        metadata.setFormatCode("18");
        metadata.setFormatCode_localized("19");
        metadata.setHealthcareFacilityTypeCode("20");
        metadata.setHealthcareFacilityTypeCode_localized("21");
        metadata.setPracticeSettingCode("2");
        metadata.setPracticeSettingCode_localized("23");
        metadata.setLoinc("24");
        metadata.setLoinc_localized("25");
        metadata.setPatientId("26");
        metadata.setUniqueId("27");
        metadata.setHash("28");
        metadata.setSize(Long.valueOf("29"));
        metadata.setURI("30");

        metadata.setSubmissionSetStatus("42");

        displayMetadata(metadata);
    }

    private void displayMetadata(DirectDocument2.Metadata metadata)
    {
        log.info("mimeType                               " + metadata.getMimeType());
        log.info("id                                     " + metadata.getId());
        log.info("description                            " + metadata.getDescription());
        log.info("creationTime                           " + metadata.getCreationTime());
        log.info("languageCode                           " + metadata.getLanguageCode());
        log.info("serviceStartTime                       " + metadata.getServiceStartTime());
        log.info("serviceStopTime                        " + metadata.getServiceStopTime());
        log.info("sourcePatient                          " + metadata.getSourcePatient().getLastName() + ", " + metadata.getSourcePatient().getFirstName());
        log.info("authorPerson                           " + metadata.getAuthorPerson());
        log.info("authorInstitution                      " + metadata.getAuthorInstitution());
        log.info("authorRole                             " + metadata.getAuthorRole());
        log.info("authorSpecialty                        " + metadata.getAuthorSpecialty());
        log.info("classCode                              " + metadata.getClassCode());
        log.info("classCode_localized                    " + metadata.getClassCode_localized());
        log.info("confidentialityCode                    " + metadata.getConfidentialityCode());
        log.info("confidentialityCode_localized          " + metadata.getConfidentialityCode_localized());
        log.info("formatCode                             " + metadata.getFormatCode());
        log.info("formatCode_localized                   " + metadata.getFormatCode_localized());
        log.info("healthcareFacilityTypeCode             " + metadata.getHealthcareFacilityTypeCode());
        log.info("healthcareFacilityTypeCode_localized   " + metadata.getHealthcareFacilityTypeCode_localized());
        log.info("practiceSettingCode                    " + metadata.getPracticeSettingCode());
        log.info("practiceSettingCode_localized          " + metadata.getPracticeSettingCode_localized());
        log.info("loinc                                  " + metadata.getLoinc());
        log.info("loinc_localized                        " + metadata.getLoinc_localized());
        log.info("patientId                              " + metadata.getPatientId());
        log.info("uniqueId                               " + metadata.getUniqueId());
        log.info("hash                                   " + metadata.getHash());
        log.info("size                                   " + metadata.getSize());
        log.info("submissionSetStatus                    " + metadata.getSubmissionSetStatus());
        
        log.info(metadata.toString());
    }
}
