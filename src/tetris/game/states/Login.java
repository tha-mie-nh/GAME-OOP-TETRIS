package tetris.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.AlphaComposite;  // Để sử dụng AlphaComposite
import java.awt.Graphics2D;       // Để sử dụng Graphics2D (kế thừa từ Graphics)

import tetris.framework.display.Window;
import tetris.framework.gamestates.GameState;
import tetris.framework.resources.ResourceManager;
import tetris.game.Game;

public class Login extends GameState {
    private String[] options;
    private int selected;
    private int[][] optionPositions;
    private String accountId = "";
    private String password = "";
    private String confirmPassword = "";
    private boolean isRegistering = false;
    private List<User> users = new ArrayList<User>();

    public Login() {
        System.out.println("Login object created: " + this);
        System.out.println("[Game][States]: Log In");
        System.out.println("Giang" + this.users);
    }
    
    @Override
    protected void init() {
        this.options = new String[] {"Account ID:", " Password:", "Log In", "Sign Up"};
        this.selected = 0;
        this.optionPositions = new int[][] {
            {Window.WIDTH / 3 - 77, 190},  // Vị trí của "Account ID"
            {Window.WIDTH / 3 - 75, 250}, // Vị trí của "Password"
            {Window.WIDTH / 3 + 20, 310}, // Vị trí của "Log In"
            {Window.WIDTH / 3 + 15, 370}  // Vị trí của "Sign Up"
        };
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics graphics) {
        this.drawBackground(graphics);
        this.drawButtons(graphics);
        this.drawOptions(graphics);
        if (isRegistering) {
            this.drawRegisterScreen(graphics);
        } else {
            this.drawLoginScreen(graphics);
        }
    }

    @Override
    public void keyPressed(int key) {
        if (isRegistering) {
            handleRegisterKeyPress(key);
        } else {
            handleLoginKeyPress(key);
        }
    }

    @Override
    public void keyReleased(int key) {}

    private void drawBackground(Graphics graphics) {
        graphics.setColor(new Color(10, 10, 30));
        graphics.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
    }

    private void drawButtons(Graphics graphics) {
        graphics.drawImage(ResourceManager.texture("logo.png"), Window.WIDTH / 3 - 70, 10, 220, 140, null);
        // Chuyển Graphics thành Graphics2D để có thể sử dụng AlphaComposite
        Graphics2D g2d = (Graphics2D) graphics;

        // Thiết lập độ mờ cho nút
        float alpha = 0.5f;  // Đặt giá trị alpha, từ 0.0f (hoàn toàn trong suốt) đến 1.0f (hoàn toàn không trong suốt)
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alphaComposite);  // Áp dụng độ mờ

        // Vẽ các nút menu với độ mờ
        for (int i = 0; i < options.length; i++) {
            g2d.drawImage(ResourceManager.texture("menu_button.png"), Window.WIDTH / 3 - 30, 160 + 60 * i, 160, 0, null);
        }

