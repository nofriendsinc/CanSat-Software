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
	private SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = {"COM5"};
	private BufferedReader input;
	private static OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private static int lineNum = 1;
	private static int index = 0;
	private static ArrayList<packet> pack = new ArrayList<packet>();
	private static boolean isReceiving = false;
	private static boolean doPrint = false;
	private static boolean isReady = false;

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
					if(!inputLine.substring(0, 2).equals("00"))	
					{
						isReceiving = true;
						doPrint = true;
						
						// <TEAM ID>,<MISSION TIME>,<PACKET COUNT>,<ALTITUDE>, <PRESSURE>,
						// <TEMP>,<VOLTAGE>,<GPS TIME>,<GPS LATITUDE>,<GPS LONGITUDE>,<GPS
						// ALTITUDE>,<GPS SATS>,<TILT X>,<TILT Y>,<TILT Z>,<SOFTWARE STATE>
						if(inputLine.substring(0,  inputLine.indexOf(',')).equals("5554"))
						{
							
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

	public packet getLastPacket()
	{
		if(pack.size() > 0)		return pack.get(pack.size() - 1);
		else					return null;
	}
}