package in.umlaut.player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gbm on 25/09/15.
 */
public class Player{

    private static final int MAX_SCORE_HISTORY = 10;
    private static final String DELIMITER = ";";
    private static final String SCORES_DELIMITER = ",";

    //TODO set running score against a game instance
    private int id;
    private String name;
    private String description;
    private List<Long> highScores;
    private Long runningScore = 0l;
    private int level;

    protected Player(String name, int id){
        this.level = 1;
        this.highScores = new ArrayList<Long>();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public int getLevel(){
        return this.level;
    }

    protected void setLevel(int level){
        this.level = level;
    }

    public List<Long> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<Long> highScores) {
        this.highScores = highScores;
    }

    public Optional<Long> getTopScore(){
        return Optional.ofNullable(this.highScores.isEmpty() ? null : this.highScores.get(0));
    }

    public void setRunningScore(Long score){
        this.runningScore = score;
    }

    public Long getRunningScore(){
        return this.runningScore;
    }

    public void clearScores(){
        this.highScores = new ArrayList<Long>();
        this.runningScore = 0l;
    }

    protected void addScore(Long score){
        if(!highScores.isEmpty() && score >= this.highScores.get(0)){
            this.highScores.add(0, score);
        } else {
            this.highScores.add(score);
            Collections.sort(highScores, new Comparator<Long>() {
                public int compare(Long o1, Long o2) {
                    return o2.compareTo(o1);
                }
            });
        }
        if(highScores.size() > MAX_SCORE_HISTORY){
            highScores.remove(MAX_SCORE_HISTORY);
        }
    }

    public static Player fromCsv(String csvLine){
        String[] tokens = csvLine.split(DELIMITER, -1);
        List<Long> highScores = Arrays.asList(tokens[3].split(","))
                .stream()
                .filter(token -> !(token == null || token.isEmpty()))
                .map(e -> Long.parseLong(e))
                .collect(Collectors.toList());
        Player p = new Player(tokens[1], Integer.parseInt(tokens[0]));
        p.setDescription(tokens[2]);
        p.setHighScores(highScores);
        p.setLevel(Integer.parseInt(tokens[4]));
        if(!(tokens[5] == null || tokens[5].isEmpty()))
            p.setRunningScore(Long.parseLong(tokens[5]));

        return p;
    }

    public String toCsv(){
        StringBuilder builder = new StringBuilder();
        List<String> scores = highScores.stream().map(score -> String.valueOf(score)).collect(Collectors.toList());
        return  builder.append(id).append(DELIMITER)
                .append(name).append(DELIMITER)
                .append(description == null || description.isEmpty() ? "" : description).append(DELIMITER)
                .append(String.join(SCORES_DELIMITER, scores)).append(DELIMITER)
                .append(String.valueOf(level)).append(DELIMITER)
                .append(String.valueOf(runningScore)).append(DELIMITER)
                .toString();
    }

    @Override
    public int hashCode(){
        return name.hashCode() * description.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Player && name.equals(((Player)o).getName())
                && description.equals(((Player)o).getDescription());
    }

    @Override
    public String toString(){
        return  new StringBuilder()
                .append(String.format("%6s",id)).append("\t\t")
                .append(String.format("%12s",name)).append("\t\t")
                .append(String.format("%12s",description)).append("\t\t")
                .append(String.format("%12s",highScores)).append("\t\t")
                .append(String.format("%12s",level))
                .toString();
    }
}
