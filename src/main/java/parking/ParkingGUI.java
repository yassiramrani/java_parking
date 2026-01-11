package parking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingGUI extends JFrame {

    private Parking parking;
    private JTextArea logArea;
    private JLabel capacityLabel;
    private JTextField plateField;
    private int parkingCapacity = 5;
    private AtomicInteger vehicleIdCounter = new AtomicInteger(100);

    // Tables
    private JTable ownerTable;
    private DefaultTableModel ownerModel;
    private JTable historyTable;
    private DefaultTableModel historyModel;

    public ParkingGUI() {
        // Initialisation du système central
        parking = new Parking(parkingCapacity);
        parking.setLogger(this::logMessage);

        // Configuration de la fenêtre
        setTitle("Système de Gestion de Parking");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- ONGLET 1 : SIMULATION ---
        JPanel simulationPanel = new JPanel(new BorderLayout());

        // Haut : Capacité & Entrée
        JPanel simTopPanel = new JPanel(new GridLayout(3, 1));
        capacityLabel = new JLabel("Capacité : " + parkingCapacity, SwingConstants.CENTER);
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        simTopPanel.add(capacityLabel);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Plaque(s) (séparées par espace/virgule) :"));
        plateField = new JTextField(10);
        JButton parkButton = new JButton("Garer Véhicule");
        JButton concurrentSimButton = new JButton("Simuler Entrée Concurrente");
        inputPanel.add(plateField);
        inputPanel.add(parkButton);
        inputPanel.add(concurrentSimButton);
        simTopPanel.add(inputPanel);

        simulationPanel.add(simTopPanel, BorderLayout.NORTH);

        // Centre : Logs
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        simulationPanel.add(scrollPane, BorderLayout.CENTER);

        // Bas : Visualisation Graphique
        ParkingPanel parkingPanel = new ParkingPanel();
        parkingPanel.setPreferredSize(new Dimension(800, 100)); // Hauteur fixe
        simulationPanel.add(parkingPanel, BorderLayout.SOUTH);

        // Timer pour mise à jour temps réel (100ms)
        Timer timer = new Timer(100, e -> parkingPanel.repaint());
        timer.start();

        tabbedPane.addTab("Simulation", simulationPanel);

        // --- ONGLET 2 : ADMINISTRATION ---
        JPanel adminPanel = new JPanel(new BorderLayout());

        // Haut : Formulaires
        JPanel formsPanel = new JPanel(new GridLayout(1, 2)); // Formulaires côte à côte

        // Formulaire d'Ajout de Véhicule
        JPanel addVehiclePanel = new JPanel(new GridLayout(5, 2));
        addVehiclePanel.setBorder(BorderFactory.createTitledBorder("Enregistrer Nouveau Véhicule"));
        JTextField ownerNameField = new JTextField();
        JTextField cinField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField newPlateField = new JTextField();
        JButton registerButton = new JButton("Enregistrer");

        addVehiclePanel.add(new JLabel("Nom Propriétaire :"));
        addVehiclePanel.add(ownerNameField);
        addVehiclePanel.add(new JLabel("CIN :"));
        addVehiclePanel.add(cinField);
        addVehiclePanel.add(new JLabel("Tél :"));
        addVehiclePanel.add(phoneField);
        addVehiclePanel.add(new JLabel("Plaque :"));
        addVehiclePanel.add(newPlateField);
        addVehiclePanel.add(new JLabel(""));
        addVehiclePanel.add(registerButton);

        // Formulaire de Modification de Véhicule
        JPanel changeVehiclePanel = new JPanel(new GridLayout(3, 2));
        changeVehiclePanel.setBorder(BorderFactory.createTitledBorder("Modifier Véhicule Propriétaire"));
        JTextField oldPlateField = new JTextField();
        JTextField updatePlateField = new JTextField();
        JButton updateButton = new JButton("Mettre à jour");

        changeVehiclePanel.add(new JLabel("Plaque Actuelle :"));
        changeVehiclePanel.add(oldPlateField);
        changeVehiclePanel.add(new JLabel("Nouvelle Plaque :"));
        changeVehiclePanel.add(updatePlateField);
        changeVehiclePanel.add(new JLabel(""));
        changeVehiclePanel.add(updateButton);

        formsPanel.add(addVehiclePanel);
        formsPanel.add(changeVehiclePanel);

        adminPanel.add(formsPanel, BorderLayout.NORTH);

        // Centre : Tableaux
        JTabbedPane tablesTab = new JTabbedPane();

        // Tableau Propriétaires
        ownerModel = new DefaultTableModel(new String[] { "ID", "Nom", "CIN", "Tél", "Plaque" }, 0);
        ownerTable = new JTable(ownerModel);
        tablesTab.addTab("Propriétaires & Véhicules", new JScrollPane(ownerTable));

        // Tableau Historique
        historyModel = new DefaultTableModel(new String[] { "Heure", "Détails" }, 0);
        historyTable = new JTable(historyModel);
        tablesTab.addTab("Historique Parking", new JScrollPane(historyTable));

        adminPanel.add(tablesTab, BorderLayout.CENTER);

        // Bas : Boutons (Actualiser & Détails)
        JPanel buttonsPanel = new JPanel();
        JButton refreshButton = new JButton("Actualiser Données");
        JButton showDetailsButton = new JButton("Voir Détails");
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(showDetailsButton);
        adminPanel.add(buttonsPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Administration", adminPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // --- LISTENERS ---

        // Tab Change Listener (Password Protection)
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Administration Tab
                String input = JOptionPane.showInputDialog(ParkingGUI.this, "Entrez le mot de passe administrateur :",
                        "Sécurité", JOptionPane.QUESTION_MESSAGE);
                if (input == null || !input.equals("admin2004")) {
                    JOptionPane.showMessageDialog(ParkingGUI.this, "Mot de passe incorrect !", "Accès Refusé",
                            JOptionPane.ERROR_MESSAGE);
                    tabbedPane.setSelectedIndex(0); // Return to Simulation
                }
            }
        });

        // Park one or multiple vehicles
        parkButton.addActionListener(e -> {
            String text = plateField.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(ParkingGUI.this, "Veuillez entrer une ou plusieurs plaques.");
                return;
            }

            // Découpage par virgule, point-virgule ou espace
            String[] plates = text.split("[,\\s;]+");

            for (String plate : plates) {
                if (!plate.isEmpty()) {
                    // Propriétaire invité (l'accès sera vérifié par le Parking)
                    Owner owner = new Owner(1, "Invité", "0000", "000");
                    Vehicle vehicle = new Vehicle(vehicleIdCounter.getAndIncrement(), plate, owner, parking);
                    new Thread(vehicle, "Véhicule-" + plate).start();
                }
            }
            plateField.setText("");
        });

        // Concurrent Simulation
        concurrentSimButton.addActionListener(e -> {
            logMessage("--- DÉMARRAGE SIMULATION ---");
            new Thread(() -> { // Run in background to avoid freezing UI if DB is slow
                try {
                    ParkingRepository repo = new ParkingRepository();
                    List<Vehicle> dbVehicles = repo.loadVehiclesFromDb(parking);

                    if (dbVehicles.isEmpty()) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ParkingGUI.this,
                                "Aucun véhicule en base pour la simulation.\nVeuillez en enregistrer d'abord.",
                                "Info", JOptionPane.WARNING_MESSAGE));
                        return;
                    }

                    int count = 0;
                    for (Vehicle v : dbVehicles) {
                        if (count >= 5)
                            break; // Limit to 5 for demo
                        new Thread(v, "Vehicle-" + v.getPlateNumber()).start();
                        count++;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ignored) {
                        } // Stagger slightly
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // Register Vehicle (DB)
        registerButton.addActionListener(e -> {
            try {
                String name = ownerNameField.getText();
                String cin = cinField.getText();
                String phone = phoneField.getText();
                String plate = newPlateField.getText();

                Owner owner = new Owner(0, name, cin, phone);
                Vehicle vehicle = new Vehicle(0, plate, owner, parking);

                ParkingRepository repo = new ParkingRepository();
                repo.addVehicle(vehicle);

                JOptionPane.showMessageDialog(ParkingGUI.this, "Vehicle Registered Successfully!");
                ownerNameField.setText("");
                cinField.setText("");
                phoneField.setText("");
                newPlateField.setText("");
                refreshData();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ParkingGUI.this, "Erreur : " + ex.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Vehicle (DB)
        updateButton.addActionListener(e -> {
            try {
                String oldPlate = oldPlateField.getText().trim();
                String newPlate = updatePlateField.getText().trim();

                if (oldPlate.isEmpty() || newPlate.isEmpty()) {
                    JOptionPane.showMessageDialog(ParkingGUI.this, "Veuillez remplir tous les champs.");
                    return;
                }

                ParkingRepository repo = new ParkingRepository();
                repo.updateVehicle(oldPlate, newPlate);

                JOptionPane.showMessageDialog(ParkingGUI.this, "Véhicule mis à jour avec succès !");
                oldPlateField.setText("");
                updatePlateField.setText("");
                refreshData();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ParkingGUI.this, "Erreur lors de la mise à jour : " + ex.getMessage());
            }
        });

        // Refresh Button
        refreshButton.addActionListener(e -> refreshData());

        // Show Details Button
        showDetailsButton.addActionListener(e -> {
            int selectedRow = ownerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ParkingGUI.this,
                        "Veuillez sélectionner un propriétaire dans le tableau.");
                return;
            }

            String name = (String) ownerModel.getValueAt(selectedRow, 1);
            String cin = (String) ownerModel.getValueAt(selectedRow, 2);
            String phone = (String) ownerModel.getValueAt(selectedRow, 3);
            String plate = (String) ownerModel.getValueAt(selectedRow, 4);

            String message = String.format("Détails du Propriétaire:\n\nNom: %s\nCIN: %s\nTéléphone: %s\nPlaque: %s",
                    name, cin, phone, plate);

            JOptionPane.showMessageDialog(ParkingGUI.this, message, "Détails Propriétaire",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Initial Load
        refreshData();
    }

    private void refreshData() {
        // Refresh Owners
        ownerModel.setRowCount(0); // Clear table
        try {
            ParkingRepository repo = new ParkingRepository();
            List<Vehicle> vehicles = repo.loadVehiclesFromDb(parking);
            for (Vehicle v : vehicles) {
                Owner o = v.getOwner();
                ownerModel.addRow(new Object[] {
                        o.getIdOwner(),
                        o.name, // Accessing protected field (same package)
                        o.cin,
                        o.phoneNumber,
                        v.getPlateNumber()
                });
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log helper
        }

        // Refresh History
        historyModel.setRowCount(0);
        try {
            Map<java.time.LocalDateTime, Object> history = parking.getParkingHistory().getParkingMap();
            for (Map.Entry<java.time.LocalDateTime, Object> entry : history.entrySet()) {
                historyModel.addRow(new Object[] {
                        entry.getKey().toString(),
                        entry.getValue().toString()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingGUI().setVisible(true);
        });
    }

    // Inner class for visualization
    class ParkingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (parking == null)
                return;

            int capacity = parkingCapacity;
            int available = parking.getAvailableSpots();
            int occupied = capacity - available;

            int width = getWidth();
            int height = getHeight();
            int boxWidth = (width / capacity) - 10;
            int boxHeight = height - 40;

            for (int i = 0; i < capacity; i++) {
                int x = i * (boxWidth + 10) + 5;
                int y = 20;

                if (i < occupied) {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, boxWidth, boxHeight);
                    g.setColor(Color.WHITE);
                    g.drawString("Occupé", x + boxWidth / 2 - 20, y + boxHeight / 2);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, boxWidth, boxHeight);
                    g.setColor(Color.BLACK);
                    g.drawString("Libre", x + boxWidth / 2 - 15, y + boxHeight / 2);
                }
                g.setColor(Color.BLACK);
                g.drawRect(x, y, boxWidth, boxHeight);
            }
        }
    }
}
