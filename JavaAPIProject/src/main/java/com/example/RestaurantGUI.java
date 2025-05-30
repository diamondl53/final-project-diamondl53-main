package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.io.*;


public class RestaurantGUI extends JFrame {
    
    private static final String API_KEY = "AIzaSyA6-T4EZ2TnGfLvIEma89qj7DncsKL4szE";
    private static final int RADIUS = 1000;
   
    private JPanel mainPanel, inputPanel, resultPanel;
    private JComboBox<String> cuisineBox;
    private JTextField latField, lonField;
    private java.util.List<String> savedRestaurants;
    private static final String SAVE_FILE = "saved_restaurants.txt";


    
    public RestaurantGUI() {
        setTitle("RESTAURANT RECOMMENDER");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("Label.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("Button.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("ComboBox.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("TextField.font", new Font("Comic Sans MS", Font.PLAIN, 17));

        savedRestaurants = new ArrayList<>();
        loadSavedRestaurantsFromFile();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);
        
        mainPanel.setBackground(new Color (255,0,0));
        
        
        inputPanel = new JPanel();
       

        
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.setBackground(new Color(245, 245, 245)); 
        inputPanel.add(new JLabel("Cuisine:"));
        
        String[] cuisines = {"Chinese", "Italian", "Mexican", "Indian", "Japanese", "Greek", "Russian", "Thai"};
        cuisineBox = new JComboBox<>(cuisines);
        inputPanel.add(cuisineBox);
        
        inputPanel.add(new JLabel("Latitude:"));
        latField = new JTextField("40.7128"); 
        inputPanel.add(latField);
        
        inputPanel.add(new JLabel("Longitude:"));
        lonField = new JTextField("-74.0060"); 
        inputPanel.add(lonField);
        
        mainPanel.add(inputPanel);
        
        // Search button
        JButton searchButton = new JButton("Find Restaurants");
        searchButton.addActionListener(e -> {
            try {
                searchRestaurants();
            } catch (Exception e1) {
                
                e1.printStackTrace();
            }
        });
        mainPanel.add(searchButton);

        // Save restaurant button
JButton saveButton = new JButton("Save Restaurant");
saveButton.addActionListener(e -> showSaveDialog());
mainPanel.add(saveButton);

// View saved restaurants button
JButton viewSavedButton = new JButton("View Saved Restaurants");
viewSavedButton.addActionListener(e -> showSavedRestaurants());
mainPanel.add(viewSavedButton);

        
        // Results panel
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        mainPanel.add(scrollPane);
    }

    private void showSaveDialog() {
    JTextField nameField = new JTextField();
    JTextField addressField = new JTextField();
    JTextField cuisineField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Restaurant Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Address:"));
    panel.add(addressField);
    panel.add(new JLabel("Cuisine:"));
    panel.add(cuisineField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Save Restaurant",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String entry = "Name: " + nameField.getText().trim() + "\n" +
                       "Address: " + addressField.getText().trim() + "\n" +
                       "Cuisine: " + cuisineField.getText().trim();
        savedRestaurants.add(entry);
        saveRestaurantToFile(entry);
        JOptionPane.showMessageDialog(this, "Restaurant saved!");
    }
}

private void showSavedRestaurants() {
    if (savedRestaurants.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No saved restaurants.");
        return;
    }

    JTextArea textArea = new JTextArea();
    for (String entry : savedRestaurants) {
        textArea.append(entry + "\n\n");
    }
    textArea.setEditable(false);
    textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 300));
    JOptionPane.showMessageDialog(this, scrollPane, "Saved Restaurants", JOptionPane.INFORMATION_MESSAGE);
}

private void saveRestaurantToFile(String entry) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE, true))) {
        writer.write(entry);
        writer.write("\n===\n"); // separator
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to save to file.");
        e.printStackTrace();
    }
}

private void loadSavedRestaurantsFromFile() {
    File file = new File(SAVE_FILE);
    if (!file.exists()) return;

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("===")) {
                savedRestaurants.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() > 0) {
            savedRestaurants.add(sb.toString().trim());
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load saved restaurants.");
        e.printStackTrace();
    }
}


    
    private void searchRestaurants() throws Exception {
        String topRestaurant = "";
        double highestRating = -1;
        resultPanel.removeAll(); 
        int count = 1;
        String cuisine = (String)cuisineBox.getSelectedItem();
        double lat = Double.parseDouble(latField.getText());
        double lon = Double.parseDouble(lonField.getText());
        
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
            "?location=" + lat + "," + lon + 
            "&radius=" + RADIUS + 
            "&type=restaurant" + 
            "&keyword=" + cuisine + 
            "&key=" + API_KEY;

        String jsonResponse = Apis.getData(url);
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray restaurants = response.getJSONArray("results");
        
        for (int i = 0; i < Math.min(3, restaurants.length()); i++) {
            
            JSONObject restaurant = restaurants.getJSONObject(i);
            String name = restaurant.getString("name");
            double rating = restaurant.optDouble("rating", 0);
            if(rating > highestRating) {
                highestRating = rating;
                topRestaurant = name;
            }
            String address = restaurant.optString("vicinity", "Address not available");
            
            JPanel restaurantPanel = new JPanel();
            restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
            restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            
            restaurantPanel.add(new JLabel(count + "."));
            restaurantPanel.add(new JLabel(name));
            restaurantPanel.add(new JLabel("Rating: " + rating));
            restaurantPanel.add(new JLabel("Address: " + address));
            
            
             if (restaurant.has("photos")) {
                 JSONArray photos = restaurant.getJSONArray("photos");
                 String photoRef = photos.getJSONObject(0).getString("photo_reference");
                 String photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                     "?maxwidth=400&photoreference=" + photoRef + "&key=" + API_KEY;
                

                 new Thread(() -> {
                     try {
                         ImageIcon icon = new ImageIcon(new URL(photoUrl));
                         JLabel imageLabel = new JLabel(icon);
                         restaurantPanel.add(imageLabel);
                         restaurantPanel.revalidate();
                     } catch (Exception e) {
                         restaurantPanel.add(new JLabel("(No image available)"));
                     }
                 }).start();
             } else {
                 restaurantPanel.add(new JLabel("(No image available)"));
             }
            
             resultPanel.add(restaurantPanel);
             count++;
         }
            if (count == 1) {
                resultPanel.add(new JLabel("No matching restaurants found."));
            } else {
                resultPanel.add(new JLabel("Top Restaurant: " + topRestaurant + " with rating " + highestRating));
            }
        
         resultPanel.revalidate();
         resultPanel.repaint();
     }

    
    
    public static void main(String[] args) {
        RestaurantGUI viewer = new RestaurantGUI();
        viewer.setVisible(true);
    }
}

