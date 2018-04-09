
public class packet
{
	// Output Format:
	// <TEAM ID>,<MISSION TIME>,<PACKET COUNT>,<ALTITUDE>, <PRESSURE>,
	// <TEMP>,<VOLTAGE>,<GPS TIME>,<GPS LATITUDE>,<GPS LONGITUDE>,<GPS
	// ALTITUDE>,<GPS SATS>,<TILT X>,<TILT Y>,<TILT Z>,<SOFTWARE STATE>

	private final int teamID = 5554;
	private float missionTime;
	private int packetCount;
	private double altitude;
	private double pressure;
	private double temp;
	private double voltage;
	private double gpsTime;
	private double gpsLat;
	private double gpsLong;
	private double gpsAlt;
	private int gpsSats;
	private double tiltX;;
	private double tiltY;
	private double tiltZ;
	private int softState;

	public packet(String inputLine)
	{
		System.out.println("Pass");
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		missionTime = Integer.parseInt(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		packetCount = Integer.parseInt(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		altitude = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		pressure = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		temp = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		voltage = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		gpsTime = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		gpsLat = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		gpsLong = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		gpsAlt = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		gpsSats = Integer.parseInt(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		tiltX = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));	
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		tiltY = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		tiltZ = Double.parseDouble(inputLine.substring(0,  inputLine.indexOf(',')));
		
		inputLine = inputLine.substring(inputLine.indexOf(',') + 1);
		softState = Integer.parseInt(inputLine.substring(0,  inputLine.indexOf(',')));
	}
	
	public float getMissionTime()
	{
		return missionTime;
	}

	public float setMissionTime(float missionTime)
	{
		return this.missionTime = missionTime;
	}

	public int getPacketCount()
	{
		return packetCount;
	}

	public int setPacketCount(int packetCount)
	{
		return this.packetCount = packetCount;
	}

	public double getAltitude()
	{
		return altitude;
	}

	public double setAltitude(double altitude)
	{
		return this.altitude = altitude;
	}

	public double getPressure()
	{
		return pressure;
	}

	public double setPressure(double pressure)
	{
		return this.pressure = pressure;
	}

	public double getTemp()
	{
		return temp;
	}

	public double setTemp(double temp)
	{
		return this.temp = temp;
	}

	public double getVoltage()
	{
		return voltage;
	}

	public double setVoltage(double voltage)
	{
		return this.voltage = voltage;
	}

	public double getGpsTime()
	{
		return gpsTime;
	}

	public double setGpsTime(double gpsTime)
	{
		return this.gpsTime = gpsTime;
	}

	public double getGpsLat()
	{
		return gpsLat;
	}

	public double setGpsLat(double gpsLat)
	{
		return this.gpsLat = gpsLat;
	}

	public double getGpsLong()
	{
		return gpsLong;
	}

	public double setGpsLong(double gpsLong)
	{
		return this.gpsLong = gpsLong;
	}

	public double getGpsAlt()
	{
		return gpsAlt;
	}

	public double setGpsAlt(double gpsAlt)
	{
		return this.gpsAlt = gpsAlt;
	}

	public int getGpsSats()
	{
		return gpsSats;
	}

	public int setGpsSats(int gpsSats)
	{
		return this.gpsSats = gpsSats;
	}

	public double getTiltX()
	{
		return tiltX;
	}

	public double setTiltX(double tiltX)
	{
		return this.tiltX = tiltX;
	}

	public double getTiltY()
	{
		return tiltY;
	}

	public double setTiltY(double tiltY)
	{
		return this.tiltY = tiltY;
	}

	public double getTiltZ()
	{
		return tiltZ;
	}

	public double setTiltZ(double tiltZ)
	{
		return this.tiltZ = tiltZ;
	}

	public int getSoftState()
	{
		return softState;
	}

	public int setSoftState(int softState)
	{
		return this.softState = softState;
	}

	public int getTeamID()
	{
		return teamID;
	}
}
