package click.pavlomoskalenko.orderengine.service;

import click.pavlomoskalenko.orderengine.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
}
