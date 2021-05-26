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

1. Separamos las interfaces de las implementaciones 

2. Separamos los elementos del bucle MAPE-K
   1. Contextos (Probes / Sensores)
   2. Propiedades
   3. Monitores
   4. Reglas de adaptación



## Flujo de control de versiones

Realizaremos un control de versiones típico. Dispondremos de una rama para hacer desarrollo y sobre ésta cada participante realizará cambios.

Es conveniente que los demás compañeros revisen los cambios de código realizado.

Es conveniente que los Commit y los PR sean descriptivos.

Nota:

La convención de nombres ha de ser "I*" para interfaces.

