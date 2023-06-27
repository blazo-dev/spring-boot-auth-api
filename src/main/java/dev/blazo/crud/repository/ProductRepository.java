package dev.blazo.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.blazo.crud.entity.Product;

/**
 * `@Repository` es una anotación que indica que la clase anotada es un
 * repositorio, que es responsable del acceso y persistencia de datos.
 * Se utiliza para indicar que la clase proporciona el mecanismo para el
 * almacenamiento, recuperación, búsqueda, actualización y eliminación de
 * objetos.
 * La anotación `@Repository` se utiliza para habilitar la detección automática
 * de beans y la conexión de la clase de repositorio en el contexto de la
 * aplicación de Spring.
 */
/**
 * La interfaz JpaRepository se define con dos parámetros genéricos:
 * - El tipo de entidad que se está manejando.
 * - El tipo de la clave primaria de la entidad.
 * 
 * La interfaz proporciona métodos CRUD para trabajar con entidades, incluyendo
 * findAll()
 * findById(ID id)
 * save(S entity)
 * deleteById(ID id)
 * 
 * También proporciona métodos para paginación, ordenamiento y consultas
 * personalizadas.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * `Optional<Product>` indica que el método `findByName(String name)` puede o no
     * devolver un objeto `Product`.
     * Se utiliza para evitar excepciones de puntero nulo al intentar acceder a un
     * objeto que no existe.
     * Si el objeto existe, se puede acceder utilizando el método `get()` de la
     * clase `Optional`.
     */
    Optional<Product> findByName(String name);

    boolean existsByName(String name);
}
