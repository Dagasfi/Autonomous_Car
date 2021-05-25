package contexto.ubicaciondriver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;

public class Activator implements BundleActivator{
	
	private static BundleContext context;
	
	protected IUbicacionDriverContext contextoUbicacionDriverContext = null;
	
	static BundleContext getContext() {
		return context;
	}


	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoUbicacionDriverContext = new ContextoUbicacionDriver(bundleContext, "probe_ubicacionDriver");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}
	
}
