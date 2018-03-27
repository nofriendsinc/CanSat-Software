
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
	private double softSate;

	public float getMissionTime()
	{
		return missionTime;
	}

	public void setMissionTime(float missionTime)
	{
		this.missionTime = missionTime;
	}

	public int getPacketCount()
	{
		return packetCount;
	}

	public void setPacketCount(int packetCount)
	{
		this.packetCount = packetCount;
	}

	public double getAltitude()
	{
		return altitude;
	}

	public void setAltitude(double altitude)
	{
		this.altitude = altitude;
	}

	public double getPressure()
	{
		return pressure;
	}

	public void setPressure(double pressure)
	{
		this.pressure = pressure;
	}

	public double getTemp()
	{
		return temp;
	}

	public void setTemp(double temp)
	{
		this.temp = temp;
	}

	public double getVoltage()
	{
		return voltage;
	}

	public void setVoltage(double voltage)
	{
		this.voltage = voltage;
	}

	public double getGpsTime()
	{
		return gpsTime;
	}

	public void setGpsTime(double gpsTime)
	{
		this.gpsTime = gpsTime;
	}

	public double getGpsLat()
	{
		return gpsLat;
	}

	public void setGpsLat(double gpsLat)
	{
		this.gpsLat = gpsLat;
	}

	public double getGpsLong()
	{
		return gpsLong;
	}

	public void setGpsLong(double gpsLong)
	{
		this.gpsLong = gpsLong;
	}

	public double getGpsAlt()
	{
		return gpsAlt;
	}

	public void setGpsAlt(double gpsAlt)
	{
		this.gpsAlt = gpsAlt;
	}

	public int getGpsSats()
	{
		return gpsSats;
	}

	public void setGpsSats(int gpsSats)
	{
		this.gpsSats = gpsSats;
	}

	public double getTiltX()
	{
		return tiltX;
	}

	public void setTiltX(double tiltX)
	{
		this.tiltX = tiltX;
	}

	public double getTiltY()
	{
		return tiltY;
	}

	public void setTiltY(double tiltY)
	{
		this.tiltY = tiltY;
	}

	public double getTiltZ()
	{
		return tiltZ;
	}

	public void setTiltZ(double tiltZ)
	{
		this.tiltZ = tiltZ;
	}

	public double getSoftSate()
	{
		return softSate;
	}

	public void setSoftSate(double softSate)
	{
		this.softSate = softSate;
	}

	public int getTeamID()
	{
		return teamID;
	}
}
