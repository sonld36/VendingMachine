package sample;

import Model.KhachHang;
import Model.KhuyenMai;
import Model.SanPham.Coke;
import Model.SanPham.Pepsi;
import Model.SanPham.SanPham;
import Model.SanPham.Soda;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class Controller implements Initializable {
    @FXML
    private VBox infoTable;
    @FXML
    private Button payButton;
    @FXML
    private VBox cokeProduct;
    @FXML
    private VBox pepsiProduct;
    @FXML
    private VBox sodaProduct;
    @FXML
    private Text name;
    @FXML
    private Pane spinner;
    @FXML
    private Text totalCash;
    @FXML
    private Text exCash;
    @FXML
    private Text slCoke;
    @FXML
    private Text slPepsi;
    @FXML
    private Text slSoda;
    @FXML
    private TextArea cashInput;
    @FXML
    private HBox curCashInfor;
    @FXML
    private Text curCash;
    @FXML
    private VBox inputInfor;
    @FXML
    private Button cancelButton;

    Coke coke;
    Pepsi pepsi;
    Soda soda;
    KhachHang kh;
    Spinner<Integer> soLuongSp;
    KhuyenMai khuyenMai;
    SanPham sanPham = null;


    private final EventHandler<MouseEvent> chooseEvent = e -> {
        VBox clicked = (VBox) e.getSource();

        if(clicked == cokeProduct){
            sanPham = coke;
        }
        else if (clicked == pepsiProduct) {
            sanPham = pepsi;
        }else if (clicked == sodaProduct){
            sanPham = soda;
        }

        name.setText(sanPham.getTenSanPham());
        totalCash.setText(cashToString(sanPham.getGia()));
        exCash.setText(cashToString(kh.getTienThua()));
        payButton.setDisable(false);

        infoTable.setVisible(true);

        soLuongSp = new Spinner<>(1, sanPham.getSoLuong(), 1);
        soLuongSp.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                int giaSP = sanPham.getGia();
                int giaMoi = giaSP*newValue;
                totalCash.setText(cashToString(giaMoi));
                exCash.setText(cashToString(kh.getTienThua() - giaMoi));
            }
        });
        spinner.getChildren().add(soLuongSp);
    };


    private final EventHandler<MouseEvent> choosePromo = e -> {
      VBox productChoose = (VBox) e.getSource();
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Confirm for The Promotion Product");
      alert.setContentText("Are you sure for this product ?");
      alert.showAndWait().ifPresent(buttonType -> {
          if(buttonType == ButtonType.OK) {

              if(productChoose == cokeProduct){
                  sanPham = coke;
              }
              else if (productChoose == pepsiProduct) {
                  sanPham = pepsi;
              }else if (productChoose == sodaProduct){
                  sanPham = soda;
              }
              sanPham.setSoLuong(sanPham.getSoLuong() - 1);
              KhuyenMai.setLimitBudget(KhuyenMai.getLimitBudget() - sanPham.getGia());
              System.out.println(KhuyenMai.getLimitBudget());
              setThongTinSP();
              alert.setContentText("Take the production please");
              alert.setTitle("Thanks");
              alert.show();
          }
      });



    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        curCashInfor.setVisible(false);
        String accept = "Đồng ý";
        String pay = "Thanh Toán";
        khuyenMai = new KhuyenMai(10);
        payButton.setText(accept);
        cancelButton.setVisible(false);
        coke = new Coke("Coke", 10000, 100);
        pepsi = new Pepsi("Pepsi", 10000, 200);
        soda = new Soda("Soda", 20000, 56);
        setThongTinSP();
        infoTable.setVisible(false);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = LocalTime.now().format(dtf);
            if(currentTime.equals("23:59:59")) {
                if(KhuyenMai.getLimitBudget() > 0) {
                    khuyenMai.setProbab(50);
                } else {
                    khuyenMai.setProbab(10);
                }
                KhuyenMai.setLimitBudget(50000);
            }
        }),
                new KeyFrame(Duration.seconds(1))
                );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        payButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(payButton.getText().equals(accept)) {
                    int tienNap = Integer.parseInt(cashInput.getText());
                    if(tienNap > 200000 || (tienNap % 10000 != 0)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Số tiền không hợp lệ hoặc lớn hơn 200000");
                        alert.show();
                    }else {
                        kh = new KhachHang(Integer.parseInt(cashInput.getText()), Integer.parseInt(cashInput.getText()));
                        curCash.setText(cashToString(kh.getTongTien()));
                        curCashInfor.setVisible(true);
                        inputInfor.setVisible(false);
                        payButton.setText(pay);
                        payButton.setDisable(true);
                        cokeProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
                        pepsiProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
                        sodaProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
                        cancelButton.setVisible(true);

                    }
                } else {
                    int sl = soLuongSp.getValue();
                    int tienCanThanhToan = sl * sanPham.getGia();
                    int tienConLai = kh.getTienThua() - tienCanThanhToan;
                    if(tienConLai < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Số tiền của quý khách không đủ để thanh toán, vui lòng nạp thêm");
                        alert.show();
                    } else {
                        khuyenMai.countSameProduct(sanPham, sl);
                        sanPham.setSoLuong(sanPham.getSoLuong() - sl);
                        kh.setTienThua(tienConLai);
                        curCash.setText(cashToString(tienConLai));
                        setThongTinSP();
                        infoTable.setVisible(false);
                        payButton.setDisable(true);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Mua sản phẩm thành công");
                        alert.showAndWait();
                        if(khuyenMai.checkVictor()) {
                            KhuyenMai.setCount(0);
                            KhuyenMai.setVictor(false);
                            alert.setContentText("Bạn là người may mắn nhận được 1 sản phẩm bất kỳ miễn phí\nHãy chọn 1 sản phẩm bất kỳ");
                            alert.show();
                            cokeProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
                            pepsiProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
                            sodaProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
                        }
                    }
                }

            }
        });

        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if(kh.getTienThua() > 0) {
                    alert.setContentText("Cảm ơn đã sử dụng dịch vụ!\nVui lòng nhận lại tiên thừa");

                } else {
                    alert.setContentText("Cảm ơn đã sử dụng dịch vụ");
                }

                cokeProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
                pepsiProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
                sodaProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
                setThongTinSP();
                infoTable.setVisible(false);
                cashInput.setVisible(true);
                payButton.setDisable(false);
                payButton.setText(accept);
                cashInput.clear();
                inputInfor.setVisible(true);
                kh = null;
                curCashInfor.setVisible(false);
                cancelButton.setVisible(false);
                alert.show();
            }
        });

    }

    public String cashToString(int cash) {
        String cashOfString = String.valueOf(cash);
        int length = cashOfString.length();
        if(length == 5) {
            cashOfString = cashOfString.substring(0,2) + "." + cashOfString.substring(2, length);
        } else if (length == 6) {
            cashOfString = cashOfString.substring(0,3) + "." + cashOfString.substring(3, length);
        }

        return cashOfString;
    }

    public void setThongTinSP() {
        slCoke.setText(String.valueOf(coke.getSoLuong()));
        slPepsi.setText(String.valueOf(pepsi.getSoLuong()));
        slSoda.setText(String.valueOf(soda.getSoLuong()));
    }


}
