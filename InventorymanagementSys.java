
import java.util.*;
import java.io.*;

class Product {
    private int productId;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String supplier;

    public Product(int productId, String name, String category, double price, int quantity, String supplier) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

   
    public void updateStock(int change) {
        this.quantity += change;
        if (this.quantity < 0) {
            this.quantity = 0;
        }
    }

 
    public double getTotalValue() {
        return price * quantity;
    }

    // Check if product is low in stock
    public boolean isLowStock(int threshold) {
        return quantity <= threshold;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %-20s | Category: %-15s | Price: $%.2f | Quantity: %d | Supplier: %s", 
                           productId, name, category, price, quantity, supplier);
    }


    public String getDetailedInfo() {
        return String.format(
            "Product ID: %d\n" +
            "Name: %s\n" +
            "Category: %s\n" +
            "Price: $%.2f\n" +
            "Quantity: %d\n" +
            "Supplier: %s\n" +
            "Total Value: $%.2f\n" +
            "-------------------",
            productId, name, category, price, quantity, supplier, getTotalValue());
    }
}


public class InventoryManagementSystem {
    
    private HashMap<Integer, Product> inventory;
   
    private TreeMap<String, List<Product>> categoryMap;
    
    private PriorityQueue<Product> lowStockQueue;
    private Scanner scanner;
    private int nextProductId;

  
    public InventoryManagementSystem() {
        inventory = new HashMap<>();
        categoryMap = new TreeMap<>();
        lowStockQueue = new PriorityQueue<>(Comparator.comparingInt(Product::getQuantity));
        scanner = new Scanner(System.in);
        nextProductId = 1001; // Starting product ID

        
    }

   
    

    
    public void addProduct(Product product) {
        inventory.put(product.getProductId(), product);

       
        categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);

