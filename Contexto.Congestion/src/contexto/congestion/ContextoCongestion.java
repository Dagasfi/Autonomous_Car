package contexto.congestion;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadStatus;

public class ContextoCongestion extends Thing implements ICongestionContext {
	
	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	
	public ContextoCongestion(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ICongestionContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setCongestion(ERoadStatus.UNKNOWN);
		this.sr = this.context.registerService(ICongestionContext.class, this, props);
		// this.registerThing();
	}
	
	@Override
	public ERoadStatus getCongestion() {
		return (ERoadStatus) this.props.get("type");
	}

	@Override
	public void setCongestion(ERoadStatus type) {
		this.props.put("type", type);
		this._updateProps();
	}

	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}
}
