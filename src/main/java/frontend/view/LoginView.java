package frontend.view;

import com.example.sams.DemoApplication;
import com.example.sams.config.SpringContext;
import com.example.sams.controller.SessionManager;
import com.example.sams.enumeration.Role;
import com.example.sams.mapper.UserMapper;
import com.example.sams.request.user.SignInRequest;
import com.example.sams.request.user.SignUpRequest;
import com.example.sams.service.IAuthService;
import com.example.sams.service.implementation.AuthService;
import frontend.controller.PageController;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;

public class LoginView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel loginPanel;
    private JPanel signupPanel;

    private IAuthService authService;

    public LoginView(ApplicationContext context) {
        this.authService = DemoApplication.context.getBean(IAuthService.class);

        setTitle("Login/Signup Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create card layout for switching between login and signup
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setSize(800,800);

        // Create login panel
        createLoginPanel();

        // Create signup panel
        createSignupPanel();

        // Add panels to card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(signupPanel, "signup");

        // Show login panel initially
        cardLayout.show(cardPanel, "login");

        // Add card panel to frame

        add(cardPanel);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        JButton goToSignupButton = new JButton("Need an account? Sign up");
        goToSignupButton.setBorderPainted(false);
        goToSignupButton.setContentAreaFilled(false);
        goToSignupButton.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginPanel.add(goToSignupButton, gbc);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginView.this,
                            "Please enter both username and password",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SignInRequest request=new SignInRequest(username, password);
                try {
                    authService.signIn(request);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginView.this,
                            "Invalid username or password",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean isAdmin = SessionManager.getUser().getRole()== Role.ROLE_ADMIN;
                JOptionPane.showMessageDialog(LoginView.this,
                        "Login successful! User type: " + (isAdmin ? "Admin" : "Regular User"),
                        "Login Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                usernameField.setText("");
                passwordField.setText("");

                Date date= Date.valueOf(java.time.LocalDate.now());
                PageController pageController=new PageController(date);
                pageController.readConfig();
                PageView pageView=new PageView(pageController);

                pageController.setView(pageView);
                pageView.setVisibility(true);

                pageController.update();

                //make frame invisible
                usernameField.setText("");
                passwordField.setText("");
                setVisible(false);
                dispose();
            }
        });

        goToSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "signup");
            }
        });
    }

    private void createSignupPanel() {
        signupPanel = new JPanel();
        signupPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        signupPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signupPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        signupPanel.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        signupPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        signupPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        signupPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        signupPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        signupPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        signupPanel.add(confirmPasswordField, gbc);

        JLabel adminPasswordLabel = new JLabel("Admin Password:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        signupPanel.add(adminPasswordLabel, gbc);

        JPasswordField adminPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        signupPanel.add(adminPasswordField, gbc);

        JButton signupButton = new JButton("Sign Up");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        signupPanel.add(signupButton, gbc);

        JButton goToLoginButton = new JButton("Already have an account? Login");
        goToLoginButton.setBorderPainted(false);
        goToLoginButton.setContentAreaFilled(false);
        goToLoginButton.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        signupPanel.add(goToLoginButton, gbc);

        // Add action listeners
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String adminPassword = new String(adminPasswordField.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginView.this,
                            "Please fill in all required fields",
                            "Signup Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(LoginView.this,
                            "Passwords do not match",
                            "Signup Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }




                // Create user
                SignUpRequest request=new SignUpRequest(
                        username,email,password,confirmPassword,adminPassword
                );

                    authService.signUp(request);
//                }
//                catch (Exception ex) {
//                    JOptionPane.showMessageDialog(LoginView.this,
//                            "Sign up failed",
//                            "Signup Error",
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }


                JOptionPane.showMessageDialog(LoginView.this,
                        "Signup successful! You can now login.",
                        "Signup Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear fields and go to login
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                adminPasswordField.setText("");
                cardLayout.show(cardPanel, "login");
            }
        });

        goToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });
    }

}