package dev.blazo.crud.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.blazo.crud.dto.MessageDTO;
import dev.blazo.crud.dto.ProductDTO;
import dev.blazo.crud.entity.Product;
import dev.blazo.crud.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Error: Product doesn't exist";

    /**
     * `@GetMapping("")` es una anotación de Spring que asigna las solicitudes HTTP
     * GET a la ruta de URL especificada.
     * En este caso, se asigna a la ruta de URL raíz "/productos", lo que significa
     * que cuando se realiza una solicitud GET
     * a "/products", se llamará al método `getAll()` en la clase
     * `ProductController`.
     */
    /**
     * Llama al método `listAll()` en la clase `ProductService` para obtener una
     * lista de todos los productos,
     * y luego devuelve un objeto `ResponseEntity` que contiene la lista de
     * productos y un código de estado HTTP 200 (OK).
     * El objeto `ResponseEntity` permite tener un mayor control sobre la respuesta,
     * como configurar encabezados o devolver un código de estado diferente.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.listAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") int id) {
        if (!productService.existsById(id))
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        Optional<Product> optionalProduct = productService.getOne(id);
        if (!optionalProduct.isPresent())
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        Product product = optionalProduct.get();
        return createProductResponse(product, HttpStatus.OK);
    }

    @GetMapping("/detail-name/{name}")
    public ResponseEntity<?> getOne(@PathVariable("name") String name) {
        if (!productService.existsByName(name))
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        Optional<Product> optionalProduct = productService.findByName(name);
        if (!optionalProduct.isPresent())
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        Product product = optionalProduct.get();
        return createProductResponse(product, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageDTO> create(@RequestBody ProductDTO productDto) {
        if (StringUtils.isBlank(productDto.getName()))
            return createRequestResponse("Error: Product name is mandatory", HttpStatus.BAD_REQUEST);

        if (productDto.getPrice() == null || productDto.getPrice() < 0)
            return createRequestResponse("Error: Product price is mandatory", HttpStatus.BAD_REQUEST);

        if (productService.existsByName(productDto.getName()))
            return createRequestResponse("Error: Product name already exists", HttpStatus.CONFLICT);

        Product product = new Product(productDto.getName(), productDto.getPrice());
        productService.save(product);
        return new ResponseEntity<>(new MessageDTO("Product created successfully!"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> update(@PathVariable("id") int id, @RequestBody ProductDTO productDto) {

        Optional<Product> optionalProductByName = productService.findByName(productDto.getName());

        if (!productService.existsById(id))
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(productDto.getName()))
            return createRequestResponse("Error: Product name is mandatory", HttpStatus.BAD_REQUEST);

        if (productDto.getPrice() == null || productDto.getPrice() < 0)
            return createRequestResponse("Error: Product price is mandatory", HttpStatus.BAD_REQUEST);

        if (productService.existsByName(productDto.getName())
                && optionalProductByName.isPresent()
                && optionalProductByName.get().getId() != id)
            return createRequestResponse("Product name already exists", HttpStatus.CONFLICT);

        Optional<Product> optionalProduct = productService.getOne(id);
        if (!optionalProduct.isPresent())
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        Product product = optionalProduct.get();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productService.save(product);
        return createRequestResponse("Product updated successfully!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") int id) {
        if (!productService.existsById(id))
            return createRequestResponse(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);

        productService.delete(id);
        return createRequestResponse("Product deleted successfully!", HttpStatus.OK);
    }

    private ResponseEntity<MessageDTO> createRequestResponse(String message, HttpStatus httpStatus) {
        MessageDTO errorMessage = new MessageDTO(message);
        return new ResponseEntity<>(errorMessage, httpStatus);
    }

    private ResponseEntity<Product> createProductResponse(Product product, HttpStatus httpStatus) {
        return new ResponseEntity<>(product, httpStatus);
    }

}
