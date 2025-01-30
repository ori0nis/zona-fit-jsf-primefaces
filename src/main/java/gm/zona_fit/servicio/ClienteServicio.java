package gm.zona_fit.servicio;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// La notación Service es imperativa para que esta clase funcione correctamente.
@Service
public class ClienteServicio implements IClienteServicio{

    // Inyectamos el repositorio dentro del servicio para que puedan comunicarse:
    @Autowired
    private ClienteRepositorio clienteRepositorio; // En este caso sí es posible crear un objeto
    // a partir de una interfaz.

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = clienteRepositorio.findAll();
        return clientes;
    }

    @Override
    public Cliente buscarClientePorId(Integer idCliente) {
        Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null); // Añadimos .orElse(null)
        // para indicar que, en caso de no encontrar el id, se retorne null.
        return cliente;
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        clienteRepositorio.save(cliente); // El JPA hace un insert o un modify de manera interna, dependiendo
        // de los datos proporcionados.
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        clienteRepositorio.delete(cliente);

    }
}
