package com.theships;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Game {
	@SuppressWarnings("unused")
	private Player gracz;
	private AIPlayer pc;
	private int mode;
	
	public Game(Player gracz, AIPlayer pc, int mode) {
		this.gracz = gracz;
		this.pc = pc;
		this.mode = mode;
	}
	
	private void init() {
		pc.randomize();
		Ship[] statki = pc.getShips();
		for(int i = 0; i < statki.length; i++) {
			final Ship statek = statki[i];
			for(int j = 0; j < statek.getLength(); j++) {
				final Field pole = statek.getField(j);
				pole.getButton().setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						pole.getButton().setClickable(false);
						if(pole.getState() == Field._ship){
							pole.setState(Field._shot);
							statek.updateState();
							if(statek.getSinkState()) {
								for(int j = 0; j < statek.getLength(); j++)
									statek.getField(j).setState(Field._sink);
								pc.shipcounter--;
								if(pc.shipcounter == 0) {
									
								}
							}
						}
						pc.makeMove(gracz, mode);
					}
				});
			}
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(pc.getMatrix()[i][j] == 0) {
					final Field test = new Field(pc.getRids()[i*10 + j], i*10 + j, Field._empty);
					test.getButton().setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if(test.getState()==Field._empty) {
								test.setState(Field._missed);
								test.getButton().setClickable(false);
							}
							if(test.getState()==Field._ship) {
								test.setState(Field._shot);
								test.getButton().setClickable(false);
							}
							pc.makeMove(gracz, mode);
						}
					});
				}
			}
		}
	}

	
	public void startGame() {
		init();
	}
}
