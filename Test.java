/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 *
 * @author MichaelAlbert
 */
public class Test {


    public static double getSD(int[] a) {
		double sd = 0.0;
		double average = 0.0;
		for (int i = 0; i < a.length; i++) {
		    average += a[i];
		}
		average = average/a.length;

		for (int i = 0; i < a.length; i++) {
		    sd += Math.pow(a[i] - average, 2);
		}
		sd = sd/a.length;
		sd = Math.sqrt(sd);
		return sd;
	}
	

    public static int findMin(double a, double b, double c) {
		int index = 0;
		double smallest = a;
		if (smallest > b){smallest = b; index = 1;}
		if (smallest > c){ smallest = c; index = 2;}
		return index;
	}
	

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Strategy s = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
				int roundsLeft = a.getCardsInDeck().size() / a.getPlayers().size();
				int playerCash = p.getCash();
				ArrayList<Card> cardsInAuction = a.getCardsInAuction();
				int cardRange = cardsInAuction.get(cardsInAuction.size()-1).getQuality() -
				    cardsInAuction.get(0).getQuality();
				int cardsLeft = cardsInAuction.size();
				int currentBid = a.getCurrentBid();
				int cashLeft = p.getCash();

				// System.out.println(cardsInAuction + " Range is: " + cardRange);
				// System.out.println("current bid is: " + currentBid);
				// System.out.println("money left is: " + cashLeft);

				if (currentBid == 0) {
				    //System.out.println("Move 1");
					return currentBid +1;
				} else {
					if (cardsLeft <= 2) {
						//System.out.println("Move 2");
						return -1;
					} else if (cardsInAuction.get(0).getQuality() > 11) {
						//System.out.println("Move 3");
						return -1;
					} else if (cardRange <= 10) {
						//System.out.println("Move 4");
						return -1;
					} else if (playerCash == roundsLeft) {
						//System.out.println("Move 5");
						return -1;
					} else if (cardsInAuction.get(cardsInAuction.size()-1).getQuality() < 10 && currentBid >= 4) {
						//System.out.println("Move 6");
						return -1;
					} else if (cardsInAuction.get(cardsInAuction.size()-1).getQuality() >= 10 && 
						cardsInAuction.get(cardsInAuction.size()-1).getQuality() < 28 && currentBid >= 8) {
						//System.out.println("Move 7");
						return -1;
					} else if (currentBid == 13) {
						return -1;
					} else {
						//System.out.println("Move 8");
						return currentBid +1;
					}
				} 
			}
			

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
		
                int players = s.getChequesAvailable().size();
                int difference = s.getChequesAvailable().get(players-1) - s.getChequesAvailable().get(0);
				ArrayList<Card> opponentsCards = new ArrayList<Card>();
				ArrayList<Card> playerCards = new ArrayList<Card>();
				ArrayList<Integer> cheques = s.getChequesAvailable();
				int[] first = {cheques.get(0), cheques.get(1), cheques.get(2)};
				int[] second = {cheques.get(1), cheques.get(2), cheques.get(3)};
				int[] third = {cheques.get(3), cheques.get(4), cheques.get(5)};
				double firstSD = getSD(first);
				double secondSD = getSD(second);
				double thirdSD = getSD(third);
				int min = findMin(firstSD, secondSD, thirdSD);
				int roundsLeft;

                for (PlayerRecord player : s.getPlayers()) {
                    if (!(player.getCards() == p.getCards())) {
                        opponentsCards.addAll(player.getCards());
                    } else {
						playerCards.addAll(player.getCards());
		   			}
				}
				
				Collections.sort(opponentsCards, new Comparator<Card>() {

					@Override
					public int compare(Card card1, Card card2) {
						return card1.getQuality() - card2.getQuality();
						}
					});

				Collections.sort(playerCards, new Comparator<Card>() {
					@Override
					public int compare(Card card1, Card card2) {
						return card1.getQuality() - card2.getQuality();
						}
					});
				roundsLeft = playerCards.size();
	


				//System.out.println(cheques);
				//System.out.println(firstSD + " " + secondSD + " " + thirdSD+ " " + min + " " + roundsLeft);
				// for(int i = 0; i < roundsLeft; i++){
				//     System.out.print(playerCards.get(i).getQuality() + " ");
				// }
				//System.out.println();
				if (playerCards.size() == 1) {
		    		// System.out.println("move 1 " + playerCards.get(0).getQuality());
		    		return playerCards.get(0);
				} else {    
		 		    if (playerCards.get(playerCards.size()-1).getQuality() > opponentsCards.get(opponentsCards.size()-1).getQuality()
		     		    && difference >= 14 && min != 2) {
						//System.out.println("move 2 " + playerCards.get(playerCards.size()-1).getQuality());
						return playerCards.get(playerCards.size()-1);
		   			} else if (cheques.get(0) == 0) {
						if (min == 2) {
			    			// System.out.println("move 3 "+ playerCards.get(playerCards.size()-1).getQuality());
			    			return playerCards.get(playerCards.size()-1);   
						} else {
			    			// System.out.println("move 4 "+ playerCards.get(playerCards.size()-2).getQuality());
			    			return playerCards.get(playerCards.size()-2);
						}
		    		} else {
						if (min == 0 || difference <= 8) {
			    			// System.out.println("move 5 " + playerCards.get(0).getQuality());
			    			return playerCards.get(0);
						} else if (min == 1) {
			 			   	// System.out.println("move 6 " + playerCards.get(roundsLeft/2).getQuality());
			 			   	if (roundsLeft == 2)
								return playerCards.get(0);
			 			   	else
								return playerCards.get(roundsLeft/2);
						} else {
			    			//System.out.println("move 7 " + playerCards.get(roundsLeft-2).getQuality());
			    			return playerCards.get(roundsLeft-2);
						}
		   		 	} 
				}
            }
        };
		
		
		/**
		 * A random strategy - make a random bid up to your amount remaining,
		 *choose a rand card to sell.
		 */
        Strategy r = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                return (int) (1 + (Math.random()*p.getCash()));
            }

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) { 
                return p.getCards().get((int) (Math.random()*p.getCards().size()));
            }
            
		};
		
		ArrayList<String> winners = new ArrayList<String>();
        for(int j = 0; j < 100000; j++) {
				ArrayList<Player> players = new ArrayList<>();
			for(int i = 0; i < 5; i++) {
				players.add(new Player("R"+ ((char) ('A' + i)), r));
			}
			players.add(new Player("N" + ('A'), s));
			GameManager g = new GameManager(players);
			g.run();
			ArrayList<PlayerRecord> finalStandings = g.getFinalStandings();
			Collections.sort(finalStandings, new Comparator<PlayerRecord>() {

				@Override
				public int compare(PlayerRecord pr1, PlayerRecord pr2) {
				return pr2.getCash() - pr1.getCash();
				}
			});

			String winner = finalStandings.get(0).getName();
			winners.add(winner);
		}

	int won = 0;
	for(int i = 0; i < winners.size(); i++){
	    if(winners.get(i) == "NA"){
		won++;
	    }
	}


	System.out.println("won: "+won);
	//System.out.println(g.getLog());
    }
}
