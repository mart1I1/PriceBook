package io;

public class Product {

    private Integer id;
    private String name;
    private Double price;

    public Product(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String toCsv() {
        return String.join(",", id.toString(), name, price.toString());
    }

    public static Product fromCsv(String csvData) {
        String[] split = csvData.split(",");
        return new Product(
                Integer.valueOf(split[0]),
                split[1],
                Double.valueOf(split[2])
        );
    }

    @Override
    public String toString() {
        return "io.Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
