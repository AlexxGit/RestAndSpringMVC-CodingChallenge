package com.github.vedenin.codingchallenge.mvc.controler;

import com.github.vedenin.codingchallenge.common.CurrencyEnum;
import com.github.vedenin.codingchallenge.converter.CurrentConvector;
import com.github.vedenin.codingchallenge.mvc.model.ConverterFormModel;
import com.github.vedenin.codingchallenge.restclient.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.inject.Inject;

/*
* Controller that provide main page of Converter application
*/
@Controller
public class ConverterController extends WebMvcConfigurerAdapter {

    public static final String CURRENCY_ENUM = "currencyEnum";
    public static final String RESULT = "result";
    @Inject
    CurrentConvector currentConvector;

    @GetMapping("/")
    public String showForm(ConverterFormModel converterFormModel, Model model) {
        model.addAttribute(CURRENCY_ENUM, CurrencyEnum.values());
        model.addAttribute(RESULT, "");
        return "form";
    }

    @PostMapping("/")
    public String checkPersonInfo(ConverterFormModel converterFormModel, Model model) {
        model.addAttribute(CURRENCY_ENUM, CurrencyEnum.values());
        BigDecimal result;
        if(converterFormModel.getType().equals("history")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = df.parse(converterFormModel.getDate());
            } catch (ParseException e) {
                throw new RuntimeException();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = currentConvector.getConvertHistoricalValue(converterFormModel.getAmount(),
                    converterFormModel.getCurrencyEnumFrom(), converterFormModel.getCurrencyEnumTo(),
                    calendar);
        } else {
            result = currentConvector.getConvertValue(converterFormModel.getAmount(),
                    converterFormModel.getCurrencyEnumFrom(),
                    converterFormModel.getCurrencyEnumTo());
        }
        model.addAttribute(RESULT, String.format("%.3f%n", result));
        return "form";
    }
}
