package Model;

import Model.SanPham.Coke;
import Model.SanPham.Pepsi;
import Model.SanPham.SanPham;
import Model.SanPham.Soda;

import java.util.SplittableRandom;

public class KhuyenMai {
    private static int count = 0;
    private SanPham lastProduct;
    private static boolean victor;
    private static int limitBudget = 50000;
    private int probab;

    SplittableRandom random = new SplittableRandom();



    public KhuyenMai(int probab) {
        this.probab = probab;
        victor = false;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        KhuyenMai.count = count;
    }

    public static boolean isVictor() {
        return victor;
    }

    public static void setVictor(boolean victor) {
        KhuyenMai.victor = victor;
    }

    public SanPham getLastProduct() {
        return lastProduct;
    }

    public void setLastProduct(SanPham lastProduct) {
        this.lastProduct = lastProduct;
    }

    public static int getLimitBudget() {
        return limitBudget;
    }

    public static void setLimitBudget(int limitBudget) {
        KhuyenMai.limitBudget = limitBudget;
    }

    public void countSameProduct(SanPham sp, int sl) {
        if(lastProduct instanceof Coke && sp instanceof Coke ||
            lastProduct instanceof Pepsi && sp instanceof Pepsi ||
            lastProduct instanceof Soda && sp instanceof Soda) {
            KhuyenMai.count += sl;
        } else {
            lastProduct = sp;
            KhuyenMai.count = sl;
        }
    }

    public int getProbab() {
        return probab;
    }

    public void setProbab(int probab) {
        this.probab = probab;
    }

    public boolean checkVictor() {
        if(count >= 3 && limitBudget > 0) {
            victor = random.nextInt(1,101) <= this.probab;
        }

        return victor;
    }

}
