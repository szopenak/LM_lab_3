import javafx.application.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.event.*;
import javafx.geometry.*;

public class Main extends Application {
	
	//////// JAVA FX GUI ELEMENTS /////////////
			// Primary stage 
			Stage primaryStage;
			Scene mainScene;
			
			// input fields
			final Text field_binary_text = new Text("Please insert binary number here: ");
			TextField field_binary = new TextField();
			
			// Buttons declaration
		
			Button btn_start = new Button("Next!");
			Button btn_next_step = new Button("Next step!");

	        // info elements
			GridPane informations = new GridPane();
	        Text cur_state_text = new Text("Current state: ");
	        TextField cur_state = new TextField("");
	        Text next_action_text = new Text("Next action:");
	        TextField next_action = new TextField("");
	        
	        // display tape
	        GridPane tape = new GridPane();
	        final Text tape_text = new Text("Data tape:");
	        
	        // history elements
	        Text history_text = new Text("History:");
	        TextArea history = new TextArea();	        
	        
	        // general stackpane
	        StackPane root = new StackPane();
	        	
	        // button row
		    HBox hb = new HBox();
	
		    // general vbox
		    VBox vb = new VBox();
		    
	////////  PROGRAM DATA ////////////////////
		    List <String> binary_array = new ArrayList<String>();
		    List<Text> tape_list = new ArrayList<Text>();
		    int curr_active_tape;
		    Turing turing = new Turing();
	            
	        public void start(Stage primaryStage) throws Exception {
	        	this.primaryStage = primaryStage;
	        	this.mainScene = initialize();
	        	
				// set handlers
	        	setHandlers();
	        	
	        	primaryStage.setTitle("TM - LAB 3");
	            primaryStage.setScene(mainScene);
	            primaryStage.show();
				
			}
	        public Scene initialize () {
	        	vb.getChildren().addAll(field_binary_text, field_binary, btn_start);
	        	root.getChildren().add(vb);
	        	vb.setSpacing(30);
	        	vb.setAlignment(Pos.CENTER);
	        	vb.setPadding(new Insets(20));
	        	Scene scene = new Scene(root, 400,500);
	        	primaryStage.setMinWidth(400);
	        	primaryStage.setMinHeight(500);
	        	return scene;
	        }
	        
	        public static void main (String[] args) {
	        	launch();
	        }
	        
	        private void setHandlers() {
	        	btn_start.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						String input = field_binary.getText();
						if (!validateBin(input)) {
							field_binary_text.setText("Type again, it's not a proper bin number!");
						} else {
							vb.getChildren().clear();
							createTapeGrid(input);
							createInfoGrid();
							createHistory();
							tape_text.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));
							hb.getChildren().add(btn_next_step);
							hb.setAlignment(Pos.CENTER);
							
							/// first view
							
							cur_state.setText(turing.getActualState());
							String readTape = tape_list.get(curr_active_tape).getText();
							String response = turing.Next(readTape);
							next_action.setText(response);
							
							// render the scene

							vb.getChildren().addAll(tape_text,tape,informations,hb,history_text, history);
							primaryStage.sizeToScene();
						}
					}
	        		
	        	});
	        	
	        	btn_next_step.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						if (turing.getActualState().equals("Q5")) {done();}
						updateHistory();
						String action [] = next_action.getText().split(",");
						if (!action[1].equals("-") && tape_list.get(curr_active_tape).getText().equals("#")) {
							resizeTapeGrid();
						}
						if(!action[1].equals("-")){
							tape_list.get(curr_active_tape).setText(action[1]);
						}
						cur_state.setText(action[0]);
						if(!action[2].equals("-")){
							moveCursor(action[2]);
						}
						updateFields();
					}
	        		
	        	});
	        	
	        }
	        
	        ///// additional functions
	        
	        private boolean validateBin(String input) {
	        	char [] input_array = input.toCharArray();
	        	if (input.isEmpty()) {return false;}
	        	for (char c : input_array) {
	        		int x = Integer.valueOf(c)-48;
	        		if (x != 0 && x != 1) {
	        			return false;
	        		}
	        	}
	        	return true;
	        }
	        
	        private void createTapeGrid(String input) {
	        	tape.setPadding(new Insets(10));
	        	tape.setHgap(10);
	        	tape.setVgap(10);
	        	tape.setAlignment(Pos.CENTER);
	        	tape.setStyle(("-fx-border-color: black;"));
	        	char [] input_array = input.toCharArray();
				binary_array.add("#");
				for (char c : input_array) {
					binary_array.add(String.valueOf(c));
				}
				binary_array.add("#");
	        	for (int i = 0; i < binary_array.size();i++) {
	        		tape_list.add(new Text(binary_array.get(i)));
	        		tape_list.get(i).setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
	        		tape.add(tape_list.get(i),i,0);
	        	}
	        	curr_active_tape = tape_list.size()-2;
	        	setTapeActive(tape_list.get(curr_active_tape));
	        }
	        
	        private void setTapeActive(Text t) {
	        	tape_list.get(curr_active_tape).setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
	        	tape_list.get(curr_active_tape).setFill(Color.BLACK);
	        	t.setFill(Color.RED);
	        	t.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
	        	curr_active_tape = tape_list.indexOf(t);
	        }
	        
	        private void createInfoGrid() {
		        cur_state_text.setTextAlignment(TextAlignment.RIGHT);
		        next_action_text.setTextAlignment(TextAlignment.CENTER);
		        cur_state.setEditable(false);
		        next_action.setEditable(false);
		        informations.setPadding(new Insets(20));
		        informations.setHgap(20);
	        	informations.setVgap(10);
	        	informations.setAlignment(Pos.CENTER);
	        	informations.getChildren().clear();
	        	informations.add(cur_state_text, 0, 0);
	        	informations.add(cur_state, 0, 1);
	        	informations.add(next_action_text, 1, 0);
	        	informations.add(next_action, 1, 1);
	        }
	        
	        private void createHistory() {
	        	history.setEditable(false);
	            history.setPrefRowCount(8);
	            history.setWrapText(true);
	            history.setPrefWidth(200);
	            history.setText("");
	            history.setEditable(false);
	        }
	        
	        private void moveCursor(String dir) {
	        	if (dir.equals("L")) {
	        		setTapeActive(tape_list.get(curr_active_tape-1));
	        	} else {
	        		setTapeActive(tape_list.get(curr_active_tape+1));
	        	}
	        	
	        }
	        
	        private void updateHistory () {
	        	history.appendText(cur_state.getText()+" - ");
	        	history.appendText(next_action.getText()+"  ;  ");
	        }
	        
	        private void updateFields() {
				cur_state.setText(turing.getActualState());
				String readTape = tape_list.get(curr_active_tape).getText();
				String response = turing.Next(readTape);
				next_action.setText(response);
	        }
	        
	        private void done() {
	        	vb.getChildren().clear();
	        	final Text final_text = new Text("Job is done!");
	        	final_text.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));
	        	vb.getChildren().addAll(tape_text,tape,informations,final_text,hb,history_text, history);
	        }
	        
	        private void resizeTapeGrid() {
	        	tape.getChildren().clear();
	        	Text sep = new Text("#");
				tape_list.add(0, sep);
				for (int i = 0; i<tape_list.size();i++) {
					tape.add(tape_list.get(i), i, 0);
				}
	        	curr_active_tape = 1;
	        	setTapeActive(tape_list.get(curr_active_tape));
	        } 
}