        System.out.println("Product added successfully!");
    }

   
    public boolean removeProduct(int productId) {
        Product product = inventory.remove(productId);
        if (product != null) {
            
            List<Product> categoryProducts = categoryMap.get(product.getCategory());
            if (categoryProducts != null) {
                categoryProducts.remove(product);
                if (categoryProducts.isEmpty()) {
                    categoryMap.remove(product.getCategory());
                }
            }
            System.out.println("Product removed successfully!");
            return true;
        }
        System.out.println("Product not found!");
        return false;
    }

    
    public boolean updateStock(int productId, int quantityChange) {
        Product product = inventory.get(productId);
        if (product != null) {
            int oldQuantity = product.getQuantity();
            product.updateStock(quantityChange);
            System.out.printf("Stock updated! %s: %d → %d\n", 
                            product.getName(), oldQuantity, product.getQuantity());
            return true;
        }
        System.out.println("Product not found!");
        return false;
    }

    public Product searchById(int productId) {
        return inventory.get(productId);
    }

   
    public List<Product> searchByName(String name) {
        List<Product> results = new ArrayList<>();
        String searchName = name.toLowerCase();

        for (Product product : inventory.values()) {
            if (product.getName().toLowerCase().contains(searchName)) {
                results.add(product);
            }
        }
        return results;
    }

 
    public List<Product> getProductsByCategory(String category) {
        return categoryMap.getOrDefault(category, new ArrayList<>());
    }

    
    public void displayAllProducts() {
        if (inventory.isEmpty()) {
            System.out.println("No products in inventory!");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println("                                INVENTORY LIST");
        System.out.println("=".repeat(100));

        for (Product product : inventory.values()) {
            System.out.println(product);
        }
        System.out.println("=".repeat(100));
    }

   
    public void displayByCategory() {
        if (categoryMap.isEmpty()) {
            System.out.println("No products in inventory!");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    PRODUCTS BY CATEGORY");
        System.out.println("=".repeat(80));

        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            System.out.println("\nCategory: " + entry.getKey().toUpperCase());
            System.out.println("-".repeat(50));
            for (Product product : entry.getValue()) {
                System.out.println("  " + product);
            }
        }
        System.out.println("=".repeat(80));
    }

    
    public void generateLowStockReport(int threshold) {
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product product : inventory.values()) {
            if (product.isLowStock(threshold)) {
                lowStockProducts.add(product);
            }
        }

        if (lowStockProducts.isEmpty()) {
            System.out.println("\nNo products are low in stock!");
            return;
        }

       
        lowStockProducts.sort(Comparator.comparingInt(Product::getQuantity));

        System.out.println("\n" + "=".repeat(80));
        System.out.printf("                 LOW STOCK REPORT (Threshold: %d)\n", threshold);
        System.out.println("=".repeat(80));

        for (Product product : lowStockProducts) {
            System.out.printf(" %s (Quantity: %d)\n", product, product.getQuantity());
        }
        System.out.println("=".repeat(80));
    }

 
    public double calculateTotalValue() {
        double total = 0;
        for (Product product : inventory.values()) {
            total += product.getTotalValue();
        }
        return total;
    }

    // Generate inventory summary
    public void generateInventorySummary() {
        if (inventory.isEmpty()) {
            System.out.println("No products in inventory!");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                INVENTORY SUMMARY");
        System.out.println("=".repeat(60));
        System.out.printf("Total Products: %d\n", inventory.size());
        System.out.printf("Total Categories: %d\n", categoryMap.size());
        System.out.printf("Total Inventory Value: $%.2f\n", calculateTotalValue());

        
        System.out.println("\nCategory Breakdown:");
        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            double categoryValue = entry.getValue().stream()
                    .mapToDouble(Product::getTotalValue)
                    .sum();
            System.out.printf("  %s: %d products, $%.2f\n", 
                            entry.getKey(), entry.getValue().size(), categoryValue);
        }
        System.out.println("=".repeat(60));
    }

    
    private void addNewProduct() {
        System.out.println("\n--- Add New Product ---");

        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();

            System.out.print("Enter category: ");
            String category = scanner.nextLine();

            System.out.print("Enter price: $");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter supplier: ");
            String supplier = scanner.nextLine();

            Product newProduct = new Product(nextProductId++, name, category, price, quantity, supplier);
            addProduct(newProduct);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers for price and quantity.");
        }
    }

    private void searchProducts() {
        System.out.println("\n--- Search Products ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Choose option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Product product = searchById(id);
                    if (product != null) {
                        System.out.println("\nProduct Found:");
                        System.out.println(product.getDetailedInfo());
                    } else {
                        System.out.println("Product not found!");
                    }
                    break;

                case 2:
                    System.out.print("Enter product name (partial match): ");
                    String name = scanner.nextLine();
                    List<Product> results = searchByName(name);
                    if (results.isEmpty()) {
                        System.out.println("No products found!");
                    } else {
                        System.out.println("\nSearch Results:");
                        for (Product p : results) {
                            System.out.println(p);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    private void updateProductStock() {
        System.out.println("\n--- Update Stock ---");

        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Product product = searchById(id);
            if (product == null) {
                System.out.println("Product not found!");
                return;
            }

            System.out.printf("Current stock for %s: %d\n", product.getName(), product.getQuantity());
            System.out.print("Enter stock change (+/- amount): ");
            int change = Integer.parseInt(scanner.nextLine());

            updateStock(id, change);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    
    public void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        INVENTORY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1.  Add New Product");
        System.out.println("2.  Remove Product");
        System.out.println("3.  Update Stock");
        System.out.println("4.  Search Products");
        System.out.println("5.  Display All Products");
        System.out.println("6.  Display by Category");
        System.out.println("7.  View Products by Category");
        System.out.println("8.  Generate Low Stock Report");
        System.out.println("9.  Generate Inventory Summary");
        System.out.println("10. Calculate Total Inventory Value");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice: ");
    }

    
    public void run() {
        System.out.println("Welcome to Inventory Management System!");
        System.out.println("System initialized with sample data.");

        while (true) {
            displayMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addNewProduct();
                        break;

                    case 2:
                        System.out.print("Enter Product ID to remove: ");
                        int removeId = Integer.parseInt(scanner.nextLine());
                        removeProduct(removeId);
                        break;

                    case 3:
                        updateProductStock();
                        break;

                    case 4:
                        searchProducts();
                        break;

                    case 5:
                        displayAllProducts();
                        break;

                    case 6:
                        displayByCategory();
                        break;

                    case 7:
                        System.out.print("Enter category name: ");
                        String category = scanner.nextLine();
                        List<Product> categoryProducts = getProductsByCategory(category);
                        if (categoryProducts.isEmpty()) {
                            System.out.println("No products found in this category!");
                        } else {
                            System.out.println("\nProducts in " + category + ":");
                            for (Product p : categoryProducts) {
                                System.out.println(p);
                            }
                        }
                        break;

                    case 8:
                        System.out.print("Enter low stock threshold: ");
                        int threshold = Integer.parseInt(scanner.nextLine());
                        generateLowStockReport(threshold);
                        break;

                    case 9:
                        generateInventorySummary();
                        break;

                    case 10:
                        System.out.printf("\nTotal Inventory Value: $%.2f\n", calculateTotalValue());
                        break;

                    case 0:
                        System.out.println("\nThank you for using Inventory Management System!");
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid choice! Please try again.");
                }

                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

 
    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();
        ims.run();
    }
}
