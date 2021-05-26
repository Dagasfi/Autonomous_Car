package monitor.sensorstatus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.context.interfaces.IDistanceSensorContext;
import sua.autonomouscar.context.interfaces.ILidarContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorSensorStatus controlador = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.controlador = new ControladorSensorStatus(bundleContext, "Monitor_Sensor");
		this.controlador.registerThing();
		
		String listenerFiltro = "(| (" + Constants.OBJECTCLASS + "=" + ILidarContext.class.getName()
				+ ")(" + Constants.OBJECTCLASS + "=" + IDistanceSensorContext.class.getName() + "))";

//		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + IDistanceSensorContext.class.getName() + ")";
		this.context.addServiceListener(controlador, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
