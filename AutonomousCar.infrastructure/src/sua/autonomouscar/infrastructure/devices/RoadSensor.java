package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

public class RoadSensor extends Thing implements IRoadSensor {
	
	public static final String ROAD_TYPE = "road-type";
	public static final String ROAD_STATUS = "road-status";
	
	public static final String WORKINGMODE = "isworking";

	public RoadSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IRoadSensor.class.getName());
		this.setRoadType(ERoadType.STD_ROAD);
		this.setRoadStatus(ERoadStatus.FLUID);
		this.setWorking(true);
	}

	@Override
	public ERoadType getRoadType() {
		return (ERoadType) this.getProperty(RoadSensor.ROAD_TYPE);
	}

	@Override
	public ERoadStatus getRoadStatus() {
		return (ERoadStatus) this.getProperty(RoadSensor.ROAD_STATUS);
	}

	@Override
	public IRoadSensor setRoadType(ERoadType type) {
		this.setProperty(RoadSensor.ROAD_TYPE, type);
		return this;
	}
	
	@Override
	public IRoadSensor setRoadStatus(ERoadStatus status) {
		this.setProperty(RoadSensor.ROAD_STATUS, status);
		return this;
	}

	@Override
	public IRoadSensor setWorking(boolean mode) {
		this.setProperty(RoadSensor.WORKINGMODE, mode);
		return this;
	}

	@Override
	public boolean isWorking() {
		return (boolean) this.getProperty(RoadSensor.WORKINGMODE);

	}
	

}
