package com.artesaniafenedif.modelo;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.google.api.Authentication.Builder;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingChannelProvider;
//import com.google.api.services.oauth2.Oauth2;
import com.google.auth.Credentials;
import com.google.cloud.speech.spi.v1beta1.SpeechClient;
import com.google.cloud.speech.spi.v1beta1.SpeechSettings;
import com.google.cloud.speech.v1beta1.RecognitionAudio;
import com.google.cloud.speech.v1beta1.RecognitionConfig;
import com.google.cloud.speech.v1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1beta1.SyncRecognizeResponse;
import com.google.cloud.speech.v1beta1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

import net.sourceforge.javaflacencoder.FLACFileWriter;



//import javaFlacEncoder.FLACFileWriter;

public class Index {
	/*
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String CLIENT_SECRET_JSON_RESOURCE = "C:/audiopro/RaulTomalaSpeech-9ae99f8f8a4a.json";
	private static HttpTransport httpTransport;
	private static final List<String> SCOPES = Arrays.asList(
		      "https://www.googleapis.com/auth/userinfo.profile",
		      "https://www.googleapis.com/auth/userinfo.email");
	private static Oauth2 oauth2;
	private static GoogleClientSecrets clientSecrets;
	private static final java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");
	private static FileDataStoreFactory dataStoreFactory;
	  */
	  
	static AudioFormat audioFormat;
	static TargetDataLine targetDataLine;
	
	
	public static String resultadoTraduccion;
		

	public static void captureAudio(){
		captureAudioInterno();
	}
	
	public static void stopAudio(){
		targetDataLine.stop();
        targetDataLine.close();
	}
	
	public static void traducirAudio() throws IOException{
		traducir();
	}
	
	//This method captures audio input from a
	  // microphone and saves it in an audio file.
	  private static void captureAudioInterno(){
	    try{
	      //Get things set up for capture
	      audioFormat = getAudioFormat();
	      DataLine.Info dataLineInfo =
	                          new DataLine.Info(
	                            TargetDataLine.class,
	                            audioFormat);
	      targetDataLine = (TargetDataLine)
	               AudioSystem.getLine(dataLineInfo);

	      //Create a thread to capture the microphone
	      // data into an audio file and start the
	      // thread running.  It will run until the
	      // Stop button is clicked.  This method
	      // will return after starting the thread.
	      new CaptureThread().start();
	    }catch (Exception e) {
	      e.printStackTrace();
	      System.exit(0);
	    }//end catch
	  }//end captureAudio method
	  
	//This method creates and returns an
	  // AudioFormat object for a given set of format
	  // parameters.  If these parameters don't work
	  // well for you, try some of the other
	  // allowable parameter values, which are shown
	  // in comments following the declarations.
	  private static AudioFormat getAudioFormat(){
	    float sampleRate = 44100.0F;
	    //8000,11025,16000,22050,44100
	    int sampleSizeInBits = 16;
	    //8,16
	    int channels = 1;
	    //1,2
	    boolean signed = true;
	    //true,false
	    boolean bigEndian = false;
	    //true,false
	    return new AudioFormat(sampleRate,
	                           sampleSizeInBits,
	                           channels,
	                           signed,
	                           bigEndian);
	  }//end getAudioFormat
	//=============================================//
	  
	  static class CaptureThread extends Thread{
		  public void run(){
		    @SuppressWarnings("unused")
			AudioFileFormat.Type fileType = null;
		    @SuppressWarnings("unused")
			File audioFile = null;

		    fileType = AudioFileFormat.Type.WAVE;
		    audioFile = new File("junk.wav");
		   
		    try{
		      targetDataLine.open(audioFormat);
		      targetDataLine.start();
		      AudioSystem.write(new AudioInputStream(targetDataLine), FLACFileWriter.FLAC, new File("./resources/audio/hola.flac"));
		      
		    }catch (Exception e){
		      e.printStackTrace();
		    }//end catch
		    
		  }//end run
		}//end inner class CaptureThread
		//=============================================//
	 /* 
	  private static Credential authorize() throws Exception {
		    // load client secrets
		  clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		        new InputStreamReader(Index.class.getResourceAsStream(CLIENT_SECRET_JSON_RESOURCE)));
		    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
		        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
		      System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
		          + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
		      System.exit(1);
		    }
		    // set up authorization code flow
		    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		        httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(
		        dataStoreFactory).build();
		    // authorize
		    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		  }
	  */
  
	  public static String traducir() throws IOException{

		  /*GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
			        new InputStreamReader(Index.class
			            .getResourceAsStream(CLIENT_SECRET_JSON_RESOURCE)));
		  com.google.cloud.speech.spi.v1beta1.SpeechSettings.Builder b;*/
		 /* httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
	      
	      Credential credential = authorize();*/
	      
	       Credentials cre ;
	      
		   /*InstantiatingChannelProvider channelProvider =
				 SpeechSettings.defaultChannelProviderBuilder()
				 .setCredentialsProvider(FixedCredentialsProvider.create(cre))
				 .build();
			SpeechSettings speechSettings = SpeechSettings.defaultBuilder().setChannelProvider(channelProvider).build();
			
			*/
			SpeechClient speech = SpeechClient.create();
				 
			
		    // The path to the audio file to transcribe
		    //String fileName = "./DiaLluvioso.flac";
		    //System.out.println("OldFileName" + fileName);
		   // String newPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		    //String newPath = Executions.getCurrent().getDesktop().getWebApp().getResource("hola.flac");
		   // System.out.println("new Path:" + fileName);
		    String newFileName = "C:\\Users\\ivans\\workspaceNeon\\FENEDIF_Ingreso\\resources\\audio\\DiaLluvioso.flac";
		    newFileName = "./resources/audio/hola.flac";
		    System.out.println("new FileName:" + newFileName);
		    // Reads the audio file into memory
		    //Path path = Paths.get(newFileName);
		    File f = new File(newFileName);
		    //File f = new File(path.toUri());
		    
		    
		    byte[] data = Files.readAllBytes(f.toPath());
		    ByteString audioBytes = ByteString.copyFrom(data);

		    // Builds the sync recognize request
		  
		    System.out.println("Leyo la ruta del archivo!");
		     RecognitionConfig config = RecognitionConfig.newBuilder()
		    	.setLanguageCode("es-EC")
		        .setEncoding(AudioEncoding.FLAC)
		        .setSampleRate(44100)
		        .build();
		    RecognitionAudio audio = RecognitionAudio.newBuilder()
		        .setContent(audioBytes)
		        .build();

		    System.out.println("Antes de enviar reconocimiento!");
		    // Performs speech recognition on the audio file
		    SyncRecognizeResponse response = speech.syncRecognize(config, audio);
		    System.out.println("Despues de enviar reconocimientofgh*hfghgf*******!");
		    List<SpeechRecognitionResult> results = response.getResultsList();

		    System.out.println("Despues de enviar reconocimiento!");
		    for (SpeechRecognitionResult result: results) {
		      List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
		      for (SpeechRecognitionAlternative alternative: alternatives) {
		        System.out.printf("Transcription: %s%n", alternative.getTranscript());
		        return alternative.getTranscript();
		        //Messagebox.show("Transcription: "+alternative.getTranscript(), "Information", Messagebox.OK, Messagebox.INFORMATION);
		      }
		    }
		    System.out.println("Fin!!!");
		    return null;
	  }

}
