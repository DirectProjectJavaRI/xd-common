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

package org.nhindirect.xd.transform.impl;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;

/**
 * Test methods in the DefaultXdmXdsTransformer class.
 * 
 * @author beau
 */
@Slf4j
public class DefaultXdmXdsTransformerTest
{

    /**
     * Test the matchName method.
     */
	@Test
    public void testMatchName()
    {
        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        String zname = null;
        String subsetDirspec = null;
        String subsetFilespec = null;

        boolean output = false;

        zname = "SUBSETDIRSPEC\\FILE\\SPEC";
        subsetDirspec = "SUBSETDIRSPEC";
        subsetFilespec = "FILE/SPEC";
        output = transformer.matchName(zname, subsetDirspec, subsetFilespec);
        assertEquals(true, output);

        zname = "SUBSETDIRSPEC/FILE/SPEC";
        subsetDirspec = "SUBSETDIRSPEC";
        subsetFilespec = "FILE/SPEC";
        output = transformer.matchName(zname, subsetDirspec, subsetFilespec);
        assertEquals(true, output);

        zname = "ZNAME";
        subsetDirspec = "SUBSETDIRSPEC";
        subsetFilespec = "FILE/SPEC";
        output = transformer.matchName(zname, subsetDirspec, subsetFilespec);
        assertEquals(false, output);
    }

    /**
     * Test the getSubmissionSetDirspec method.
     */
	@Test
    public void testGetSubmissionSetDirspec()
    {
        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        String input = null;
        String output = null;

        input = "123";
        output = transformer.getSubmissionSetDirspec(input);
        assertEquals("", output);

        input = "123\\456";
        output = transformer.getSubmissionSetDirspec(input);
        assertEquals("123", output);

        input = "123\\456\\789";
        output = transformer.getSubmissionSetDirspec(input);
        assertEquals("123/456", output);

        input = "";
        output = transformer.getSubmissionSetDirspec(input);
        assertEquals("", output);

        input = null;
        output = transformer.getSubmissionSetDirspec(input);
        assertEquals(null, output);
    }

    /**
     * Test the getDocId(ExtrinsicObjectType) method.
     */
	@Test
    public void testGetDocId_ExtrinsicObjectType()
    {
        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        String output = null;
        ExtrinsicObjectType input = null;
        ExternalIdentifierType eit = null;
        List<ExternalIdentifierType> externalIdentifiers = null;

        eit = new ExternalIdentifierType();
        eit.setIdentificationScheme("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
        eit.setValue("eitValue");

        input = new ExtrinsicObjectType();
        externalIdentifiers = input.getExternalIdentifier();
        externalIdentifiers.add(eit);

        output = transformer.getDocId(input);
        assertEquals("eitValue", output);

        eit = new ExternalIdentifierType();
        eit.setIdentificationScheme("urn:uuid:incorrecd-uuid");
        eit.setValue("eitValue");

        input = new ExtrinsicObjectType();
        externalIdentifiers = input.getExternalIdentifier();
        externalIdentifiers.add(eit);

        output = transformer.getDocId(input);
        assertEquals(null, output);

        input = new ExtrinsicObjectType();
        externalIdentifiers = input.getExternalIdentifier();

        output = transformer.getDocId(input);
        assertEquals(null, output);
    }

    /**
     * Test the getDocId(SubmitObjectsRequest) method.
     */
	@Test
    public void testGetDocId_SubmitObjectsRequest()
    {
        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        QName qname = null;
        String output = null;
        SubmitObjectsRequest input = null;
        ExternalIdentifierType eit = null;
        RegistryObjectListType registryObject = null;
        RegistryPackageType registryPackageType = null;
        ExtrinsicObjectType extrinsicObjectType = null;
        List<ExternalIdentifierType> externalIdentifiers = null;
        JAXBElement<ExtrinsicObjectType> jaxbExtrinsicObject = null;
        JAXBElement<RegistryPackageType> jaxbRegistryPackageTypeObject = null;
        List<JAXBElement<? extends IdentifiableType>> identifiableList = null;

        registryObject = new RegistryObjectListType();
        identifiableList = registryObject.getIdentifiable();

        eit = new ExternalIdentifierType();
        eit.setIdentificationScheme("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
        eit.setValue("eitValue");

        extrinsicObjectType = new ExtrinsicObjectType();
        externalIdentifiers = extrinsicObjectType.getExternalIdentifier();
        externalIdentifiers.add(eit);

        qname = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject");
        jaxbExtrinsicObject = new JAXBElement<ExtrinsicObjectType>(qname, ExtrinsicObjectType.class,
                extrinsicObjectType);

        registryPackageType = new RegistryPackageType();

        qname = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage");
        jaxbRegistryPackageTypeObject = new JAXBElement<RegistryPackageType>(qname, RegistryPackageType.class,
                registryPackageType);

        identifiableList.add(jaxbRegistryPackageTypeObject);
        identifiableList.add(jaxbExtrinsicObject);

        input = new SubmitObjectsRequest();
        input.setRegistryObjectList(registryObject);

        output = transformer.getDocId(input);
        assertEquals("eitValue", output);

        registryObject = new RegistryObjectListType();

        input = new SubmitObjectsRequest();
        input.setRegistryObjectList(registryObject);

        output = transformer.getDocId(input);
        assertEquals(null, output);
    }

    /**
     * Test the getXDMRequest method.
     */
	@Test
    public void testGetXDMRequest_File()
    {
        log.info("Begin testGetXDMRequest_File");

        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        File input = getSampleXdmAsFile();
        ProvideAndRegisterDocumentSetRequestType output = null;

        try
        {
            output = transformer.transform(input);
            assertTrue(output != null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    /**
     * Test the getXDMRequest method.
     */
	@Test
    public void testGetXDMRequest_DataHandler()
    {
        log.info("Begin testGetXDMRequest_DataHandler");

        DefaultXdmXdsTransformer transformer = new DefaultXdmXdsTransformer();

        DataHandler input = getSampleXdmAsDataHandler();
        ProvideAndRegisterDocumentSetRequestType output = null;

        try
        {
            output = transformer.transform(input);
            assertTrue(output != null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    /**
     * Return a sample XDM file as a File.
     * 
     * @return a sample XDM file.
     */
    private static File getSampleXdmAsFile()
    {
        URL url = DefaultXdmXdsTransformerTest.class.getClassLoader().getResource("samplexdm.zip");
        File file = new File(url.getPath());

        return file;
    }

    /**
     * Return a sample XDM file as a DataHandler.
     * 
     * @return a sample XDM file.
     */
    private static DataHandler getSampleXdmAsDataHandler()
    {
        URL url = DefaultXdmXdsTransformerTest.class.getClassLoader().getResource("samplexdm.zip");
        DataHandler dh = new DataHandler(url);

        return dh;
    }

}
