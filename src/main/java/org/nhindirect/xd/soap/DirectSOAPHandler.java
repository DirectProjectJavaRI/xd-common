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

import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang3.StringUtils;
import org.nhindirect.xd.soap.type.MetadataLevelEnum;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

/**
 * This class handles the SOAP-Requests before they reach the Web Service
 * Operation. It is possible to read and manipulate the SOAP-Message.
 * 
 * @author Siegfried Bolz
 */
@Slf4j
public class DirectSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
	
    public static final String ENDPOINT_ADDRESS = "javax.xml.ws.service.endpoint.address";
    public static final String RESPONSE_NODE_NAME = "RegistryResponse";
    public static final String PNR_NODE_NAME = "ProvideAndRegisterDocumentSetRequest";

    /**
     * Is called after constructing the handler and before executing any othe
     * method.
     */
    @PostConstruct
    public void init()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public void close(MessageContext messageContext)
    {
    }

    /**
     * Is executed before this handler is being destroyed - means after close()
     * has been executed.
     */
    @PreDestroy
    public void destroy()
    {
    }

    /**
     * This method handles the incoming and outgoing SOAP-Message. It's an
     * excellent point to manipulate the SOAP.
     * 
     * @param SOAPMessageContext
     *            The SOAPMessageContext object.
     * @return true for successful handling, false otherwise.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context)
    {
        log.info("Entering DirectSOAPHandler.handleMessage(SOAPMessageContext)");
        
        // Inquire incoming or outgoing message.
        boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try
        {
            if (outbound)
            {
                log.info("Handling an outbound message");
                
                boolean isACK = !context.containsKey(ENDPOINT_ADDRESS);
                
                SafeThreadData threadData = SafeThreadData.GetThreadInstance(Thread.currentThread().getId());

                SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
                dumpSOAPMessage(msg);
                SOAPPart sp = msg.getSOAPPart();

                // edit Envelope
                SOAPEnvelope env = sp.getEnvelope();
		/* Remove old header */
		if (env.getHeader() != null){
                    env.getHeader().detachNode();
                }
                SOAPHeader sh = env.addHeader();

                @SuppressWarnings("unused")
                SOAPBody sb = env.getBody();
                try{

                    if (threadData.getAction() != null)
                    {
                        QName qname = new QName("http://www.w3.org/2005/08/addressing", "Action");
                        SOAPHeaderElement saction = sh.addHeaderElement(qname);
                        boolean must = true;

                        saction.setMustUnderstand(must);
                        saction.setValue(threadData.getAction());
                    }
                    if (threadData.getRelatesTo() != null)
                    {
                        QName qname = new QName("http://www.w3.org/2005/08/addressing", "RelatesTo");
                        SOAPHeaderElement relates = sh.addHeaderElement(qname);
                        relates.setValue(threadData.getRelatesTo());
                    }
                    if (threadData.getFrom() != null)
                    {
                        QName qname = new QName("http://www.w3.org/2005/08/addressing", "From");
                        QName child = new QName("http://www.w3.org/2005/08/addressing", "Address");
                        SOAPHeaderElement efrom = sh.addHeaderElement(qname);
                        SOAPElement address = efrom.addChildElement(child);
                        address.setValue(threadData.getFrom());
                    }
                    if (threadData.getMessageId() != null)
                    {
                        QName qname = new QName("http://www.w3.org/2005/08/addressing", "MessageID");
                        SOAPHeaderElement message = sh.addHeaderElement(qname);
                        message.setValue(threadData.getMessageId());
                    }
                    if (threadData.getTo() != null)
                    {
                        QName qname = new QName("http://www.w3.org/2005/08/addressing", "To");
                        SOAPHeaderElement sto = sh.addHeaderElement(qname);
                        sto.setValue(threadData.getTo());
                    }

                    SOAPHeaderElement directHeader = sh.addHeaderElement(new QName("urn:direct:addressing", "addressBlock"));
                    directHeader.setPrefix("direct");
                    directHeader.setRole("urn:direct:addressing:destination");
                    directHeader.setRelay(true);

                    if (StringUtils.isNotBlank(threadData.getDirectFrom()))
                    {
                        SOAPElement directFromElement = directHeader.addChildElement(new QName("urn:direct:addressing", "from"));
                        directFromElement.setPrefix("direct");
                        URI uri = new URI(threadData.getDirectFrom());
                        directFromElement.setValue((new URI("mailto", uri.getSchemeSpecificPart(), null)).toString());
                    }

                    if (StringUtils.isNotBlank(threadData.getDirectTo()))
                    {
                        /**
                         * consider multiple recipients
                         */
                        String[] directTos = threadData.getDirectTo().split(";");
                        for (String directToAddr : directTos){
                            SOAPElement directToElement = directHeader.addChildElement(new QName("urn:direct:addressing", "to"));
                            directToElement.setPrefix("direct");
                            URI uri = new URI(directToAddr);
                            directToElement.setValue((new URI("mailto", uri.getSchemeSpecificPart(), null)).toString());
                        }
                    }

                    SOAPElement directMetadataLevelElement = directHeader.addChildElement(new QName("urn:direct:addressing", "metadata-level"));
                    directMetadataLevelElement.setPrefix("direct");
                    directMetadataLevelElement.setValue(MetadataLevelEnum.MINIMAL.getLevel());
                } catch (Throwable tb){
                    if (log.isDebugEnabled()){
                        log.debug("Failed to write SOAP Header", tb);
                    } else{
                        log.error("Failed to write SOAP Header: " + tb.getMessage());
                    }
                }
                if (isACK){
                    SafeThreadData.clean(Thread.currentThread().getId());
                }
                
            }
            else
            {
                log.info("Handling an inbound message");
                
                SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
                boolean isResponse = isResponse(msg);
                
                if (! isResponse) {
                    // Issue 249 - before handling the inbound case, we should clear 
                    // out the old thread data if we don't this the To: (SMTP recipients) will 
                    // append from the previous thread data 
                    SafeThreadData.clean(Thread.currentThread().getId());
                }
                
                SafeThreadData threadData = SafeThreadData.GetThreadInstance(Thread.currentThread().getId());
                
                ServletRequest sr = (ServletRequest) context.get(MessageContext.SERVLET_REQUEST);
                if (sr != null)
                {
                    threadData.setRemoteHost( sr.getRemoteHost() );
                    threadData.setThisHost( sr.getServerName() );
                    threadData.setPid( getPID() );
                }

                SOAPPart sp = msg.getSOAPPart();

                // edit Envelope
                SOAPEnvelope env = sp.getEnvelope();
                SOAPHeader sh = env.getHeader();

                @SuppressWarnings("unchecked")
                Iterator<Node> it = sh.extractAllHeaderElements();
                while (it.hasNext())
                {
                    try{
                        Node header = it.next();

                        if (StringUtils.contains(header.toString(), "MessageID"))
                            {
                                threadData.setMessageId(header.getTextContent());
                            }
                            else if (StringUtils.contains(header.toString(), "Action"))
                            {
                                threadData.setAction(header.getTextContent());
                            }
                            else if (StringUtils.contains(header.toString(), "RelatesTo"))
                            {
                                threadData.setRelatesTo(header.getTextContent());
                            }
                            else if (StringUtils.contains(header.toString(), "ReplyTo"))
                            {
                                NodeList reps = header.getChildNodes();
                                for (int i = 0; i < reps.getLength(); i++)
                                {
                                    Node address = reps.item(i);
                                    if (StringUtils.contains(address.getNodeName(), "Address"))
                                    {
                                        threadData.setEndpoint(address.getTextContent());
                                    }
                                }
                            }
                        else if (StringUtils.contains(header.toString(), "From"))
                        {
                            NodeList reps = header.getChildNodes();
                            for (int i = 0; i < reps.getLength(); i++)
                            {
                                Node address = reps.item(i);
                                if (StringUtils.contains(address.getNodeName(), "Address"))
                                {
                                    threadData.setFrom(address.getTextContent());
                                }
                            }
                        }
                        else if (StringUtils.contains(header.toString(), "To")) // must be after ReplyTo
                            {
                                threadData.setTo(header.getTextContent());
                            }
                        else if (StringUtils.contains(header.toString(), "addressBlock"))
                        {
                            NodeList childNodes = header.getChildNodes();

                            for (int i = 0; i < childNodes.getLength(); i++)
                            {
                                Node node = childNodes.item(i);

                                if (StringUtils.contains(node.getNodeName(), "from"))
                                {
                                    threadData.setDirectFrom(node.getTextContent());
                                }
                                else if (StringUtils.contains(node.getNodeName(), "to"))
                                {
                                    // XDR-MULTIPLE-RECIPIENT-ISSUE - this is the part where old thread data 
                                    // gets into the To: and will cause unwanted recipients 
                                    // (see above for the clear)
                                    String recipient = node.getTextContent();
                                    if (threadData.getDirectTo() == null){
                                        threadData.setDirectTo(recipient);
                                    } else{
                                        /**
                                         * if multiple recipients, save addresses in one parameters separate by (;)
                                         */
                                        threadData.setDirectTo( threadData.getDirectTo() + ";" + recipient);
                                    }
                                }
                                else if (StringUtils.contains(node.getNodeName(), "metadata-level"))
                                {
                                    threadData.setDirectMetadataLevel(node.getTextContent());
                                }
                            }
                        }
                    } catch (Throwable tb){
                        if (log.isDebugEnabled()){
                            log.debug("Failed to read input parameter.", tb);
                        } else{
                            log.error("Failed to read input parameter.");
                        }
                    }
                }

                threadData.save();
            }
        }
        catch (Exception e)
        {
            log.warn("Error handling SOAP message.", e);
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
     */
    @Override
    public Set<QName> getHeaders()
    {
        Set<QName> set = new HashSet<QName>();

        set.add(new QName("http://www.w3.org/2005/08/addressing", "Action"));
        set.add(new QName("http://www.w3.org/2005/08/addressing", "MessageID"));
        set.add(new QName("http://www.w3.org/2005/08/addressing", "To"));
        set.add(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security"));

	/* Supporting ReplyTo in the addressing namespace */
	set.add(new QName("http://www.w3.org/2005/08/addressing", "ReplyTo"));
        return set;
    }

    /**
     * Returns the message encoding (e.g. utf-8).
     * 
     * @param msg
     *            The SOAPMessage object.
     * @return the message encoding.
     * @throws javax.xml.soap.SOAPException
     */
    protected String getMessageEncoding(SOAPMessage msg) throws SOAPException
    {
        String encoding = "utf-8";
        if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null)
        {
            encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
        }
        return encoding;
    }

    /**
     * Dump SOAP Message to console.
     * 
     * @param msg
     *            The SOAPMessage object.
     */
    protected void dumpSOAPMessage(SOAPMessage msg)
    {
        if (msg == null)
        {
            log.info("SOAP Message is null");
            return;
        }

        log.info("");
        log.info("--------------------");
        log.info(" DUMP OF SOAP MESSAGE");
        log.info("--------------------");

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            msg.writeTo(baos);
            log.info(baos.toString(getMessageEncoding(msg)));

            // show included values
            String values = msg.getSOAPBody().getTextContent();
            log.trace("Included values:" + values);
        }
        catch (Exception e)
        {
            log.warn("Unable to dump soap message.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleFault(SOAPMessageContext context)
    {
        log.info("ServerSOAPHandler.handleFault");
        boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound)
        {
            log.info("Direction=outbound (handleFault)");
        }
        else
        {
            log.info("Direction=inbound (handleFault)");
        }

        try
        {
            @SuppressWarnings("unused")
            SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
            // dumpSOAPMessage(msg);

            if (context.getMessage().getSOAPBody().getFault() != null)
            {
                String detailName = null;
                try
                {
                    detailName = context.getMessage().getSOAPBody().getFault().getDetail().getFirstChild()
                            .getLocalName();
                    log.info("detailName=" + detailName);
                }
                catch (Exception e)
                {
                    log.warn("Unable to extract detailName", e);
                }
            }
        }
        catch (SOAPException e)
        {
            log.warn("Error handling fault", e);
        }

        return true;
    }

    /**
     * Get the current process ID.
     * 
     * @return the current process ID
     */
    public String getPID()
    {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return processName.split("@")[0];
    }
    
    /**
     * Determines if the specified SOAPMessage is a Registry Response.
     * 
     * @param soapMessage
     * @return false if message is null, otherwise true if this a a response
     * @throws SOAPException
     */
    protected boolean isResponse(SOAPMessage soapMessage) throws SOAPException {
        boolean isResponse = false;
        
        if (soapMessage != null) {
            SOAPBody soapBody = soapMessage.getSOAPBody();
            
            @SuppressWarnings("rawtypes")
            Iterator childElements = soapBody.getChildElements();
            if (childElements != null) {
                while (childElements.hasNext()) {
                    Node node = (Node) childElements.next();
                    String nodeName = node.getNodeName();
                    if (StringUtils.equalsIgnoreCase(nodeName, RESPONSE_NODE_NAME)) {
                        isResponse = true;
                        break;
                    } else if (StringUtils.equalsIgnoreCase(nodeName, PNR_NODE_NAME)) {
                        isResponse = false;
                        break;
                    }
                }    
            }    
        }
        
        return isResponse;
    }
}

