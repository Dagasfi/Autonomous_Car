package monitor.Userlocation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;


import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorUserlocation controlador = null;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		// TODO Auto-generated method stub
		Activator.context = bundleContext;
		this.controlador = new ControladorUserlocation(bundleContext, "monitorUserLocation");
		this.controlador.registerThing();
		
		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + IUbicacionDriverContext.class.getName() + ")";
		this.context.addServiceListener(controlador, listenerFiltro);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}