package io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductBook {

    private FileManager fileManager;

    public ProductBook(String filename) throws Exception {
        this.fileManager = new FileManager(filename);
    }

    public void addProduct(String name, Double price) {
        List<Product> products = getProducts();
        if (!products.isEmpty()) {
            products.stream()
                    .mapToInt(Product::getId)
                    .max()
                    .ifPresent(max -> {
                        Product product = new Product(max + 1, name, price);
                        try {
                            fileManager.addData(product.toCsv());
                        } catch (IOException e) {
                            System.out.println("Не удалось добавить продукт!!!");
                        }
                    });
        } else {
            try {
                fileManager.addData(new Product(1, name, price).toCsv());
            } catch (IOException e) {
                System.out.println("Не удалось добавить продукт!!!");
            }
        }
    }

    public List<Product> getProducts() {
        try {
            return fileManager.getAllData().stream()
                    .map(Product::fromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Не удалось получить список продуктов!!!");
        }
        return new ArrayList<>();
    }

    public void deleteProduct(Product product) {
        try {
            fileManager.removeData(product.toCsv());
        } catch (IOException e) {
            System.out.println("Не удалось удалить продукт!!!");
        }
    }

    public void editProduct(Product oldProduct, Product editedProduct) {
        try {
            fileManager.editData(oldProduct.toCsv(), editedProduct.toCsv());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
