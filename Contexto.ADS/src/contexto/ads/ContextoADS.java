package contexto.ads;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IADSContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoADS extends Thing implements IADSContext{
	

	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	

	
	
	
	public ContextoADS(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IADSContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setADSLevel(0);
		this.setADSType(this.MANUAL);
		this.sr = this.context.registerService(IADSContext.class, this, props);
	}
	
	@Override
	public int getADSLevel() {
		return (int) this.props.get("level");
	}

	@Override
	public void setADSLevel(int level) {
		this.props.put("level", level);
		this._updateProps();		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

	@Override
	public String getADSType() {
		return (String) this.props.get("type");
	}

	@Override
	public void setADSType(String type) {
		this.props.put("type", type);
		this._updateProps();			
	}
}
