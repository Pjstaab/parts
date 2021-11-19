package c482.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart
     */
    public void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    /**
     * @param newProduct
     */
    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }

    /**
     * @param partId
     * @return
     */
    public Part lookupPart(int partId) {
        for (Part p: allParts) {
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    /**
     * @param productId
     * @return
     */
    public Product lookupProduct(int productId) {
        for (Product p: allProducts) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    /**
     * This function looks up parts. One issue was the case sensitivity of the compare so I had to force things to
     * lowercase to get it to work.
     * @param partName
     * @return
     */
    public Part lookupPart(String partName) {
        for (Part p: allParts) {
            if (p.getName().toLowerCase().startsWith(partName.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

    /**
     * This looks up products. Same thing with parts and stuff being lowercase
     * @param productName
     * @return
     */
    public Product lookupProduct(String productName) {
        for (Product p: allProducts) {
            if (p.getName().toLowerCase().startsWith(productName.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

    /**
     * This updates a part with new data
     * @param index
     * @param selectedPart
     */
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This updates a product with new data
     * @param index
     * @param newProduct
     */
    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * This deletes a part
     * @param selectedPart
     * @return
     */
    public boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        for (Product product: allProducts) {
            product.deleteAssociatedPart(selectedPart);
        }
        return true;
    }

    /**
     * This deletes a product
     * @param selectedProduct
     * @return
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * This returns a list of all of the parts
     * @return
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This returns a list of all of the products
     * @return
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}