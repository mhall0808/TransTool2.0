/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transtool2.pkg0;

import GradeItems.WriteGrades;
import Items.Folder;
import Items.Item;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import transtool.xmlTools.GatherItems;

/**
 *
 * @author hallm8
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    AnchorPane contentPane;
    @FXML
    Button browseButton;
    @FXML
    TextField destFileText;
    @FXML
    Button destSaveButton;
    @FXML
    TreeView treeView;
    @FXML
    AnchorPane itemOptionsPane;

    String zipPath = new String();
    String zipDest = new String();
    String zipName = new String();
    String brainho = new String();

    Image module = new Image(
            getClass().getResourceAsStream("module.png"));
    Image content = new Image(
            getClass().getResourceAsStream("content.png"));
    Image discussionBoard = new Image(
            getClass().getResourceAsStream("discussionBoard.png"));
    Image dropbox = new Image(
            getClass().getResourceAsStream("dropbox.png"));
    Image quiz = new Image(
            getClass().getResourceAsStream("quiz.png"));
    Image def = new Image(
            getClass().getResourceAsStream("default.png"));

    ArrayList<Folder> modules;

    @FXML
    private void handleDragOver(DragEvent event) {
        contentPane.setStyle("-fx-background-color: #66CDAA;");
        System.out.println("Drag over induced!!");
    }

    @FXML
    private void handleDragExited(DragEvent event) {
        contentPane.setStyle("-fx-background-color: #FFFFFF;");
    }

    /**
     * Drag Drop Handle takes a file that was dragged onto the screen. Also it
     * will perform a bit of error handling. You will note that multiple files
     * may be dragged. This is intentional, because I eventually want to allow
     * multiple objects being ran at the same time.
     *
     * @param event
     */
    @FXML
    private void dragDropHandle(DragEvent event) {
        folderOptions();
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String filePath = null;
            for (File file : db.getFiles()) {
                filePath = file.getAbsolutePath();
                System.out.println(filePath);

                zipPath = file.getAbsolutePath();
                System.out.println(file.getParent());
                zipDest = file.getParent();
                destFileText.setText(zipDest + "\\course.zip");
                destSaveButton.setDisable(false);
                zipName = "course.zip";
                unzipAll();
                populateArray();
            }

        }
        event.setDropCompleted(success);
        event.consume();
    }
    
    

    @FXML
    private void browseButtonHandle(ActionEvent event) {

        System.out.println("Browse was clicked!");
        //Create a file chooser, which will browse for a file.
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show load file dialog
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            zipPath = file.getAbsolutePath();
            System.out.println(file.getPath());
            zipDest = file.getParent();
            destFileText.setText(zipDest + "\\course.zip");
            zipName = "course.zip";
            destSaveButton.setDisable(false);
        }
    }

    @FXML
    private void saveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save File As...");
        fileChooser.setInitialFileName("course");
        if (zipDest != null) {
            fileChooser.setInitialDirectory(new File(zipDest));
        }

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            zipDest = file.getParent();
            zipName = file.getName();
            System.out.println(file.getAbsoluteFile());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        contentPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    event.consume();
                }
            }
        });

    }

    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
            System.out.println("Node click: " + name);
        }
    }

    @FXML
    private void onEditStart(ActionEvent event) {
        String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
        System.out.println("Node click: " + name);
    }

    /**
     *
     */
    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;

        public TextFieldTreeCellImpl() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    private void unzipAll() {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(zipDest + "\\temp");
            brainho = zipDest + "\\temp\\brainhoneyManifest.xml";
            System.out.println(brainho);
        } catch (ZipException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void populateArray() {
        modules = new ArrayList<>();
        // Open up the folder to gather files
        GatherItems gather = new GatherItems();
        gather.setBrainhoneyPath(brainho);

        // populate items.
        gather.populateItems();

        WriteGrades categories = new WriteGrades(zipDest, gather.getGradeCategories());
        categories.setItems(gather.getItems());
        categories.writeToXML();

        gather.setItems(categories.getItems());
        //gather.writeItems();

        gather.item2Folder();

        ArrayList<Folder> folders = gather.getFolders();

        /**
         * Loop through and add sub folders to each folder by finding the parent
         * and adding to it.
         */
        for (Folder folder : folders) {
            if (!folder.getParent().equals("DEFAULT")) {
                for (Folder parentFolder : folders) {
                    if (parentFolder.getFolderID().equals(folder.getParent())) {
                        ArrayList<Folder> newChild = parentFolder.getFolders();
                        newChild.add(folder);
                        parentFolder.setFolders(newChild);
                        break;
                    }
                }
            }
        }

        treeView.setEditable(true);
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl();
            }
        });
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleTreeClicked(event);
        };

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

        ImageView rootImage = new ImageView(module);
        rootImage.setFitHeight(16);
        rootImage.setFitWidth(16);
        TreeItem<String> rootItem = new TreeItem<String>(gather.getCourseTitle(), rootImage);

        rootItem.setExpanded(true);
        int i = 0;
        for (Folder folder : folders) {
            ImageView image = new ImageView(module);
            image.setFitHeight(16);
            image.setFitWidth(16);

            if (folder.getParent().equals("DEFAULT")) {
                rootItem.getChildren().add(populateTree(folder));
                modules.add(folder);
            }
        }
        TreeView<String> tree = new TreeView<String>(rootItem);
        treeView.setRoot(rootItem);
    }

    /**
     * Populate Tree
     *
     * A recursive function that sorts through a list, and we don't know how
     * deep it is, exactly. Therefore, we must resort to demonic influences,
     * such as recursion.
     *
     * Life is pain.
     *
     * @param folderList
     * @return
     */
    TreeItem<String> populateTree(Folder folder) {
        ImageView image = new ImageView(module);
        image.setFitHeight(16);
        image.setFitWidth(16);
        TreeItem<String> folderList = new TreeItem<>(folder.getName(), image);

        /**
         * First, we rotate through all items, and place them on the treeview.
         */
        ArrayList<Item> items = folder.getItems();
        for (Item item : items) {
            ImageView itemIcon;

            System.out.println("Getting category: " + item.getItemType());
            System.out.println(item.getName());
            switch ("Item ID is:  " + item.getItemType()) {
                case "Dropbox":
                    itemIcon = new ImageView(dropbox);
                    break;
                case "Content":
                    itemIcon = new ImageView(content);
                    break;
                case "Quiz":
                    itemIcon = new ImageView(quiz);
                    break;
                case "Discussion":
                    itemIcon = new ImageView(discussionBoard);
                    break;
                default:
                    itemIcon = new ImageView(def);
                    System.out.println("error!  Fell out of range:" + item.getItemType());
            }
            itemIcon.setFitHeight(16);
            itemIcon.setFitWidth(16);
            folderList.getChildren().add(new TreeItem<String>(item.getName(), itemIcon));
        }

        /**
         * NEXT: We create a recursive function that cycles through each folder
         * and fills the treeview. Then we pass it back to the root where it
         * will all attach.
         */
        for (Folder childFolder : folder.getFolders()) {
            folderList.getChildren().add(populateTree(childFolder));
        }

        return folderList;
    }

    private void handleTreeClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
            System.out.println("Node click: " + name);

            
            for (Folder folder: modules){
                if(findItem(folder, name) != null){
                    System.out.println(name + " successfully found!!! =)");
                }
            }
            
            
        }
    }

    /**
     *  FIND ITEM
     *      This will sort through each folder in search of the item in question.
     *  if found, return that value.
     * 
     * @param folder
     * @return 
     */
    private Item findItem(Folder folder, String itemName) {
       
        // First, check this folder
        if (folder.getName().equals(itemName)){
            return folder;
        }
       
        // next, check the items one by one.
        for (Item item : folder.getItems()){
            if (item.getName().equals(itemName)){
                return item;
            }
        }
        
        // Finally, check each folder and its contents.
        for (Folder childFolder: folder.getFolders()){
            if (findItem(childFolder, itemName) != null){
                return findItem(childFolder, itemName);
            }
        }
        return null;
    }
    
    /**
     * FOLDER OPTIONS
     * 
     *  When a folder is selected, these options will appear on the screen.  
     * The user will then be able to edit the values and make changes.
     * 
     * 
     */
    private void folderOptions(){
        
        /**
         * To make things easier to read, I am separating everything.  For this,
         * I am creating a save button, a label, a delete button, and a textbox.
         */
        Button saveChanges = new Button();
        saveChanges.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Test function worked properly!!!");
    }
});
        saveChanges.setText("Save Changes");
        saveChanges.setId("saveButton");
        saveChanges.onMouseClickedProperty();
        saveChanges.setLayoutX(25);
        saveChanges.setLayoutY(85);
        itemOptionsPane.getChildren().add(saveChanges); 
        
        
        // Label
        Label moduleName = new Label();
        moduleName.setText("Module Name");
        moduleName.setLayoutX(25);
        moduleName.setLayoutY(25);
        itemOptionsPane.getChildren().add(moduleName);
        
        // Text Field for entering a name in.
        TextField moduleInput = new TextField();
        moduleInput.setText("testing text");
        moduleInput.setLayoutX(25);
        moduleInput.setLayoutY(50);
        moduleInput.setPrefWidth(250);
        itemOptionsPane.getChildren().add(moduleInput);
        
        // Lastly, we need a remove button.
        Button deleteModule = new Button();
        deleteModule.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Test function worked properly!!!");
    }
});
        deleteModule.setText("Delete Module");
        deleteModule.setId("deleteModule");
        deleteModule.onMouseClickedProperty();
        deleteModule.setLayoutX(150);
        deleteModule.setLayoutY(85);
        itemOptionsPane.getChildren().add(deleteModule);
    }

}



