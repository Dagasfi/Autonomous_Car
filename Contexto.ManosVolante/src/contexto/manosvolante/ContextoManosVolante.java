package contexto.manosvolante;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IDriverSleepingContext;
import sua.autonomouscar.context.interfaces.IManosVolanteContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoManosVolante extends Thing implements IManosVolanteContext{
	
	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;

	public ContextoManosVolante(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IManosVolanteContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setManosVolante(false);
		this.sr = this.context.registerService(IManosVolanteContext.class, this, props);
	}

	@Override
	public boolean isManosVolante() {
		return (boolean)this.props.get("isManosVolante");
	}

	@Override
	public void setManosVolante(boolean isManosVolante) {
		this.props.put("isManosVolante", isManosVolante);
		this._updateProps();
		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

	

}
