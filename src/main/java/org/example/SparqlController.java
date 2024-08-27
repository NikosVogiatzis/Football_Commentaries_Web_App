package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SparqlController {

    @Autowired
    private UIClass uiClass;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/run-query")
    @ResponseBody
    public String runQuery(@RequestParam("queryType") String queryType, String target) {
        String query;
        String Title_Of_Table = "";
        switch (queryType) {
            case "score":
            	Title_Of_Table = "Score Variation Per Game";
            	query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
            			+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
            			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
            			+ "\r\n"
            			+ "select DISTINCT ?Match ?FirstHalfScore ?SecondHalfScore ?OTFirstHalfScore ?OTSecondHalfScore ?PenaltyProcedure ?FinalScore \r\n"
            			+ "where {\r\n"
            			+ "    ?Match rdf:type :Match .\r\n"
            			+ "    ?fh_score rdf:type :First_Half .\r\n"
            			+ "    ?sh_score rdf:type :Second_Half .\r\n"
            			+ "    ?end_game rdf:type :End_Game .\r\n"
            			+ "    \r\n"
            			+ "    \r\n"
            			+ "    ?fh_score :at_game ?Match .\r\n"
            			+ "    ?fh_score :ended_with_score ?FirstHalfScore .\r\n"
            			+ "    \r\n"
            			+ "    ?sh_score :at_game ?Match .\r\n"
            			+ "    ?sh_score :ended_with_score ?SecondHalfScore .\r\n"
            			+ "    \r\n"
            			+ "    \r\n"
            			+ "    ?end_game :at_game ?Match .\r\n"
            			+ "    ?end_game :ended_with_score ?FinalScore .\r\n"
            			+ "    \r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?extra_fh_score rdf:type :First_Half_Extra_Time .\r\n"
            			+ "        ?extra_sh_score rdf:type :Second_Half_Extra_Time .\r\n"
            			+ "        ?penalty_procedure rdf:type :Penalty_Procedure .\r\n"
            			+ "        \r\n"
            			+ "        ?extra_fh_score :at_game ?Match .\r\n"
            			+ "    	?extra_fh_score :ended_with_score ?OTFirstHalfScore .\r\n"
            			+ "    \r\n"
            			+ "        ?extra_sh_score :at_game ?Match .\r\n"
            			+ "    	   ?extra_sh_score :ended_with_score ?OTSecondHalfScore .\r\n"
            			+ "        \r\n"
            			+ "        ?penalty_procedure :at_game ?Match .\r\n"
            			+ "    	?penalty_procedure :ended_with_score ?PenaltyProcedure .\r\n"
            			+ "    \r\n"
            			+ "    }\r\n"
            			+ "        \r\n"
            			+ "    \r\n"
            			+ "}\r\n"
            			+ "ORDER BY ?Match";
            	break;
            	
            case "goals_per_game":
            	Title_Of_Table = "Statistics about all Goals scored";
            	query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
            			+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
            			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
            			+ "\r\n"
            			+ "select DISTINCT ?Goal ?WayOfScoring ?Scorrer ?CountsForTeam (?teams_played AS ?Game) ?MinuteScorred  where {\r\n"
            			+ "    ?Goal rdf:type :Goal_Scored .\r\n"
            			+ "    ?Scorrer rdf:type :Player .\r\n"
            			+ "    ?match rdf:type :Match .\r\n"
            			+ "    ?match rdfs:comment ?teams_played .\r\n"
            			+ "    ?Goal :scored_with ?WayOfScoring .\r\n"
            			+ "    ?Goal :scored_by ?Scorrer .\r\n"
            			+ "    ?Goal :counts_for ?CountsForTeam .\r\n"
            			+ "    ?Goal :at_game ?match .\r\n"
            			+ "    ?Goal :happened_at ?MinuteScorred .\r\n"
            			+ "} ORDER BY ?Goal";
                break;
            case "fouls_AA_per_game":
            	Title_Of_Table = "Fouls and Attacking Attempts per game";
                query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
                		+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
                		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
                		+ "\r\n"
                		+ "SELECT ?Match ?Teams (COUNT(DISTINCT ?foul) AS ?Fouls)  (COUNT(DISTINCT ?attacking_attempt) AS ?AttackingAttempts) \r\n"
                		+ "WHERE {\r\n"
                		+ "    ?Match a :Match .\r\n"
                		+ "    ?Match rdfs:comment ?Teams.\r\n"
                		+ "    OPTIONAL {\r\n"
                		+ "        ?foul a :Foul .\r\n"
                		+ "        ?foul :at_game ?Match .\r\n"
                		+ "    }\r\n"
                		+ "    OPTIONAL {\r\n"
                		+ "        ?attacking_attempt a :Attacking_Attempt .\r\n"
                		+ "        ?attacking_attempt :at_game ?Match .\r\n"
                		+ "    }\r\n"
                		+ "} \r\n"
                		+ "GROUP BY ?Match ?Teams\r\n"
                		+ "ORDER BY ?Match";
                break;
                
          
            case "top_scorrers":
            	
            	Title_Of_Table = "Top 10 Scorrers\n";
	                query = "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
	                		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
	                		+ "select ?Player ?Team (COUNT(?goals) AS ?Goals) where {\r\n"
	                		+ "    ?Player rdf:type :Player .\r\n"
	                		+ "    ?Player :playsFor ?Team .\r\n"
	                		+ "	?goals rdf:type :Goal_Scored .\r\n"
	                		+ "	?Player :scored ?goals .\r\n"
	                		+ "} \r\n"
	                		+ "GROUP BY ?Player ?Team\r\n"
	                		+ "ORDER BY DESC(?Goals)\r\n"
	                		+ "LIMIT 10";
                break;  
                
            case "retrieve_players":
            	Title_Of_Table = "";
                query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
                		+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
                		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
                		+ "\r\n"
                		+ "SELECT ?Player\r\n"
                		+ "WHERE{\r\n"
                		+ "    ?Player a :Player .\r\n"
                		+ "    \r\n"
                		+ "    \r\n"
                		+ "}\r\n"
                		+ "ORDER BY ?Player\r\n"
                		+ "";
                break;
                
                
            case "retrieve_teams":
            	Title_Of_Table = "";
                query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
                		+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
                		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
                		+ "\r\n"
                		+ "SELECT ?Team\r\n"
                		+ "WHERE{\r\n"
                		+ "    ?Team a :Team .\r\n"
                		+ "    \r\n"
                		+ "    \r\n"
                		+ "}\r\n"
                		+ "ORDER BY ?Team\r\n"
                		+ "";
                
                break;     
                
            case "statistics_player":
            	System.out.println(target);
            	Title_Of_Table = "All statistics about " + target;
            	String players_statistics = target;
            	players_statistics = players_statistics.replace(" ", "_"); 
            	
            	query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
            			+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
            			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
            			+ "\r\n"
            			+ "SELECT ?Player ?Team\r\n"
            			+ "       (COUNT(DISTINCT ?fouls) AS ?Fouls)\r\n"
            			+ "       (COUNT(DISTINCT ?attackingAttempts) AS ?AttackingAttempts)\r\n"
            			+ "       (COUNT(DISTINCT ?redCards) AS ?RedCards)\r\n"
            			+ "	      (COUNT(DISTINCT ?goals) AS ?Goals)\r\n"
            			+ "\r\n"
            			+ "\r\n"
            			+ "	       (COUNT(DISTINCT ?corners) AS ?CornersConceded)\r\n"
            			+ "	   \r\n"
            			+ "       (COUNT(DISTINCT ?injuries) AS ?Injuries)	\r\n"
            			+ "	   \r\n"
            			+ "	   (COUNT(DISTINCT ?freekicks) AS ?FreeKicks)		\r\n"
            			+ "       (COUNT(DISTINCT ?own_goals) AS ?OwnGoals)	\r\n"
            			+ "       (COUNT(DISTINCT ?pen_no_goal) AS ?PenaltiesMissed)	\r\n"
            			+ "       (COUNT(DISTINCT ?pen_goal) AS ?PenaltiesScored)\r\n"
            			+ "	   (COUNT(DISTINCT ?yellowCards) AS ?YellowCards)	\r\n"
            			+ "       (COUNT(DISTINCT ?dang_play) AS ?DangerousPlays)\r\n"
            			+ "\r\n"
            			+ "       (COUNT(DISTINCT ?handball) AS ?HandBalls)\r\n"
            			+ "\r\n"
            			+ "	   (COUNT(DISTINCT ?offside) AS ?Offsides)		\r\n"
            			+ "       (COUNT(DISTINCT ?penalty) AS ?PenalntiesConceded)\r\n"
            			+ "       \r\n"
            			+ "	   (COUNT(DISTINCT ?subout) AS ?Subsituted)		\r\n"
            			+ "       (COUNT(DISTINCT ?subin) AS ?StartedAsSub)\r\n"
            			+ "       (COUNT(DISTINCT ?assists) AS ?Assists)\r\n"
            			+ "\r\n"
            			+ "\r\n"
            			+ "WHERE {\r\n"
            			+ "    ?Player rdf:type :Player .\r\n"
            			+ "    ?Player rdfs:label \"" + players_statistics + "\" .\r\n"
            			+ "    ?Player :playsFor ?Team . \r\n"
            			+ "		\r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?assists a :Assist .\r\n"
            			+ "        ?assists :assist_made_by ?Player .\r\n"
            			+ "    }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?fouls a :Foul .\r\n"
            			+ "        ?fouls :fouled_by  ?Player. }\r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?attackingAttempts a :Attacking_Attempt .\r\n"
            			+ "        ?attackingAttempts :noted_by ?Player . }\r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?goals a :Goal_Scored .\r\n"
            			+ "        ?goals :scored_by  ?Player. }\r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?redCards a :Red_Card .\r\n"
            			+ "        ?redCards :recieved_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?corners a :Corner .\r\n"
            			+ "        ?corners :conceded_by  ?Player. }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?injuries a :Delay .\r\n"
            			+ "        ?injuries :player_injured ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?freekicks a :Free_Kick .\r\n"
            			+ "        ?freekicks :won_by  ?Player. }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?own_goals a :Own_Goal .\r\n"
            			+ "        ?own_goals :scored_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?pen_no_goal a :Penalty_No_Goal .\r\n"
            			+ "        ?pen_no_goal :missed_by  ?Player. }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?pen_goal a :Penalty_Scored_Goal .\r\n"
            			+ "        ?pen_goal :scored_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?yellowCards a :Yellow_Card .\r\n"
            			+ "        ?yellowCards :recieved_by  ?Player. }\r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?dang_play a :Dangerous_Play .\r\n"
            			+ "        ?dang_play :conceded_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL { \r\n"
            			+ "        ?handball a :Hand_Ball .\r\n"
            			+ "        ?handball :conceded_by  ?Player. }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?offside a :Offside .\r\n"
            			+ "        ?offside :offsde_caused_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?penalty a :Penalty .\r\n"
            			+ "        ?penalty :penalty_caused_by ?Player . }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?subin a :Substitution .\r\n"
            			+ "        ?subin :sub_player_in ?Player . }\r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?subout a :Substitution .\r\n"
            			+ "        ?subout :sub_player_out ?Player . }\r\n"
            			+ "    \r\n"
            			+ "} GROUP BY ?Player ?Team"
            			;
            	
                break;
             
                
            case "statistics_team":
            	System.out.println(target);
            	Title_Of_Table = "Statistics about " + target;
            	String team_statistics = target;
            	players_statistics = team_statistics.replace(" ", "_"); 
            	query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
            			+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
            			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
            			+ "\r\n"
            			+ "SELECT ?Team \r\n"
            			+ "       (COUNT(DISTINCT ?attacking_attempts) AS ?AttackingAttempts)\r\n"
            			+ "       (COUNT(DISTINCT ?freekicks) AS ?FreeKicks)\r\n"
            			+ "	      (COUNT(DISTINCT ?corner) AS ?Corners)\r\n"
            			+ "	      (COUNT(DISTINCT ?goal) AS ?Goals)\r\n"
            			+ "       (COUNT(DISTINCT ?offside) AS ?Offsides)\r\n"
            			+ "WHERE {\r\n"
            			+ "    ?Team rdf:type :Team .\r\n"
            			+ "    ?Team rdfs:label \"" + players_statistics + "\" .\r\n"
            			+ "		\r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?attacking_attempts a :Attacking_Attempt .\r\n"
            			+ "        ?attacking_attempts :counts_for ?Team .\r\n"
            			+ "    }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?freekicks a :Free_Kick .\r\n"
            			+ "        ?freekicks :won_for ?Team .\r\n"
            			+ "    }\r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?goal a :Goal_Scored .\r\n"
            			+ "        ?goal :counts_for ?Team .\r\n"
            			+ "    }\r\n"
            			+ "    \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "        ?corner a :Corner .\r\n"
            			+ "        ?corner :corner_won_for ?Team .\r\n"
            			+ "    }\r\n"
            			+ "        \r\n"
            			+ "    OPTIONAL {\r\n"
            			+ "         ?offside a :Offside .\r\n"
            			+ "         ?offside :offside_for ?Team.\r\n"
            			+ "    }\r\n"
            			+ "    \r\n"
            			+ "    \r\n"
            			+ "} GROUP BY ?Team\r\n"
            			+ "";
            	
            	break;
            	
            case "MVP":
            	Title_Of_Table = "5 MVP Players";
                query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
                		+ "PREFIX : <http://www.semanticweb.org/vogia/ontologies/2024/3/untitled-ontology-53/>\r\n"
                		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
                		+ "\r\n"
                		+ "SELECT ?Player ?Team\r\n"
                		+ "       (SUM(?goalCount) AS ?Goals)\r\n"
                		+ "       (SUM(?attackingAttemptCount) AS ?AttackingAttempts)\r\n"
                		+ "       (SUM(?assistCount) AS ?Assists)\r\n"
                		+ "       ((SUM(?goalCount) + SUM(?attackingAttemptCount) + SUM(?assistCount)) AS ?TotalContribution)\r\n"
                		+ "WHERE {\r\n"
                		+ "   ?Team a :Team . \r\n"
                		+ "   ?Player :playsFor ?Team  . \r\n"
                		+ "  { SELECT ?Player (COUNT(?goal) AS ?goalCount) WHERE { ?Player :scored ?goal } GROUP BY ?Player }\r\n"
                		+ "  UNION\r\n"
                		+ "  { SELECT ?Player (COUNT(?attacking_attempt) AS ?attackingAttemptCount) WHERE { ?Player :noted_attacking_attempt ?attacking_attempt } GROUP BY ?Player }\r\n"
                		+ "  UNION\r\n"
                		+ "  { SELECT ?Player (COUNT(?assist) AS ?assistCount) WHERE { ?Player :assisted ?assist } GROUP BY ?Player }\r\n"
                		+ "}\r\n"
                		+ "GROUP BY ?Player ?Team\r\n"
                		+ "ORDER BY DESC(?TotalContribution)\r\n"
                		+ "LIMIT 5";
                break;

                
            default:
                return "Invalid query type.";
        }
        String results = uiClass.executeQuery(query);
        return formatResultsAsHtml(results, Title_Of_Table);
    }

    private String formatResultsAsHtml(String results, String Title_Of_Table) {
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<div class='table-title'><h3 class='table-title-header'>")
        	.append(Title_Of_Table)
        	.append("</h3></div>");
        tableHtml.append("<table class='table-fill'><thead><tr>");

        String[] rows = results.split("\n");
        if (rows.length > 0) {
            String[] headers = rows[0].split("\\s+");
            for (String header : headers) {
                tableHtml.append("<th class='text-left'>").append(header).append("</th>");
            }
            tableHtml.append("</tr></thead><tbody class='table-hover'>");

            for (int i = 1; i < rows.length; i++) {
                tableHtml.append("<tr>");
                String[] columns = rows[i].split("\\s+");
                for (String column : columns) {
                	String _to_append = column.replace("_", " ");
                    tableHtml.append("<td class='text-left'>").append(_to_append).append("</td>");
                }
                tableHtml.append("</tr>");
            }

            tableHtml.append("</tbody></table>");
        }

        return tableHtml.toString();
    }
    
    
   
}