package com.ganesh.productservice.service;

import com.ganesh.productservice.dto.response.ProductResponse;
import com.ganesh.productservice.model.Product;
import com.ganesh.productservice.repository.ProductRepository;
import com.ganesh.productservice.dto.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
//    @Autowired
    private final ProductRepository productRepository;
    public void createProduct(ProductRequest productRequest){

        Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
//        List<ProductResponse> productResponses = new ArrayList<>();
//        for(Product product: products){
//            ProductResponse productResponse = new ProductResponse();
//            productResponse.setId(product.getId());
//            productResponse.setName(product.getName());
//            productResponse.setDescription(product.getDescription());
//            productResponse.setPrice(product.getPrice());
//            productResponses.add(productResponse);
//        }

        return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder().id(product.getId()).name(product.getName()).description(product.getDescription()).price(product.getPrice()).build();
    }
}
