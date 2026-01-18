package practice;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main 
{
	public static void main(String[] args)
	{
		FileInputStream fir =null; 
		try 
		{
			fir = new FileInputStream("src/message.txt");
			BlockingQueue<String> ans = getLinesChannel(fir);

			//put the output 
			while(true)
			{
				String line;
				try {
					line = ans.take();
					if(line.equals("EOF"))
					{
						break;
					}
					System.out.printf("read: %s\n",line);
				} 
				catch (InterruptedException e)
				{	
					Thread.currentThread().interrupt();
				}
			}

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//create the moduler function 
	public static BlockingQueue<String> getLinesChannel(InputStream fir)
	{
		BlockingQueue<String> bq = new LinkedBlockingDeque<>();

		// Start the thread
		Thread worker =new Thread(()->{
			ByteArrayOutputStream byteArr =null;
			try 
			{
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
							try 
							{
								bq.put(fullLine);
							}
							catch (InterruptedException e)
							{
								Thread.currentThread().interrupt();
							}
							byteArr.reset();
						}
						else
						{
							byteArr.write(b);
						}
					}
				}
//				old code it's have an error
//				if(byteArr.size()>0)
//				{
//					bq.put("EOF");
//				}
				
				// FIXED CODE

				// 1. First, rescue any leftover text that didn't have a \n
				if(byteArr.size() > 0)
				{
				    String lastLine = byteArr.toString("UTF-8");
				    try
				    {
						bq.put(lastLine);
					}
				    catch (InterruptedException e)
				    {
						Thread.currentThread().interrupt();
					}
				}

				// 2. ALWAYS send the Poison Pill, no matter what happened before
				try
				{
					bq.put("EOF");
				} 
				catch (InterruptedException e) 
				{
					Thread.currentThread().interrupt();
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
			
		});

		worker.start();
		return bq;
	}
}