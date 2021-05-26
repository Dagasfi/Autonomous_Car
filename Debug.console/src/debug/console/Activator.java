package debug.console;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.simulation.console.commands.MyCommandProvider;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		MyCommandProvider myCommands = new MyCommandProvider(this.context);
		myCommands.show();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
