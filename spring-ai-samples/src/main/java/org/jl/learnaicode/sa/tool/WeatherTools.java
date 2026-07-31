package org.jl.learnaicode.sa.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Map;

public class WeatherTools {
    private static final Map<String, String> WEATHER = Map.of(
            "北京", "晴天，28°C，湿度 45%",
            "上海", "多云，26°C，湿度 65%",
            "广州", "雷阵雨，31°C，湿度 80%"
    );

    @Tool(description = "获取指定城市的天气信息，参数 city 为城市名（如 北京）")
    String getWeather(@ToolParam(description = "城市名称") String city) {
        return WEATHER.getOrDefault(city, "未找到该城市天气数据");
    }

    @Tool(description = "获取当前日期和时间")
    String getCurrentDateTime() {
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
