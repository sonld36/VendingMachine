package Model;

import Model.Production.Coke;
import Model.Production.Pepsi;
import Model.Production.Production;
import Model.Production.Soda;

import java.util.SplittableRandom;

/**
 * Class Promotion là class quản lý về hoạt động khuyến mãi
 */
public class Promotion {
    private static int count = 0;
    private Production lastProduct;
    private static boolean victor;
    private static int limitBudget = 50000;
    private int probab;

    SplittableRandom random = new SplittableRandom();



    public Promotion(int probab) {
        this.probab = probab;
        victor = false;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Promotion.count = count;
    }

    public static boolean isVictor() {
        return victor;
    }

    public static void setVictor(boolean victor) {
        Promotion.victor = victor;
    }

    public Production getLastProduct() {
        return lastProduct;
    }

    public void setLastProduct(Production lastProduct) {
        this.lastProduct = lastProduct;
    }

    public static int getLimitBudget() {
        return limitBudget;
    }

    public static void setLimitBudget(int limitBudget) {
        Promotion.limitBudget = limitBudget;
    }


    public int getProbab() {
        return probab;
    }

    public void setProbab(int probab) {
        this.probab = probab;
    }

    /**
     * Hàm countSameProduct dùng khi khách hàng thanh toán
     * chức năng để đếm sản phẩm được mua liên tiếp bao nhiêu lần
     * @param product là đối tượng sản phẩm
     * @param quantity là số lượng mua
     */
    public void countSameProduct(Production product, int quantity) {
        if(lastProduct instanceof Coke && product instanceof Coke ||
            lastProduct instanceof Pepsi && product instanceof Pepsi ||
            lastProduct instanceof Soda && product instanceof Soda) {
            Promotion.count += quantity;
        } else {
            lastProduct = product;
            Promotion.count = quantity;
        }

    }

    /**
     * Hàm checkVictor được chạy khi khách hàng thanh toán
     * sau khi đã kiểm tra các điều kiện cơ bản của khuyến mãi
     * khách hàng đó sẽ nhận được khuyến mãi nếu victor = true và ngược lại
     * @return
     */
    public boolean checkVictor() {
        if(count >= 3 && limitBudget > 0) {
            victor = random.nextInt(1,101) <= this.probab;
        }

        return victor;
    }

}
