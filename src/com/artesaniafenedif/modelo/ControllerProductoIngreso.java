package com.artesaniafenedif.modelo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerProductoIngreso {

	@FXML TextField txtNombreProducto;
	@FXML TextArea txtDescripcion;
	@FXML TextField txtPrecio;
	@FXML TextField txtCantidad;
	@FXML Button btnCancelar;
	@FXML Button btnGuardar;
	@FXML Button btnIngresarMic;
	
	public void initialize()
	{
		//txtDescripcion = new TextArea();
		//txtNombreProducto = new TextField();
		txtNombreProducto.setText("Prueba Raul");
	}
	
	public void ingresarPorMicrofono()
	{
		Index.captureAudio();
	}
	
	public void pararTraducir()
	{
		try {
			txtDescripcion.setText("Esperando Texto");
			Index.stopAudio();
			String traduccion = Index.traducir();
			System.out.println("En controller: " + traduccion);
			if(traduccion!=null)
			{
				txtNombreProducto.setText(traduccion);
				txtDescripcion.setText(traduccion);
			}else{
				txtDescripcion.setText("Traduccion Fallo, repita proceso!");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
