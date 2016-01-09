/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper.models;

import RustMapHelper.AddMapPieceStage;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joeca_000
 */
public class Data {

    private static final Data INSTANCE = new Data();

    public Object data_object;

    public static Data getInstance() {
        return INSTANCE;
    }

    private Data() {
    }

    public void openDataFile(String path) {
        try {
            checkFile(path);
            YamlReader reader = new YamlReader(new FileReader(path));
            data_object = reader.read();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.WARNING, "Data file not found, creating one...", ex);
        } catch (YamlException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String GetBaseMapPath() {
        return "map.png";
//        Map map = (Map) data_object;
//        return (String) map.get("base_map_path");
    }

    private void checkFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
