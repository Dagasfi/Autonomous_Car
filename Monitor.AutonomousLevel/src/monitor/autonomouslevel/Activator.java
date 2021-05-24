package monitor.autonomouslevel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorAutonomousLevel controlador = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.controlador = new ControladorAutonomousLevel(bundleContext, "Monitor_autonomousLevel");
		this.controlador.registerThing();

		//L1 Necesita Distancia, Linea y NotificationService
		//L2 Necesita además, del Engine y del Steering.
		//L3 Necesita además. del RoadSensor y de IHumanSensors
		String listenerFiltro = "(| (" + Constants.OBJECTCLASS + "=" + ILineSensor.class.getName() + ")"
						+ "(" + Constants.OBJECTCLASS + "=" + IDistanceSensor.class.getName() + ")"
						+ "(" + Constants.OBJECTCLASS + "=" + IHumanSensors.class.getName() + ")"
						+ "(" + Constants.OBJECTCLASS + "=" + IRoadSensor.class.getName() + ")"		
						+ ")";
		
		
		this.context.addServiceListener(controlador, listenerFiltro);
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if(this.controlador != null) {
			this.controlador.unregisterThing();	
		}
		Activator.context = null;
	}

}
