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

package org.nhindirect.xd.routing.impl;

import org.apache.commons.lang3.StringUtils;

import org.nhind.config.rest.AddressService;
import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.config.model.Address;
import org.nhindirect.config.model.EntityStatus;
import org.nhindirect.xd.routing.RoutingResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a RoutingResolver.
 * 
 * @author beau
 */
public class RoutingResolverImpl extends RoutingResolver
{
    private AddressService addressService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingResolverImpl.class);

    /**
     * Construct a RoutingResolverImpl without a configuration service backing.
     */
    public RoutingResolverImpl()
    {

    }

    /**
     * Construct a RoutingResolverImpl with a configuration service backing.
     * 
     * @param configServiceUrl
     *            The configuration service backing.
     */
    public RoutingResolverImpl(AddressService addressService)
    {
    	this.addressService = addressService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.routing.RoutingResolver#resolve(java.lang.String)
     */
    @Override
    public String resolve(String address)
    {
        Address addr = lookup(address);

        if (addr != null)
        {
            if (StringUtils.isNotBlank(addr.getEndpoint()))
                return addr.getEndpoint();

            // fallback
            return addr.getEmailAddress();
        }

        // fallback
        return address;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.routing.RoutingResolver#isSmtpEndpoint(java.lang.String)
     */
    @Override
    public boolean isSmtpEndpoint(String address)
    {
        if (address == null)
            return false;

        Address addr = lookup(address);

        if (addr != null && StringUtils.isNotBlank(addr.getType()))
        {
            if (StringUtils.equalsIgnoreCase(addr.getType(), "SMTP"))
                return true;
            
            return false;
        }

        // fallback
        return StringUtils.contains(address, '@');
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.routing.RoutingResolver#isXdEndpoint(java.lang.String)
     */
    @Override
    public boolean isXdEndpoint(String address)
    {
        if (address == null)
            return false;

        Address addr = lookup(address);

        if (addr != null && StringUtils.isNotBlank(addr.getType()))
        {
            if (StringUtils.equalsIgnoreCase(addr.getType(), "XD"))
                return true;
            
            return false;
        }

        // fallback
        return !StringUtils.contains(address, '@');
    }

    private Address lookup(String address)
    {
        if (addressService == null)
        {
            LOGGER.warn("Attempt to lookup address with unititialized address service, falling back to default routing.");
            return null;
        }

        Address addr;

        try
        {
            addr = addressService.getAddress(address);

            if (addr == null || !addr.getStatus().equals(EntityStatus.ENABLED))
            {
                if (LOGGER.isTraceEnabled())
                    LOGGER.trace("Unable to find address " + address + " in address store.");
                
                return null;
            }

            return addr;
        }
        catch (ServiceException e)
        {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("Unable to look up address, falling back to default routing.", e);
        }

        return null;
    }
}
