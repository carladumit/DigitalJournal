package org.carladumit.digitaljournal.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.carladumit.digitaljournal.exceptions.DatabaseException;
import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.EntryNotFoundException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.service.JournalService;
import org.carladumit.digitaljournal.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainJournalController {

    @FXML private VBox viewJournal;
    @FXML private VBox viewNewEntry;
    @FXML private VBox detailPane;
    @FXML private Label todayLabel;
    @FXML private Label entriesCountLabel;
    @FXML private Button btnNewEntry;
    @FXML private VBox viewOnThisDay;
    @FXML private VBox onThisDayContainer;
    @FXML private ToggleButton onThisDayToggle;
    @FXML private ToggleButton allEntriesToggle;
    @FXML private Text onThisDayTitle;
    @FXML private Label onThisDaySubtitle;
    @FXML private VBox viewBrowse;

    @FXML private TableView<JournalEntry> entriesTableView;
    @FXML private TableColumn<JournalEntry, String> dateColumn;
    @FXML private TableColumn<JournalEntry, String> moodColumn;
    @FXML private TableColumn<JournalEntry, String> previewColumn;

    @FXML private TableView<JournalEntry> yearlyTableView;
    @FXML private TableColumn<JournalEntry, String> yearColumn;
    @FXML private TableColumn<JournalEntry, String> yearMoodColumn;
    @FXML private TableColumn<JournalEntry, String> yearEntryColumn;

    @FXML private Label lblSelectedDate;
    @FXML private Label lblSelectedRating;
    @FXML private TextArea txtSelectedContent;
    @FXML private Button btnDeleteEntry;
    @FXML private Label historyStatusLabel;

    @FXML private TextArea journalTextArea;
    @FXML private Label statusLabel;

    private UserService userService;
    private JournalService journalService;
    private String selectedRating = null;
    private Button selectedEmojiButton = null;

    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter ENTRY_DATE_FORMAT = DateTimeFormatter.ofPattern("d MMMM, yyyy");
    private final ToggleGroup viewToggleGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        LocalDate today = LocalDate.now();
        todayLabel.setText(today.format(HEADER_DATE_FORMAT));
        onThisDayTitle.setText("On This Day");
        onThisDaySubtitle.setText("Your memories from " + today.format(DateTimeFormatter.ofPattern("d MMMM")) + " across the years");

        setupTable();
        setupTableSelection();
        setupYearlyTable();

        onThisDayToggle.setToggleGroup(viewToggleGroup);
        allEntriesToggle.setToggleGroup(viewToggleGroup);
        allEntriesToggle.setSelected(true);
    }

    public void initServices(UserService userService, JournalService journalService) {
        this.userService = userService;
        this.journalService = journalService;
        loadUserEntries();
    }

    private void setupTable() {
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntryDate().format(ENTRY_DATE_FORMAT)));
        moodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatMood(cellData.getValue().getRating())));
        previewColumn.setCellValueFactory(cellData -> new SimpleStringProperty(createPreview(cellData.getValue().getText())));

        dateColumn.setSortable(true);
        moodColumn.setSortable(true);
        previewColumn.setSortable(false);
        entriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableSelection() {
        entriesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldEntry, newEntry) -> {
            if (newEntry != null) {
                displayEntryDetails(newEntry);
            } else {
                clearSelectionDetails();
            }
        });
    }

    private void setupYearlyTable() {
        yearColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEntryDate().getYear())));
        yearMoodColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMood(cellData.getValue().getRating())));
        yearEntryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(createPreview(cellData.getValue().getText())));

        yearColumn.setSortable(true);
        yearMoodColumn.setSortable(false);
        yearEntryColumn.setSortable(false);
        yearlyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadUserEntries() {
        try {
            List<JournalEntry> entries = journalService.getUserEntries();
            entriesTableView.setItems(FXCollections.observableArrayList(entries));
            updateEntriesCount(entries.size());
            clearSelectionDetails();
        } catch (DatabaseException e) {
            historyStatusLabel.setText("Unable to load your journal entries.");
        }
    }

    private void loadOnThisDayEntries() {
        try {
            LocalDate today = LocalDate.now();
            List<JournalEntry> entries = journalService.getUserEntriesAcrossYears(today);
            yearlyTableView.setItems(FXCollections.observableArrayList(entries));
        } catch (DatabaseException e) {
            yearlyTableView.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateEntriesCount(int count) {
        if (count == 0) {
            entriesCountLabel.setText("");
        } else if (count == 1) {
            entriesCountLabel.setText("1 entry");
        } else {
            entriesCountLabel.setText(count + " entries");
        }
    }

    private void displayEntryDetails(JournalEntry entry) {
        detailPane.setVisible(true);
        detailPane.setManaged(true);
        lblSelectedDate.setText(entry.getEntryDate().format(ENTRY_DATE_FORMAT));
        lblSelectedRating.setText(formatMood(entry.getRating()));
        txtSelectedContent.setText(entry.getText());
        btnDeleteEntry.setDisable(false);
        historyStatusLabel.setText("");
    }

    private void clearSelectionDetails() {
        detailPane.setVisible(false);
        detailPane.setManaged(false);
        lblSelectedDate.setText("");
        lblSelectedRating.setText("");
        txtSelectedContent.clear();
        btnDeleteEntry.setDisable(true);
    }

    @FXML
    private void showNewEntryView() {
        clearNewEntryForm();
        viewJournal.setVisible(false);
        viewJournal.setManaged(false);
        viewNewEntry.setVisible(true);
        viewNewEntry.setManaged(true);
        viewBrowse.setVisible(false);
        viewBrowse.setManaged(false);
        journalTextArea.requestFocus();
    }

    @FXML
    private void showJournalView() {
        viewNewEntry.setVisible(false);
        viewNewEntry.setManaged(false);
        viewJournal.setVisible(true);
        viewJournal.setManaged(true);
        viewBrowse.setVisible(true);
        viewBrowse.setManaged(true);
        loadUserEntries();
    }

    @FXML
    private void selectRating(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (selectedEmojiButton != null) {
            selectedEmojiButton.getStyleClass().remove("emoji-btn-selected");
        }
        selectedEmojiButton = clickedButton;
        selectedEmojiButton.getStyleClass().add("emoji-btn-selected");
        selectedRating = (String) clickedButton.getUserData();
        statusLabel.setText("");
    }

    @FXML
    private void handleSaveEntry() {
        if (selectedRating == null) {
            showStatus("Please select how your day was.", false);
            return;
        }

        String text = journalTextArea.getText().trim();
        if (text.isEmpty()) {
            showStatus("Please write something about your day.", false);
            return;
        }

        try {
            journalService.createEntry(LocalDate.now(), selectedRating, text);
            showStatus("Entry saved successfully! ✨", true);
            showJournalView();
        } catch (EntryAlreadyExistsException e) {
            showStatus("You have already written today's entry.", false);
        } catch (DatabaseException e) {
            showStatus("Database unavailable.", false);
        }
    }

    @FXML
    private void showOnThisDayView() {
        viewJournal.setVisible(false);
        viewJournal.setManaged(false);
        viewOnThisDay.setVisible(true);
        viewOnThisDay.setManaged(true);
        loadOnThisDayEntries();
    }

    @FXML
    private void showAllEntriesView() {
        viewOnThisDay.setVisible(false);
        viewOnThisDay.setManaged(false);
        viewJournal.setVisible(true);
        viewJournal.setManaged(true);
        loadUserEntries();
    }

    @FXML
    private void handleDeleteEntry() {
        JournalEntry selectedEntry = entriesTableView.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            return;
        }

        LocalDate selectedDate = selectedEntry.getEntryDate();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Entry");
        alert.setHeaderText("Delete entry for " + selectedDate.format(ENTRY_DATE_FORMAT) + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    journalService.deleteEntry(selectedDate);
                    loadUserEntries();
                    historyStatusLabel.setText("Entry deleted.");
                } catch (EntryNotFoundException | DatabaseException e) {
                    historyStatusLabel.setText("Unable to delete entry.");
                }
            }
        });
    }

    @FXML
    private void handleSignOut() {
        if (userService != null) {
            userService.logout();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 440, 560);
            Stage stage = (Stage) viewJournal.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Digital Journal - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createPreview(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String cleanText = text.replaceAll("\\s+", " ").trim();
        return (cleanText.length() <= 80) ? cleanText : cleanText.substring(0, 80) + "...";
    }

    private String formatMood(String rating) {
        if (rating == null) {
            return "";
        }
        return switch (rating.toUpperCase()) {
            case "SAD" -> "😭  Sad";
            case "BAD" -> "🙁  Bad";
            case "NEUTRAL" -> "😐  Neutral";
            case "GOOD" -> "🙂  Good";
            case "GREAT" -> "😄  Great";
            default -> rating;
        };
    }

    private void clearNewEntryForm() {
        journalTextArea.clear();
        selectedRating = null;
        if (selectedEmojiButton != null) {
            selectedEmojiButton.getStyleClass().remove("emoji-btn-selected");
            selectedEmojiButton = null;
        }
        statusLabel.setText("");
    }

    private void showStatus(String message, boolean success) {
        statusLabel.getStyleClass().removeAll("status-success", "status-error");
        statusLabel.getStyleClass().add(success ? "status-success" : "status-error");
        statusLabel.setText(message);
    }
}