package sua.autonomouscar.properties;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.interfaces.ESensorStatus;
import sua.autonomouscar.interfaces.EAutonomousLevel;
import sua.autonomouscar.interfaces.EDrivingMode;
import sua.autonomouscar.interfaces.EDriverAttention;

public class PropList {
	ERoadType road_type_prop = null;
	EAutonomousLevel nivel_autonomia = null;
	EDrivingMode modo_de_conduccion = null;
	ERoadStatus congestion_prop = null;
	/* EFaceStatus no tiene todos los estados de Driver_attention*/
	EDriverAttention user_status_prop = null;
	ESensorStatus sensor_status_prop = null;
	int user_location_prop = -1;
	
	public void setRoad_type_prop(ERoadType rtp)
	{
		road_type_prop = rtp;
	}
	
	public EAutonomousLevel getNivel_autonomia() {
		return nivel_autonomia;
	}

	public EDrivingMode getModo_de_conduccion() {
		return modo_de_conduccion;
	}

	public ERoadStatus getCongestion_prop() {
		return congestion_prop;
	}

	public EDriverAttention getUser_status_prop() {
		return user_status_prop;
	}

	public ESensorStatus getSensor_status_prop() {
		return sensor_status_prop;
	}

	public int getUser_location_prop() {
		return user_location_prop;
	}

	public ERoadType getRoad_type_prop()
	{
		return road_type_prop;
	}
	
	public void setNivel_autonomia(EAutonomousLevel na)
	{
		nivel_autonomia = na;
	}
	
	public void setModo_de_conduccion(EDrivingMode mc)
	{
		modo_de_conduccion = mc;
	}
	
	public void setCongestion_prop(ERoadStatus cp)
	{
		congestion_prop = cp;
	}
	
	public void setUser_status_prop(EDriverAttention rtp)
	{
		user_status_prop = rtp;
	}
	
	public void setSensor_status_prop(ESensorStatus ssp)
	{
		sensor_status_prop = ssp;
	}
	
	public void setUser_location_prop(int ulp)
	{
		user_location_prop = ulp;
	}
}