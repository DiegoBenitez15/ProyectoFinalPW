package Clases;
import com.google.common.base.Optional;
import com.opsmatters.bitly.Bitly;
import com.opsmatters.bitly.BitlyException;
import com.opsmatters.bitly.api.model.v4.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BitlyURL {
    private final Bitly client;
    private final String accessToken = "4484de6ebc3b141f250e0e5881b208b2ae6f401a";
    private UnitQuery units = UnitQuery.builder().unit(Unit.DAY).units(7).unitReference(toStringUTC(Instant.now())).size(20).build();

    public BitlyURL() {
        this.client = new Bitly(accessToken);
    }

    public String shortURL(String url){
        try
        {
            Optional<CreateBitlinkResponse> response = client.bitlinks().shorten(url);
            return response.get().getLink();
        }
        catch(BitlyException | IOException eb)
        {
            System.out.println("ERROR COMPADRE");
            return null;
        }
    }

    public static String toStringUTC(Instant dt)
    {
        return LocalDateTime.ofInstant(dt, ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"+0000";
    }

    public int getSummaryClicks(String url) throws IOException, URISyntaxException {
        Optional<CreateBitlinkResponse> res = client.bitlinks().shorten(url);
        String bitlink = res.get().getId();
        Optional<GetBitlinkClicksSummaryResponse> response = client.bitlinks().getClicksSummary(bitlink, units);
        return response.get().getTotalClicks();
    }
}
