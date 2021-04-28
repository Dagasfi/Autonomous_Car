package simulacion.sensortipocarretera.highway;
import org.osgi.framework.BundleActivator;


public class Activator implements BundleActivator {

		

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		this.contextoCarretera = new ContextoCarretera(bundleContext);
		
		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
