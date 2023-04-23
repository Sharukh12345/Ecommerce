package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;



public class UserInterface {
    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;

    VBox body;
    Button signInButton;
    Label welcomeLabel;
    Customer loggedInCustomer;
    ProductList productList=new ProductList();
    VBox productPage;
    Button placeOrderButton=new Button("Place Order");
    ObservableList<Product> itemsInCart= FXCollections.observableArrayList();
    public  BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        root.setTop(headerBar);
        body=new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
       // root.setCenter(loginPage);
        root.setCenter(body);
        productPage=productList.getAllProducts();
        body.getChildren().add(productPage);
        root.setBottom(footerBar);
        return root;
    }

    public UserInterface() {
        createLoginPage();
        createHeaderbar();
        createfooterBar();
    }

    private void createLoginPage() {
        Text userNameText = new Text("User Name");
        Text passwordText = new Text("Password");

        TextField userName = new TextField("rona@gmail.com");
        userName.setPromptText("Type your user name here");
        PasswordField password = new PasswordField();
        password.setText("abc123");
        password.setPromptText("Type your password here");
        Label messageLabel=new Label(" hello"); //to display text
        Button loginButton=new Button( "Login"); //to create login botton

        loginPage = new GridPane();
        loginPage.setStyle("-fx-background-color:white;"); //set background color in javaFx
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText, 0, 0);
        loginPage.add(userName, 1, 0);//column after row here
        loginPage.add(passwordText, 0, 1);
        loginPage.add(password, 1, 1);
        loginPage.add(messageLabel, 0, 2);
        loginPage.add(loginButton, 1, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name=userName.getText();
                String pass=password.getText();
                Login login=new Login();
                loggedInCustomer =login.customerLogin( name, pass);
                if(loggedInCustomer!=null){
                    messageLabel.setText("welcome " + loggedInCustomer.getName());
                    welcomeLabel.setText("welcome-"+loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
                else{
                    messageLabel.setText("Login Failed ! please give correct username password ");
                }

            }
        });
    }
    private void createHeaderbar(){
        Button homeButton=new Button();

        Image image=new Image("C:\\Users\\hi\\IdeaProjects\\ECommerce\\src\\img.png");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

        TextField searchbBar=new TextField();
        searchbBar.setPromptText( "Search Here");
        searchbBar.setPrefWidth(180);  //to increase the WidthSize of searchBar
        Button searchButton=new Button("Search");

        signInButton=new Button("sign In");
        welcomeLabel=new Label();
        Button cartButton=new Button("cart");
        Button orderButton=new Button("orders");
        headerBar=new HBox();
        headerBar.setPadding(new Insets( 10));
        headerBar.setSpacing( 10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.setStyle("-fx-background-color:white;");
        headerBar.getChildren().addAll(homeButton,searchbBar, searchButton,signInButton,cartButton,orderButton);
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();//remove every thing
                body.getChildren().add(loginPage);//put login page
                headerBar.getChildren().remove(signInButton);
            }
        });
        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage=productList.getProductInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);//all cases need to be handle for this here
            }
        });


        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //need list of products and a customer.

                if(itemsInCart==null){
                    //plese select a product to place order
                    showDialog("plese add some products in cart to place order ! ");
                    return;
                }
                if(loggedInCustomer==null){
                    showDialog("please login first to place order! ");
                    return;
                }
                int count=Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count !=0){
                    showDialog("order for"+count+"products placed successfully !!");
                }
                else{
                    showDialog("order failed !!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer==null &&headerBar.getChildren().indexOf(signInButton)==-1){
                    headerBar.getChildren().add(signInButton);
                }
            }
        });

}

    private void createfooterBar(){

        Button buyNowButton=new Button("Buy Now");
        Button addToCartButton=new Button("add To Cart");

        footerBar=new HBox();
        footerBar.setPadding(new Insets( 10));
        footerBar.setSpacing( 10);
        footerBar.setAlignment(Pos.CENTER);
        //footerBar.setStyle("-fx-background-color:white;");
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);
      buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              Product product=productList.getSelectedProduct();
              if(product==null){
                  //plese select a product to place order
                  showDialog("plese select a product first to place order ! ");
                  return;
              }
              if(loggedInCustomer==null){
                  showDialog("please login first to place order! ");
                  return;
              }
              boolean status=Order.placeOrder(loggedInCustomer,product);
              if(status==true){
                  showDialog("order placed successfully !!");
              }
              else{
                  showDialog("order failed !!");
              }
          }
      });
      addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              Product product=productList.getSelectedProduct();
              if(product==null){
                  //plese select a product to place order
                  showDialog("plese select a product first add to cart ! ");
                  return;
              }
              itemsInCart.add(product);
              showDialog("selected item has been added to cart successfully");
          }
      });


    }
    private void showDialog(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}
