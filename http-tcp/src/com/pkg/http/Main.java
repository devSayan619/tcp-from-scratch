package com.pkg.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Main 
{
	public static BlockingQueue<String> getLinesChannels(InputStream fis)
	{
		BlockingQueue<String> queB = new LinkedBlockingQueue<String>(10);
		
		new Thread(()->{
			
			String currentLine = "";
			try
			{
				byte[] buffer = new byte[8];
				int byteRead;
				
				while ((byteRead = fis.read(buffer)) != -1)
				{
					String chunk = new String(buffer, 0, byteRead);
//                System.out.println(chunk);
					String [] parts = chunk.split("\n",-1);
					for(int i =0; i<parts.length-1;++i)
					{
						 queB.put(currentLine+parts[i]);
						currentLine="";
					}
					currentLine +=parts[parts.length-1];
				}
				
				if(!currentLine.isEmpty())
				{
					queB.put(currentLine);
				}
				queB.put("EOF");
			} 
			
			catch (IOException | InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			finally
			{
				if (fis != null)
				{
					try
					{
						fis.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
		return queB;
	}
    public static void main(String[] args) 
    {
    	ServerSocket socket = null;
        try 
        {
        	socket  = new ServerSocket(6969);
        	System.out.println("Server listening on port 6969...");
        	while(true)
        	{	
        		Socket cliSocket = socket.accept();
        		System.out.println("Accepted connection");
        		BlockingQueue<String> queue = getLinesChannels(cliSocket.getInputStream());
        		while(true)
        		{
        			String msg = queue.take();
        			
        			if(msg.equals("EOF"))
        			{
        				System.out.println("Connection closed");
        				break;
        			}
        			System.out.println(msg);
        		}
        	}

		} 
        catch (IOException | InterruptedException e)
        {
			e.printStackTrace();
		}
        finally
        {
        	if(socket!=null)
        	{
        		try
        		{
        			socket.close();
        		}
        		catch(IOException e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
}
