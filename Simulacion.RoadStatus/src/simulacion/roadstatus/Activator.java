package simulacion.roadstatus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.context.interfaces.IDistanceSensorContext;
import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IMirandoAlFrente;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.infrastructure.devices.DistanceSensor;
import sua.autonomouscar.interfaces.ERoadStatus;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ICongestionContext contextoCongestion = null;
		ServiceReference ref = (ServiceReference)this.context.getServiceReference(ICongestionContext.class);
		if(ref != null) {
			System.out.println("[SIM] - ROAD STATUS TO JAM!!!");
			contextoCongestion = (ICongestionContext) this.context.getService(ref);
			contextoCongestion.setCongestion(ERoadStatus.JAM);		
		}
		
		IDistanceSensorContext contextoSensorDistancia= null;
		ServiceReference ref2 = (ServiceReference)this.context.getServiceReference(IDistanceSensorContext.class);
		
		if(ref2 != null) {
			System.out.println("[SIM] - DISTANCE SENSOR TO False!!!");
			contextoSensorDistancia = (IDistanceSensorContext) this.context.getService(ref2);
			// contextoSensorDistancia.setDistanceSensorWorkingMode(false);;	
			System.out.println("[SIM] - DISTANCE SENSOR TO False!!!");

		}
		
    ServiceReference ref3 = (ServiceReference)this.context.getServiceReference(IDistanceSensor.class);
		
		if(ref3 != null) {
			System.out.println("[SIM] - DISTANCE SENSOR TO False!!!");
			DistanceSensor sensorDistancia = (DistanceSensor) this.context.getService(ref3);
			// sensorDistancia.setWorking(false);
			System.out.println("[SIM] - "+ sensorDistancia.getId() +" SENSOR TO False!!!");

		}
    
		IDriverSleepingContext contextoDriverSleeping= null;
		ServiceReference ref4 = (ServiceReference)this.context.getServiceReference(IDriverSleepingContext.class);
		
		if(ref4 != null) {
			System.out.println("[SIM] - DRIVER SLEEPING TO False!!!");
			contextoDriverSleeping = (IDriverSleepingContext) this.context.getService(ref4);
			contextoDriverSleeping.setDriverSleeping(false);;	
			System.out.println("[SIM] - DRIVER SLEEPING TO False!!!");

		}
		
		IMirandoAlFrente contextoMirandoAlFrente= null;
		ServiceReference ref5 = (ServiceReference)this.context.getServiceReference(IMirandoAlFrente.class);
		
		if(ref5 != null) {
			System.out.println("[SIM] - Mirando Al Frente TO true!!!");
			contextoMirandoAlFrente = (IMirandoAlFrente) this.context.getService(ref5);
			contextoMirandoAlFrente.setMirandoAlFrente(true);	
			System.out.println("[SIM] - Mirando Al Frente TO true!!!");

		}
		
		IUbicacionDriverContext contextoUbicacionDriver = null;
		ServiceReference ref6 = (ServiceReference)this.context.getServiceReference(IUbicacionDriverContext.class);
		
		if(ref6 != null) {
			System.out.println("[SIM] - Ubicacion Driver TO 1!!!");
			contextoUbicacionDriver = (IUbicacionDriverContext) this.context.getService(ref6);
			contextoUbicacionDriver.setUbicacionDriver(1);	
			System.out.println("[SIM] - Ubicacion Driver TO 1!!!");

		}
		
		IManosVolanteContext contextoManosVolanteContext = null;
		ServiceReference ref7 = (ServiceReference)this.context.getServiceReference(IManosVolanteContext.class);
		
		if(ref7 != null) {
			System.out.println("[SIM] - Manos Volante TO True!!!");
			contextoManosVolanteContext = (IManosVolanteContext) this.context.getService(ref7);
			contextoManosVolanteContext.setManosVolante(true);	
			System.out.println("[SIM] - Manos Volante TO True!!!");

		}
		System.out.println("[SIM]- STOPPED.");
		
		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ICongestionContext contextoCongestion = null;
		ServiceReference ref = (ServiceReference)this.context.getServiceReference(ICongestionContext.class);
		if(ref != null) {
			System.out.println("[SIM] - ROAD STATUS TO FLUID!!!");
			contextoCongestion = (ICongestionContext) this.context.getService(ref);
			contextoCongestion.setCongestion(ERoadStatus.FLUID);		
		}
		Activator.context = null;
	}

}
