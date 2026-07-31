package org.jl.learnaicode.as.tool;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Map;

public class WeatherTools {
    private static final Map<String, String> WEATHER = Map.of(
            "北京", "晴天，28°C，湿度 45%",
            "上海", "多云，26°C，湿度 65%",
            "广州", "雷阵雨，31°C，湿度 80%"
    );

    @Tool(name = "get_weather",description = "获取城市天气")
    public String getWeather(@ToolParam(name = "city", description = "城市名") String city) {
        return WEATHER.getOrDefault(city, "未找到该城市天气数据");
    }
    @Tool(name = "get_current_datetime",description = "获取当前时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
