package monitor.congestion;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.driving.l0.manual.L0_ManualDriving;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.monitors.interfaces.IMonitor;

public class ControladorCongestion extends Monitor implements ServiceListener{
	
	
	protected BundleContext context = null;
	
	public ControladorCongestion(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}


	@Override
	public void serviceChanged(ServiceEvent event) {
		System.out.println("SE HA DETECTADO UN CAMBIO");
		System.out.println("EVENTOO:::: " + event.getServiceReference());
		
		ICongestionContext contextoCarretera = (ICongestionContext) this.context.getService(event.getServiceReference());
		ERoadStatus congestionType = contextoCarretera.getCongestion();
		ServiceReference ref = this.context.getServiceReference(L0_ManualDriving.class);
		if ( ref == null ) {
			System.out.println("[Controlador] No ACC found, nothing to do!");
			return;
		}
		System.out.println("SE HA ENCONTRADO, PASAMOS");
		L0_ManualDriving theDrivingMode = (L0_ManualDriving) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			
				switch (congestionType) {
				case FLUID:
					
					if ( !theDrivingMode.isDriving() ) {
						System.out.println("[Controlador] Auto-starting the MODE L0 when in a FLUID...");
						theDrivingMode.startDriving();
					}
					
					break;
					
				case JAM:
					if ( !theDrivingMode.isDriving() ) {
						System.out.println("[Controlador] Auto-starting the MODE L0 when in a JAMMM...");
						theDrivingMode.startDriving();
					}
					break;

				default:
					
					if ( theDrivingMode.isDriving() ) {
						System.out.println("[Controlador] De-activating the L0 MODE when NOT in JAM OR FLUID");
						theDrivingMode.stopDriving();
					}
					
					break;
				}
			
			break;

		default:
			break;
		}
		
	}
	
	

}
