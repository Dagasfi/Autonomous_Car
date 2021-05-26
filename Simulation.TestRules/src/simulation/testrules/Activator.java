package simulation.testrules;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.simulation.console.commands.MyCommandProvider;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

//		ICongestionContext contextoCongestion = null;
//		ServiceReference ref = (ServiceReference)this.context.getServiceReference(ICongestionContext.class);
//		if(ref != null) {
//			System.out.println("[SIM] - ROAD STATUS TO FLUID!!!");
//			contextoCongestion = (ICongestionContext) this.context.getService(ref);
//			contextoCongestion.setCongestion(ERoadStatus.FLUID);		
//		}
		
		
		IRoadSensor contextoRoad = null;
		ServiceReference ref2 = (ServiceReference)this.context.getServiceReference(IRoadSensor.class);
		if(ref2 != null) {
			System.out.println("[SIM] - PASAMOS A CITY!");
			contextoRoad = (IRoadSensor) this.context.getService(ref2);
			contextoRoad.setRoadStatus(ERoadStatus.FLUID);
			contextoRoad.setRoadType(ERoadType.CITY);
		}
		MyCommandProvider myCommands = new MyCommandProvider(this.context);
		myCommands.show();
		myCommands.road("type", "highway");
		myCommands.road("status", "jam");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		
		MyCommandProvider myCommands = new MyCommandProvider(this.context);
		myCommands.engine("accelerate", 3000);
		myCommands.road("type", "highway");
		myCommands.road("status", "fluid");
		Activator.context = null;

	}

}
