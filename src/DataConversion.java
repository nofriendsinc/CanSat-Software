import java.io.*;
import java.util.Scanner;
public class DataConversion
{
	public static void main(String[] args) throws FileNotFoundException
	{
		//Output Format:
		//<TEAM ID>,<MISSION TIME>,<PACKET COUNT>,<ALTITUDE>, <PRESSURE>,
		//<TEMP>,<VOLTAGE>,<GPS TIME>,<GPS LATITUDE>,<GPS LONGITUDE>,<GPS
		//ALTITUDE>,<GPS SATS>,<TILT X>,<TILT Y>,<TILT Z>,<SOFTWARE STATE>
		
		Scanner fileRead = null;
		String fileIn = "";
		String fileOut = "";
		
		FileOutputStream fileWrite = new FileOutputStream(new File("CANSAT2018_TLM_<TEAM_ID>_<TEAM_NAME>.csv"));
		PrintWriter writer = new PrintWriter(fileWrite);
		
		try
		{
			FileInputStream fileReader = new FileInputStream("input.txt");
			fileRead = new Scanner(fileReader);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		
		fileRead.nextLine();
		fileRead.nextLine();
		fileRead.nextLine();
		
		while(fileRead.hasNext())
		{
			fileIn = fileRead.nextLine().substring(34);
			fileOut = "";
			
			for(int i = 0; i < fileIn.length(); i += 2)
			{
		        char out = (char)Integer.parseInt(fileIn.substring(i, i+2), 16);
		        if(out == (char)9)
		        {
		        	out = ',';
		        }
		        writer.print(out);
			}
		}
		writer.close();
		//bob
	}
}
