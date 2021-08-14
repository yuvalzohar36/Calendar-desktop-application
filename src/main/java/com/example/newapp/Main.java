package com.example.newapp;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private ComboBox<String> combo_box_hour_start, combo_box_minute_start, combo_box_hour_end, combo_box_minute_end;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("My Calendar Application");
        BorderPane borderPane = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        Scene scene = new Scene(borderPane, 500, 500);
        Scene scene2 = new Scene(borderPane2, 500, 500);

        Label new_event_label = new Label("New event");
        new_event_label.setFont(new Font("Arial", 20));
        Label time_msg = new Label("Pick date and time");
        Label pick_start_time_label = new Label("Start time");
        Label pick_end_time_label = new Label ("End time");

        HBox hbox_replace_to_events_scene = new HBox();
        Button viewEvents = new Button();
        viewEvents.setText("View Events");
        hbox_replace_to_events_scene.getChildren().add(viewEvents);
        HBox hbox_replace_to_main_scene = new HBox();
        Button newEvent = new Button();
        newEvent.setText("New event");
        hbox_replace_to_main_scene.getChildren().add(newEvent);
        borderPane2.setBottom(hbox_replace_to_main_scene);
        Label usertext = new Label("");
        HBox hbox = new HBox();
        HBox hbox_2 = new HBox();
        VBox vbox = new VBox();
        VBox vbox2 = new VBox();
        ComboBox[] comboBox_list = handle_combo_boxes();
        DatePicker date = date_picker_handler();
        TextField[] text_fields = handle_text_field();
        Button confirm_button = confirm_date_button();
        newEvent.setOnAction(e->primaryStage.setScene(scene));
        viewEvents.setOnAction(e->{
            primaryStage.setScene(scene2);
            manage_events_screen(borderPane2);
        });
        confirm_button.setOnAction(e->handle_confirm_button_logic(date, text_fields[1], usertext, text_fields[0], comboBox_list[0], comboBox_list[1], comboBox_list[2], comboBox_list[3]));
        hbox.getChildren().addAll(comboBox_list[0],comboBox_list[1]);
        hbox_2.getChildren().addAll(comboBox_list[2], comboBox_list[3]);
        vbox.getChildren().addAll(text_fields[0],text_fields[1],usertext, time_msg,date,pick_start_time_label,hbox,pick_end_time_label, hbox_2);
        vbox2.getChildren().add(confirm_button);
        borderPane.setLeft(vbox);
        borderPane.setRight(vbox2);
        borderPane.setTop(new_event_label);
        borderPane.setBottom(hbox_replace_to_events_scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handle_confirm_button_logic(DatePicker date, TextField msg, Label usertext, TextField subject, ComboBox hours_start, ComboBox minutes_start, ComboBox hours_end, ComboBox minutes_end){
        if(date.getValue() != null && date.getValue() != null && !msg.getText().equals("")
        && !subject.getText().equals("") && hours_start.getValue() != null && minutes_start.getValue() != null && hours_end.getValue() != null && minutes_end.getValue() != null){
            String start = date.getValue().toString()+"T"+hours_start.getValue().toString()+":"+
                    minutes_start.getValue().toString()+":00-07:00";
            String end = date.getValue().toString()+"T"+ hours_end.getValue().toString() +":"+
                    minutes_end.getValue().toString()+":00-07:00";
            DateTime start_date = new DateTime(start);
            DateTime end_date = new DateTime(end);
            CalendarGoogle new_cal = new CalendarGoogle(start_date, end_date, subject.getText(),
                    msg.getText(), "No Location") {
            };
            try {
                new_cal.create_new_event();
                usertext.setText("Event created successfully");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            usertext.setText("You must fill all fields!");
        }
    }

    private ComboBox[] handle_combo_boxes(){
        combo_box_hour_start = new ComboBox<>();
        combo_box_hour_start.getItems().addAll("08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        combo_box_hour_start.setPromptText("Hour");
        combo_box_minute_start = new ComboBox<>();
        combo_box_minute_start.getItems().addAll("00","10","20","30","40","50");
        combo_box_minute_start.setPromptText("Minute");
        combo_box_hour_end = new ComboBox<>();
        combo_box_hour_end.getItems().addAll("08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        combo_box_hour_end.setPromptText("Hour");
        combo_box_minute_end = new ComboBox<>();
        combo_box_minute_end.getItems().addAll("00","10","20","30","40","50");
        combo_box_minute_end.setPromptText("Minute");
        ComboBox[] comboBoxes = {combo_box_hour_start, combo_box_minute_start, combo_box_hour_end, combo_box_minute_end};
        return comboBoxes;
    }

    private DatePicker date_picker_handler() {
        DatePicker d = new DatePicker();
        d.setShowWeekNumbers(true);
        return d;
    }

    private Button confirm_date_button(){
        Button confirmDate = new Button();
        confirmDate.setText("Create");
        return confirmDate;
    }

    private TextField[] handle_text_field(){
        TextField text_field = new TextField();
        text_field.setText("Message");
        TextField subject_field_text = new TextField();
        subject_field_text.setText("Subject");
        return new TextField[]{subject_field_text, text_field};
    }

    private void manage_events_screen(BorderPane events_screen_border_pane){
        Label header_events_screen = new Label("View all coming events");
        header_events_screen.setFont(new Font("Arial", 20));
        HBox main_hbox = new HBox();
        HBox events_hbox = new HBox();
        main_hbox.getChildren().add(header_events_screen);
        events_screen_border_pane.setTop(header_events_screen);
        try {
            List<Event> events_list= CalendarGoogle.get_events();
            String all_events_data = "";
            TableView tableView = create_tableView();
            for(Event event: events_list){
                DateTime start = event.getStart().getDateTime();

                if (start == null) {
                    start = event.getStart().getDate();

                }
                String start_string = start.toString().substring(0, 10);
                tableView.getItems().add(new TableViewHandler(event.getSummary(), start_string));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);                //all_events_data += event.getSummary() +"     at:" +start_string + "\n";
            }

            events_hbox.getChildren().add(tableView);
            events_screen_border_pane.setCenter(events_hbox);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private TableView create_tableView(){
        TableView tableView = new TableView();

        TableColumn<String, String> column1 = new TableColumn<>("Subjcet");
        column1.setCellValueFactory(new PropertyValueFactory<>("subject"));
        TableColumn<String, String> column2 = new TableColumn<>("Date");
        column2.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        return tableView;
    }
}