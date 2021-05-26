# SUA_ImplementacionBROS

## Descripción

Implementación de un vehículo autónomo para la asignatura Sistemas Ubicuos y Adaptativos.

## Dependencias

- Desactivar los Target platform.
- Activar solo OSGI
- Activamos equinox.console
  - Tiene dependencia con::
  - org.apache.felix.gogo.runtime
  	- org.apache.felix.gogo.shell
  - org.apache.felix.gogo.command
- Activamos org.eclipse.osgi.services
- Ativamos org.eclipse.osgi.util



## Modo de trabajo

1) Separamos las interfaces de las implementaciones 

La convención de nombres ha de ser "I*" para interfaces.

2) Separamos los elementos del bucle MAPE-K

​	2.1) Contextos (Probes / Sensores)

​	2.2) Propiedades

​	2.3) Monitores

​	2.4) Reglas de adaptación



