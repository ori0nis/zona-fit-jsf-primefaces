package gm.zona_fit.controlador;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

// Recordamos marcarlo como componente para hacerlo parte de la fábrica de Spring
@Component
@Data // Get y set automáticos
@ViewScoped // Hacemos que la info de IndexControlador esté disponible durante el tiempo de vida
// de la vista (como nuestra web solo tendrá una vista, es decir, una página, será siempre)
public class IndexControlador {

    @Autowired
    IClienteServicio clienteServicio;
    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;

    // Esto manda toda la info que se va ejecutando de nuestra app a un log:
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    // La instancia de esta clase está manejada automáticamente por parte de JSF, por lo que
    // no podemos utilizar constructores.

    @PostConstruct // Indicamos que después de que JSF mande llamar al constructor interno, se llame
    // a este método:
    public void init(){
        cargarDatos();
    }

    // Método público porque lo utilizaremos desde la vista
    public void cargarDatos(){
        // Ya hemos inyectado el servicio arriba, así que lo inicializamos:
        this.clientes = this.clienteServicio.listarClientes();
        this.clientes.forEach(cliente -> logger.info(cliente.toString()));
    }

    public void agregarCliente(){
        this.clienteSeleccionado = new Cliente();
    }

    public void guardarCliente(){
        logger.info("Cliente guardado: " + this.clienteSeleccionado);
        // Agregar:
        if(this.clienteSeleccionado.getId() == null){
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            this.clientes.add(this.clienteSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente agregado"));
        }
        // Modificar:
        else{
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente actualizado"));
        }
        // Ocultamos ventana modal:
        PrimeFaces.current().executeScript("PF('ventanaModalCliente').hide()");
        // Actualizamos tabla usando AJAX:
        PrimeFaces.current().ajax().update("forma-clientes:mensajes", "forma-clientes:clientes-tabla");
        // Reseteamos el cliente seleccionado para poder volver a trabajar con él
        this.clienteSeleccionado = null;
    }

    public void eliminarCliente(){
        logger.info("Cliente a eliminar: " + this.clienteSeleccionado);
        this.clienteServicio.eliminarCliente(this.clienteSeleccionado);
        // Para no tener que recargar toda la BD, eliminamos el registro de la lista de clientes:
        this.clientes.remove(this.clienteSeleccionado);
        // Reset del objeto:
        this.clienteSeleccionado = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente eliminado"));
        PrimeFaces.current().ajax().update("forma-clientes:mensajes", "forma-clientes:clientes-tabla");
    }
}
