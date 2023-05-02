package com.example.dividend_project.service;

import com.example.dividend_project.exception.impl.NoCompanyException;
import com.example.dividend_project.model.Company;
import com.example.dividend_project.model.Dividend;
import com.example.dividend_project.model.ScrapedResult;
import com.example.dividend_project.model.constants.CacheKey;
import com.example.dividend_project.persist.CompanyRepository;
import com.example.dividend_project.persist.DividendRepository;
import com.example.dividend_project.persist.entity.CompanyEntity;
import com.example.dividend_project.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(
                company.getTicker(), company.getName()), dividends);
    }
}
