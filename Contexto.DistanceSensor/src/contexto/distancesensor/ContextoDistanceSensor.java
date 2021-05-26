package contexto.distancesensor;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.context.interfaces.IDistanceSensorContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoDistanceSensor extends Thing implements IDistanceSensorContext{

	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	
	public ContextoDistanceSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IDistanceSensorContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setDistanceSensorWorkingMode(true);
		this.sr = this.context.registerService(IDistanceSensorContext.class, this, props);
	}
	
	@Override
	public boolean isDistanceSensorWorking() {
		return (boolean)this.props.get("isWorking");
	}


	@Override
	public void setDistanceSensorWorkingMode(boolean isWorking) {
		this.props.put("isWorking", isWorking);
		this._updateProps();
		
	}

	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}
}
