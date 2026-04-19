package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.model.Product;

public interface ProductService {
    Product findProduct(String name);
}
