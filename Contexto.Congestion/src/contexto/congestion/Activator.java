package contexto.congestion;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.context.interfaces.ICongestionContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ICongestionContext contextoCongestion = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoCongestion = new ContextoCongestion(bundleContext, "probe_congestion");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
