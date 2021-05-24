package contexto.ubicaciondriver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected IUbicacionDriverContext contextoUbicacionDriver = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoUbicacionDriver = new ContextoUbicacionDriver(bundleContext, "probe_UbicacionDriver");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
