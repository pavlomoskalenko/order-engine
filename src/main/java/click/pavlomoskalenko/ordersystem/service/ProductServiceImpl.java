package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dao.ProductRepository;
import click.pavlomoskalenko.ordersystem.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findProduct(String name) {
        return productRepository.findProductByNameIgnoreCase(name).orElse(null);
    }
}
