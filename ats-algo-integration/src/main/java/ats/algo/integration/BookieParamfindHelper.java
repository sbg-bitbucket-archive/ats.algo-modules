package ats.algo.integration;

import static ats.algo.core.markets.ATSMarketTypeConverter.extractMarketCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.core.AtsBean;
import generated.ats.betsync.dto.BookiePrices;
import generated.ats.betsync.dto.BookmakersPrices;
import generated.ats.betsync.dto.ChannelPrices;
import generated.ats.betsync.dto.MarketPrice;
import generated.ats.betsync.dto.SelectionPrice;

public class BookieParamfindHelper extends AtsBean {

    public MarketPricesList generateMarketPricesList(final BookmakersPrices bookiePrices) {
        MarketPricesList marketPrices = new MarketPricesList();
        Map<ats.algo.core.comparetomarket.MarketPrice, MarketGroup> priceGroupMapping = Maps.newHashMap();

        bookiePrices.getBookies().forEach((b) -> addBookiePrices(marketPrices, b, priceGroupMapping));

        return marketPrices;
    }

    private void addBookiePrices(MarketPricesList marketPricesList, BookiePrices bookiePrices,
                    Map<ats.algo.core.comparetomarket.MarketPrice, MarketGroup> priceGroupMapping) {
        ats.algo.core.comparetomarket.MarketPrices prices = new ats.algo.core.comparetomarket.MarketPrices();
        marketPricesList.put(bookiePrices.getBookie(), prices);

        Double weighting = bookiePrices.getWeighting();

        if (weighting != null) {
            prices.setSourceWeight(weighting);
        } else {
            prices.setSourceWeight(0);
        }

        List<MarketPrice> marketPrices = bookiePrices.getMarketPrices().getMarket();
        for (MarketPrice marketPrice : marketPrices) {
            String algoType = marketPrice.getType();
            String sportType = "";

            for (SupportedSportType sst : SupportedSportType.values()) {
                if (algoType.startsWith(sst.name())) {
                    sportType = sst.name();
                    algoType = algoType.substring(sst.name().length() + 1);
                    break;
                }
            }

            MarketCategory marketCategory = extractMarketCategory(algoType);
            MarketGroup group = MarketGroup.NOT_SPECIFIED;

            String line = marketPrice.getSubtype();
            String sequenceId = "M";

            if (line != null && line.startsWith("G")) {
                sequenceId = line;
                line = null;
            } else if (line != null && line.startsWith("S") && sportType.equals("TENNIS")) {
                sequenceId = line;
                line = null;
            } else if (line != null && line.contains("#") && !algoType.startsWith("FT:AH")) {
                String[] split = line.split("#");
                sequenceId = split[0];
                line = split[1];

            } else if (algoType.startsWith("FT:") || algoType.startsWith("FTOT:")) {
                if (algoType.startsWith("FT:AH") && (line.contains("#"))) {
                    String[] split = line.split("#");
                    sequenceId = split[0];
                    line = split[1];

                } else if (algoType.contains("AXB") || algoType.contains("ML")) {
                    sequenceId = "M";
                    line = null;

                }
            } else if (algoType.startsWith("P:")) {
                sequenceId = "P1";

            }

            ats.algo.core.comparetomarket.MarketPrice mp = new ats.algo.core.comparetomarket.MarketPrice(algoType,
                            algoType + "|" + line + "|" + sequenceId + "|" + marketCategory, marketCategory, line,
                            sequenceId);

            for (ChannelPrices channelPrices : marketPrice.getChannel()) {
                for (SelectionPrice selectionPrice : channelPrices.getSelection()) {
                    BigDecimal decimalOdds = selectionPrice.getDecimalOdds();
                    mp.put(selectionPrice.getType(), decimalOdds.doubleValue());
                }
            }

            // are multiple lines possible ?
            prices.addMarketPrice(mp);
            priceGroupMapping.put(mp, group);
        }
    }
}
