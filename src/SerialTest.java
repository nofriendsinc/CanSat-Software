import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Enumeration;

public class SerialTest implements SerialPortEventListener
{
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = {"COM5"};
	private BufferedReader input;
	private static OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	static int lineNum = 1;
	static int index = 0;
	static ArrayList<packet> pack = new ArrayList<packet>();
	static boolean isReceiving = false;
	static boolean doPrint = false;
	static boolean isReady = false;

	public void initialize()
	{
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES)
			{
				if (currPortId.getName().equals(portName))
				{
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null)
		{
			System.out.println("Could not find COM port.");
			return;
		}

		try
		{
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e)
		{
			System.err.println(e.toString());
		}
	}

	public synchronized void close()
	{
		if (serialPort != null)
		{
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				String inputLine = "";
				if (input.ready())
				{
					inputLine = input.readLine();
					//System.out.println(inputLine);
					if(!inputLine.substring(0, 2).equals("00"))	
					{
						isReceiving = true;
						doPrint = true;
						switch (lineNum)
						{
						case 1:
							pack.add(new packet());
							pack.get(index).setMissionTime(Float.parseFloat(inputLine));
							System.out.println("t = " + pack.get(index).getPacketCount());
							lineNum = 2;
							break;
						case 2:
							pack.get(index).setPacketCount(Integer.parseInt(inputLine));
							System.out.println("p = " + pack.get(index).getPacketCount());
							lineNum = 3;
							break;
						case 3:
							pack.get(index).setTiltX(Double.parseDouble(inputLine));
							System.out.println("x = " + pack.get(index).getTiltX());
							lineNum = 4;
							break;
						case 4:
							pack.get(index).setTiltY(Double.parseDouble(inputLine));
							System.out.println("y = " + pack.get(index).getTiltY());
							lineNum = 5;
							break;
						case 5:
							pack.get(index).setTiltZ(Double.parseDouble(inputLine));
							System.out.println("z = " + pack.get(index).getTiltZ());
							lineNum = 1;
							index++;
							break;
						}
					}
					else	
					{
						isReceiving = false;
					}
					if(!isReceiving && doPrint)	
					{
						System.out.println("ERROR ERROR");
						doPrint = false;
					}
				}
			} catch (Exception e)
			{
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	public static void main(String[] args) throws Exception
	{
		/*SerialTest main = new SerialTest();
		main.initialize();
		Thread t = new Thread()
		{
			public void run()
			{
				// the following line will keep this app alive for 1000 seconds,
				// waiting for events to occur and responding to them (printing
				// incoming messages to console).
				try
				{
					Thread.sleep(1000000);
				} catch (InterruptedException ie)
				{
				}
			}
		};
		t.start();
		System.out.println("Started");*/
	}
}