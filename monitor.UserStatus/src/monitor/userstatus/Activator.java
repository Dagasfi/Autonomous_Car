package monitor.userstatus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IMirandoAlFrente;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;
import sua.autonomouscar.devices.interfaces.IThing;


public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ControladorUserStatus controlador = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.controlador = new ControladorUserStatus(bundleContext, "Monitor_User");
		this.controlador.registerThing();
		
		String listenerFiltro = "(| (" + Constants.OBJECTCLASS + "=" + IDriverSleepingContext.class.getName()
			+ ")(" + Constants.OBJECTCLASS + "=" + IManosVolanteContext.class.getName() 
			+ ")(" + Constants.OBJECTCLASS + "=" + IMirandoAlFrente.class.getName()
			+ ")(" + Constants.OBJECTCLASS + "=" + IUbicacionDriverContext.class.getName() + "))";	
		
		this.context.addServiceListener(controlador, listenerFiltro);
	}
	
	


	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
