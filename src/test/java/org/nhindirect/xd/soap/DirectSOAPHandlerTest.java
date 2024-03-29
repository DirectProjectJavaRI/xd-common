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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

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

}
