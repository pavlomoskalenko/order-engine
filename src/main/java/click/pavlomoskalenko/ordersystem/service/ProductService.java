package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
}
