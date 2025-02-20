package by.froleod.ws.productmicroservice.service;

import by.froleod.ws.productmicroservice.service.dto.CreateProductDto;

import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createProduct(CreateProductDto product) throws ExecutionException, InterruptedException;
}
