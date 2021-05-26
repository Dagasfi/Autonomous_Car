package monitor.userstatus;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IMirandoAlFrente;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;
import sua.autonomouscar.interfaces.EDriverAttention;

public class ControladorUserStatus extends Monitor implements ServiceListener{
	
	
	protected BundleContext context = null;
	
	private boolean manosVolanteContext = false;
	private boolean driverSleepingContext = false;
	private boolean mirandoAlFrenteContext = false;
	private int UbicacionDriverContext = 0;

	public ControladorUserStatus(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		
		Object service = this.context.getService(event.getServiceReference());
		
		if (service instanceof IManosVolanteContext) {
			IManosVolanteContext manosVolanteContext = (IManosVolanteContext)  this.context.getService(event.getServiceReference());
			this.manosVolanteContext = manosVolanteContext.isManosVolante();
		}else if(service instanceof IDriverSleepingContext) {
			IDriverSleepingContext driverSleepingContext = (IDriverSleepingContext)  this.context.getService(event.getServiceReference());
			this.driverSleepingContext = driverSleepingContext.isDriverSleeping();
		}else if(service instanceof IUbicacionDriverContext) {
			IUbicacionDriverContext UbicacionDriverContext = (IUbicacionDriverContext)  this.context.getService(event.getServiceReference());
			this.UbicacionDriverContext = UbicacionDriverContext.getUbicacionDriver();
		}else if(service instanceof IMirandoAlFrente) {
			IMirandoAlFrente mirandoAlFrenteContext = (IMirandoAlFrente)  this.context.getService(event.getServiceReference());
			this.mirandoAlFrenteContext = mirandoAlFrenteContext.isMirandoAlFrente();
		}
		
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			if(this.mirandoAlFrenteContext && this.manosVolanteContext && this.UbicacionDriverContext == 1) {
				propertiesList.setUser_status_prop(EDriverAttention.Attentive);
				propertiesList.setHandOnWheel(true);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Attentive);
			}else if(this.manosVolanteContext && this.UbicacionDriverContext == 1) {
				propertiesList.setUser_status_prop(EDriverAttention.Hands_on_wheel);
				propertiesList.setHandOnWheel(true);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Hands_on_wheel);			
					
			}else if(!this.manosVolanteContext && this.UbicacionDriverContext != 1) {
				propertiesList.setUser_status_prop(EDriverAttention.Not_in_Driver_seat);
				propertiesList.setHandOnWheel(false);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Not_in_Driver_seat);			
			
			
			}else if(!this.manosVolanteContext && this.UbicacionDriverContext == 1) {
				propertiesList.setUser_status_prop(EDriverAttention.Not_hands_on_wheel);
				propertiesList.setHandOnWheel(false);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Not_hands_on_wheel);
			
			
			}else if(this.driverSleepingContext && this.UbicacionDriverContext == 1) {
				propertiesList.setUser_status_prop(EDriverAttention.Slept);
				//y con manos en el volante.
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Slept);
			
			
			}else if(!this.mirandoAlFrenteContext) {
				propertiesList.setUser_status_prop(EDriverAttention.Not_Attentive);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: user_status_prop=" + EDriverAttention.Not_Attentive);
			}
			break;

		default:
			break;
		
		}

	}
}


