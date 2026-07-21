package org.jl.learnaicode.la.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Map;

public class WeatherTools {
    private static final Map<String, String> WEATHER = Map.of(
            "北京", "晴天，28°C，湿度 45%",
            "上海", "多云，26°C，湿度 65%",
            "广州", "雷阵雨，31°C，湿度 80%"
    );

    @Tool(name = "get_weather", value = {"获取指定城市的天气信息，参数 city 为城市名（如 北京）"})
    public String getWeather(@P(description = "城市名称") String city) {
        return WEATHER.getOrDefault(city, "未找到该城市天气数据");
    }

    @Tool(name = "get_current_datetime", value = {"获取当前日期和时间"})
    public String getCurrentDateTime() {
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
