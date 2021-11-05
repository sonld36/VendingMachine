package Model;

public class KhachHang {
    private int tongTien;
    private int tienThua;

    public KhachHang() {
    }

    public KhachHang(int tongTien, int tienThua) {
        this.tongTien = tongTien;
        this.tienThua = tienThua;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getTienThua() {
        return tienThua;
    }

    public void setTienThua(int tienThua) {
        this.tienThua = tienThua;
    }
}
