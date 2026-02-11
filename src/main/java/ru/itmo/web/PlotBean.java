package ru.itmo.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.itmo.web.model.HitResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named("plotBean")
@SessionScoped
public class PlotBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private ResultBean resultsBean;

    private BigDecimal x = BigDecimal.ZERO;
    private BigDecimal y = BigDecimal.ZERO;
    private String r = "1";
    private List<HitResult> resultModel;

    @PostConstruct
    public void init() {
        resultModel = new ArrayList<>();
        loadResultsFromDatabase();
    }


    private void loadResultsFromDatabase() {
        resultModel.clear();
        resultModel.addAll(resultsBean.getAllResults());
    }


    public void checkHit() {
        try {
            BigDecimal rValue = new BigDecimal(r);
            boolean hit = calculate(x, y, rValue);
            HitResult result = new HitResult(x, y, r, hit);
            resultsBean.saveResult(result);
            resultModel.add(0, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean calculate(BigDecimal x, BigDecimal y, BigDecimal r) {
        if ((x.compareTo(BigDecimal.ZERO) >= 0) && (y.compareTo(BigDecimal.ZERO) >= 0)){
            BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
            BigDecimal vir = x.negate().add(halfR);
            return (y.compareTo(vir) <= 0);
        }

        if (x.compareTo(BigDecimal.ZERO) <= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal xSquared = x.multiply(x);
            BigDecimal ySquared = y.multiply(y);
            BigDecimal rSquared = r.multiply(r);
            return xSquared.add(ySquared).compareTo(rSquared) <= 0;
        }

        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
            return x.compareTo(r) <= 0 && y.compareTo(halfR.negate()) >= 0;
        }

        return false;
    }

    public void clearResults() {
        resultsBean.clearAllResults();
        resultModel.clear();
    }
    public void updateX() {
    }

    // Getters and Setters
    public BigDecimal getX() { return x; }
    public void setX(BigDecimal x) { this.x = x; }

    public BigDecimal getY() { return y; }
    public void setY(BigDecimal y) { this.y = y; }

    public String getR() { return r; }
    public void setR(String r) { this.r = r; }

    public List<HitResult> getResultModel() { return resultModel; }
    public void setResultModel(List<HitResult> resultModel) { this.resultModel = resultModel; }
}