package sua.autonomouscar.properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected PropList listaPropiedades = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.listaPropiedades = new PropList(bundleContext, "ALL_prop_list");
		this.listaPropiedades.registerThing();
		System.out.println("[ACTIVADOR] - Activamos las propiedades.");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
