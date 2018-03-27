
public class test
{
	public static void main(String[] args) throws Exception
	{
		SerialTest main = new SerialTest();
		main.initialize();
		Thread t = new Thread()
		{
			public void run()
			{
				// the following line will keep this app alive for 1000 seconds,
				// waiting for events to occur and responding to them (printing
				// incoming messages to console).
				while(true)
				{
					try
					{
						Thread.sleep(1000);
						//System.out.println(main.pack.get(main.pack.size() - 1).getPacketCount());
					} catch (InterruptedException ie)
					{
					}
				}
			}
		};
		t.start();
		System.out.println("Started");
	}
}
