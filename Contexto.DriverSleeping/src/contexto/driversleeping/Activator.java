package contexto.driversleeping;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.context.interfaces.IDriverSleepingContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected IDriverSleepingContext contextoDriverSleeping = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoDriverSleeping = new ContextoDriverSleeping(bundleContext, "probe_DriverSleeping");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
