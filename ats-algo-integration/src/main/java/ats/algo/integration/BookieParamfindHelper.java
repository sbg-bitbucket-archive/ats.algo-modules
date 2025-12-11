package ats.algo.integration;

import static ats.algo.core.markets.ATSMarketTypeConverter.extractMarketCategory;
import static ats.algo.core.markets.ATSMarketTypeConverter.startsWithALetter;

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

            for (SupportedSportType sst : SupportedSportType.values()) {
                if (algoType.startsWith(sst.name())) {
                    algoType = algoType.substring(sst.name().length() + 1);
                    break;
                }
            }

            MarketCategory marketCategory = extractMarketCategory(algoType);
            MarketGroup group = MarketGroup.NOT_SPECIFIED;

            String line = marketPrice.getSubtype();
            String sequenceId = null;

            if (line != null && line.startsWith("G")) {
                sequenceId = line;
                line = null;

            } else if (line != null && line.contains("#") && !algoType.startsWith("FT:AH")) {
                String[] split = line.split("#");
                sequenceId = split[0];
                line = split[1];
                // } else if (line != null && line.contains("#")
                // && algoType.startsWith("FT:AH")) {
                //
                // String[] split = line.split("#");
                // sequenceId = split[0];
                // line = split[1];
                //
                // String ahcpLine = line;
                // debug("old AHCP line %s", ahcpLine);
                // String score;
                // if (sequenceId.contains("P"))
                // score = sequenceId.substring(2, sequenceId.length());
                // else
                // score = sequenceId.substring(1, sequenceId.length());
                // String[] index = score.split("-");
                // int ahcp = Integer.parseInt(index[0]) - Integer.parseInt(index[1]);
                // Double ahcpL = Double.parseDouble(ahcpLine);
                // ahcpL -= ahcp;
                // ahcpLine = String.valueOf(ahcpL);
                // line = ahcpLine;
                // debug("new AHCP line Generate %s", ahcpLine);


            } else if (algoType.startsWith("FT:") || algoType.startsWith("FTOT:")) {
                if (algoType.startsWith("FT:AH") && (line.contains("#"))) {
                    String[] split = line.split("#");
                    sequenceId = split[0];
                    line = split[1];

                } else {
                    sequenceId = "M";

                }
            } else if (algoType.startsWith("P:")) {
                sequenceId = "P1";

            }

            if (sequenceId == null) {
                if (startsWithALetter(line)) {
                    sequenceId = line;
                    line = null;

                } else {

                    sequenceId = ""; // Prevent param find trading rules from blowing up - should we assume current and
                                     // try to determine from AlgoMgrs Match state?
                    warn("Could not determine %s sequence from provided market type %s, subtype %s, line %s", algoType,
                                    marketPrice.getType(), marketPrice.getSubtype(), marketPrice.getLine());
                }
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
