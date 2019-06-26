/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;
	Airport partenza;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="numeroTratteTxtInput"
    private TextField numeroTratteTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	Integer compagnieMin = null;
    	try {
    		compagnieMin = Integer.parseInt(compagnieMinimo.getText());
    	}catch(NumberFormatException e ) {
    		txtResult.setText("Inserire numero di compagnie minimo");
    		return;
    	}
    	
    	model.creaGrafo(compagnieMin);
    	txtResult.setText("grafo creato, vertici "+ model.getAeroporti().size());
    	cmbBoxAeroportoPartenza.getItems().addAll(model.getAeroporti());

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	partenza = cmbBoxAeroportoPartenza.getValue();
    	if(partenza == null) {
    		txtResult.setText("Selezionare un aeroporto");
    		return;
    	}
    	txtResult.clear();
    	List<Vicino> connessi = model.getConnessi(partenza);
    	for(Vicino a : connessi) {
    		txtResult.appendText(a.getVicino()+" "+a.getDistanza()+"\n");
    	}
    	
    	cmbBoxAeroportoDestinazione.getItems().addAll(model.getAeroporti());
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	txtResult.clear();
    	Airport destinazione = cmbBoxAeroportoDestinazione.getValue();
    	if(destinazione == null||partenza == null) {
    		txtResult.setText("Selezionare un aeroporto di partenza e di destinazione");
    		return;
    	}
    	Integer numeroTratte=null;
    	try{
    		numeroTratte= Integer.parseInt(numeroTratteTxtInput.getText());
    	}catch(NumberFormatException e ){
    		txtResult.setText("Inserire un numero intero di tratte");
    		return;
    	}
    	List<Airport> percorso = model.cercaPercorso(partenza,destinazione, numeroTratte);
    	for(Airport a : percorso) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	txtResult.appendText("Peso totale "+model.getPeso(percorso));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroTratteTxtInput != null : "fx:id=\"numeroTratteTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    
    public void setModel(Model model) {
  		this.model = model;
  		
  	}
}
