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

public class FXMLDecryptionController implements Initializable 
{
    @FXML 
    private Button backButt;
    @FXML
    private Button decryptButt;
    @FXML
    private TextField decPath;
    @FXML
    private TextField keyPath;
    @FXML
    private TextField outPath;
    @FXML
    private TextField resultField;
    //Returns to main menu
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
    //Connects UI with C++ engine
    private void executeSysCall(String comm[])
    {
        String result;
        //Clears the result field in the UI
        resultField.clear();
        //Execute C++ encryption engine
        SystemCallExecution call = new SystemCallExecution();
        call.execute(comm);
        //Returns result from the C++ decryption engine
        result = call.getList().get(0);
        resultField.appendText(result);
    }
    //Decrypts file
    @FXML
    private void decryptFile(ActionEvent event)
    {
        String jarPath = FXMLDocumentController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        
        if (event.getSource() == decryptButt)
        {
            String result;
            String path;
            String pathFinal;
            //Creates readable path to the C++ decryption engine
            path = jarPath.substring(0, jarPath.length() - 20);
            //Trying to let the program accept spaces in path, 
            //currently fixing in another branch
            pathFinal = path.replace("%20", "\\ ");
            pathFinal = pathFinal + "/AESdecrypt"; 
            //Sets arguments to be ingested by the C++ decryption engine
            String program = pathFinal;
            String arg1 = decPath.getText();
            String arg2 = keyPath.getText();
            String arg3 = outPath.getText();
            //Error checking user input
            if (arg1.length() == 0 || arg2.length() == 0 || arg3.length() == 0)
            {
                String command[] = {program};
                //Execute system call
                executeSysCall(command);
            }
            else
            {
                String command[] = {program, arg1, arg2, arg3};
                //Execute system call
                executeSysCall(command);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
}
