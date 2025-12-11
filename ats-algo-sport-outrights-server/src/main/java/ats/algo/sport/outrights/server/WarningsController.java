package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.server.api.Warnings;
import ats.core.util.json.JsonUtil;

@SuppressWarnings("deprecation")
@Controller
public class WarningsController {

    @GetMapping("/warnings")
    @ResponseBody
    public String getWarnings() {
        return JsonUtil.marshalJson(new Warnings());
    }

}
