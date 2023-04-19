import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static final double root_ullat = MapServer.ROOT_ULLAT, root_ullon = MapServer.ROOT_ULLON,
                                root_lrlat = MapServer.ROOT_LRLAT, root_lrlon = MapServer.ROOT_LRLON;

    private static double[] depthLonDPP = new double[8];

    public Rasterer() {
        depthLonDPP[0] = (root_lrlon - root_ullon) / MapServer.TILE_SIZE;
        for(int i = 1; i < 8; i++) {
            depthLonDPP[i] = depthLonDPP[i-1] / 2;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double reqUllat = params.get("ullat");
        double reqUllon = params.get("ullon");
        double reqLrlat = params.get("lrlat");
        double reqLrlon = params.get("lrlon");

        double reqLonDPP = (reqLrlon - reqUllon) / params.get("w");
        int depth = getDepth(reqLonDPP);

        if (reqUllon >= root_lrlon || reqLrlon <= root_ullon || reqLrlat >= root_ullat || reqUllat <= root_lrlat
                || reqUllon >= reqLrlon || reqLrlat >= reqUllat) {
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            results.put("query_success", false);
        }


        results.put("depth", depth);

        double picLon = (root_lrlon - root_ullon) / Math.pow(2, depth);
        double picLat = (root_ullat - root_lrlat) / Math.pow(2, depth);

        /* raster's pic's number e.g.(xOfLeft,yOfUpper) is the first pic,
            (xOfRight,yOfLow) is the last pic,
         */
        int xOfLeft = Math.max((int) Math.floor((reqUllon - root_ullon) / picLon), 0);
        int xOfRight = Math.min((int) Math.floor((reqLrlon- root_ullon) / picLon), (int) Math.pow(2, depth) - 1);
        int yOfLow = Math.max((int) Math.floor((reqLrlat - root_lrlat) / picLat), 0);
        int yOfUpper = Math.min((int) Math.floor((reqUllat - root_lrlat) / picLat), (int) Math.pow(2, depth) - 1);

        String[][] files = new String[yOfUpper - yOfLow + 1][xOfRight -xOfLeft + 1];
        for (int y = yOfUpper; y > yOfLow - 1; y--) {
            for (int x = xOfLeft; x < xOfRight + 1; x++) {
                files[yOfUpper - y][x - xOfLeft] = "d" + depth + "_x" + x + "_y" +
                        ((int) Math.pow(2, depth) - y - 1) + ".png";
            }
        }
        results.put("render_grid", files);

        double raster_ul_lon = root_ullon + xOfLeft * picLon;
        double raster_ul_lat = root_lrlat + (yOfUpper + 1) * picLat;
        double raster_lr_lon = root_ullon + (xOfRight + 1) * picLon;
        double raster_lr_lat = root_lrlat + yOfLow * picLat;
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);

        results.put("query_success", true);

        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser.");
        return results;
    }

    private int getDepth(double LonDPP) {
        int i = 0;
        while (depthLonDPP[i] > LonDPP && i < 7) {
           i++;
        }
        return i;
    }

}
