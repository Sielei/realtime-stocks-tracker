package com.hs.stocks.visualizer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StocksVisualizerController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String visualizeStocks(){
        return "stocks";
    }
}
