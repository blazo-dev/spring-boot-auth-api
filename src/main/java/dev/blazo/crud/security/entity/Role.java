package dev.blazo.crud.security.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import dev.blazo.crud.security.enums.RoleName;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    /**
     * `@Id` indica que el campo `id` es la llave primaria de la entidad.
     * `@GeneratedValue(strategy) = GenerationType.IDENTITY)` especifica que el
     * valor de la llave primaria
     * va a ser automáticamente generado cuando un registro sea insertado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * `@NotNull` es una restricción de que valida que el campo `roleName` no sea
     * null cuando un objeto `Role` sea creado o actualizado.
     */
    @NotNull
    /**
     * `@Enumerated(EnumType.STRING)` especifica cómo se debe persistir un tipo
     * enumerado en la base de datos.
     * En este caso, especifica que el campo `roleName` de la entidad `Role` se debe
     * persistir como una cadena de texto en la base de datos,
     * en lugar de como un entero u otro tipo.
     * Esto es útil para mejorar la legibilidad y mantenimiento de la base de
     * datos, además de asegurar que se almacenen y recuperen los valores correctos.
     */
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(@NotNull RoleName roleName) {
        this.roleName = roleName;
    }
}
