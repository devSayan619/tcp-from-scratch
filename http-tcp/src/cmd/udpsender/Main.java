package cmd.udpsender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main 
{
	public static void main(String[] args)
	{
		try 
		{
			DatagramSocket dc = new DatagramSocket();
			InetAddress iad = InetAddress.getByName("localhost");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			while(true)
			{
				System.out.print("> ");
				String line = reader.readLine();
				if(line ==null)
				{
					break;
				}
				
				byte[] buffer = line.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length,iad,42069);
				dc.send(packet);
			}
		} 
		catch (IOException  e)
		{
			e.printStackTrace();
		}
	}
}