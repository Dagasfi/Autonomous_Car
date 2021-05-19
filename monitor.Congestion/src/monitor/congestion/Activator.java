package monitor.congestion;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.monitors.Monitor;


public class Activator implements BundleActivator {

	private static BundleContext context;
	protected Monitor monitor = null;


	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.monitor = new MonitorCongestion(bundleContext, "monitorCongestion");
		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.monitor != null )
			this.monitor.unregisterThing();
		
		Activator.context = null;
	}

}

