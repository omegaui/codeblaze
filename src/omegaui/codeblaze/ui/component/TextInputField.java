/*
 * Copyright (C) 2022 Omega UI

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package omegaui.codeblaze.ui.component;
import omegaui.listener.KeyStrokeListener;

import java.awt.geom.RoundRectangle2D;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

import static java.awt.event.KeyEvent.*;

public class TextInputField extends JTextField implements ValidationAware{
	
	private String hint;
	private String pressHint;
	
	private Shape shape;
	
	private int arcX = 10;
	private int arcY = 10;
	
	private Color color1;
	private Color color2;
	private Color color3;

	private Runnable onAction = ()->{};
	private Runnable onAnyKeyPressed = ()->{};
	private Runnable onAnyKeyReleased = ()->{};

	private TextInputFieldValidationTask validationTask = (field)->{
		return true;
	};
	
	public TextInputField(String hint, String pressHint){
		this.hint = hint;
		this.pressHint = pressHint;
		
		setColors(HOVER, BACKGROUND, GLOW);
		
		setText(hint);
		setForeground(color3);
		setCaretColor(primaryColor);
		
		setBorder(BorderFactory.createLineBorder(HOVER, 2, true));
		setHorizontalAlignment(JTextField.CENTER);
		setOpaque(false);

		KeyStrokeListener keyListener = new KeyStrokeListener(this);
		keyListener.setOnAnyKeyPressed((e)->{
			if(e.getKeyCode() != VK_ENTER){
				resetValidationChecks();
				onAnyKeyPressed.run();
			}
		});
		keyListener.setOnAnyKeyReleased((e)->{
			onAnyKeyReleased.run();
		});
		keyListener.putKeyStroke((e)->{
			if(validationTask.performValidation(TextInputField.this)){
				validationPassed();
				getOnAction().run();
			}
			else{
				validationFailed();
			}
		}, VK_ENTER).useAutoReset().setStopKeys(VK_CONTROL, VK_ALT, VK_SHIFT);
		addKeyListener(keyListener);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent e){
				if(TextInputField.super.getText().equals("") || TextInputField.super.getText().equals(TextInputField.this.pressHint)){
					setText(TextInputField.this.hint);
					repaint();
				}
			}
			@Override
			public void mousePressed(MouseEvent e){
				if(TextInputField.super.getText().equals(TextInputField.this.hint)){
					setText(TextInputField.this.pressHint);
					repaint();
				}
			}
		});
		
		addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e){
				setBorder(BorderFactory.createLineBorder(primaryColor, 2, true));
			}
			
			@Override
			public void focusLost(FocusEvent e){
				setBorder(BorderFactory.createLineBorder(HOVER, 2, true));
				if(TextInputField.super.getText().equals("") || TextInputField.super.getText().equals(TextInputField.this.pressHint)){
					setText(TextInputField.this.hint);
					repaint();
				}
			}
		});
	}
	
	public TextInputField(String text){
		this.hint = "";
		this.pressHint = "";
		
		setColors(HOVER, BACKGROUND, GLOW);
		
		setText(text);
		setForeground(color3);
		setCaretColor(primaryColor);
		
		setBorder(BorderFactory.createLineBorder(HOVER, 2, true));
		setHorizontalAlignment(JTextField.CENTER);
		setOpaque(false);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent e){
				if(getText().equals("") || getText().equals(TextInputField.this.pressHint)){
					setText(TextInputField.this.hint);
					repaint();
				}
			}
			@Override
			public void mousePressed(MouseEvent e){
				if(getText().equals(TextInputField.this.hint)){
					setText(TextInputField.this.pressHint);
					repaint();
				}
			}
		});
	}

	@Override
	public String getText(){
		return super.getText().equals(hint) ? "" : super.getText();
	}

	@Override
	public void resetValidationChecks() {
		validationPassed();
	}
	
	@Override
	public void validationPassed() {
		setBorder(BorderFactory.createLineBorder(HOVER, 2, true));
	}
	
	@Override
	public void validationFailed() {
		setBorder(BorderFactory.createLineBorder(warningColor, 2, true));
	}

	public void setValidationTask(TextInputFieldValidationTask validationTask){
		this.validationTask = validationTask;
	}
	
	public java.lang.Runnable getOnAction() {
		return onAction;
	}
	
	public void setOnAction(java.lang.Runnable onAction) {
		this.onAction = onAction;
	}
	
	public void setOnAnyKeyPressed(java.lang.Runnable onAnyKeyPressed) {
		this.onAnyKeyPressed = onAnyKeyPressed;
	}

	public void setOnAnyKeyReleased(java.lang.Runnable onAnyKeyReleased) {
		this.onAnyKeyReleased = onAnyKeyReleased;
	}

	public void setHint(String hint) {
		this.hint = hint;
		repaint();
	}
	
	public void setPressHint(String pressHint) {
		this.pressHint = pressHint;
		repaint();
	}
	
	public void setArc(int x, int y){
		this.arcX = x;
		this.arcY = y;
	}
	
	public void setColors(Color c1, Color c2, Color c3){
		this.color1 = c1;
		this.color2 = c2;
		this.color3 = c3;
		setBackground(color2);
		setForeground(color3);
	}
	
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
		}
		return shape.contains(x, y);
	}
	
	@Override
	public void paint(Graphics graphics){
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
		
		if(!getText().equals(hint))
			super.paint(g);
		else{
			g.setColor(color3);
			g.drawString(hint, getWidth()/2 - g.getFontMetrics().stringWidth(hint)/2, getHeight()/2 - g.getFontMetrics().getHeight()/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() + 1);
			paintBorder(g);
			g.dispose();
		}
	}
}

