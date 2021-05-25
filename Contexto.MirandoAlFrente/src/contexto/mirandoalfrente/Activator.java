package contexto.mirandoalfrente;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IMirandoAlFrente;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected IMirandoAlFrente contextoMirandoAlFrente = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoMirandoAlFrente = new ContextoMirandoAlFrente(bundleContext, "probe_MirandoAlFrente");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
