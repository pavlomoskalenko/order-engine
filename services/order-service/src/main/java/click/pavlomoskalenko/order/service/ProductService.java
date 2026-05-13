package click.pavlomoskalenko.order.service;

import click.pavlomoskalenko.order.api.rest.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
}
