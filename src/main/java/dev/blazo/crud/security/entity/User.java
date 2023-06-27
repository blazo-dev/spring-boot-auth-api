package dev.blazo.crud.security.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;
import java.util.*;

import java.io.Serializable;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    /*
     * Este código define una relación muchos a muchos entre la entidad `User` y la
     * entidad `Role`.
     * Utiliza la anotación `@ManyToMany` para especificar la relación, con `fetch =
     * FetchType.EAGER`
     * indicando que los roles deben cargarse de forma inmediata cuando se recupera
     * un usuario de la base de datos.
     */
    @ManyToMany(fetch = FetchType.EAGER)

    /**
     * `@JoinTable` se utiliza para especificar los detalles de la tabla de unión en
     * una relación muchos a muchos.
     * En este caso, especifica el nombre de la tabla de unión como "user_role" y el
     * mapeo de las columnas de clave externa.
     * El atributo `joinColumns` especifica la columna en la tabla de unión que
     * referencia la clave primaria de la entidad `User`.
     * El atributo `inverseJoinColumns` especifica la columna en la tabla de unión
     * que referencia la clave primaria de la entidad `Role`.
     */
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(@NotNull String name, @NotNull String username, @NotNull String email, @NotNull String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
