/*
public class ProxyList {
    private List<ProxyUnit> proxyUnitList;
    private String letUsHideToken;

    public String getDailyProxiesToken() {
        return dailyProxiesToken;
    }

    public void setDailyProxiesToken(String dailyProxiesToken) {
        this.dailyProxiesToken = dailyProxiesToken;
    }

    public String getLetUsHideToken() {
        return letUsHideToken;
    }

    public void setLetUsHideToken(String letUsHideToken) {
        this.letUsHideToken = letUsHideToken;
    }

    private String dailyProxiesToken;
    public ProxyList()
    {
        proxyUnitList = null;
    }

    public List<ProxyUnit> getProxyUnitList() {
        return proxyUnitList;
    }
    public ProxyUnit getProxyUnit(int index) throws Exception
    {
        return proxyUnitList.get(index);
    }
    void GenerateFromLetUsHide(int timeout) throws Exception
    {
        if(letUsHideToken.isEmpty())
            return;
        JSONArray array = null;
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) new URL("http://letushide.com/fpapi/?key=" + letUsHideToken).openConnection();
        StringBuffer text = new StringBuffer();
        InputStreamReader in = null;
        in = new InputStreamReader((InputStream) urlConnection.getContent());
        BufferedReader buff = new BufferedReader(in);
        JSONObject json = new JSONObject(buff.readLine());
        array = (JSONArray) json.get("data");
        JSONObject jsonObject;
        if(proxyUnitList == null)
            proxyUnitList = new ArrayList<ProxyUnit>();
        for(int i=0;i<array.length();i++)
        {
            jsonObject = (JSONObject) array.get(i);
            proxyUnitList.add(new ProxyUnit(true, jsonObject.get("host").toString(), Integer.parseInt(jsonObject.get("port").toString())));
        }
    }
    void GenerateFromDailyProxies(int timeout) throws  Exception
    {
        if(dailyProxiesToken.isEmpty())
            return;

        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) new URL("http://www.daily-proxies.com/daily-proxies/deliver_proxys.php?" +
                    "token="+dailyProxiesToken+"&limit=1000").openConnection();
        StringBuffer text = new StringBuffer();
        InputStreamReader in = null;
        in = new InputStreamReader((InputStream) urlConnection.getContent());
        BufferedReader buff = new BufferedReader(in);
        if(proxyUnitList == null)
            proxyUnitList = new ArrayList<ProxyUnit>();
        String line; //= buff.readLine();
        String host, port;
        while((line = buff.readLine())!= null)
        {
            host = line.split("/:/")[0];
            port = line.split("/:/")[1];
            proxyUnitList.add(new ProxyUnit(true, host, Integer.parseInt(port)));
        }
    }
}
*/
