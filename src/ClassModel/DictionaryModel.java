/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassModel;

import java.util.HashMap;
import javax.swing.DefaultListModel;

/**
 *
 * @author Phong
 */
public class DictionaryModel {
    public DictionaryModel() {
        this._dictionary = new HashMap<>();
    }

    private HashMap<String, String> _dictionary;

    public void Add(String Word, String Mean) {
        _dictionary.put(Word, Mean);
    }

    public String Find(String Word) {
        if (_dictionary.get(Word) == null)
            return "Khong tim thay";
        return _dictionary.get(Word);
    }

    public boolean IsContain(String Key){
        return _dictionary.containsKey(Key);
    }
    
   
}
