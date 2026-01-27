package com.pkg.http.request;

import java.io.BufferedReader;
import java.io.IOException;

public class Request 
{
	RequestLine requestLine;
	
	public static Request requestFromReader(BufferedReader buff) throws IOException
	{
		return new Request();
	}
}