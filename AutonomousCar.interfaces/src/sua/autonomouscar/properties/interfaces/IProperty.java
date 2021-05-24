package sua.autonomouscar.properties.interfaces;

import sua.autonomouscar.interfaces.EAutonomousLevel;
import sua.autonomouscar.interfaces.EDriverAttention;
import sua.autonomouscar.interfaces.EDrivingMode;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.interfaces.ESensorStatus;
import sua.autonomouscar.interfaces.IIdentifiable;

public interface IProperty extends IIdentifiable{
	
	
	public ERoadType getRoad_type_prop();
	public EAutonomousLevel getNivel_autonomia();
	public EDrivingMode getModo_de_conduccion();
	public ERoadStatus getCongestion_prop();
	/* EFaceStatus no tiene todos los estados de Driver_attention*/
	public EDriverAttention getUser_status_prop();
	public ESensorStatus getSensor_status_prop();
	public int getUser_location_prop();
	public String getMaxAutonomyLevel();
	
	public void setRoad_type_prop(ERoadType roadType);
	public void setNivel_autonomia(EAutonomousLevel autonomLevel);
	public void setModo_de_conduccion(EDrivingMode drivingMode);
	public void setCongestion_prop(ERoadStatus roadStatus);
	/* EFaceStatus no tiene todos los estados de Driver_attention*/
	public void setUser_status_prop(EDriverAttention driverAtt);
	public void setSensor_status_prop(ESensorStatus sensorStatus);
	public void setUser_location_prop(int location);
	public void setMaxAutonomyLevel(String level);
}
