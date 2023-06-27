package dev.blazo.crud.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase representa un producto en el sistema.
 * 
 * - La anotación @Entity indica que la clase es una entidad mapeada a una tabla
 * en la base de datos.
 * 
 * - La anotación @Table especifica el nombre de la tabla en la base de datos en
 * la que se mapea esta entidad.
 * 
 * - Las anotaciones @Getter y @Setter generan automáticamente los métodos
 * getter y setter para los campos de la clase.
 * 
 * - La anotación @NoArgsConstructor crea un constructor sin argumentos.
 * 
 * - La anotación @AllArgsConstructor crea un constructor que acepta argumentos
 * para todos los campos de la clase.
 * 
 * - La implementación de la interfaz Serializable en una clase indica que los
 * objetos de esa clase pueden ser convertidos en una secuencia de bytes para
 * ser almacenados en disco, transmitidos a través de la red o cualquier otra
 * forma de persistencia. Básicamente, la serialización permite convertir un
 * objeto en una forma que se puede almacenar o transferir y luego restaurar ese
 * objeto a su estado original.
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    /**
     * - La anotación @Id indica que este campo es el identificador principal de la
     * entidad.
     * - La anotación @GeneratedValue especifica la estrategia de generación de
     * valores para el campo.
     * En este caso, se utiliza la estrategia de identidad (identity), que se basa
     * en una columna auto-incrementable en la base de datos para generar valores
     * únicos automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private float price;

    public Product(String name, float price) {
        this.name = name;
        this.price = price;
    }
}
