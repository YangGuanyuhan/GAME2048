package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseListenerInGameFrame implements MouseListener {
    GameFrame frame;
    Container pane;

    MouseListenerInGameFrame(GameFrame gameFrame){
        this.frame = gameFrame;
        pane = this.frame.getContentPane();
        pane.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.frame.mouseClickedAtPoint(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        return;
    }

}
