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

package org.nhindirect.xd.transform.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the methods in the SimplePerson class.
 * 
 * @author beau
 */
public class SimplePersonTest
{
    /**
     * Test methods in the SimplePerson class.
     */
	@Test
    public void testSimplePerson()
    {
        SimplePerson person = new SimplePerson();

        String firstName = "A";
        String lastName = "B";
        String middleName = "C";
        String streetAddress1 = "D";
        String streetAddress2 = "E";
        String telephone = "F";
        String birthDateTime = "G";
        String languageCode = "H";
        String ethnicityCode = "I";
        String age = "J";
        String ageUnits = "K";
        String genderCode = "L";
        String zipCode = "M";
        String state = "N";
        String county = "O";
        String city = "P";
        String country = "Q";
        String pcpOid = "R";
        String pcpName = "S";
        String ethnicityCodeSystem = "T";
        String ethnicityCodeName = "U";
        String genderCodeSystem = "V";
        String patientEuid = "W";
        String localId = "X";
        String localOrg = "Y";
        String ssn = "Z";
        String npi = "AA";
        String email = "BB";
        String department = "CC";
        String suffix = "DD";
        String title = "EE";
        String systemCode = "FF";
        String systemName = "GG";
        String phoneExt = "HH";

        Map<String, String> otherOrgIds = new HashMap<>();
        otherOrgIds.put("A", "B");

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setMiddleName(middleName);
        person.setStreetAddress1(streetAddress1);
        person.setStreetAddress2(streetAddress2);
        person.setTelephone(telephone);
        person.setBirthDateTime(birthDateTime);
        person.setLanguageCode(languageCode);
        person.setEthnicityCode(ethnicityCode);
        person.setAge(age);
        person.setAgeUnits(ageUnits);
        person.setGenderCode(genderCode);
        person.setZipCode(zipCode);
        person.setState(state);
        person.setCounty(county);
        person.setCity(city);
        person.setCountry(country);
        person.setPcpOid(pcpOid);
        person.setPcpName(pcpName);
        person.setEthnicityCodeSystem(ethnicityCodeSystem);
        person.setEthnicityCodeName(ethnicityCodeName);
        person.setGenderCodeSystem(genderCodeSystem);
        person.setPatientEuid(patientEuid);
        person.setLocalId(localId);
        person.setLocalOrg(localOrg);
        person.setSsn(ssn);
        person.setNpi(npi);
        person.setEmail(email);
        person.setDepartment(department);
        person.setSuffix(suffix);
        person.setTitle(title);
        person.setSystemCode(systemCode);
        person.setSystemName(systemName);
        person.setPhoneExt(phoneExt);
        person.setOtherOrgIds(otherOrgIds);

        assertEquals(firstName, person.getFirstName());
        assertEquals(lastName, person.getLastName());
        assertEquals(middleName, person.getMiddleName());
        assertEquals(streetAddress1, person.getStreetAddress1());
        assertEquals(streetAddress2, person.getStreetAddress2());
        assertEquals(telephone, person.getTelephone());
        assertEquals(birthDateTime, person.getBirthDateTime());
        assertEquals(languageCode, person.getLanguageCode());
        assertEquals(ethnicityCode, person.getEthnicityCode());
        assertEquals(age, person.getAge());
        assertEquals(ageUnits, person.getAgeUnits());
        assertEquals(genderCode, person.getGenderCode());
        assertEquals(zipCode, person.getZipCode());
        assertEquals(state, person.getState());
        assertEquals(county, person.getCounty());
        assertEquals(city, person.getCity());
        assertEquals(country, person.getCountry());
        assertEquals(pcpOid, person.getPcpOid());
        assertEquals(pcpName, person.getPcpName());
        assertEquals(ethnicityCodeSystem, person.getEthnicityCodeSystem());
        assertEquals(ethnicityCodeName, person.getEthnicityCodeName());
        assertEquals(genderCodeSystem, person.getGenderCodeSystem());
        assertEquals(patientEuid, person.getPatientEuid());
        assertEquals(localId, person.getLocalId());
        assertEquals(localOrg, person.getLocalOrg());
        assertEquals(ssn, person.getSsn());
        assertEquals(npi, person.getNpi());
        assertEquals(email, person.getEmail());
        assertEquals(department, person.getDepartment());
        assertEquals(suffix, person.getSuffix());
        assertEquals(title, person.getTitle());
        assertEquals(systemCode, person.getSystemCode());
        assertEquals(systemName, person.getSystemName());
        assertEquals(phoneExt, person.getPhoneExt());
        assertEquals(otherOrgIds, person.getOtherOrgIds());

        person.setSsn(null);
        person.setSSN(ssn);

        assertEquals(ssn, person.getSSN());
    }
}
