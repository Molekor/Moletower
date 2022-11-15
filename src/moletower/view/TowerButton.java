package moletower.view;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import moletower.controller.BuyListener;

public class TowerButton extends Button implements ActionListener {

	private int towerTypeId;
	private BuyListener listener ;

	public TowerButton(String caption, int towerTypeId) {
		super(caption);
		this.towerTypeId = towerTypeId;
		this.addActionListener(this);
	}

	public void setListener(BuyListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.listener != null) {
			this.listener.requestTowerBuy(this.towerTypeId);
		}
	}
	
}
