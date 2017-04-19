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
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable 
{
    @FXML
    private Button encButt;
    @FXML
    private Button decButt;
    
    @FXML
    private void loadEncFXML(ActionEvent event) 
    {
        if (event.getSource() == encButt)
        {
            try{
                Stage stageMain = (Stage)encButt.getScene().getWindow();
                stageMain.close();
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLEncryption.fxml"));
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
    private void loadDecFXML(ActionEvent event) 
    {
        if (event.getSource() == decButt)
        {
            try{
                Stage stageMain = (Stage)decButt.getScene().getWindow();
                stageMain.close();
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDecryption.fxml"));
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
}
