package ats.algo.scorecast;

public class Selection {

    public enum Team {
        A,
        B,
        NEITHER
    }

    private Team team;
    private String name;
    private double price;
    private double prob;

    public Selection(Team team, String name) {
        this.team = team;
        this.name = name;
        this.price = -1;
        this.prob = -1;
    }

    public Selection(Team team, String name, double price) {
        this.team = team;
        this.name = name;
        this.price = price;
        this.prob = -1;
    }

    public String toString() {
        String s;
        s = String.format("team: %s, name: %s, price: %.1f, prob: %.4f", team.toString(), name, price, prob);
        return s;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public static Selection createFromPrice(Team team, String name, double price) {
        Selection selection = new Selection(team, name);
        selection.price = price;
        selection.prob = -1;
        return selection;
    }

    public static Selection createFromProb(Team team, String name, double prob) {
        Selection selection = new Selection(team, name);
        selection.price = -1;
        selection.prob = prob;
        return selection;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((team == null) ? 0 : team.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Selection other = (Selection) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (team != other.team)
            return false;
        return true;
    }
}
