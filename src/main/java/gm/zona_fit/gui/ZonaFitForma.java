package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Desactivamos este componente para poder refactorizar a app web:
// @Component
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarFormularioButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer idCliente; // Creamos el valor de id para poder trabajar con él.

    // En este caso utilizamos @Autowired aquí, ya que necesitamos que la dependencia se inyecte desde el
    // momento de la creación de la ventana.
    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        // Inyectamos la dependencia:
        this.clienteServicio = clienteServicio;
        iniciarForma();

        guardarButton.addActionListener(e -> guardarCliente());
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarFormularioButton.addActionListener(e -> limpiarFormulario());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Método para recuperar los clientes existentes:
                cargarClienteSeleccionado();
            }
        });

    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void createUIComponents(){
        this.tablaModeloClientes = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresía"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla = new JTable(tablaModeloClientes);
        // Restringimos la selección de la tabla a un solo registro:
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargamos listado de clientes:
        listarClientes();
    }

    private void listarClientes(){
        this.tablaModeloClientes.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(renglonCliente);
        });
    }

    private void guardarCliente(){
        if(nombreTexto.getText().equals("")){
            mostrarMensaje("Proporciona un nombre");
            nombreTexto.requestFocusInWindow(); // Enfocamos al campo de texto para que el usuario lo cumplimente.
            return;
        }
        if(apellidoTexto.getText().equals("")){
            mostrarMensaje("Proporciona un apellido");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if(membresiaTexto.getText().equals("")){
            mostrarMensaje("Proporciona un número de membresía");
            nombreTexto.requestFocusInWindow();
            return;
        }

        // Recuperamos los valores del formulario:
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());
        // Recuperamos el valor de id, inicializado en cargarClienteSeleccionado():
        var cliente = new Cliente(this.idCliente, nombre, apellido, membresia);
        this.clienteServicio.guardarCliente(cliente);
        if(this.idCliente != null){
            mostrarMensaje("Cliente modificado");
        } else {
            mostrarMensaje("Cliente agregado");
        }
        limpiarFormulario();
        listarClientes();
    }

    private void limpiarFormulario(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        // Limpiamos el id también:
        this.idCliente = null;
        // Deseleccionamos el registro de la tabla:
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void cargarClienteSeleccionado(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){ // -1 significa que no se ha seleccionado ningún registro.
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString(); // Apuntamos hacia el registro 0 (el id)
            // El valor de this.idCliente será accessible para todos los demás métodos de esta clase.
            this.idCliente = Integer.parseInt(id); // Si el valor de id ya existe a la hora de hacer una inserción,
            // el registro se modificará. Si no, se creará. Esto lo hace el JPA automáticamente.
            var nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }

    private void eliminarCliente(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var cliente = new Cliente();
            cliente.setId(this.idCliente);
            clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("Cliente con id: " + id + " eliminado");
            limpiarFormulario();
            listarClientes();
        } else {
            mostrarMensaje("Seleccione un cliente a eliminar");
        }
    }
}
