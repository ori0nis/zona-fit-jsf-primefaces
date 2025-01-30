// En Spring, el paquete/clase que hace las veces de DAO se suele llamar repositorio
package gm.zona_fit.repositorio;

import gm.zona_fit.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// Con esta simple clase, implementamos todos los métodos que comúnmente se utilizan en una clase
// que mapea una BD de manera automática (buscar, agregar, modificar, eliminar).
// Indicamos la clase que contiene los datos y el tipo de dato de la llave primaria
public interface ClienteRepositorio extends JpaRepository<Cliente,Integer> {

}
