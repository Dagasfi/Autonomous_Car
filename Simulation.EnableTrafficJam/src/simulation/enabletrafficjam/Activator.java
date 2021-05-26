package simulation.enabletrafficjam;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.infrastructure.devices.DistanceSensor;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		ServiceReference ref3 = (ServiceReference)this.context.getServiceReference(IDistanceSensor.class);
			
		if(ref3 != null) {
			System.out.println("[SIM] - DISTANCE SENSOR TO False!!!");
			DistanceSensor sensorDistancia = (DistanceSensor) this.context.getService(ref3);
			 sensorDistancia.setWorking(false);
			System.out.println("[SIM] - "+ sensorDistancia.getId() +" SENSOR TO False!!!");

		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
