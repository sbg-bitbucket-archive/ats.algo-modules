package ats.algo.core.dto.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import ats.algo.core.common.*;
import com.google.common.collect.Maps;

import ats.algo.core.SportMetaData;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.core.util.json.JsonUtil;

/**
 * Utility methods for DTO JSON serialisation
 */
public class DtoJsonUtil {

    @SuppressWarnings("rawtypes")
    private static Map<String, Class> paramTypeMap;
    @SuppressWarnings("rawtypes")
    private static Map<String, Class> incidentTypeMap;

    @SuppressWarnings("rawtypes")
    static void register(Class clazz, Map<String, Class> map) {
        map.put(clazz.getSimpleName(), clazz);
    }

    static {
        paramTypeMap = Maps.newHashMap();
        incidentTypeMap = Maps.newHashMap();
        for (SupportedSportType stt : SupportedSportType.values()) {
            SportMetaData sportMeta = SportMetaData.forSportType(stt);
            if (sportMeta != null) {
                register(sportMeta.getMatchParams(), paramTypeMap);
                register(sportMeta.getMatchIncident(), incidentTypeMap);
            }
        }
        register(GenericMatchParams.class, paramTypeMap);
        register(ElapsedTimeMatchIncident.class, incidentTypeMap);
        register(DatafeedMatchIncident.class, incidentTypeMap);
        register(TeamSheetMatchIncident.class, incidentTypeMap);
        register(PlayerMatchIncident.class, incidentTypeMap);
        register(AbandonMatchIncident.class, incidentTypeMap);
        register(ResultedMarketsMatchIncident.class, incidentTypeMap);
    }

    @SuppressWarnings("unchecked")
    public static MatchParams unmarshallMatchParams(String json) {
        String rootName = JsonUtil.getRootName(json);
        return (MatchParams) JsonUtil.unmarshalJson(json, paramTypeMap.get(rootName));
    }

    @SuppressWarnings("unchecked")
    public static MatchIncident unmarshallMatchIncident(String json) {
        String rootName = JsonUtil.getRootName(json);
        return (MatchIncident) JsonUtil.unmarshalJson(json, incidentTypeMap.get(rootName));
    }

    public static <T> T loadJsonFromUri(String uri, Class<T> clazz) throws IOException, URISyntaxException {
        Path p = Paths.get(new URI(uri));
        String json = new String(Files.readAllBytes(p));
        return JsonUtil.unmarshalJson(json, clazz);
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }
        if (object instanceof String) {
            return Long.valueOf((String) object);
        }
        throw new IllegalArgumentException("Expected String or Number and not " + object);
    }

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        if (object instanceof String) {
            return Integer.valueOf((String) object);
        }
        throw new IllegalArgumentException("Expected String or Number and not " + object);
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        if (object instanceof String) {
            return Double.valueOf((String) object);
        }
        throw new IllegalArgumentException("Expected String or Number and not " + object);
    }

    public static String toString(Object object, String defaultWhenNull) {
        if (object == null) {
            return defaultWhenNull;
        }
        return toString(object);
    }


    public static String toString(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        throw new IllegalArgumentException("Expected String and not " + object);
    }

    public static boolean toBoolean(Object object, boolean def) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        if (object instanceof String) {
            return Boolean.parseBoolean((String) object);
        }
        return def;
    }

}
