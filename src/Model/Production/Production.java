package Model.Production;

public class Production {
    private String productName;
    private int price;
    private int quantity;
    private static boolean extra = false;

    public Production() {
    }

    /**
     * đối tượng quản lý sản phẩm như giá, số lượng
     * @param productName
     * @param price
     * @param quantity
     */
    public Production(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int gia) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
