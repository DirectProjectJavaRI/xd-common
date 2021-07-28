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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Test class for the ThreadData class.
 * 
 * @author beau
 */
public class ThreadDataTest 
{    
    /**
     * Test methods in the ThreadData class.
     */
	@Test
    public void testThreadData() {
        Map<Long, Map<String, String>> map = ThreadData.getThreadMapView();
        
        // Compare against current size (which may affected by previous tests)
        int mapSize = map.size();

        ThreadData t1 = new ThreadData(new Long(99991));
        t1.setAction("action.1");
        t1.setFrom("from.1");
        t1.setMessageId("messageId.1");
        t1.setPid("pid.1");
        t1.setRelatesTo("relatesTo.1");
        t1.setRemoteHost("remoteHost.1");
        t1.setReplyAddress("replyAddress.1");
        t1.setThisHost("thisHost.1");
        t1.setTo("to.1");
       
        assertEquals(mapSize + 1, map.size());
        assertEquals(true, map.containsKey(new Long(99991)));
        assertEquals("action.1", map.get(new Long(99991)).get(ThreadData.ACTION));        
        assertEquals("from.1", map.get(new Long(99991)).get(ThreadData.FROM));    
        assertEquals("messageId.1", map.get(new Long(99991)).get(ThreadData.MESSAGE));    
        assertEquals("pid.1", map.get(new Long(99991)).get(ThreadData.PID));    
        assertEquals("relatesTo.1", map.get(new Long(99991)).get(ThreadData.RELATESTO));    
        assertEquals("remoteHost.1", map.get(new Long(99991)).get(ThreadData.REMOTEHOST));    
        assertEquals("replyAddress.1", map.get(new Long(99991)).get(ThreadData.REPLY));    
        assertEquals("thisHost.1", map.get(new Long(99991)).get(ThreadData.THISHOST));    
        assertEquals("to.1", map.get(new Long(99991)).get(ThreadData.TO));    
        
        assertEquals("action.1", t1.getAction());        
        assertEquals("from.1", t1.getFrom());    
        assertEquals("messageId.1", t1.getMessageId());    
        assertEquals("pid.1", t1.getPid());    
        assertEquals("relatesTo.1", t1.getRelatesTo());    
        assertEquals("remoteHost.1", t1.getRemoteHost());    
        assertEquals("replyAddress.1", t1.getReplyAddress());    
        assertEquals("thisHost.1", t1.getThisHost());    
        assertEquals("to.1", t1.getTo());    
        
        t1.setTo("to.1.1");

        assertEquals("to.1.1", map.get(new Long(99991)).get(ThreadData.TO));    
        
        ThreadData t2 = new ThreadData(new Long(99992));
        t2.setAction("action.2");
        t2.setFrom("from.2");
        t2.setMessageId("messageId.2");
        t2.setPid("pid.2");
        t2.setRelatesTo("relatesTo.2");
        t2.setRemoteHost("remoteHost.2");
        t2.setReplyAddress("replyAddress.2");
        t2.setThisHost("thisHost.2");
        t2.setTo("to.2");
        
        assertEquals(mapSize + 2, map.size());
        assertEquals(true, map.containsKey(new Long(99992)));    
        
        ThreadData.clean(new Long(99991));
        assertTrue(map.get(new Long(99991)) == null);
        
        String out = t1.toString();
        assertTrue(StringUtils.containsIgnoreCase(out, "No map found"));
        
        out = t2.toString();
        assertTrue(StringUtils.contains(out, "99992"));
    }

}
