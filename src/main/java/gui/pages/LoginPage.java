package gui.pages;

import application.GradingApplication;
import courses.CourseMetaData;
import database.H2DatabaseUtil;
import gui.buttons.BackButton;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoginPage extends Page implements ActionListener {

    private JTextField UserName;
    private JTextField PassWord;
    private JButton loginButton;

    private final static Logger LOG = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage() {super("Welcome to Office-Grader", "Login");}

    public LoginPage(String info) {super("Welcome to Office-Grader", info); }

    @Override
    public void loadPage(){
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        UserName = new JTextField("Username");
        PassWord = new JTextField("Password");

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints userNameGBC = new GridBagConstraints();
        userNameGBC.gridx = 0;
        userNameGBC.gridy = 2;
        userNameGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints passwordGBC = new GridBagConstraints();
        passwordGBC.gridx = 0;
        passwordGBC.gridy = 3;
        passwordGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.anchor = GridBagConstraints.EAST;
        submitButtonGBC.gridx = 0;
        submitButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(UserName, userNameGBC);
        add(PassWord, passwordGBC);
        add(loginButton, submitButtonGBC);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        LOG.info("Login button clicked");

        if (e.getSource() == loginButton) {
            System.out.println(UserName.getText());
            System.out.println(PassWord.getText());
            String test = "admin";
            if ((PassWord.getText().equals(test) && (UserName.getText().equals(test)))) {
                GradingApplication.PAGE_LOADER.loadNewPage(new AllCoursesPage());
            } else {
                GradingApplication.PAGE_LOADER.loadNewPage(new LoginPage("Please try again"));
            }

        }
    }

}
