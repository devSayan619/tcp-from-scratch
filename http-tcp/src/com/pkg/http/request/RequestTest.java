package com.pkg.http.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class RequestTest 
{
    @Test
    void testRequestLineParse() throws IOException 
    {
    	String input = "GET / HTTP/1.1\\r\\nHost: localhost:42069\\r\\nUser-Agent: curl/7.81.0\\r\\nAccept: */*\\r\\n\\r\\n";
    	BufferedReader buffer = new BufferedReader(new StringReader(input));
    	
    	Request r = Request.requestFromReader(buffer);
    	
    	assertEquals("GET", r.requestLine.method);
    	assertEquals("/", r.requestLine.requestTarget);
    	assertEquals("1.1", r.requestLine.httpVersion);
    	assertNull(r);
    }

    @Test
    void testGoodGetRequestWithPath() throws IOException 
    {
    	String input = "GET /coffee HTTP/1.1\\r\\nHost: localhost:42069\\r\\nUser-Agent: curl/7.81.0\\r\\nAccept: */*\\r\\n\\r\\n";
    	BufferedReader buffer = new BufferedReader(new StringReader(input));
    	
    	Request r = Request.requestFromReader(buffer);
    	
    	assertEquals("GET", r.requestLine.method);
    	assertEquals("/coffee", r.requestLine.requestTarget);
    	assertEquals("1.1", r.requestLine.httpVersion);
    	assertNull(r);
    }
    
    
    @Test
    void testInvalidNumberOfParts()  
    {
    	String input = "/coffee HTTP/1.1\\r\\nHost: localhost:42069\\r\\nUser-Agent: curl/7.81.0\\r\\nAccept: */*\\r\\n\\r\\n";
    	BufferedReader buffer = new BufferedReader(new StringReader(input));
    	
    	assertThrows(Exception.class, ()->{
    		Request.requestFromReader(buffer);
    	});
    }
}