package contexto.lidar;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.context.interfaces.ILidarContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ILidarContext contextoLidar = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoLidar = new ContextoLidar(bundleContext, "probe_Lidar");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
