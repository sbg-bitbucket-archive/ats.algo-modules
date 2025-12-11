package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ats.core.util.json.JsonUtil;
import ats.org.json.JSONException;
import ats.org.json.JSONObject;

public class JsonFormatterTest {

    @Test
    public void test1() {
        PairOfIntegers p = new PairOfIntegers();
        p.A = 3;
        p.B = 4;
        String json = JsonUtil.marshalJson(p);
        // System.out.println(json);
        try {
            String formattedJson = JsonFormatter.format(json);
            System.out.println(formattedJson);
        } catch (JSONException e) {
            fail();
        }

    }

    static String footballExample =
                    "{\"MatchState\":{\"subClass\":\"FootballSimpleMatchState\",\"preMatch\":true,\"matchCompleted\":false,\"matchPeriod\":[\"FootballMatchPeriod\",\"PREMATCH\"],\"elapsedTimeSeconds\":0,\"goalsA\":0,\"goalsB\":0,\"cornersA\":0,\"cornersB\":0,\"yellowCardsA\":0,\"yellowCardsB\":0,\"redCardsA\":0,\"redCardsB\":0,\"firstHalfGoalsA\":0,\"firstHalfGoalsB\":0,\"secondHalfGoalsA\":0,\"secondHalfGoalsB\":0,\"teamSheet\":{\"teamSheetMap\":{\"A.Player1\":{\"teamId\":\"A\",\"playerName\":\"A.Player1\",\"playerStatus\":\"PLAYING\"},\"B.Player5\":{\"teamId\":\"B\",\"playerName\":\"B.Player5\",\"playerStatus\":\"PLAYING\"},\"B.Player6\":{\"teamId\":\"B\",\"playerName\":\"B.Player6\",\"playerStatus\":\"PLAYING\"},\"B.Player7\":{\"teamId\":\"B\",\"playerName\":\"B.Player7\",\"playerStatus\":\"PLAYING\"},\"B.Player8\":{\"teamId\":\"B\",\"playerName\":\"B.Player8\",\"playerStatus\":\"PLAYING\"},\"A.Player5\":{\"teamId\":\"A\",\"playerName\":\"A.Player5\",\"playerStatus\":\"PLAYING\"},\"B.Player9\":{\"teamId\":\"B\",\"playerName\":\"B.Player9\",\"playerStatus\":\"PLAYING\"},\"A.Player4\":{\"teamId\":\"A\",\"playerName\":\"A.Player4\",\"playerStatus\":\"PLAYING\"},\"A.Player3\":{\"teamId\":\"A\",\"playerName\":\"A.Player3\",\"playerStatus\":\"PLAYING\"},\"A.Player2\":{\"teamId\":\"A\",\"playerName\":\"A.Player2\",\"playerStatus\":\"PLAYING\"},\"A.Player9\":{\"teamId\":\"A\",\"playerName\":\"A.Player9\",\"playerStatus\":\"PLAYING\"},\"B.Player14\":{\"teamId\":\"B\",\"playerName\":\"B.Player14\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player8\":{\"teamId\":\"A\",\"playerName\":\"A.Player8\",\"playerStatus\":\"PLAYING\"},\"B.Player15\":{\"teamId\":\"B\",\"playerName\":\"B.Player15\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player7\":{\"teamId\":\"A\",\"playerName\":\"A.Player7\",\"playerStatus\":\"PLAYING\"},\"B.Player16\":{\"teamId\":\"B\",\"playerName\":\"B.Player16\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player6\":{\"teamId\":\"A\",\"playerName\":\"A.Player6\",\"playerStatus\":\"PLAYING\"},\"B.Player17\":{\"teamId\":\"B\",\"playerName\":\"B.Player17\",\"playerStatus\":\"ON_THE_BENCH\"},\"B.Player1\":{\"teamId\":\"B\",\"playerName\":\"B.Player1\",\"playerStatus\":\"PLAYING\"},\"B.Player2\":{\"teamId\":\"B\",\"playerName\":\"B.Player2\",\"playerStatus\":\"PLAYING\"},\"B.Player3\":{\"teamId\":\"B\",\"playerName\":\"B.Player3\",\"playerStatus\":\"PLAYING\"},\"B.Player4\":{\"teamId\":\"B\",\"playerName\":\"B.Player4\",\"playerStatus\":\"PLAYING\"},\"B.Player10\":{\"teamId\":\"B\",\"playerName\":\"B.Player10\",\"playerStatus\":\"PLAYING\"},\"B.Player11\":{\"teamId\":\"B\",\"playerName\":\"B.Player11\",\"playerStatus\":\"PLAYING\"},\"B.Player12\":{\"teamId\":\"B\",\"playerName\":\"B.Player12\",\"playerStatus\":\"ON_THE_BENCH\"},\"B.Player13\":{\"teamId\":\"B\",\"playerName\":\"B.Player13\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player12\":{\"teamId\":\"A\",\"playerName\":\"A.Player12\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player11\":{\"teamId\":\"A\",\"playerName\":\"A.Player11\",\"playerStatus\":\"PLAYING\"},\"A.Player10\":{\"teamId\":\"A\",\"playerName\":\"A.Player10\",\"playerStatus\":\"PLAYING\"},\"A.Player17\":{\"teamId\":\"A\",\"playerName\":\"A.Player17\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player16\":{\"teamId\":\"A\",\"playerName\":\"A.Player16\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player15\":{\"teamId\":\"A\",\"playerName\":\"A.Player15\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player14\":{\"teamId\":\"A\",\"playerName\":\"A.Player14\",\"playerStatus\":\"ON_THE_BENCH\"},\"A.Player13\":{\"teamId\":\"A\",\"playerName\":\"A.Player13\",\"playerStatus\":\"ON_THE_BENCH\"}}},\"eventId\":0,\"goalScorerNames\":{}}}";

