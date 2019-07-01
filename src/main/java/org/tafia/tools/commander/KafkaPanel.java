package org.tafia.tools.commander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tafia.tools.application.Global;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Created by Dason on 2018/4/22.
 */
public class KafkaPanel extends JPanel {

    public KafkaPanel() {
        setLayout(new BorderLayout());
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton addButton = new JButton("+");
        addButton.setToolTipText("新建");
        addButton.setEnabled(false);
        buttons.add(addButton);
        JButton delButton = new JButton("×");
        delButton.setToolTipText("删除");
        delButton.setEnabled(false);
        buttons.add(delButton);
        add(buttons, BorderLayout.NORTH);
        JSONObject kafkaData = (JSONObject) JSON.toJSON(Global.getData("kafka"));
        java.util.List<KafkaConnection> connections = kafkaData.getJSONArray("connections").toJavaList(KafkaConnection.class);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Connections");
        for (KafkaConnection connection : connections) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(connection.getName());
            for (String topic : connection.getTopics()) {
                node.add(new DefaultMutableTreeNode(topic));
            }
            root.add(node);
        }
        JTree tree = new JTree(root);
        //tree.setBackground(Global.GRAY_240);
        tree.setFont(new Font(tree.getFont().getName(), Font.PLAIN, 12));
        tree.addTreeSelectionListener(e -> {
            if (e.getPath().getPathCount() == 2) {
                addButton.setEnabled(true);
                delButton.setEnabled(true);
            } else {
                addButton.setEnabled(false);
                delButton.setEnabled(false);
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tree);
        scrollPane.setBorder(new MatteBorder(5, 5, 5, 5, Color.WHITE));
        //scrollPane.setBackground(Global.GRAY_240);
        add(scrollPane, BorderLayout.CENTER);
    }
}
