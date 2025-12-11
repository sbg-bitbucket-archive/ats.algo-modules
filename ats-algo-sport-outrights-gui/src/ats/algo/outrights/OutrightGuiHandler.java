package ats.algo.outrights;

import java.util.Map;

public interface OutrightGuiHandler {

    public void refreshOutrightGui(Map<String, String> params);

    public void publishUpdate(String body);

    public void publishInfo(String body);
}
