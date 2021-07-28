package org.nhindirect.xd.routing.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.nhind.config.rest.AddressService;
import org.nhind.config.rest.DomainService;
import org.nhindirect.config.model.Address;
import org.nhindirect.config.model.Domain;
import org.nhindirect.config.model.EntityStatus;
import org.nhindirect.xd.common.SpringBaseTest;
import org.nhindirect.xd.routing.RoutingResolver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author beau
 */
public class RoutingResolverImplTest extends SpringBaseTest
{

	@Autowired
    private AddressService addressService;

	@Autowired
    private DomainService domainService;	


    /**
     * Test the default resolver.
     */
    @Test
    public void testDefaultResolver()
    {
        RoutingResolver resolver = new RoutingResolverImpl();

        List<String> smtpEndpoints = Arrays.asList("smtp@nologs.org", "smtp2@nologs.org");
        List<String> xdEndpoints = Arrays.asList("http://my.domain.com:8080/endpoint", "http://my.domain.com:8080/endpoint2");

        List<String> endpoints = new ArrayList<String>();
        endpoints.addAll(smtpEndpoints);
        endpoints.addAll(xdEndpoints);

        Collection<String> smtpResolved = resolver.getSmtpEndpoints(endpoints);
        Collection<String> xdResolved = resolver.getXdEndpoints(endpoints);

        assertEquals(2, smtpResolved.size());
        assertTrue(smtpResolved.contains(smtpEndpoints.get(0)));
        assertTrue(smtpResolved.contains(smtpEndpoints.get(1)));

        assertEquals(2, xdResolved.size());
        assertTrue(xdResolved.contains(xdEndpoints.get(0)));
        assertTrue(xdResolved.contains(xdEndpoints.get(1)));

        assertTrue(resolver.isSmtpEndpoint(smtpEndpoints.get(0)));
        assertTrue(resolver.isSmtpEndpoint(smtpEndpoints.get(1)));
        assertTrue(resolver.isXdEndpoint(xdEndpoints.get(0)));
        assertTrue(resolver.isXdEndpoint(xdEndpoints.get(1)));

        assertFalse(resolver.isSmtpEndpoint(xdEndpoints.get(0)));
        assertFalse(resolver.isSmtpEndpoint(xdEndpoints.get(1)));
        assertFalse(resolver.isXdEndpoint(smtpEndpoints.get(0)));
        assertFalse(resolver.isXdEndpoint(smtpEndpoints.get(1)));
    }

    /**
     * Test the resolver with a configuration service backing.
     * 
     * @throws Exception
     */
    @Test
    public void testResolverWithConfigService() throws Exception
    {
        
        Address[] addrs = new Address[3];

        List<String> smtpEndpoints = Arrays.asList("smtp@nologs.org");
        List<String> xdEndpoints = Arrays.asList("xd@nologs.org");
        List<String> emptyEndpoints = Arrays.asList("empty@nologs.org");

        List<String> endpoints = new ArrayList<String>();
        endpoints.addAll(smtpEndpoints);
        endpoints.addAll(xdEndpoints);
        endpoints.addAll(emptyEndpoints);

        // SMTP
        addrs[0] = new Address();
        addrs[0].setEmailAddress(smtpEndpoints.get(0));
        addrs[0].setDisplayName("displayName");
        addrs[0].setType("SMTP");
        addrs[0].setStatus(EntityStatus.ENABLED);

        // XD
        addrs[1] = new Address();
        addrs[1].setEmailAddress(xdEndpoints.get(0));
        addrs[1].setDisplayName("displayName");
        addrs[1].setType("XD");
        addrs[1].setEndpoint("xd_endpoint");
        addrs[1].setStatus(EntityStatus.ENABLED);

        // EMPTY
        addrs[2] = new Address();
        addrs[2].setEmailAddress(emptyEndpoints.get(0));
        addrs[2].setDisplayName("displayName");
        addrs[2].setStatus(EntityStatus.ENABLED);

        Domain d = new Domain();
        d.setDomainName("domainName");
        d.setAddresses(Arrays.asList(addrs));
        d.setStatus(EntityStatus.ENABLED);

        
        domainService.addDomain(d);

        RoutingResolver resolver = new RoutingResolverImpl(addressService);

        Collection<String> smtpResolved = resolver.getSmtpEndpoints(endpoints);
        assertEquals(2, smtpResolved.size());
        assertEquals((new ArrayList<String>(smtpResolved)).get(0), smtpEndpoints.get(0));
        assertEquals((new ArrayList<String>(emptyEndpoints)).get(0), emptyEndpoints.get(0));

        Collection<String> xdResolved = resolver.getXdEndpoints(endpoints);
        assertEquals(1, xdResolved.size());
        assertEquals((new ArrayList<String>(xdResolved)).get(0), xdEndpoints.get(0));
        assertEquals(1, xdResolved.size());
        assertEquals((new ArrayList<String>(xdResolved)).get(0), xdEndpoints.get(0));       

        String endpoint = resolver.resolve(xdEndpoints.get(0));
        assertEquals(addrs[1].getEndpoint(), endpoint);
    }

}
