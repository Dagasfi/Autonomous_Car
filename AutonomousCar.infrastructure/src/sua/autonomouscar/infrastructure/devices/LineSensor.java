package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.infrastructure.Thing;

public class LineSensor extends Thing implements ILineSensor {
	
	public static final String DETECTION = "detection";
	public static final String WORKINGMODE = "isworking";

	
	public LineSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ILineSensor.class.getName());
		this.setWorking(true);
		this.setLineDetected(false);
	}

	@Override
	public boolean isLineDetected() {
		return (boolean) this.getProperty(LineSensor.DETECTION);
	}
	
	@Override
	public ILineSensor setLineDetected(boolean value) {
		this.setProperty(LineSensor.DETECTION, value);
		return this;
	}

	@Override
	public boolean isWorking() {
		return (boolean) this.getProperty(LineSensor.WORKINGMODE);
	}


	@Override
	public void setWorking(boolean mode) {
		this.setProperty(LineSensor.WORKINGMODE, mode);		
	}



}
