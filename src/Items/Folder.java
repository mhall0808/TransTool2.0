/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import java.util.ArrayList;

/**
 *
 * @author hallm8
 */
public class Folder extends Item{
    private String parent;
    private String folderID;
    private ArrayList<Item> items;
    private ArrayList<Folder> folders;

    public Folder() {
        this.folderID   = new String();
        this.parent     = new String();
        this.items      = new ArrayList<>();
        this.folders    = new ArrayList<>();
        itemType        = "Folder";
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }
    
    
    
    
    
}
