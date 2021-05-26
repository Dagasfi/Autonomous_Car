package contexto.distancesensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.context.interfaces.IDistanceSensorContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected IDistanceSensorContext contextoSensorDistancia = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoSensorDistancia = new ContextoDistanceSensor(bundleContext, "probe_distanceSensor");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
