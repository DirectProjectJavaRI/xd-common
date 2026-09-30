package org.nhindirect.xd.common;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.nhindirect.xd.transform.pojo.SimplePerson;

/**
 * Tests the opt-in submission set / document entry metadata (status, source patient, referenceIdList, extra slots,
 * coding scheme overrides, time formatting) and the output fixes (classificationNode marker, no empty author or
 * PID values).
 */
public class DirectDocumentsMetadataTest
{
    private static final String REFERENCE_ID = "EP-1^^^&1.2.3&ISO^urn:dt-org:dsm:map:2025:pregnancy";

    private DirectDocuments buildDocuments()
    {
        DirectDocuments documents = new DirectDocuments();

        DirectDocuments.SubmissionSet ss = documents.getSubmissionSet();
        ss.setUniqueId("2.25.1");
        ss.setSourceId("sender@example.org");
        ss.setSubmissionTime(new Date());
        ss.setAvailabilityStatus("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        ss.setSourcePatientId("13450148^^^&1.2.3&ISO");
        SimplePerson person = new SimplePerson();
        person.setLocalId("13450148");
        person.setLocalOrg("1.2.3");
        person.setLastName("Miller");
        person.setFirstName("Sarah");
        ss.setSourcePatient(person);
        ss.getReferenceIdList().add(REFERENCE_ID);
        ss.getExtraSlots().put("urn:dt-org:test:purpose", List.of("send-test"));
        ss.setContentTypeCode("send-test", true);
        ss.setContentTypeCodingScheme("urn:test:transactions");

        DirectDocument2 document = new DirectDocument2();
        document.setData("hello".getBytes());
        DirectDocument2.Metadata metadata = document.getMetadata();
        metadata.setMimeType("text/plain");
        metadata.setUniqueId("2.25.2");
        metadata.setTitle("A Title");
        metadata.setClassCode("send-test", true);
        metadata.setClassCodeCodingScheme("urn:test:transactions");
        metadata.setLoinc("98145-6");
        metadata.setLoincCodingScheme("2.16.840.1.113883.6.1");
        metadata.getReferenceIdList().add(REFERENCE_ID);
        metadata.setSourcePatient(person);
        metadata.setTimeZone(TimeZone.getTimeZone("UTC"));
        metadata.setCreationTimePattern("yyyyMMddHHmmss");
        metadata.setCreationTime(new Date(0));
        documents.getDocuments().add(document);

        return documents;
    }

    @Test
    public void testSubmissionSetAndDocumentMetadataIsEmitted()
    {
        String xml = buildDocuments().getSubmitObjectsRequestAsString().replaceAll("ns\\d+:", "");

        assertTrue(xml.contains("status=\"urn:oasis:names:tc:ebxml-regrep:StatusType:Approved\""));
        assertTrue(xml.contains("urn:ihe:iti:xds:2013:referenceIdList"));
        assertTrue(xml.contains(REFERENCE_ID.replace("&", "&amp;")));
        assertTrue(xml.contains("urn:dt-org:test:purpose"));
        assertTrue(xml.contains("<Value>send-test</Value>"));
        assertTrue(xml.contains("urn:test:transactions"));
        assertTrue(xml.contains("2.16.840.1.113883.6.1"));
        assertTrue(xml.contains("<LocalizedString value=\"A Title\"/>"));
        assertTrue(xml.contains("<Value>19700101000000</Value>"));
        assertTrue(xml.contains("<Value>13450148^^^&amp;1.2.3&amp;ISO</Value>"));
    }

    @Test
    public void testSubmissionSetMarkerUsesClassificationNode()
    {
        String xml = buildDocuments().getSubmitObjectsRequestAsString().replaceAll("ns\\d+:", "");

        assertTrue(xml.contains("classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\""));
        assertFalse(xml.contains("classificationScheme=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\""));
    }

    @Test
    public void testEmptyAuthorAndEmptyPidValuesAreOmitted()
    {
        DirectDocuments documents = new DirectDocuments();
        documents.getSubmissionSet().setUniqueId("2.25.1");
        documents.getSubmissionSet().setSourceId("sender@example.org");

        DirectDocument2 document = new DirectDocument2();
        document.setData("x".getBytes());
        document.getMetadata().setMimeType("text/plain");
        document.getMetadata().setUniqueId("2.25.2");
        SimplePerson person = new SimplePerson();
        person.setLocalId("1");
        person.setLastName("Miller");
        document.getMetadata().setSourcePatient(person);
        documents.getDocuments().add(document);

        String xml = documents.getSubmitObjectsRequestAsString().replaceAll("ns\\d+:", "");

        // no author information was provided anywhere, so no author classifications
        assertFalse(xml.contains("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d"));
        assertFalse(xml.contains("urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d"));
        // no birth date, gender or address were provided
        assertFalse(xml.contains("PID-7|"));
        assertFalse(xml.contains("PID-8|"));
        assertFalse(xml.contains("PID-11|"));
        // no assigning authority: bare id, never the literal text "null"
        assertFalse(xml.contains("&amp;null&amp;"));
        assertTrue(xml.contains("<Value>1</Value>"));
    }
}
