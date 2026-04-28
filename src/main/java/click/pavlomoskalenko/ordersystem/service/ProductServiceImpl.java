package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dao.ProductRepository;
import click.pavlomoskalenko.ordersystem.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::new)
                .toList();
    }
}
