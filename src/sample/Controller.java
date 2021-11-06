package sample;

import Model.User;
import Model.Promotion;
import Model.Production.Coke;
import Model.Production.Pepsi;
import Model.Production.Production;
import Model.Production.Soda;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
    private Text quanCoke;
    @FXML
    private Text quanPepsi;
    @FXML
    private Text quanSoda;
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
    @FXML
    private HBox congratInfor;

    Coke coke;
    Pepsi pepsi;
    Soda soda;
    User user;
    Spinner<Integer> productQuantity;
    Promotion promotion;
    Production production = null;

    /**
     hàm chooseEvent dành cho 3 Vbox của 3 sản phẩm
     khi click vào Vbox thì có thông tin để mua và thanh toán SP

    **/
    private final EventHandler<MouseEvent> chooseEvent = e -> {
        VBox clicked = (VBox) e.getSource();

        if(clicked == cokeProduct){
            production = coke;
        }
        else if (clicked == pepsiProduct) {
            production = pepsi;
        }else if (clicked == sodaProduct){
            production = soda;
        }

        if(production.getQuantity() > 0) {
            name.setText(production.getProductName());
            totalCash.setText(cashToString(production.getPrice()));
            exCash.setText(cashToString(user.getRemainCash()));
            payButton.setDisable(false);

            infoTable.setVisible(true);

            productQuantity = new Spinner<>(1, production.getQuantity(), 1);
            productQuantity.valueProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                    int productPrice = production.getPrice();
                    int newPrice = productPrice*newValue;
                    totalCash.setText(cashToString(newPrice));
                    exCash.setText(cashToString(user.getRemainCash() - newPrice));
                }
            });
            spinner.getChildren().add(productQuantity);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sorry !!");
            alert.setContentText("Sản phẩm bạn chọn đã hết, vui lòng chọn sản phẩm khác !!");
            alert.show();
        }


    };

    /**
     * hàm choosePromo được sử dụng khi khách hàng được nhận sản phẩm khuyến mãi
     *
     */
    private final EventHandler<MouseEvent> choosePromo = e -> {
      VBox productChoose = (VBox) e.getSource();
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Xác nhận");
      alert.setContentText("Bạn chắc chắn muốn nhận sản phẩm này ?");
      alert.showAndWait().ifPresent(buttonType -> {
          if(buttonType == ButtonType.OK) {

              if(productChoose == cokeProduct){
                  production = coke;
              }
              else if (productChoose == pepsiProduct) {
                  production = pepsi;
              }else if (productChoose == sodaProduct){
                  production = soda;
              }

              if(production.getQuantity() > 0) {
                  production.setQuantity(production.getQuantity() - 1);
                  Promotion.setLimitBudget(Promotion.getLimitBudget() - production.getPrice());
                  System.out.println(Promotion.getLimitBudget());
                  setProductInfor();
                  destroyEventHandlerPromo();
                  addEventHanlerChoose();
                  alert.setContentText("Vui lòng lấy sản phẩm");
                  alert.setTitle("xin cảm ơn !!");
                  alert.show();
                  congratInfor.setVisible(false);
              } else {
                  alert.setTitle("Sorry !!");
                  alert.setContentText("Sản phẩm bạn chọn đã hết, vui lòng chọn sản phẩm khác !!");
                  alert.show();
              }


          }
      });
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String accept = "Đồng ý";
        String pay = "Thanh Toán";

        promotion = new Promotion(10);
        coke = new Coke("Coke", 10000, 100);
        pepsi = new Pepsi("Pepsi", 10000, 200);
        soda = new Soda("Soda", 20000, 0);

        setProductInfor();
        payButton.setText(accept);
        infoTable.setVisible(false);
        cancelButton.setVisible(false);
        curCashInfor.setVisible(false);
        congratInfor.setVisible(false);

        /**
         * biến clock là một cái đồng hồ
         * dùng để check khi đến cuối ngày đã đạt chỉ tiêu khuyến mãi hay chưa
         * nếu chưa đạt thì set lại xác suất nhận khuyến mãi cho ngày hôm sau
         *
         */
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = LocalTime.now().format(dtf);
            if(currentTime.equals("23:59:59")) {
                if(Promotion.getLimitBudget() > 0) {
                    promotion.setProbab(50);
                } else {
                    promotion.setProbab(10);
                }
                Promotion.setLimitBudget(50000);
            }
        }),
                new KeyFrame(Duration.seconds(1))
                );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        /**
         * payButton vừa có chức năng đồng ý đưa tiền vào vừa có chức năng thanh toán.
         * nhận biết bằng hàm getText của button
         * khi nhập tiền họp lệ thì hệ thống sẽ đồng ý cho thao tác với sản phẩm
         * nhập tiền không hợp lệ thì sẽ đưa ra thông báo cho người sử dụng
         */
        /**
         * khi thanh toán sẽ kiểm tra xem người sử dụng có đủ tiền hay không
         * nếu đủ tiền thì thanh toán thành công
         * cập nhật thông tin sản phẩm
         * kiểm tra khách hàng có đạt điều kiện để nhận khuyến mãi hay không
         */
        payButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(payButton.getText().equals(accept)) {
                    int depoMoney = checkCashInput(cashInput.getText());

                    if(depoMoney == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Số tiền không hợp lệ hoặc lớn hơn 200000");
                        alert.show();
                    }else {
                        user = new User(depoMoney, depoMoney);
                        curCash.setText(cashToString(user.getTotalCash()));
                        curCashInfor.setVisible(true);
                        inputInfor.setVisible(false);
                        payButton.setText(pay);
                        payButton.setDisable(true);
                        addEventHanlerChoose();
                        cancelButton.setVisible(true);
                    }
                } else {
                    int quantity = productQuantity.getValue();
                    int needtoPay = quantity * production.getPrice();
                    int remainCash = user.getRemainCash() - needtoPay;
                    if(remainCash < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Số tiền của quý khách không đủ để thanh toán, vui lòng nạp thêm");
                        alert.show();
                    } else {
                        promotion.countSameProduct(production, quantity);
                        production.setQuantity(production.getQuantity() - quantity);
                        user.setRemainCash(remainCash);
                        curCash.setText(cashToString(remainCash));
                        setProductInfor();
                        infoTable.setVisible(false);
                        payButton.setDisable(true);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Mua sản phẩm thành công");
                        alert.showAndWait();
                        if(promotion.checkVictor()) {
                            Promotion.setCount(0);
                            Promotion.setVictor(false);
                            alert.setContentText("Bạn là người may mắn nhận được 1 sản phẩm bất kỳ miễn phí\nHãy chọn 1 sản phẩm bất kỳ");
                            alert.show();
                            congratInfor.setVisible(true);
                            destroyEventHanlerChoose();
                            addEventHandlerPromo();
                        }
                    }
                }

            }
        });


        /**
         * khi khách hàng không muốn mua sản phẩm nữa thì cancelButton được sử dụng
         * nếu tiền thừa của khách hàng vẫn còn thì trả lại tiền thừa cho khách hàng và ngược lại
         * sẽ set lại các event cho sản phẩm và thao tác cần thiết.
         */
        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if(user.getRemainCash() > 0) {
                    alert.setContentText("Cảm ơn đã sử dụng dịch vụ!\nTiền thừa của quý khách là: " + cashToString(user.getRemainCash()) + " VNĐ");

                } else {
                    alert.setContentText("Cảm ơn đã sử dụng dịch vụ");
                }
                destroyEventHanlerChoose();
                setProductInfor();
                infoTable.setVisible(false);
                cashInput.setVisible(true);
                payButton.setDisable(false);
                payButton.setText(accept);
                cashInput.clear();
                inputInfor.setVisible(true);
                user = null;
                curCashInfor.setVisible(false);
                cancelButton.setVisible(false);
                alert.show();
            }
        });

    }

    /**
     * Hàm chuyển đổi định dạng tiền để hiển thị trên máy
     * @param cash số tiền đưa vào theo dạng số nguyên.
     * @return trả về định dạng tiền hiển thị theo dạng chuỗi
     */
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


    /**
     * set lại các thông tin hiển thị sản phẩm trên máy.
     */
    public void setProductInfor() {
        quanCoke.setText(String.valueOf(coke.getQuantity()));
        quanPepsi.setText(String.valueOf(pepsi.getQuantity()));
        quanSoda.setText(String.valueOf(soda.getQuantity()));
    }

    /**
     * Hàm check thông tin tiền nhập vào có hợp lệ hay không.
     * @param cash
     * @return 
     */
    public int checkCashInput(String cash) {
        if(cash.equals("") || cash.matches(".*[a-zA-Z].*")) {
            return 0;
        }
        int check;
        if(cash.length() == 5) {
            check = 10000;
        } else {
            check = 100000;
        }
        int deposit = Integer.parseInt(cashInput.getText());
        if(deposit > 200000 || deposit <= 0 || deposit % check != 0) {
            return 0;
        } else {
            return deposit;
        }
    }

    /**
     * thêm các eventhandle khuyến mãi vào sản phẩm của hệ thống.
     */
    public void addEventHandlerPromo() {
        cokeProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
        pepsiProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
        sodaProduct.addEventHandler(MOUSE_CLICKED, choosePromo);
    }

    /**
     * xóa bỏ các eventhandle khuyến mãi khỏi sản phẩm của hệ thống.
     */
    public void destroyEventHandlerPromo() {
        cokeProduct.removeEventHandler(MOUSE_CLICKED, choosePromo);
        pepsiProduct.removeEventHandler(MOUSE_CLICKED, choosePromo);
        sodaProduct.removeEventHandler(MOUSE_CLICKED, choosePromo);
    }

    /**
     * thêm các eventhandle chọn sản phẩm vào trong các sản phẩm của hệ thống.
     */
    public void addEventHanlerChoose() {
        cokeProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
        pepsiProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
        sodaProduct.addEventHandler(MOUSE_CLICKED, chooseEvent);
    }


    /**
     * xóa bỏ các eventhanle chọn sản phẩm khỏi các sản phẩm của hệ thống.
     */
    public void destroyEventHanlerChoose() {
        cokeProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
        pepsiProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
        sodaProduct.removeEventHandler(MOUSE_CLICKED, chooseEvent);
    }
}
