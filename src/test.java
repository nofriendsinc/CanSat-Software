
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
				while(true)
				{
					try
					{
						Thread.sleep(1000);
						System.out.println(main.getLastPacket());
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
