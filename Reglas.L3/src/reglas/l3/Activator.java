package reglas.l3;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.properties.interfaces.IProperty;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected L3Rules ruleHandler = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.ruleHandler = new L3Rules(bundleContext, "rules_l3");
		this.ruleHandler.registerThing();
		
		String listenerFiltro = "(" + Constants.OBJECTCLASS + "=" + IProperty.class.getName() + ")";
		this.context.addServiceListener(ruleHandler, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.ruleHandler != null )
			this.ruleHandler.unregisterThing();
		Activator.context = null;
	}

}
