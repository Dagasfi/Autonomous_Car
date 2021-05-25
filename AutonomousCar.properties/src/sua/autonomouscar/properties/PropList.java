package sua.autonomouscar.properties;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.interfaces.ESensorStatus;
import sua.autonomouscar.properties.interfaces.IProperty;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.EAutonomousLevel;
import sua.autonomouscar.interfaces.EDrivingMode;
import sua.autonomouscar.interfaces.EDriverAttention;

public class PropList extends Thing implements IProperty{
	
	

	public static final String ROADTYPEPROP = "road_type_prop";
	public static final String LEVELAUTONOMY0 = "0";
	public static final String LEVELAUTONOMY1 = "1";
	public static final String LEVELAUTONOMY2 = "2";
	public static final String LEVELAUTONOMY3 = "3";
	
	
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	public PropList(BundleContext context, String id) {
		
		super(context, id);
		this.addImplementedInterface(IProperty.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.sr = this.context.registerService(IProperty.class, this, props);
	}

	@Override
	public ERoadType getRoad_type_prop() {
		return (ERoadType) this.props.get(PropList.ROADTYPEPROP);
	}

	@Override
	public EAutonomousLevel getNivel_autonomia() {
		return (EAutonomousLevel) this.props.get("nivel_autonomia_prop");
	}

	@Override
	public EDrivingMode getModo_de_conduccion() {
		return (EDrivingMode) this.props.get("modo_conduccion_prop");
	}

	@Override
	public ERoadStatus getCongestion_prop() {
		return (ERoadStatus) this.props.get("congestion_prop");
	}

	@Override
	public EDriverAttention getUser_status_prop() {
		return (EDriverAttention) this.props.get("user_status_prop");
	}

	@Override
	public ESensorStatus getSensor_status_prop() {
		return (ESensorStatus) this.props.get("sensor_status_prop");
	}

	@Override
	public int getUser_location_prop() {
		return (int) this.props.get("user_location_prop");
	}

	@Override
	public void setRoad_type_prop(ERoadType roadType) {
		this.props.put(PropList.ROADTYPEPROP, roadType);
		this._updateProps();
	}

	@Override
	public void setNivel_autonomia(EAutonomousLevel autonomLevel) {
		this.props.put("nivel_autonomia_prop", autonomLevel);
		this._updateProps();
	}

	@Override
	public void setModo_de_conduccion(EDrivingMode drivingMode) {
		this.props.put("modo_conduccion_prop", drivingMode);
		this._updateProps();
	}

	@Override
	public void setCongestion_prop(ERoadStatus roadStatus) {
		this.props.put("congestion_prop", roadStatus);
		this._updateProps();
	}

	@Override
	public void setUser_status_prop(EDriverAttention driverAtt) {
		this.props.put("user_status_prop", driverAtt);
		this._updateProps();
	}

	@Override
	public void setSensor_status_prop(ESensorStatus sensorStatus) {
		this.props.put("sensor_status_prop", sensorStatus);
		this._updateProps();
	}

	@Override
	public void setUser_location_prop(int location) {
		this.props.put("user_location_prop", location);
		this._updateProps();
	}

	@Override
	public String getMaxAutonomyLevel() {
		return (String) this.props.get("max_autonomy_level_prop");
	}

	@Override
	public void setMaxAutonomyLevel(String level) {
		this.props.put("max_autonomy_level_prop", level);
		this._updateProps();
	}

	@Override
	public int getCurrentADSLevel() {
		return (int) this.props.get("current_ADS_level_prop");
	}

	@Override
	public void setCurrentADSLevel_prop(int level) {
		this.props.put("current_ADS_level_prop", level);
		this._updateProps();
		
	}

	@Override
	public String getCurrentADSType() {
		return (String) this.props.get("current_ADS_type_prop");
	}

	@Override
	public void setCurrentADSType_prop(String type) {
		this.props.put("current_ADS_type_prop", type);
		this._updateProps();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dictionary<String, Object> getDevicesWorking() {
		return (Dictionary<String, Object>) this.props.get("devices_working_prop");
	}

	@Override
	public void setDevicesWorking(Dictionary<String, Object> devices) {
		this.props.put("devices_working_prop", devices);
		this._updateProps();
		
	}

	@Override
	public boolean getHandOnWheel() {
		return (boolean) this.props.get("hands_on_wheel_prop");
	}

	@Override
	public void setHandOnWheel(boolean handsOnWheel) {
		this.props.put("hands_on_wheel_prop", handsOnWheel);
		this._updateProps();		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}
	
}

