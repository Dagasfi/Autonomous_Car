package contexto.mirandoalfrente;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IMirandoAlFrente;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoMirandoAlFrente extends Thing implements IMirandoAlFrente{

	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	
	public ContextoMirandoAlFrente(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IMirandoAlFrente.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setMirandoAlFrente(true);
		this.sr = this.context.registerService(IMirandoAlFrente.class, this, props);
	}

	@Override
	public boolean isMirandoAlFrente() {
		return (boolean)this.props.get("isMirandoAlFrente");
	}

	@Override
	public void setMirandoAlFrente(boolean isMirandoAlFrente) {
		this.props.put("isMirandoAlFrente", isMirandoAlFrente);
		this._updateProps();
		
	}
	
	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

}
