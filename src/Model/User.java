package Model;

public class User {
    private int totalCash;
    private int remainCash;

    public User() {
    }

    /**
     * Đối tượng người sử dụng máy bán hàng
     * @param totalCash tổng tiền ban đầu người sử dụng đưa vào
     * @param remainCash số tiền còn lại cuối cùng
     */
    public User(int totalCash, int remainCash) {
        this.totalCash = totalCash;
        this.remainCash = remainCash;
    }

    public int getTotalCash() {
        return totalCash;
    }

    public void setTongTien(int totalCash) {
        this.totalCash = totalCash;
    }

    public int getRemainCash() {
        return remainCash;
    }

    public void setRemainCash(int remainCash) {
        this.remainCash = remainCash;
    }
}
