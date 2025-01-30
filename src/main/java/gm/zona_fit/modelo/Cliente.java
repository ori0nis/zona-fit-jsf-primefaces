package gm.zona_fit.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

// Marcamos la clase como una entidad de BD, diciéndole al framework que vamos a mapear
// la tabla de la BD:
@Entity
@Data // Generamos los métodos get y set de todo con lombok
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor completo
@ToString
@EqualsAndHashCode
public class Cliente {
    // Integer es un objeto y no un tipo primitivo aquí, pero su uso es el mismo
    // Utilizamos un objeto porque su valor por default es null, mientras que el de int es 0
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Aclaramos que el valor del id va a ser autoincremental
    private Integer id;
    private String nombre;
    private String apellido;
    private Integer membresia;
}
