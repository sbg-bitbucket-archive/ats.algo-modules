package ats.algo.integration;

import java.util.Map;

import org.springframework.core.io.Resource;

import com.google.common.collect.Maps;

import ats.core.AtsBean;
import ats.core.util.CsvReader;
import ats.core.util.CsvReader.Row;

public class MarketTypeAliasResolver extends AtsBean {
    private Resource marketTypeAliasFile;
    private Map<String, String> mappingMarketCodes = Maps.newHashMap();
    private Map<String, String> mappingSelectionNames = Maps.newHashMap();
    private Map<String, String> mappingSelectionTypes = Maps.newHashMap();
    private Map<String, String> mappingSubTypes = Maps.newHashMap();

    public void init() {
        try {
            if (marketTypeAliasFile != null && marketTypeAliasFile.exists()) {
                CsvReader csvReader = new CsvReader(marketTypeAliasFile, true);
                csvReader.init();
                for (Row row : csvReader.getRows()) {
                    info("Current row being handled = " + row);
                    mappingMarketCodes.put(row.getValue("Algo_Market_Type"), row.getValue("ATS_Market_Type"));
                    mappingSubTypes.put(row.getValue("Algo_Market_Type"), row.getValue("Algo_SubType_Format"));
                    mappingSelectionTypes.put(row.getValue("Algo_Selection_Type"), row.getValue("ATS_Selection_Type"));
                    mappingSelectionNames.put(row.getValue("Algo_Selection_Type"), row.getValue("ATS_Selection_Name"));
                }

                info("Mapping Market Codes = " + mappingMarketCodes);
                info("Mapping Subtypes = " + mappingSubTypes);
                info("Mapping Selection Types = " + mappingSelectionTypes);
                info("Mapping Selection Names = " + mappingSelectionNames);
            } else {
                info("Assuming no market type aliasing since their is no mapping file");
            }
        } catch (Exception ex) {
            warn("Problem loading %s csv %s", marketTypeAliasFile, ex.getMessage());
        }
    }

    public String getATSMarketCode(TradedEvent tradedEvent, String marketType) {
        String ATSMarketCode = mappingMarketCodes.get(marketType);
        if (ATSMarketCode != null) {
            return ATSMarketCode;
        }
        return tradedEvent.getSport() + ":" + marketType;
    }

    public String getATSSelectionType(String selectionType) {
        String ATSSelectionType = mappingSelectionTypes.get(selectionType);
        if (ATSSelectionType != null) {
            return ATSSelectionType;
        }
        return selectionType;
    }

    public String getATSSelectionName(String selectionName) {
        String ATSSelectionName = mappingSelectionNames.get(selectionName);
        if (ATSSelectionName != null) {
            return ATSSelectionName;
        }
        return selectionName;
    }

    public String getSubTypeFormat(String subtype) {
        String ATSSubtype = mappingSubTypes.get(subtype);
        if (ATSSubtype != null) {
            return ATSSubtype;
        }
        return subtype;
    }

    public double getPer(Row row, int index) {
        double per = Double.valueOf(row.getValue(index));
        return per;
    }

    public void setMarketTypeAliasFile(Resource marketTypeAliasFile) {
        this.marketTypeAliasFile = marketTypeAliasFile;
    }
}
