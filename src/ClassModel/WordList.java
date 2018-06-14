/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassModel;

import Form.ConnectionDB;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author Phong
 */
public class WordList {
    private static WordList mInstance= null;
    
    private ArrayList<String> WordEV , WordVE;
    private DictionaryModel EV,VE,EE;
    
    private ObservableList<String> historyWord;
    
    public ObservableList<String> getHistoryWord()
    {
        return historyWord;
    }
    
    public void UpdateHistoryWord()
    {
        ConnectionDB db = new ConnectionDB();
        db.createConnection();
        historyWord = db.select();
       
    }
    
    public static synchronized WordList getInstance(){
        if(null == mInstance){
            mInstance = new WordList();
        }
        return mInstance;
    }

    
    public ArrayList<String> getWordEV() {
        return WordEV;
    }

    public void setWordEV(ArrayList<String> WordEV) {
        this.WordEV = WordEV;
    }

    public ArrayList<String> getWordVE() {
        return WordVE;
    }

    public void setWordVE(ArrayList<String> WordVE) {
        this.WordVE = WordVE;
    }

    public DictionaryModel getEV() {
        return EV;
    }

    public void setEV(DictionaryModel EV) {
        this.EV = EV;
    }

    public DictionaryModel getVE() {
        return VE;
    }

    public void setVE(DictionaryModel VE) {
        this.VE = VE;
    }

    public DictionaryModel getEE() {
        return EE;
    }

    public void setEE(DictionaryModel EE) {
        this.EE = EE;
    }

    public WordList() {
        WordEV = new ArrayList<>();
        WordVE = new ArrayList<>();
        EV = new DictionaryModel();
        EE = new DictionaryModel();
        VE = new DictionaryModel();
        
        ConnectionDB db = new ConnectionDB();
        db.createConnection();
        historyWord = db.select();
        
    }
    
    
}
