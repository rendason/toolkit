package org.tafia.tools.application;

import org.tafia.tools.utils.Exceptions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.*;
import static org.tafia.tools.application.Global.getGlobalConfig;
import static org.tafia.tools.application.Global.*;

/**
 * Created by Dason on 2018/4/20.
 */
public class ApplicationFrame extends JFrame {

    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    private static final GridBagConstraints SIDE_BAR_CONSTRAINTS = new GridBagConstraints(
            0, 0, 1, 1, 0, 1, CENTER, VERTICAL, ZERO_INSETS, 0, 0
    );

    private static final GridBagConstraints ITEM_PANE_CONSTRAINTS = new GridBagConstraints(
            1, 0, 1, 1, 0.25, 1, CENTER, BOTH, ZERO_INSETS, 0, 0
    );

    private static final GridBagConstraints DETAIL_PANE_CONSTRAINTS = new GridBagConstraints(
            2, 0, REMAINDER, 1, 0.75, 1, CENTER, BOTH, ZERO_INSETS, 0, 0
    );

    private JComponent sideBar;

    private JComponent itemPane;

    private JComponent detailPane;

    public ApplicationFrame() {

        GlobalConfig.Application appConfig = getGlobalConfig().getApplication();
        setTitle(appConfig.getTitle());
        Dimension appSize = new Dimension(appConfig.getWidth(), appConfig.getHeight());
        setPreferredSize(appSize);
        locateCenter(appSize);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        constructCommander();
        constructExecutor();

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        makeLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}
        SwingUtilities.updateComponentTreeUI(itemPane);

        pack();
    }

    private void locateCenter(Dimension appSize) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - appSize.width) / 2,
                (screenSize.height - appSize.height) / 2);
    }

    private void constructSideBar() {
        GlobalConfig.Commander.Side sideBarConfig = getGlobalConfig().getCommander().getSide();
        JPanel sideBar = new JPanel();
        sideBar.setBackground(GRAY_64);
        Dimension size = new Dimension(sideBarConfig.getWidth(), getPreferredSize().height);
        sideBar.setMinimumSize(new Dimension(sideBarConfig.getWidth(), getPreferredSize().height));
        sideBar.setSize(size);
        sideBar.setBorder(new MatteBorder(0, 0, 0, 1, GRAY_240));
        add(sideBar);
        this.sideBar = sideBar;
    }

    private void constructItemBar() {
        GlobalConfig.Commander.Item itemPaneConfig = getGlobalConfig().getCommander().getItem();
        JPanel itemPane = new JPanel();
        itemPane.setBackground(GRAY_240);
        Dimension size = new Dimension(itemPaneConfig.getWidth(), getPreferredSize().height);
        itemPane.setPreferredSize(size);
        itemPane.setSize(size);
        add(itemPane);
        this.itemPane = itemPane;
    }

    private void constructExecutor() {
        JPanel executorlPane = new JPanel();
        executorlPane.setLayout(new BorderLayout());
        executorlPane.setBackground(GRAY_64);
        int width = getPreferredSize().width - sideBar.getPreferredSize().width - itemPane.getPreferredSize().width;
        executorlPane.setPreferredSize(new Dimension(width, getPreferredSize().height ));
        add(executorlPane);
        this.detailPane = executorlPane;
    }

    private void makeLayout(GridBagLayout layout) {
        layout.setConstraints(sideBar, SIDE_BAR_CONSTRAINTS);
        layout.setConstraints(itemPane, ITEM_PANE_CONSTRAINTS);
        layout.setConstraints(detailPane, DETAIL_PANE_CONSTRAINTS);
    }

    private void constructCommander() {
        constructSideBar();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        constructItemBar();
        CardLayout cardLayout = new CardLayout();
        itemPane.setLayout(cardLayout);
        java.util.List<JButton> buttonList = new ArrayList<>();
        for (GlobalConfig.Commander.Option option : getGlobalConfig().getCommander().getOptions()) {
            JButton button = createSideButton(option.getName());
            button.addActionListener(e -> {
                JButton current = ((JButton) e.getSource());
                current.setBackground(GRAY_48);
                buttonList.stream().filter(b -> b != current).forEach(b -> b.setBackground(GRAY_64));
                cardLayout.show(itemPane, option.getName());
            });
            if (buttonList.isEmpty()) button.setBackground(GRAY_48);
            buttonList.add(button);
            sideBar.add(button);
            JComponent item;
            try {
                item = (JComponent) Class.forName(option.getImpl()).newInstance();
            } catch (Exception e) {
                throw Exceptions.checked(e);
            }
            itemPane.add(option.getName(), item);
        }
    }

    private JButton createSideButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new VerticalButtonUI(270));
        button.setBorder(new EmptyBorder(2, 20, 2, 20));
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 12));
        button.setBackground(GRAY_64);
        button.setForeground(Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton b = (JButton) e.getSource();
                if (GRAY_64.equals(b.getBackground())) {
                    b.setBackground(GRAY_56);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton b = (JButton) e.getSource();
                if (GRAY_56.equals(b.getBackground())) {
                    b.setBackground(GRAY_64);
                }
            }
        });
        return button;
    }

    private static class VerticalButtonUI extends BasicButtonUI {

        protected int angle;

        public VerticalButtonUI(int angle) {
            super();
            this.angle = angle;
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            Dimension dim = super.getPreferredSize(c);
            return new Dimension(dim.height, dim.width);
        }

        private static Rectangle paintIconR = new Rectangle();
        private static Rectangle paintTextR = new Rectangle();
        private static Rectangle paintViewR = new Rectangle();
        private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton button = (JButton)c;
            String text = button.getText();
            Icon icon = (button.isEnabled()) ? button.getIcon() : button.getDisabledIcon();

            if ((icon == null) && (text == null)) {
                return;
            }

            FontMetrics fm = g.getFontMetrics();
            paintViewInsets = c.getInsets(paintViewInsets);

            paintViewR.x = paintViewInsets.left;
            paintViewR.y = paintViewInsets.top;

            // Use inverted height &amp; width
            paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
            paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

            paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
            paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tr = g2.getTransform();

            if (angle == 90) {
                g2.rotate( Math.PI / 2 );
                g2.translate( 0, - c.getWidth() );
                paintViewR.x = c.getHeight()/2 - (int)fm.getStringBounds(text, g).getWidth()/2;
                paintViewR.y = c.getWidth()/2 - (int)fm.getStringBounds(text, g).getHeight()/2;
            }
            else if (angle == 270) {
                g2.rotate( - Math.PI / 2 );
                g2.translate( - c.getHeight(), 0 );
                paintViewR.x = c.getHeight()/2 - (int)fm.getStringBounds(text, g).getWidth()/2;
                paintViewR.y = c.getWidth()/2 - (int)fm.getStringBounds(text, g).getHeight()/2;
            }

            if (icon != null) {
                icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
            }

            if (text != null) {
                int textX = paintTextR.x;
                int textY = paintTextR.y + fm.getAscent();

                if (button.isEnabled()) {
                    paintText(g,c,new Rectangle(paintViewR.x,paintViewR.y,textX,textY),text);
                } else {
                    paintText(g,c,new Rectangle(paintViewR.x,paintViewR.y,textX,textY),text);
                }
            }

            g2.setTransform( tr );
        }
    }

}
