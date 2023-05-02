package com.example.dividend_project.scraper;

import com.example.dividend_project.model.Company;
import com.example.dividend_project.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
