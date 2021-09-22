package c482.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart
     */
    public void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    public static void addProduct(Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }

    public static Part lookupPart(int partId) {
        for (Part p: allParts) {
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    public static Product lookupProduct(int productId) {
        for (Product p: allProducts) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    public static Part lookupPart(String partName) {
        for (Part p: allParts) {
            if (p.getName() == partName) {
                return p;
            }
        }
        return null;
    }

    public static Product lookupProduct(String productName) {
        for (Product p: allProducts) {
            if (p.getName() == productName) {
                return p;
            }
        }
        return null;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        return allParts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}