    @Test
    public void test2() {
        try {
            System.out.println(JsonFormatter.format(footballExample));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void test3() {
        try {
            JSONObject jsonObject = new JSONObject(footballExample).getJSONObject("MatchState");
            @SuppressWarnings("unchecked")
            Iterator<String> keys = (Iterator<String>) jsonObject.keys();
            while (keys.hasNext())
                System.out.println(keys.next());
            JSONObject jsonObject2 = jsonObject.getJSONObject("teamSheet");
            jsonObject.remove("teamSheet");
            System.out.println("WITHOUT TEAMSHEET:");
            System.out.println(JsonFormatter.format(jsonObject, footballExample));
            System.out.println("\nJUST THE TEAMSHEET:");
            System.out.println(JsonFormatter.format(jsonObject2, footballExample));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testList() {
        TestClass t = new TestClass();
        t.p = new PairOfIntegers();
        t.p.A = 3;
        t.p.B = 4;
        t.list = new ArrayList<String>(3);
        t.list.add("First");
        t.list.add("Second");
        t.list.add("Third");
        String json = JsonUtil.marshalJson(t, true);
        System.out.println(json);
        try {
            System.out.println(JsonFormatter.format(json));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testx() {
        System.out.printf("%-10s %s", "key: ", "value");
    }

    @Test
    public void testEmptyList() {
        TestClass t = new TestClass();
        t.p = new PairOfIntegers();
        t.p.A = 3;
        t.p.B = 4;
        t.list = new ArrayList<String>(3);
        String json = JsonUtil.marshalJson(t, true);
        System.out.println(json);
        try {
            System.out.println(JsonFormatter.format(json));
        } catch (JSONException e) {
            fail();
        }

    }


    class TestClass implements Serializable {
        private static final long serialVersionUID = 1L;

        private PairOfIntegers p;
        private List<String> list;

        public PairOfIntegers getP() {
            return p;
        }

        public void setP(PairOfIntegers p) {
            this.p = p;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

    }

}
