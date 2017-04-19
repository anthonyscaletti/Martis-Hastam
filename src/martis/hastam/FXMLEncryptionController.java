//@Author Tony Wagdi

package martis.hastam;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import SCE.SystemCallExecution;
import java.io.File;

public class FXMLEncryptionController implements Initializable 
{
    @FXML 
    private Button backButt;
    @FXML
    private Button encryptButt;
    @FXML
    private TextField encPath;
    @FXML
    private TextField keyPath;
    @FXML
    private TextField outPath;
    @FXML
    private TextField resultField;
    
    @FXML
    private void backToMain(ActionEvent event)
    {
        if (event.getSource() == backButt)
        {
            try{
                Stage stageMain = (Stage)backButt.getScene().getWindow();
                stageMain.close();
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stageEnc = new Stage();
                
                stageEnc.setResizable(false);
                stageEnc.setTitle("Martis Hastam 1.0");
                stageEnc.setScene(scene);
                stageEnc.show();
            }
            catch(IOException e){
            }
        }
    }
    
    @FXML
    private void encryptFile(ActionEvent event) throws IOException
    {
        String jarPath = FXMLEncryptionController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        
        if (event.getSource() == encryptButt)
        {
            String result;
            String path = jarPath.substring(0, jarPath.length() - 20);
            String pathFinal = path.replace("%20", "\\ ");
            pathFinal = pathFinal + "/AESencrypt"; 
            System.out.println(pathFinal);
            
            String program = pathFinal;
            String arg1 = encPath.getText();
            String arg2 = keyPath.getText();
            String arg3 = outPath.getText();
            
            String command[] = {program, arg1, arg2, arg3};
            
            resultField.clear();
            
            SystemCallExecution call = new SystemCallExecution();
            call.execute(command);
            
            result = call.getList().get(0);
            resultField.appendText(result);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
}