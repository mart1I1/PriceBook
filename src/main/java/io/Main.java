package io;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        String filename = "book.txt";
        ProductBook productBook = new ProductBook(filename);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Посмотреть весь список - 1");
            System.out.println("Добавить новую запись - 2");
            System.out.println("Удалить запись по id - 3");
            System.out.println("Изменить запись по id - 4");
            System.out.println("Выход - 0");
            System.out.print("Введите код операции: ");
            switch (in.readLine()) {
                case "1": {
                    productBook.getProducts().forEach(System.out::println);
                    break;
                }
                case "2": {
                    System.out.print("Введите название: ");
                    String name = in.readLine();
                    System.out.print("Введите цену: ");
                    String price = in.readLine();
                    productBook.addProduct(name, Double.valueOf(price));
                    break;
                }
                case "3": {
                    System.out.println("Введите id: ");
                    Integer id = Integer.valueOf(in.readLine());
                    productBook.getProducts().stream()
                            .filter(product -> product.getId().equals(id))
                            .findFirst()
                            .ifPresent(productBook::deleteProduct);
                    break;
                }
                case "4": {
                    System.out.println("Введите id: ");
                    Integer id = Integer.valueOf(in.readLine());
                    System.out.print("Введите название: ");
                    String name = in.readLine();
                    System.out.print("Введите цену: ");
                    Double price = Double.valueOf(in.readLine());
                    productBook.getProducts().stream()
                            .filter(product -> product.getId().equals(id))
                            .findFirst()
                            .ifPresent(product ->
                                    productBook.editProduct(
                                            product,
                                            new Product(product.getId(), name, price)
                                    )
                            );
                    break;
                }
                case "0": {
                    in.close();
                    return;
                }
            }
            System.out.println("------------------------------------------");
        }
    }
}
