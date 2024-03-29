package com.theships; 

import java.util.Random;

import android.app.Activity;
import android.view.View;

public class AIPlayer extends Player {
	private int[][] shots = new int[10][10];
	private int[][] moves;
	private int mc = 0, c = 0;
	public static final int _easy = 0;
	public static final int _hard = 1;
	
	public AIPlayer(int[] parsedGrid, View[] rids, boolean fake) {
		super(parsedGrid, rids, fake);
		
	}

	public AIPlayer(View[] views2) {
		super(views2);
		moves = new int[4][14];
		int rand = new Random().nextInt(4);
		int temp = moves[0][0] = rand;
		int prev = temp;
		for(int j = 0; j < 2; j++) {
			for (int i = 1; i < 13; i++) {
				if((moves[j][i-1]%10 + 4) > 9) {
					temp += 20;
					temp = ((temp%10 - 2) >= 0) ? temp - 2 : temp + 2;
					moves[j][i] = temp;
				} else
					moves[j][i] = moves[j][i-1] + 4;
				if(moves[j][i]>=100)
					moves[j][i] = -1;
			}
			moves[j + 1][0] = ((prev+11)%10 - 4) >=0 ? prev + 7 : prev + 11;
			prev = moves[j + 1][0];
			temp = prev;
		}
		prev = (moves[0][0] - 2) >= 0 ? moves[0][0] - 2 : moves[0][0] + 2;
		temp = moves[2][0] = prev;
		for(int j = 2; j < 4; j++) {
			for (int i = 1; i < 13; i++) {
				if((moves[j][i-1]%10 + 4) > 9) {
					temp += 20;
					temp = ((temp%10 - 2) >= 0) ? temp - 2 : temp + 2;
					moves[j][i] = temp;
				} else
					moves[j][i] = moves[j][i-1] + 4;
				if(moves[j][i]>=100)
					moves[j][i] = -1;
			}
			if(j != 3) {
				moves[j + 1][0] = ((prev+11)%10 - 4) >=0 ? prev + 7 : prev + 11;
				prev = moves[j + 1][0];
				temp = prev;
			}
		}
		for(int i = 0; i < 4 ; i++) 
			moves[i][13] = -1;
		for(int j = 0; j < 4; j++)
			for(int i = 0; i < 12; i++) {
				int t = new Random().nextInt(12);
				int tk = moves[j][i];
				moves[j][i] = moves[j][t];
				moves[j][t] = tk;
			}
	}
	
	public void makeMove(Player p, int mode, Activity a) {
		int x, y;
		if(mode == _easy) {
			do {
				x = new Random().nextInt(10);
				y = new Random().nextInt(10);
			} while(shots[x][y] == 1);
			shots[x][y] = 1;
			if(p.getMatrix()[x][y] == 0) 
				new Field(p.getRids()[x*10 + y], x*10 + y, Field._missed);
			if(p.getMatrix()[x][y] == 1) {
				for(int i = 0; i < 10; i++) {
					Ship statek = p.getShips()[i];
					for(int j = 0; j < statek.getLength(); j++) {
						if(statek.getField(j).getnr() == x*10 + y) {
							statek.getField(j).setState(Field._shot);
							statek.updateState();
							if(statek.getSinkState()) {
								for(int k = 0; k < statek.getLength(); k++) 
									statek.getField(k).setState(Field._sink);
								p.shipcounter--;
								if(p.shipcounter == 0) {
									winAI(a);
								}
							}
						}
					}
				}
			}
		}
		if(mode == _hard) {
			if(mc == 4) {
				do {
					x = new Random().nextInt(10);
					y = new Random().nextInt(10);
				} while(shots[x][y] == 1);
			} else {
				x = moves[mc][c]/10;
				y = moves[mc][c]%10;
				c++;
				if(moves[mc][c] == -1) {
					mc++;
					c = 0;
				}
			}
			shots[x][y] = 1;
			
			if(p.getMatrix()[x][y] == 0) 
				new Field(p.getRids()[x*10 + y], x*10 + y, Field._missed);
			if(p.getMatrix()[x][y] == 1) {
				for(int i = 0; i < 10; i++) {
					Ship statek = p.getShips()[i];
					for(int j = 0; j < statek.getLength(); j++) {
						if(statek.getField(j).getnr() == x*10 + y) {
							statek.getField(j).setState(Field._shot);
							statek.updateState();
							if(statek.getSinkState()) {
								for(int k = 0; k < statek.getLength(); k++) 
									statek.getField(k).setState(Field._sink);
								p.shipcounter--;
								if(p.shipcounter == 0) {
									winAI(a);
								}
							}
						}
					}
				}
			}
		}
	}
	public void winAI (Activity a) {
		a.setContentView(R.layout.lost);
	}

}
