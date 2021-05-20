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
		return (ERoadType) this.props.get("road_type_prop");
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
		this.props.put("road_type_prop", roadType);
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

	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}
	
	
}

