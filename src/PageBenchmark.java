


import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class PageBenchmark extends Application {

    BarChart<Number, String> bc = null;
    public ArrayList<Page> pages = new ArrayList<Page>();
    public Button btn;
    public Label label, label2, label3;
    TextField titleField;
    HBox root;
    HBox root2;
    HBox root3;

    private void init(Stage primaryStage) {
        btn = new Button();
        
        label = new Label();
        label2 = new Label();
        label3 = new Label();
        
        label.setText("Loading data... Please wait...");
        label2.setText("Enter Wikipedia Title: ");
        label3.setText(
        		"Hi! Wikipedia PageRank Benchmarking サービスへようこそ。 \n"
                        + "このサービスは 入力するWikipedia のページをWikipedia の様々 \n"
                        + "なページと比較するサービスでございます。比較基準は課題１－１で計算した \n"
                        + "PageRank のことです。上にある TextField に日本語 Wikipedia の\n"
                        + "ページのタイトルを入力して、 'Go' ボタンを押してください。\n"
                        + "例：インド、安倍晋三、バラク・オバマ、ドイツ etc.");
        
        btn.setDisable(true);

        BorderPane pane = new BorderPane();
        root = new HBox();
        root2 = new HBox();
        root3 = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5f);
        root2.setAlignment(Pos.CENTER);
        root3.setAlignment(Pos.CENTER);
        pane.setTop(root);
        pane.setCenter(root2);
        pane.setBottom(root3);
        primaryStage.setScene(new Scene(pane, 800, 600));
        primaryStage.setTitle("Wikipedia PageRank Benchmarking");

        //Text Area for display of Statistics Data  
        titleField = new TextField();
        btn.setText("Go");

        root.getChildren().addAll(label2, titleField, btn);
        root2.getChildren().add(label3);
        root3.getChildren().add(label);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (bc != null) {
                    root2.getChildren().remove(bc);
                }
                root2.getChildren().remove(label3);
                bc = createChart(titleField.getText());
                if (bc == null) {
                    label3.setText("Sorry, no such title found. Try again.");
                    root2.getChildren().add(label3);
                }else   
                    root2.getChildren().addAll(bc);
            }
        });
    }

    //Method for creation of chart.  
    protected BarChart<Number, String> createChart(String title) {
        LogarithmicAxis xAxis = new LogarithmicAxis(0.00000001f, 0.003f);
        CategoryAxis yAxis = new CategoryAxis();

        xAxis.setLabel("PageRank Score (logscale)");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Page");

        BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
        XYChart.Series<Number, String> series1 = new XYChart.Series<Number, String>();
        series1.setName("PageRank Score");

        int index = -1;

        for (int i = 0; i < pages.size(); i++) {
            Page p = pages.get(i);
            if (p.getTitle().equals(title)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        int totalPages = pages.size();

        series1.getData().add(new XYChart.Data<Number, String>(pages.get(totalPages - 1).getPageRank(),
                "Rank: " + (totalPages) + ", " + pages.get(totalPages - 1).getTitle()));
        String s = String.valueOf(totalPages);
        char[] c = s.toCharArray();

        int i = Character.getNumericValue(c[0]) * (int) Math.pow(10, s.length() - 1);
        System.out.println("i = " + i + ", len = " + s.length() + ", size = " + totalPages);

        for (; i >= 0; i -= 100000) {
            if (i < index) {
                series1.getData().add(new XYChart.Data<Number, String>(pages.get(index).getPageRank(),
                        "Rank: " + (index + 1) + ", " + pages.get(index).getTitle()));
            }

            if (i != 0) {
                series1.getData().add(new XYChart.Data<Number, String>(pages.get(i - 1).getPageRank(),
                        "Rank: " + (i) + ", " + pages.get(i - 1).getTitle()));
            } else {
                series1.getData().add(new XYChart.Data<Number, String>(pages.get(0).getPageRank(),
                        "Rank: " + (1) + ", " + pages.get(0).getTitle()));
            }
        }

        bc.getData().add(series1);
        return bc;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadData();
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadData() {
        DataLoader loader = new DataLoader(this);
        loader.start();
    }

    public void dataLoaded() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btn.setDisable(false);
                label.setText("Loading completed, you can enter title now.");
            }
        });
    }
}
