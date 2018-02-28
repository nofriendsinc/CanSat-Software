
public class packet
{
	//Output Format:
	//<TEAM ID>,<MISSION TIME>,<PACKET COUNT>,<ALTITUDE>, <PRESSURE>,
	//<TEMP>,<VOLTAGE>,<GPS TIME>,<GPS LATITUDE>,<GPS LONGITUDE>,<GPS
	//ALTITUDE>,<GPS SATS>,<TILT X>,<TILT Y>,<TILT Z>,<SOFTWARE STATE>
	
	private float missionTime;
	private int packetCount;
	private double TiltX;;
	private double TiltY;
	private double TiltZ;
	
	public float getMissionTime()
	{
		return packetCount;
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
	
	public double getTiltX()
	{
		return TiltX;
	}
	
	public void setTiltX(double tiltX)
	{
		TiltX = tiltX;
	}
	
	public double getTiltY()
	{
		return TiltY;
	}
	
	public void setTiltY(double tiltY)
	{
		TiltY = tiltY;
	}
	
	public double getTiltZ()
	{
		return TiltZ;
	}
	
	public void setTiltZ(double tiltZ)
	{
		TiltZ = tiltZ;
	}
}
