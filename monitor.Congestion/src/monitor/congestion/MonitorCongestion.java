package monitor.congestion;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.monitors.interfaces.IMonitor;

public class MonitorCongestion extends Monitor implements ServiceListener{


	public MonitorCongestion(BundleContext context, String id) {
		super(context, id);
		
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		// TODO Auto-generated method stub
		
	}


}
