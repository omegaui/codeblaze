package omegaui.codeblaze.ui.component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import omegaui.codeblaze.io.ResizeAware;

import omegaui.component.TextComp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import omegaui.codeblaze.App;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.UIManager;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ToolMenu extends JPanel implements ResizeAware{

	private App app;

	private TextComp iconComp;

	private Menu fileMenu;
	private Menu settingsMenu;
	private Menu helpMenu;

	public ToolMenu(App app){
		this.app = app;
		setPreferredSize(new Dimension(100, 80));
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);

		iconComp = new TextComp(appSmallIcon, BACKGROUND, BACKGROUND, BACKGROUND, null);
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);

		fileMenu = new Menu("File");
		add(fileMenu);

		settingsMenu = new Menu("Settings");
		add(settingsMenu);

		helpMenu = new Menu("Help");
		add(helpMenu);
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(0, 0, 40, 40);

		fileMenu.setBounds(45, 8, 50, 25);
		settingsMenu.setBounds(95, 8, 65, 25);
		helpMenu.setBounds(160, 8, 50, 25);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
	

	public class Menu extends JComponent {
		
		private String text;

//		private OPopupWindow popup;
		
		private volatile boolean enter;
		
		public Menu(
//			OPopupWindow popup, 
			String text) {
			this.text = text;
//			this.popup = popup;
			setFont(PX14);
			setSize(100, ToolMenu.this.getHeight());
			setPreferredSize(getSize());
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					enter = true;
					repaint();
				}
				@Override
				public void mouseExited(MouseEvent e) {
					enter = false;
					repaint();
				}
				@Override
				public void mousePressed(MouseEvent e) {
					showPopup();
				}
			});
		}

		public void showPopup(){
//			if(popup.isVisible()){
//				popup.setVisible(false);
//				return;
//			}
//			popup.setLocation(getX() + screen.getX(), getY() + getHeight() + 15 + getHeight() + screen.getY());
//			popup.setVisible(true);
		}
		
		@Override
		public void paint(Graphics g2D) {
			Graphics2D g = (Graphics2D)g2D;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setColor(BACKGROUND);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(secondaryColor);
			g.setFont(getFont());
			int x = g.getFontMetrics().stringWidth(text);
			int cx = x;
			x = getWidth()/2 - x/2;
			if(enter) {
				g.setColor(tertiaryColor);
				g.fillRect(x, getHeight() - 3, cx, 2);
			}
			g.setFont(getFont());
			g.drawString(text, x, getFont().getSize());
		}
	}
}

