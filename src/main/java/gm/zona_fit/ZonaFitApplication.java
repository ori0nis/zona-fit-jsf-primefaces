package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

// Esta app ya es de Spring por default
// Implementamos CommandLikeRunner para convertir esta app en una de consola
// @SpringBootApplication // Comentamos para poder levantar la app desde la Forma
public class ZonaFitApplication implements CommandLineRunner {

	// Inyectamos dependencia de servicio
	@Autowired
	private IClienteServicio clienteServicio;

	// Cuando trabajamos con frameworks, necesitamos un loggeador de consola más avanzado:
	private static final Logger logger =
			LoggerFactory.getLogger(ZonaFitApplication.class);

	// Definimos una variable que nos permite crear saltos de línea al utilizar logger
	String nl = System.lineSeparator();

    public static void main(String[] args) {
		// Este es el método que nos permite ejecutar la app (no funcionará hasta que hagamos
		// la configuración con la BD.
		logger.info("Iniciando la app"); // Levantamos la fábrica de Spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("App finalizada!");
	}

	@Override
	public void run(String... args) throws Exception {
		// Después de levantar la fábrica de Spring (método main), podemos levantar la app
		// A partir de ahora, este es nuestro método main.
		logger.info(nl + "*** App de Zona Fit ***");
		appZonaFit();
	}

	// Los métodos ya no pueden ser estáticos porque provienen de run
	private void appZonaFit(){
		var salir = false;
		Scanner consola = new Scanner(System.in);

		while(!salir){
			try{
				logger.info("""
				Bienvenido a Zona Fit. Menú:\s
				\t1. Buscar cliente por id
				\t2. Ver la lista de clientes
				\t3. Agregar un cliente
				\t4. Modificar un cliente
				\t5. Eliminar un cliente
				\t6. Salir
				Elige tu opción:\s
			""");
				var opcion = Integer.parseInt(consola.nextLine());
				switch (opcion){
					case 1 -> {
						logger.info("Introduce el id del cliente: ");
						var idCliente = Integer.parseInt(consola.nextLine());
						Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
						if(cliente != null){
							logger.info("Cliente encontrado: " + cliente + nl);
						} else {
							logger.info("Cliente no encontrado: " + cliente + nl);
						}
					}
					case 2 -> {
						logger.info(nl + "--- Lista de clientes ---" + nl);
						var clientes = clienteServicio.listarClientes();
						clientes.forEach(cliente -> logger.info(cliente.toString() + nl));
					}
					case 3 -> {
						logger.info("Introduce el cliente que deseas agregar: ");
						logger.info("Nombre: ");
						var nombre = consola.nextLine();
						logger.info("Apellido: ");
						var apellido = consola.nextLine();
						logger.info("Número de membresía: ");
						var numeroMembresia = Integer.parseInt(consola.nextLine());
						Cliente cliente = new Cliente();
						// Aquí no tenemos constructores como en la anterior app, así que utilizamos los métodos set:
						cliente.setNombre(nombre);
						cliente.setApellido(apellido);
						cliente.setMembresia(numeroMembresia);
						clienteServicio.guardarCliente(cliente);
						logger.info("Cliente agregado: " + cliente + nl);
					}
					case 4 -> {
						logger.info("Id del cliente: ");
						var idCliente = Integer.parseInt(consola.nextLine());
						Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
						if(cliente != null){
							logger.info("Nombre: ");
							var nombre = consola.nextLine();
							logger.info("Apellido: ");
							var apellido = consola.nextLine();
							logger.info("Número de membresía: ");
							var numeroMembresia = Integer.parseInt(consola.nextLine());
							cliente.setNombre(nombre);
							cliente.setApellido(apellido);
							cliente.setMembresia(numeroMembresia);
							clienteServicio.guardarCliente(cliente);
							logger.info("Cliente modificado: " + cliente + nl);
						} else {
							logger.info("El cliente no se encontró: " + cliente + nl);
						}
					}
					case 5 -> {
						logger.info("Id del cliente: ");
						var idCliente = Integer.parseInt(consola.nextLine());
						var cliente = clienteServicio.buscarClientePorId(idCliente);
						if(cliente != null){
							clienteServicio.eliminarCliente(cliente);
							logger.info("Cliente eliminado: " + cliente + nl);
						} else {
							logger.info("El cliente no se encontró: " + cliente + nl);
						}
					}
					case 6 -> {
						logger.info("Hasta pronto!" + nl + nl);
						salir = true;
					}
					default -> logger.info("Opción no reconocida");
				}
			}catch(Exception e){
                logger.error("Error al ejecutar la app: " + e.getMessage());
			}finally {
				logger.info(nl);
			}
		}
	}
}
