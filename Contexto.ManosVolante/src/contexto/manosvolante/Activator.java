package contexto.manosvolante;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


import sua.autonomouscar.context.interfaces.IManosVolanteContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected IManosVolanteContext contextoManosVolante = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoManosVolante = new ContextoManosVolante(bundleContext, "probe_ManosVolante");
	}
	

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
