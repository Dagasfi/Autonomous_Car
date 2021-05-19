package sua.autonomouscar.infrastructure.monitors;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.infrastructure.devices.DistanceSensor;
import sua.autonomouscar.monitors.interfaces.IMonitor;

public abstract class Monitor extends Thing implements IMonitor {

	public Monitor(BundleContext context, String id) {
		
		super(context, id);
		this.addImplementedInterface(IMonitor.class.getName());
	}

	@Override
	public void notifyRule(String ruleName) {
		// TODO Auto-generated method stub
		
	}

	

}
