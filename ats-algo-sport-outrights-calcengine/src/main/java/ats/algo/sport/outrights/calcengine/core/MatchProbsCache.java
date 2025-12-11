// package ats.algo.sport.outrights.calcengine.core;

//
// import java.util.HashMap;
// import java.util.Map;
//
// public class MatchProbsCache {
//
// private Map<String, Map<String,MatchProbs>> cache;
//
// public MatchProbsCache(Teams teams, double homeTeamRatingsAdjustment,boolean isPlayedAtNeutralGround) {
// cache = new HashMap<>(teams.size());
// for (Team teamH: teams.values()) {
// Map<String,MatchProbs> innerCache = new HashMap<>(teams.size()-1);
// String idH = teamH.getTeamID();
// for (Team teamA : teams.values()) {
// String idA = teamA.getTeamID();
// if (!idA.equals(idH)) {
// MatchProbs matchProbs = new MatchProbs(teamH, teamA, homeTeamRatingsAdjustment, isPlayedAtNeutralGround);
// innerCache.put(idA, matchProbs);
// }
// }
// cache.put(idH, innerCache);
// }
// }
//
// public MatchProbs get(String homeTeamID, String awayTeamID) {
// return cache.get(homeTeamID).get(awayTeamID);
// }
//
// @Override
// public int hashCode() {
// final int prime = 31;
// int result = 1;
// result = prime * result + ((cache == null) ? 0 : cache.hashCode());
// return result;
// }
//
// @Override
// public boolean equals(Object obj) {
// if (this == obj)
// return true;
// if (obj == null)
// return false;
// if (getClass() != obj.getClass())
// return false;
// MatchProbsCache other = (MatchProbsCache) obj;
// if (cache == null) {
// if (other.cache != null)
// return false;
// } else if (!cache.equals(other.cache))
// return false;
// return true;
// }
//
//
//
// }
