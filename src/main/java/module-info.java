module com.example.battleship_solver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.battleship_solver to javafx.fxml;
    exports com.example.battleship_solver;
}