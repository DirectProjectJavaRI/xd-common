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

package org.nhindirect.xd.soap;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Test class for methods in DirectSOAPHandler.
 * 
 * @author beau
 */
public class DirectSOAPHandlerTest
{

    /**
     * Test the getHeaders method.
     */
	@Test
    public void testGetHeaders()
    {
        DirectSOAPHandler handler = new DirectSOAPHandler();

        Set<QName> headers = handler.getHeaders();
        assertEquals(5, headers.size());

        if (!headers.contains(new QName("http://www.w3.org/2005/08/addressing", "Action")))
        {
            fail("Headers missing expected object");
        }
        if (!headers.contains(new QName("http://www.w3.org/2005/08/addressing", "To")))
        {
            fail("Headers missing expected object");
        }
         if (!headers.contains(new QName("http://www.w3.org/2005/08/addressing", "MessageID")))
        {
            fail("Headers missing expected object");
        }
        if (!headers.contains(new QName(
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security")))
        {
            fail("Headers missing expected object");
        }
        if (!headers.contains(new QName(
                "http://www.w3.org/2005/08/addressing", "ReplyTo")))
        {
            fail("Headers missing expected object");
        }        
    }

    /**
     * Test the getMessageEncoding method.
     */
	@Test
    public void testGetMessageEncoding()
    {
        String output = null;
        SOAPMessage message = null;
        DirectSOAPHandler handler = new DirectSOAPHandler();

        try
        {
            MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            message = mf.createMessage();

            message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "ISO-8859-1");
            assertNotNull(message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING));
            output = handler.getMessageEncoding(message);
            assertEquals("ISO-8859-1", output);

            message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, null);
            assertNull(message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING));
            output = handler.getMessageEncoding(message);
            assertEquals("utf-8", output);
        }
        catch (Exception e)
        {
            fail("Exception thrown during mock SOAPMessage creation/handling.");
            e.printStackTrace();
        }
    }

    @AfterEach
    public void cleanUpThreadData()
    {
        SafeThreadData.clean(Thread.currentThread().threadId());
    }

    /**
     * Builds a mocked outbound SOAPMessageContext wrapping the given message.
     */
    private SOAPMessageContext buildOutboundContext(SOAPMessage message)
    {
        SOAPMessageContext context = mock(SOAPMessageContext.class);
        when(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.TRUE);
        when(context.containsKey(DirectSOAPHandler.ENDPOINT_ADDRESS)).thenReturn(true);
        when(context.getMessage()).thenReturn(message);
        return context;
    }

    private void populateOutboundThreadData()
    {
        Long threadId = Thread.currentThread().threadId();
        SafeThreadData threadData = SafeThreadData.GetThreadInstance(threadId);
        threadData.setAction("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b");
        threadData.setMessageId("urn:uuid:test-message-id");
        threadData.setTo("https://example.org/xdr");
        threadData.setDirectFrom("sender@direct.example.org");
        threadData.setDirectTo("recipient@direct.example.org");
        threadData.save();
    }

    /**
     * Verifies that handleMessage() writes the WS-Addressing and direct:addressBlock
     * headers into the outbound message such that they actually show up when the
     * message is serialized (writeTo()) after handleMessage() returns -- not just
     * present on the live in-memory SOAPHeader object.
     */
    @Test
    public void testHandleMessage_outbound_addsExpectedHeadersToSerializedMessage() throws Exception
    {
        DirectSOAPHandler handler = new DirectSOAPHandler();

        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = mf.createMessage();
        message.getSOAPBody().addChildElement(new QName("urn:test", "TestRequest")).setTextContent("test-body");

        populateOutboundThreadData();

        SOAPMessageContext context = buildOutboundContext(message);

        assertTrue(handler.handleMessage(context));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String xml = baos.toString("utf-8");

        assertTrue(xml.contains("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"), "Missing Action header value: " + xml);
        assertTrue(xml.contains("urn:uuid:test-message-id"), "Missing MessageID header value: " + xml);
        assertTrue(xml.contains("https://example.org/xdr"), "Missing To header value: " + xml);
        assertTrue(xml.contains("addressBlock"), "Missing direct:addressBlock header: " + xml);
        assertTrue(xml.contains("mailto:sender@direct.example.org"), "Missing direct:from value: " + xml);
        assertTrue(xml.contains("mailto:recipient@direct.example.org"), "Missing direct:to value: " + xml);
        assertTrue(xml.contains("minimal"), "Missing direct:metadata-level value: " + xml);

        assertFalse(SafeThreadData.getThreadMapView().containsKey(Thread.currentThread().threadId()),
                "SafeThreadData should be cleaned up for this thread after handleMessage() returns");
    }

    /**
     * Regression test for a bug where headers added directly to the envelope's DOM
     * (env.addHeader()/addHeaderElement()) were silently dropped from the serialized
     * message. Root cause: SAAJ's MessageImpl only re-serializes into its cached byte
     * buffer when saveChanges() is called, and saveChanges() is skipped whenever
     * saveRequired() reports false. saveRequired() is driven by an internal dirty flag
     * that is only flipped by attachment/property-setting methods -- never by direct
     * envelope/header DOM mutations. In production, the JAX-WS runtime has already
     * called saveChanges() once (or equivalent) before invoking this handler, so
     * saveRequired() is already false by the time handleMessage() runs. This test
     * reproduces that exact pre-saved state by calling saveChanges() once before
     * invoking the handler, which a naive "if (saveRequired()) saveChanges();" guard
     * would then skip, leaving the newly-added headers unserialized.
     */
    @Test
    public void testHandleMessage_outbound_headersSurviveWhenMessageAlreadyMarkedSaved() throws Exception
    {
        DirectSOAPHandler handler = new DirectSOAPHandler();

        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = mf.createMessage();
        message.getSOAPBody().addChildElement(new QName("urn:test", "TestRequest")).setTextContent("test-body");

        // Simulate the JAX-WS runtime already having saved/serialized the message
        // once before handing it to the handler chain.
        message.saveChanges();
        assertFalse(message.saveRequired(),
                "Test setup invalid: message should report no save required immediately after saveChanges()");

        populateOutboundThreadData();

        SOAPMessageContext context = buildOutboundContext(message);

        assertTrue(handler.handleMessage(context));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String xml = baos.toString("utf-8");

        assertTrue(xml.contains("urn:uuid:test-message-id"), "Missing MessageID header value: " + xml);
        assertTrue(xml.contains("mailto:sender@direct.example.org"), "Missing direct:from value: " + xml);
        assertTrue(xml.contains("mailto:recipient@direct.example.org"), "Missing direct:to value: " + xml);
    }

    /**
     * Verifies that an inbound direct:addressBlock/X-DIRECT-FINAL-DESTINATION-DELIVERY element
     * is parsed into SafeThreadData, so it can later be threaded through to the outbound SMTP
     * Disposition-Notification-* headers.
     */
    @Test
    public void testHandleMessage_inbound_parsesFinalDestinationDeliveryFromAddressBlock() throws Exception
    {
        String requestXml =
                "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
              + "  <soap:Header>"
              + "    <direct:addressBlock xmlns:direct=\"urn:direct:addressing\">"
              + "      <direct:from>outgoing_from@ett-domain.example.org</direct:from>"
              + "      <direct:to>direct_to@sut-domain.example.org</direct:to>"
              + "      <direct:X-DIRECT-FINAL-DESTINATION-DELIVERY>true</direct:X-DIRECT-FINAL-DESTINATION-DELIVERY>"
              + "    </direct:addressBlock>"
              + "  </soap:Header>"
              + "  <soap:Body>"
              + "    <xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\"/>"
              + "  </soap:Body>"
              + "</soap:Envelope>";

        MimeHeaders mimeHeaders = new MimeHeaders();
        mimeHeaders.addHeader("Content-Type", "application/soap+xml");
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = mf.createMessage(mimeHeaders,
                new ByteArrayInputStream(requestXml.getBytes("utf-8")));

        SOAPMessageContext context = mock(SOAPMessageContext.class);
        when(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.FALSE);
        when(context.getMessage()).thenReturn(message);

        assertTrue(new DirectSOAPHandler().handleMessage(context));

        SafeThreadData threadData = SafeThreadData.GetThreadInstance(Thread.currentThread().threadId());
        assertEquals("outgoing_from@ett-domain.example.org", threadData.getDirectFrom());
        assertEquals("direct_to@sut-domain.example.org", threadData.getDirectTo());
        assertEquals("true", threadData.getFinalDestinationDelivery());
    }

    /**
     * Verifies that when SafeThreadData carries a finalDestinationDelivery flag, handleMessage()
     * mirrors it back out as a direct:addressBlock/X-DIRECT-FINAL-DESTINATION-DELIVERY element on
     * an outbound (forwarded) SOAP message.
     */
    @Test
    public void testHandleMessage_outbound_mirrorsFinalDestinationDeliveryIntoAddressBlock() throws Exception
    {
        DirectSOAPHandler handler = new DirectSOAPHandler();

        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = mf.createMessage();
        message.getSOAPBody().addChildElement(new QName("urn:test", "TestRequest")).setTextContent("test-body");

        populateOutboundThreadData();
        SafeThreadData threadData = SafeThreadData.GetThreadInstance(Thread.currentThread().threadId());
        threadData.setFinalDestinationDelivery("true");
        threadData.save();

        SOAPMessageContext context = buildOutboundContext(message);

        assertTrue(handler.handleMessage(context));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String xml = baos.toString("utf-8");

        assertTrue(xml.contains("X-DIRECT-FINAL-DESTINATION-DELIVERY"),
                "Missing direct:X-DIRECT-FINAL-DESTINATION-DELIVERY element: " + xml);
    }

}
