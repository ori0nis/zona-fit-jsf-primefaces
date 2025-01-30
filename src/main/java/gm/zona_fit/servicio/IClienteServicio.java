package gm.zona_fit.servicio;

import gm.zona_fit.modelo.Cliente;
import java.util.List;

// Creamos una interfaz de servicio para poder afinar los métodos que ya nos aporta el repositorio.
// Es decir, el repositorio nos da los métodos básicos y con el servicio podemos meter validaciones (if,
// while, acciones específicas).
// Utilizaremos el repositorio para acciones específicas y el servicio para concretas.
public interface IClienteServicio {
    public List<Cliente> listarClientes();

    public Cliente buscarClientePorId(Integer idCliente);

    // Este método hará inserciones (si id = null) y actualizaciones (si id!= null)
    public void guardarCliente(Cliente cliente);

    public void eliminarCliente(Cliente cliente);
}
