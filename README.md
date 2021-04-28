# SUA_ImplementacionBROS

## Descripción

Implementación de un vehículo autónomo para la asignatura Sistemas Ubicuos y Adaptativos.

## Dependencias

Desactivar los Target platform.
Activar solo OSGI
Activamos equinox.console
	Tiene dependencia con: org.apache.felix.gogo.runtime // org.apache.felix.gogo.shell // org.apache.felix.gogo.command
Activamos org.eclipse.osgi.services
Ativamos org.eclipse.osgi.util


## Modo de trabajo

Separar interfaces de implementaciones.

	Autonomouscar.interfaces
		- IEngine
		- 