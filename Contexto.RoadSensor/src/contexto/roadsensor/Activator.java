package contexto.roadsensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.context.interfaces.IRoadContext;

public class Activator implements BundleActivator {
	
	private static BundleContext context;
	
	protected IRoadContext contextoSensorDistancia = null;

	static BundleContext getContext() {
		return context;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoSensorDistancia = new ContextoRoadSensor(bundleContext, "probe_roadSensor");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
