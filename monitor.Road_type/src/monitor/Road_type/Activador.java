package monitor.Road_type;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;


public class Activador implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorRoadType controlador = null;


	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activador.context = bundleContext;
		this.controlador = new ControladorRoadType(bundleContext, "monitorRoadType");
		this.controlador.registerThing();
		
		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + IRoadContext.class.getName() + ")";
		this.context.addServiceListener(controlador, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.controlador != null )
			this.controlador.unregisterThing();
		
		Activador.context = null;
	}

}