package contexto.ads;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ContextoADS contextoADS = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoADS = new ContextoADS(bundleContext, "probe_ADS");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
