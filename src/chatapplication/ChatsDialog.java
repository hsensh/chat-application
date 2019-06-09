/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

/**
 * This class is responsible for collecting the credentials to send messages
 * to new users
 * It shares similar features with the sending system in ClientForm
 * @see ClientForm
 * @author Hsen
 */
public class ChatsDialog extends javax.swing.JDialog {

    /**
     * Creates new form ChatsDialog
     */
    private String recipient;
    private String message;
    public ChatsDialog(java.awt.Frame parent, boolean modal, String allChatStreams, StringBuilder localConvStream) {
        super(parent, modal);
        initComponents();
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        
        System.out.println(allChatStreams);
        String chatStreamsArr[] = allChatStreams.split("###");
        chatsPanel.removeAll();
        for(String chatStream: chatStreamsArr) {
            boolean found = false;
            
            String chatStatus[] = chatStream.split("##");
            String name = chatStatus[0];
            String status;
            if(chatStatus[1].equals("true")) {
                status = "Online";
            } else {
                status = "Offline";
            }
            for(String stream: localConvStream.toString().split("###")) {
                if(stream.split("##")[0].equals(name)) {
                    found = true;
                }
            }
            if(!found) {
                generateChatButtons(name, status);
                revalidate();
                repaint();
            }
        }
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newChatPanel = new javax.swing.JPanel();
        chatsLabel = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        messageTextField = new javax.swing.JTextField();
        newChatScrollPane = new javax.swing.JScrollPane();
        chatsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        chatsLabel.setText("Chats");

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        newChatScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        newChatScrollPane.setToolTipText("");
        newChatScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        newChatScrollPane.setMaximumSize(new java.awt.Dimension(150, 213));
        newChatScrollPane.setMinimumSize(new java.awt.Dimension(150, 213));
        newChatScrollPane.setPreferredSize(new java.awt.Dimension(150, 213));

        chatsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        chatsPanel.setMaximumSize(new java.awt.Dimension(300, 300));
        chatsPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        chatsPanel.setPreferredSize(new java.awt.Dimension(0, 1000));
        chatsPanel.setLayout(new javax.swing.BoxLayout(chatsPanel, javax.swing.BoxLayout.Y_AXIS));
        newChatScrollPane.setViewportView(chatsPanel);

        javax.swing.GroupLayout newChatPanelLayout = new javax.swing.GroupLayout(newChatPanel);
        newChatPanel.setLayout(newChatPanelLayout);
        newChatPanelLayout.setHorizontalGroup(
            newChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(messageTextField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(newChatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newChatPanelLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(chatsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
            .addGroup(newChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newChatPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newChatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        newChatPanelLayout.setVerticalGroup(
            newChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newChatPanelLayout.createSequentialGroup()
                .addComponent(chatsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
                .addComponent(messageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton))
            .addGroup(newChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newChatPanelLayout.createSequentialGroup()
                    .addContainerGap(23, Short.MAX_VALUE)
                    .addComponent(newChatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(71, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newChatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newChatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if(sendButton.getText().isBlank() || recipient.isBlank()) {
            JOptionPane.showConfirmDialog(
                null,
                "Missing components",
                "You have to select a recipient and type a message",
                JOptionPane.DEFAULT_OPTION
            );
        } else {
            message = messageTextField.getText();
        }
        setVisible(false);
        
    }//GEN-LAST:event_sendButtonActionPerformed


    
    private void generateChatButtons(String name, String status) {
        JToggleButton btn = new JToggleButton(name);
        btn.setName(name + "Button");
        btn.setBackground(new Color(214,217,223));
        JLabel label = new JLabel(status);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 10));
        assignActionToButton(btn);
        label.setName(name + "Label");
        
        chatsPanel.add(btn);
        chatsPanel.add(label);
    }
    
    private void assignActionToButton(JToggleButton btn) {
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var btn = ((JToggleButton)e.getSource());
                btn.setBackground(new Color(214,217,223));
                recipient = btn.getText();
                btn.setSelected(false);
                for(Component component: chatsPanel.getComponents()) {
                    if(component instanceof JToggleButton) {
                        var tglbtn = (JToggleButton)component;
                        tglbtn.setSelected(false);
                    }
                }
                btn.setSelected(true);
                revalidate();
            }
        });
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessage() {
        return message;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel chatsLabel;
    private javax.swing.JPanel chatsPanel;
    private javax.swing.JTextField messageTextField;
    private javax.swing.JPanel newChatPanel;
    private javax.swing.JScrollPane newChatScrollPane;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
