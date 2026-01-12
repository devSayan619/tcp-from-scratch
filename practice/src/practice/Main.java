package practice;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main 
{
	public static void main(String[] args)
	{
		FileInputStream fir =null; 
		ByteArrayOutputStream byteArr =null;
		try 
		{
			fir = new FileInputStream("src/message.txt");
			byte[] buffer = new byte[8];
			byteArr = new ByteArrayOutputStream();
			int byteRead;
			while((byteRead=fir.read(buffer))!=-1)
			{
				for(int i = 0;i< byteRead; ++i)
				{
					byte b = buffer[i];
					if(b == 10)
					{
						String fullLine = byteArr.toString("UTF-8");
                        System.out.printf("read: %s\n", fullLine);
                        
                        // Clear the builder for the next line
                        byteArr.reset();
					}
					else
					{
						byteArr.write(b);
					}
				}
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if(fir!=null)
			{
				try 
				{
					fir.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}