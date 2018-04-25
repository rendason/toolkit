package org.tafia.tools.commander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tafia.tools.application.Global;

import javax.swing.*;
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
        buttons.add(new JButton("+"));
        buttons.add(new JButton("-"));
        buttons.add(new JButton("×"));
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
        tree.setFont(new Font(tree.getFont().getName(), Font.PLAIN, 12));
        add(tree, BorderLayout.CENTER);
    }
}
