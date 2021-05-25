package monitor.ads;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.context.interfaces.IADSContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	protected ControladorADS controlador = null;
	
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		this.controlador = new ControladorADS(bundleContext, "monitorADS");
		this.controlador.registerThing();
		
		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + IADSContext.class.getName() + ")";
		this.context.addServiceListener(controlador, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.controlador != null )
			this.controlador.unregisterThing();
		
		Activator.context = null;
	}

}
