# CertantVTV
Esta es mi resolución para el ejercicio propuesto de Verificación Técnica Vehicular

Solo falta realizar la baja de Vehiculos, Inspecciones e Inspectores

Enunciado:

Prueba técnica VTV
La Verificación Técnica Vehicular es el control obligatorio periódico del estado mecánico y de emisión de gases contaminantes de los automotores, y se le encarga a Certant realizar el software que generará los reportes de estas inspecciones.
.
Existen dos tipos de dueños de vehículo: “común” o “exento”, un exento no abona el valor de la inspección.
Para poder establecer que un vehículo finalizó una inspección de la VTV con el estado aprobatorio (apto) se tienen en cuenta dos tipos de controles: basados en observaciones y basados en mediciones. Las observaciones son el primer paso de una inspección y se relacionan con aquello que un inspector de la VTV puede analizar visualmente: se revisan las luces, patente, espejos, chasis, vidrios y seguridad y emergencia del vehículo.
Las mediciones son determinadas por la maquinaria que evalúa la suspensión, dirección y tren delantero; sistema de frenos y contaminación ambiental.
Una vez que se obtienen todos los datos se determina si un vehículo está: “apto” (si tiene observaciones y mediciones aptas), “condicional” (si tiene por lo menos una observación o medición condicional) o “rechazado” (si tiene por lo menos una observación o medición rechazada).
Un vehículo apto tiene una oblea con validez de un año, uno condicional no puede recibir la oblea, pero puede volver en el transcurso del día de la inspección con el arreglo en la parte desfavorable de esta para poder hacer la VTV nuevamente sin volver a pagar, uno rechazado no recibe oblea y tiene que volver a pagar para poder hacer la inspección.
El programa deberá poder listar al final del día los datos de los autos, con sus respectivos atributos a saber:
• Dominio
• Marca
• Modelo
• Nombre del propietario
Por su parte, una inspección deberá tener los siguientes datos.
• Nro de inspección
• Fecha de inspección
• Estado de inspección
• Si está o no exento
• Inspector a cargo
• Automóvil inspeccionado
Se le pide al desarrollador diseñar y poner en práctica los casos de uso necesarios para poder elevar los informes correspondientes, todo esto implementado en un programa desarrollado con el lenguaje de programación Java. • Una persona física en particular con sus datos, y si tiene más de un auto mostrar los datos de las inspecciones realizadas en la planta. • Corroborar que la fecha de vencimiento de la VTV para autos que la aprobaron sea de un año. • La lista de autos aptos, condicionales y rechazados. • Poder generar el alta, modificación y borrado de datos de automóviles, inspecciones e inspectores, ya que estamos modernizando los sistemas anteriores.
Para poder realizar lo pedido, se debe crear una base de conocimientos recargados, con no menos de 10 autos inspeccionados y sus correspondientes características, como así también de inspectores. Además de lo pedido, se le solicita al desarrollador un paquete con no menos de cinco pruebas unitarias. Algunas pautas: - No hay una interfaz preferida para la implementación del sistema: una interfaz de línea de comandos está bien, pero es obligatorio que el programa tenga un main que permita acceder a la solución de los requisitos. - No existe un método de almacenamiento preferido. Si se siente cómodo usando una base de datos, adelante (deseable, claro está). Si prefiere almacenar la información en un archivo de texto, también puede hacerlo.
