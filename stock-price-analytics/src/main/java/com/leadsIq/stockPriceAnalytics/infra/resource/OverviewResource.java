package com.leadsIq.stockPriceAnalytics.infra.resource;

import static com.leadsIq.stockPriceAnalytics.domain.util.Constants.INVALID_WEEK_PARAMETER_ERROR_MESSAGE;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPricesFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.exception.ClientException;
import com.leadsIq.stockPriceAnalytics.domain.usecase.FetchWeeklyStockPricesUseCase;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OverviewResource {
    private final FetchWeeklyStockPricesUseCase
        fetchWeeklyStockPricesUseCase;

    @GetMapping("/overview")
    public String getOverview(@RequestParam(name = "week", required = false) String week,
                              Model model) {
        var filter = buildFetchStockPricesFilter(week);
        List<StockPrice> stockPrices = fetchWeeklyStockPricesUseCase.execute(filter);
        model.addAttribute("week", filter.startDate());
        model.addAttribute("stockPrices", stockPrices);
        return "overview";
    }

    private FetchStockPricesFilter buildFetchStockPricesFilter(String week) {
        try {
            LocalDate startDate;
            if (week != null) {
                startDate = LocalDate.parse(week + "-1", DateTimeFormatter.ofPattern("YYYY-'W'ww-e"));
            } else {
                startDate =
                    LocalDate.now().with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
            }
            LocalDate endDate = startDate.plusDays(4);
            return new FetchStockPricesFilter(startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ClientException(INVALID_WEEK_PARAMETER_ERROR_MESSAGE.formatted(week), e);
        }
    }
}
