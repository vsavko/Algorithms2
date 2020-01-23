import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
	private final int teamNumber;
	private final HashMap<String, TeamStats> teams;
	private final int [][] scores;
	private final String [] TeamNames;

	
	private class TeamStats{
		public Integer number;
		public ArrayList<String> eliminatingTeams;
		public boolean isEliminated;
		public TeamStats(Integer number, ArrayList<String> eliminatingTeams, boolean isEliminated) {
			super();
			this.number = number;
			this.eliminatingTeams = eliminatingTeams;
			this.isEliminated = isEliminated;
		}
	}
	
	 // create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		if (filename == null) throw new IllegalArgumentException("No such file!");
		In in = new In(filename);
		String temp;
		teamNumber = Integer.parseInt(in.readLine());
		teams = new HashMap<>();
		scores = new int[teamNumber][teamNumber+3];
		int teamNr = 0, counter =0;
		TeamNames = new String[teamNumber];
		int numberOfMAtchesCombos = ((teamNumber-1)*(teamNumber-1)-(teamNumber-1))/2;
		int nrOfGraphNodes = 2 + (teamNumber-1) + numberOfMAtchesCombos;

		//parse the text file with teams
		while(in.hasNextLine()) {
			temp = in.readLine().trim();
			String [] tempTeam = (temp.split(" +"));
			teams.put(tempTeam[0], new TeamStats(counter++, new ArrayList<String>(), false));
			TeamNames[teamNr] = tempTeam[0];
			for(int i = 1; i < teamNumber +4; i++) {		
				scores[teamNr][i-1] = Integer.parseInt(tempTeam[i]);
			}
			teamNr++;
		}

		//set up a network flow graph to check which teams are eliminated
		for(teamNr = 0; teamNr < teamNumber; teamNr++) {
			FlowNetwork flowNetwork = new FlowNetwork(nrOfGraphNodes);
			int vertex = 1, nrOfRemainingGames = 0;
			for(int i = 0, ii = 0, z = 0; i < teamNumber; i++) {
				if (i == teamNr) continue;
				for(int j = i+1, jj = ii +1; j < teamNumber; j++) {
					if(j == teamNr) continue;
					flowNetwork.addEdge(new FlowEdge(0,vertex,scores[i][3+j]));
					nrOfRemainingGames += scores[i][3+j];
					//edges from s to matches of 2 teams
					//if (teamNr == 1)
					//	System.out.println(i + " " + ii + " jj " + jj);
					flowNetwork.addEdge(new FlowEdge(vertex,numberOfMAtchesCombos + 1 +ii,Double.POSITIVE_INFINITY));
					flowNetwork.addEdge(new FlowEdge(vertex,numberOfMAtchesCombos + 1 +jj,Double.POSITIVE_INFINITY));
					++vertex;
					++jj;
				}
				
				//simple math condition that a team is eliminated if its wins + remaining games < some other team's wins
				if (scores[teamNr][0] + scores[teamNr][2] - scores[i][0] < 0) { //
					teams.get(TeamNames[teamNr]).isEliminated = true;
					teams.get(TeamNames[teamNr]).eliminatingTeams.add(TeamNames[i]);
				}
				
				//edges from matches of 2 teams to the winner team
				flowNetwork.addEdge(new FlowEdge(z + numberOfMAtchesCombos + 1,nrOfGraphNodes-1,
						Math.max(0,scores[teamNr][0] + scores[teamNr][2] - scores[i][0])));
				++z;
				++ii;
			}
			//if nrOfRemainingGames > maxflow then a team is eliminated
			if (teams.get(TeamNames[teamNr]).isEliminated == false) {
				FordFulkerson ff = new FordFulkerson(flowNetwork, 0, nrOfGraphNodes-1);
				
				//if (teamNr == 1) System.out.println(flowNetwork);
				
				//if team is eliminated
				//System.out.println("teamNr " +teamNr  +" " + nrOfRemainingGames + " " + ff.value());
				if (nrOfRemainingGames > ff.value()) {
					
					teams.get(TeamNames[teamNr]).isEliminated = true;
					for(int i = numberOfMAtchesCombos +1, ii = 0; i < nrOfGraphNodes-1; i++) {
						if (teamNr == ii ) ++ii;
						//System.out.println("i " + i);
						if (ff.inCut(i) == true ) {
							teams.get(TeamNames[teamNr]).eliminatingTeams.add(TeamNames[ii]);
						}
						++ii;
					}
					
				}
			}
		}
	}
	
	// number of teams
	public int numberOfTeams() {
		return teamNumber;
	}
	
	// all teams
	public Iterable<String> teams(){
		return teams.keySet();
	}
	
	// number of wins for given team
	public int wins(String team){
		if (!teams.containsKey(team)) throw new IllegalArgumentException("No such team!");
		return scores[teams.get(team).number][0];
	}
	
	// number of losses for given team
	public int losses(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException("No such team!");
		return scores[teams.get(team).number][1];
	}
	
	// number of remaining games for given team
	public int remaining(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException("No such team!");
		return scores[teams.get(team).number][2];
	}
	
	// number of remaining games between team1 and team2
	public int against(String team1, String team2)  {
		if (!teams.containsKey(team1) || !teams.containsKey(team2)) throw new IllegalArgumentException("No such team!");
		return scores[teams.get(team1).number][teams.get(team2).number + 3];
	}
	
	// is given team eliminated?
	public boolean isEliminated(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException("No such team!");
		return teams.get(team).isEliminated;
	}
	
	 // subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException("No such team!");
		if (teams.get(team).isEliminated)
			return teams.get(team).eliminatingTeams;
		else
			return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseballElimination baseball = 
				new BaseballElimination("D:\\java\\projects\\BaseballElemination\\base\\teams4b.txt");
		
		//System.out.println("numberOfTeams " + baseball.numberOfTeams());
		int maxScore = 0;
		
		for (String key: baseball.teams()) {
			if (baseball.wins(key) > maxScore) maxScore = baseball.wins(key);
		}
		
		//System.out.println("against " + baseball.against("Atlanta","Philadelphia"));
		
		System.out.println("max Score " + maxScore);
		
		for (String key: baseball.teams()) {
			//System.out.println(key + " is eliminated?  "+ baseball.isEliminated(key));
			if(baseball.isEliminated(key) ) {
				System.out.println(key + " is eliminated?  "+ baseball.isEliminated(key) + " score " + (baseball.wins(key) + 
						baseball.remaining(key)) );
				for(String key2: baseball.certificateOfElimination(key)) {
					System.out.println(key2);
				}
			}
		}
		
	}

}
