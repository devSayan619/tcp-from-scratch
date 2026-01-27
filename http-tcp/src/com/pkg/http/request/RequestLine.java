package com.pkg.http.request;

public class RequestLine 
{
	String httpVersion;
	String requestTarget;
	String method;

	public RequestLine(String httpVersion, String requestTarget, String method) 
	{
		super();
		this.httpVersion = httpVersion;
		this.requestTarget = requestTarget;
		this.method = method;
	}
}