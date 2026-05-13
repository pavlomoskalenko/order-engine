package click.pavlomoskalenko.order.service;

import click.pavlomoskalenko.order.api.rest.dto.ProductResponse;
import click.pavlomoskalenko.order.repository.ProductRepository;
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
