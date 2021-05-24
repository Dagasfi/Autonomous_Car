package contexto.ubicaciondriver;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.context.interfaces.IUbicacionDriverContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoUbicacionDriver extends Thing implements IUbicacionDriverContext{
	
	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;

	public ContextoUbicacionDriver(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IUbicacionDriverContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setUbicacionDriver(1);
		this.sr = this.context.registerService(IUbicacionDriverContext.class, this, props);
		
	}

	@Override
	public int getUbicacionDriver() {
		return (int)this.props.get("ubicacionDriver");
	}

	@Override
	public void setUbicacionDriver(int location) {
		this.props.put("ubicacionDriver", location);
		this._updateProps();
		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

}