    // Khôi phục lại composite gốc nếu bạn cần vẽ các đối tượng khác không có độ mờ
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));  // Khôi phục alpha = 1.0 (không mờ)
    }

    private void drawOptions(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            if (selected == i) {
                graphics.setColor(Color.GREEN);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.drawString(options[i], optionPositions[i][0], optionPositions[i][1]);

        }
    }

    private void drawLoginScreen(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString(accountId, Window.WIDTH / 3 + 70, 190);
        graphics.drawString(password.replaceAll(".", "*"), Window.WIDTH / 3 + 70, 250);  // Password hidden
    }

    private void drawRegisterScreen(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString("Create Account", Window.WIDTH / 3, 150);
        graphics.drawString(accountId, Window.WIDTH / 3 + 70, 190);
        graphics.drawString(password.replaceAll(".", "*"), Window.WIDTH / 3 + 70, 250);  // Password hidden
        graphics.drawString(confirmPassword.replaceAll(".", "*"), Window.WIDTH / 3 + 70, 310);  // Password hidden
    }

    private void handleLoginKeyPress(int key) {
        if (key == KeyEvent.VK_UP) {
            if (this.selected > 0) this.selected--;
        } else if (key == KeyEvent.VK_DOWN) {
            if (this.selected < this.options.length - 1) this.selected++;
        } else if (key == KeyEvent.VK_ENTER) {
            if (this.selected == 2) {
                loadUsers();
                // Handle Log In
                if (validateLogin(accountId, password) == true) {
                    Game.STATE_MANAGER.changeState(new MainMenu());
                } else {
                    System.out.println("Invalid credentials");
                }
            } else if (this.selected == 3) {
                // Switch to Register screen
                isRegistering = true;
                this.options = new String[] {"Account ID", "Password", "Nhap lai", "Register", "Log In"};
                this.optionPositions = new int[][] {
                    {Window.WIDTH / 3 - 80, 190},  // Vị trí của "Account ID"
                    {Window.WIDTH / 3 - 80, 250}, // Vị trí của "Password"
                    {Window.WIDTH / 3 - 10, 310}, // Vị trí của "Log In"
                    {Window.WIDTH / 3 + 15, 370},  // Vị trí của "Sign Up"
                    {Window.WIDTH / 3 + 15, 430}
                };
                this.selected = 0;
            }
        } else if (key == KeyEvent.VK_BACK_SPACE) {
            if (selected == 0 && accountId.length() > 0) {
                accountId = accountId.substring(0, accountId.length() - 1);
            } else if (selected == 1 && password.length() > 0) {
                password = password.substring(0, password.length() - 1);
            }
        } else if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z || key == KeyEvent.VK_SPACE || key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
            char keyChar = (char) key;
            if (selected == 0) {
                accountId += keyChar;
            } else if (selected == 1) {
                password += keyChar;
            }
        }
    }
    private void handleRegisterKeyPress(int key) {
        if (key == KeyEvent.VK_UP) {
            if (this.selected > 0) this.selected--;
        } else if (key == KeyEvent.VK_DOWN) {
            if (this.selected < this.options.length - 1) this.selected++;
        } else if (key == KeyEvent.VK_ENTER) {
            if (this.selected == 3) {
                loadUsers();
                // Register new user
                if (password.equals(confirmPassword)) {
                    saveUser(accountId, password);
                    System.out.println("Registration successful.");
                    isRegistering = false;
                    this.options = new String[] {"Account ID", "Password", "Log In", "Sign Up"};
                    this.optionPositions = new int[][] {
                        {Window.WIDTH / 3 - 80, 190},  // Vị trí của "Account ID"
                        {Window.WIDTH / 3 - 80, 250}, // Vị trí của "Password"
                        {Window.WIDTH / 3 + 20, 310}, // Vị trí của "Log In"
                        {Window.WIDTH / 3 + 15, 370}  // Vị trí của "Sign Up"
                    };
                    this.selected = 0;
                } else {
                    System.out.println("Passwords do not match.");
                }
            } else if (this.selected == 4) {
                // Switch back to Log In screen
                isRegistering = false;
                this.options = new String[] {"Account ID", "Password", "Log In", "Sign Up"};
                this.optionPositions = new int[][] {
                    {Window.WIDTH / 3 - 80, 190},  // Vị trí của "Account ID"
                    {Window.WIDTH / 3 - 80, 250}, // Vị trí của "Password"
                    {Window.WIDTH / 3 + 20, 310}, // Vị trí của "Log In"
                    {Window.WIDTH / 3 + 15, 370}  // Vị trí của "Sign Up"
                };
                this.selected = 0;
            }
        } else if (key == KeyEvent.VK_BACK_SPACE) {
            if (selected == 0 && accountId.length() > 0) {
                accountId = accountId.substring(0, accountId.length() - 1);
            } else if (selected == 1 && password.length() > 0) {
                password = password.substring(0, password.length() - 1);
            } else if (selected == 2 && confirmPassword.length() > 0) {
                confirmPassword = confirmPassword.substring(0, confirmPassword.length() - 1);
            }
        } else if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z || key == KeyEvent.VK_SPACE || key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
            char keyChar = (char) key;
            if (selected == 0) {
                accountId += keyChar;
            } else if (selected == 1) {
                password += keyChar;
            } else if (selected == 2) {
                confirmPassword += keyChar;
            }
        }
    }

    private boolean validateLogin(String accountId, String password) {
        System.out.println("Giang2" + this.users);
        for (User user : this.users) {
            if (user.getUsername().equals(accountId) == true && user.getPassword().equals(password) == true) {
                return true;
            }
        }
        return false;
    }

    private void loadUsers() {
        this.users = new ArrayList<User>();
        File file = new File("account.txt");
        if (!file.exists()) {
            System.out.println("User file not found. Ensure the file exists in the correct path.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Loại bỏ khoảng trắng ở đầu và cuối dòng
                if (line.isEmpty()) {
                    continue; // Bỏ qua dòng trống
                }
    
                String[] parts = line.split(",");
                if (parts.length == 2) { // Đảm bảo định dạng hợp lệ
                    String username = parts[0].trim(); // Loại bỏ khoảng trắng thừa
                    String password = parts[1].trim(); // Loại bỏ khoảng trắng thừa
                    this.users.add(new User(username, password));
                } else {
                    System.out.println("Invalid user format in line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    

    private void saveUser(String username, String password) {
        loadUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("account.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // Simple User class for storing account info
    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}