package sua.autonomouscar.context.interfaces;

public interface IADSContext {
	public static final String MANUAL = "L0_ManualDriving";
	
	public static final String ASSISTED= "L1_AssistedDriving";
	
	public static final String ADCRUISE = "L2_CruiseControl";
	public static final String LANEKEEPING = "L2_LaneKeepingAssist";
	
	public static final String CITY = "L3_CityChauffer";
	public static final String JAM = "L3_TrafficJamChauffer";
	public static final String HIGHWAY = "L3_HighwayChauffer";

	public int getADSLevel();
	public void setADSLevel(int level);  // Solo para simulación.
	
	public String getADSType();
	public void setADSType(String type);  // Solo para simulación.

}
