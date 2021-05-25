package contexto.driversleeping;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoDriverSleeping extends Thing implements IDriverSleepingContext{
	
	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;

	public ContextoDriverSleeping(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IDriverSleepingContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setDriverSleeping(false);
		this.sr = this.context.registerService(IDriverSleepingContext.class, this, props);
	}

	@Override
	public boolean isDriverSleeping() {
		return (boolean)this.props.get("isSleeping");
	}

	@Override
	public void setDriverSleeping(boolean isSleeping) {
		this.props.put("isSleeping", isSleeping);
		this._updateProps();
		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

}
