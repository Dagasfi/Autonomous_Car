package monitor.congestion;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorCongestion controlador = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.controlador = new ControladorCongestion(bundleContext, "Monitor_congestion");
		this.controlador.registerThing();
		
		
		
		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + ICongestionContext.class.getName() + ")";

		this.context.addServiceListener(controlador, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.context.removeServiceListener(controlador);
		Activator.context = null;
	}

}
