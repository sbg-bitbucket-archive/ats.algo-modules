package ats.algo.inplayGS;

public class Selection {

    public enum Team {
        A,
        B,
        NEITHER
    }

    Team team;
    String name;
    double price;
    double prob;

    public Selection() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

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
        s = String.format("%s %s. Price: %.3f, prob: %.4f", team.toString(), name, price, prob);
        return s;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getProb() {
        return prob;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public Selection copy() {
        Selection cc = new Selection(this.team, this.name, this.price);
        cc.prob = this.prob;
        return cc;
    }
}
