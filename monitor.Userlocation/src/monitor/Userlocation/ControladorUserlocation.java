package monitor.Userlocation;


import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;


public class ControladorUserlocation extends Monitor implements ServiceListener{

	protected BundleContext context = null;
	
	public ControladorUserlocation(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}
	
	@Override
	public void serviceChanged(ServiceEvent event) {
		// TODO Auto-generated method stub
		IUbicacionDriverContext contextoUbicacionDriver = (IUbicacionDriverContext) this.context.getService(event.getServiceReference());
		int ubicacionDriver = contextoUbicacionDriver.getUbicacionDriver();
		
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			propertiesList.setUser_location_prop(ubicacionDriver);
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: userlocation_prop=" + ubicacionDriver);			
			break;
		default:
			break;
		}
	}


}
