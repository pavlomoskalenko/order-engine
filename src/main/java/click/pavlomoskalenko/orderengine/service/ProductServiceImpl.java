package click.pavlomoskalenko.orderengine.service;

import click.pavlomoskalenko.orderengine.dao.ProductRepository;
import click.pavlomoskalenko.orderengine.dto.ProductResponse;
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
